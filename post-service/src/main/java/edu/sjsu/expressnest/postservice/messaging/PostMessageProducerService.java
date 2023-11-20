package edu.sjsu.expressnest.postservice.messaging;

import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostMessageProducerService {

    private final KafkaTemplate<String, PostMessage> kafkaTemplate;

    @Autowired
    public PostMessageProducerService(KafkaTemplate<String, PostMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<RecordMetadata > sendMessage(String topic, PostMessage data) {
    	CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message + 
                    "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" + 
                    message + "] due to : " + ex.getMessage());
            }
        });
}
