package edu.sjsu.expressnest.postservice.messaging;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PostEventProducer {

	private final KafkaTemplate<String, PostEvent> kafkaTemplate;
	
	@Value("${expressnest.postservice.kafka.topicname}")
	private String topicName;

	@Autowired
	public PostEventProducer(KafkaTemplate<String, PostEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendEvent(PostEvent message) {
		CompletableFuture<SendResult<String, PostEvent>> future = kafkaTemplate.send(topicName, message);
		log.info("Sending PostEvent={} to topic={}: PostID={}: ActionBy={}", message.getPostEventType(), topicName, message.getPostId(), message.getActionedBy());
		future.whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("SUCCESS: PostEvent={} sent: PostId={}, Offset={}", message.getPostEventType(), message.getPostId(),
						result.getRecordMetadata().offset());
			} else {
				log.error("FAILURE: Unable to send PostEvent={}: PostID={}, Error={}", message.getPostEventType(), message.getPostId(),
						ex.getMessage());
			}
		});
	}
}
