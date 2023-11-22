package edu.sjsu.expressnest.feeds.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import edu.sjsu.expressnest.feeds.messaging.FollowEvent;
import edu.sjsu.expressnest.feeds.messaging.PostEvent;

@Configuration
public class KafkaConsumerConfig {
	
	@Value("$(expressnest.feedservice.kafka.server)")
	private String bootstrapServer;
		
	@Value("$(expressnest.feedservice.kafka.group-id)")
	private String groupId;
	
	@Bean
    public ConsumerFactory<String, PostEvent> postEventConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "feeds-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(PostEvent.class));
    }

    @Bean
    public ConsumerFactory<String, FollowEvent> followEventConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "feeds-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(FollowEvent.class));
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PostEvent> postEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PostEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(postEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FollowEvent> followEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FollowEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(followEventConsumerFactory());
        return factory;
    }

}
