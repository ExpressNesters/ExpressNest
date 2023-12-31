package edu.sjsu.expressnest.feeds.messaging;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.feeds.model.UserFeeds;
import edu.sjsu.expressnest.feeds.model.UserFollowers;
import edu.sjsu.expressnest.feeds.repository.UserFeedsRepository;
import edu.sjsu.expressnest.feeds.repository.UserFollowersRepository;
import edu.sjsu.expressnest.feeds.util.PostEventType;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PostEventConsumer {
	
	@Autowired
	private UserFollowersRepository userFollowersRepository;
	
	@Autowired
	private UserFeedsRepository userFeedsRepository;
	
	@KafkaListener(topics = "${expressnest.feedservice.kafka.topicname.postevent}", containerFactory = "postEventKafkaListenerContainerFactory")
	public void processPostEvents(PostEvent postEvent) {
	    PostEventType eventType = PostEventType.valueOf(postEvent.getType());
	    switch (eventType) {
	        case CREATE:
	            handleCreateEvent(postEvent);
	            break;
	        case UPDATE:
	            handleUpdateEvent(postEvent);
	            break;
	        case DELETE:
	            handleDeleteEvent(postEvent);
	            break;
	        default:
	            log.warn("Unknown EventType: {}", eventType);
	            throw new IllegalArgumentException("Invalid EventType: " + eventType);
	    }
	}

	private void handleCreateEvent(PostEvent postEvent) {
	    log.info("Handling PostCreatedEvent: PostId={}, ActionedBy={}", postEvent.getPostId(), postEvent.getActionedBy());
	    userFollowersRepository.findById(postEvent.getActionedBy())
	        .map(UserFollowers::getFollowerIds)
	        .ifPresent(followerIds -> followerIds.forEach(followerId -> 
	            updateUserFeedWithNewPost(followerId, postEvent.getPostId())));
	}

	private void updateUserFeedWithNewPost(long followerId, long postId) {
		 UserFeeds userFeeds = userFeedsRepository.findById(followerId)
				 .orElseGet(() -> newUserFeeds(followerId));
		 userFeeds.getPostIds().add(postId);
		 userFeedsRepository.save(userFeeds);
	}
	
	private UserFeeds newUserFeeds(long followerId) {
	    UserFeeds newUserFeeds = new UserFeeds();
	    newUserFeeds.setUserId(followerId);
	    newUserFeeds.setPostIds(new ArrayList<Long>());
	    return newUserFeeds;
	}

	private void handleUpdateEvent(PostEvent postEvent) {
	    log.info("Handling PostUpdatedEvent: PostId={}, ActionedBy={}", postEvent.getPostId(), postEvent.getActionedBy());
	    // Do nothing. 
	}

	private void handleDeleteEvent(PostEvent postEvent) {
	    log.info("Handling PostDeletedEvent: PostId={}, ActionedBy={}", postEvent.getPostId(), postEvent.getActionedBy());
	    userFollowersRepository.findById(postEvent.getActionedBy())
        .map(UserFollowers::getFollowerIds)
        .ifPresent(followerIds -> followerIds.forEach(followerId -> 
            removePostFromUserFeed(followerId, postEvent.getPostId())));
	}

	private void removePostFromUserFeed(long followerId, long postId) {
	    userFeedsRepository.findById(followerId)
	        .ifPresent(userFeeds -> {
	            userFeeds.getPostIds().remove(postId);
	            userFeedsRepository.save(userFeeds);
	        });
	}

}
