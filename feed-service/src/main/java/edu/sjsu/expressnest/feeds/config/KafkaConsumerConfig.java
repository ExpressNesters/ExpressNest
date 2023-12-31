package edu.sjsu.expressnest.feeds.config;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class KafkaConsumerConfig {

    @Value("${expressnest.feedservice.kafka.server}")
    private String kafkaServer;
    
    @Value("${expressnest.feedservice.kafka.username}")
    private String kafkaUsername;

    @Value("${expressnest.feedservice.kafka.password}")
    private String kafkaPassword;

    @Bean
    public ConsumerFactory<String, PostEvent> postEventConsumerFactory() {
        return createConsumerFactory(PostEvent.class);
    }

    @Bean
    public ConsumerFactory<String, FollowEvent> followEventConsumerFactory() {
        return createConsumerFactory(FollowEvent.class);
    }

    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> eventType) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "feeds-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "PLAIN");
        String jaasTemplate = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\'%s\' password=\'%s\';";
        String jaasCfg = String.format(jaasTemplate, kafkaUsername, kafkaPassword);
        props.put("sasl.jaas.config", jaasCfg);
        
        log.info("props::::::::::::::: {}" , props.values().stream().map(v -> v.toString()).collect(Collectors.joining("\n")));
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(eventType));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PostEvent> postEventKafkaListenerContainerFactory() {
        return createKafkaListenerContainerFactory(postEventConsumerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FollowEvent> followEventKafkaListenerContainerFactory() {
        return createKafkaListenerContainerFactory(followEventConsumerFactory());
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> createKafkaListenerContainerFactory(ConsumerFactory<String, T> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
