package edu.sjsu.expressnest.feeds.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PostEventConsumer {
	
	@KafkaListener(topics = "post-events")
	public void processPostEvents(PostEvent postEvent) {
        switch (postEvent.getType()) {
            case "CREATE":
                log.info("PostCreatedEvent: PostId={}, actionedBy={}", postEvent.getPostId(), postEvent.getActionedBy());
                break;
            case "UPDATE":
                log.info("PostUpdatedEvent: PostId={}, actionedBy={}", postEvent.getPostId(), postEvent.getActionedBy());
                break;
            case "DELETE":
                log.info("PostDeletedEvent: PostId={}, actionedBy={}", postEvent.getPostId(), postEvent.getActionedBy());
                break;
            default:
                log.warn("Unknown EventType: {}", postEvent.getType());
        }
    }

}
