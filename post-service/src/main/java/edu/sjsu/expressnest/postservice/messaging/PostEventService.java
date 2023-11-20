package edu.sjsu.expressnest.postservice.messaging;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.postservice.util.EventType;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PostEventService {

	private final KafkaTemplate<String, PostEvent> kafkaTemplate;

	@Autowired
	public PostEventService(KafkaTemplate<String, PostEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendEvent(PostEvent message, EventType eventType) {
		CompletableFuture<SendResult<String, PostEvent>> future = kafkaTemplate.send(eventType.getTopicName(), message);
		log.info("Sending {} Event: PostID={}, ActionBy={}", eventType, message.getPostId(), message.getActionedBy());
		future.whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("SUCCESS: {} Event sent: PostId={}, Offset={}", eventType, message.getPostId(),
						result.getRecordMetadata().offset());
			} else {
				log.error("FAILURE: Unable to send {} Event: PostID={}, Error={}", eventType, message.getPostId(),
						ex.getMessage(), ex);
			}
		});
	}
}
