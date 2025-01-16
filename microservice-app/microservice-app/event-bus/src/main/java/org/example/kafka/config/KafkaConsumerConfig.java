package org.example.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.events.MultipleNotificationEvent;
import org.example.events.ReferralRewardedEvent;
import org.example.events.WorkoutPerformedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    private final String bootstrapAddress = "broker:9092";

    @Bean
    public ConsumerFactory<String, MultipleNotificationEvent> multipleNotificationEvent() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(MultipleNotificationEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MultipleNotificationEvent> multipleNotificationEventTemplate() {
        ConcurrentKafkaListenerContainerFactory<String, MultipleNotificationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(multipleNotificationEvent());
        return factory;
    }
    @Bean
    public ConsumerFactory<String, ReferralRewardedEvent> referralRewardedEvent() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(ReferralRewardedEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ReferralRewardedEvent> referralRewardedEventTemplate() {
        ConcurrentKafkaListenerContainerFactory<String, ReferralRewardedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(referralRewardedEvent());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, WorkoutPerformedEvent> workoutPerformedEvent() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(WorkoutPerformedEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WorkoutPerformedEvent> workoutPerformedEventTemplate() {
        ConcurrentKafkaListenerContainerFactory<String, WorkoutPerformedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(workoutPerformedEvent());
        return factory;
    }



}
