package edu.sjsu.expressnest.feeds.messaging;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.feeds.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.feeds.model.UserFollowers;
import edu.sjsu.expressnest.feeds.repository.UserFollowersRepository;
import edu.sjsu.expressnest.feeds.util.FollowEventType;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FollowEventConsumer {
	
	@Autowired
	private UserFollowersRepository userFollowersRepository;
	
	@KafkaListener(topics = "follow-events", containerFactory = "followEventKafkaListenerContainerFactory")
	public UserFollowers processPostEvents(FollowEvent followEvent) throws ResourceNotFoundException {
	    FollowEventType eventType = FollowEventType.valueOf(followEvent.getType());
	    switch (eventType) {
	        case FOLLOW:
	            return handleFollowEvent(followEvent);
	        case UNFOLLOW:
	            return handleUnfollowEvent(followEvent);
	        default:
	            log.warn("Unknown EventType: {}", eventType);
	            throw new IllegalArgumentException("Invalid EventType: " + eventType);
	    }
	}

	private UserFollowers handleFollowEvent(FollowEvent followEvent) {
	    log.info("FollowEvent: User={} followed User={}", followEvent.getFollowerId(), followEvent.getFolloweeId());
	    return userFollowersRepository.findById(followEvent.getFolloweeId())
	        .map(userFollowers -> updateUserFollowersWithNewFollower(userFollowers, followEvent.getFollowerId()))
	        .orElseGet(() -> createUserFollowers(followEvent.getFolloweeId(), followEvent.getFollowerId()));
	}

	private UserFollowers updateUserFollowersWithNewFollower(UserFollowers userFollowers, long followerId) {
		if (!userFollowers.getFollowerIds().contains(followerId)) {
	        userFollowers.getFollowerIds().add(followerId);
	    }
		return userFollowersRepository.save(userFollowers);
	}
	
	private UserFollowers handleUnfollowEvent(FollowEvent followEvent) throws ResourceNotFoundException {
	    log.info("UnfollowEvent: User={} unfollowed User={}", followEvent.getFollowerId(), followEvent.getFolloweeId());
	    return userFollowersRepository.findById(followEvent.getFolloweeId())
	        .map(userFollowers -> updateUserFollowersToRemoveFollower(userFollowers, followEvent.getFollowerId()))
	        .orElseThrow(() -> new ResourceNotFoundException("UserID does not exist for FolloweeId: " + followEvent.getFolloweeId()));
	}
	
	private UserFollowers createUserFollowers(long followeeId, long followerId) {
	    UserFollowers newUserFollowers = new UserFollowers();
	    newUserFollowers.setUserId(followeeId);
	    newUserFollowers.setFollowerIds(Arrays.asList(followerId));
	    return userFollowersRepository.save(newUserFollowers);
	}

	private UserFollowers updateUserFollowersToRemoveFollower(UserFollowers userFollowers, long followerId) {
		if (userFollowers.getFollowerIds().contains(followerId)) {
			 userFollowers.getFollowerIds().remove(followerId);
	    }
	    return userFollowersRepository.save(userFollowers);
	}
}
