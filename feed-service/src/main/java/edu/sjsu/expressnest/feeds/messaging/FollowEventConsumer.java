package edu.sjsu.expressnest.feeds.messaging;

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
	
	private UserFollowersRepository userFollowersRepository;
	
	@KafkaListener(topics = "follow-events")
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

	private UserFollowers handleFollowEvent(FollowEvent followEvent) throws ResourceNotFoundException {
	    log.info("FollowEvent: User={} followed User={}", followEvent.getFollowerId(), followEvent.getFolloweeId());
	    return userFollowersRepository.findById(followEvent.getFolloweeId())
	        .map(userFollowers -> updateUserFollowersWithNewFollower(userFollowers, followEvent.getFollowerId()))
	        .orElseThrow(() -> new ResourceNotFoundException("UserID does not exist for FolloweeId: " + followEvent.getFolloweeId()));
	}

	private UserFollowers handleUnfollowEvent(FollowEvent followEvent) throws ResourceNotFoundException {
	    log.info("UnfollowEvent: User={} unfollowed User={}", followEvent.getFollowerId(), followEvent.getFolloweeId());
	    return userFollowersRepository.findById(followEvent.getFolloweeId())
	        .map(userFollowers -> updateUserFollowersToRemoveFollower(userFollowers, followEvent.getFollowerId()))
	        .orElseThrow(() -> new ResourceNotFoundException("UserID does not exist for FolloweeId: " + followEvent.getFolloweeId()));
	}

	private UserFollowers updateUserFollowersWithNewFollower(UserFollowers userFollowers, long followerId) {
	    userFollowers.getFollowerIds().add(followerId);
	    return userFollowersRepository.save(userFollowers);
	}

	private UserFollowers updateUserFollowersToRemoveFollower(UserFollowers userFollowers, long followerId) {
	    userFollowers.getFollowerIds().remove(followerId);
	    return userFollowersRepository.save(userFollowers);
	}
}
