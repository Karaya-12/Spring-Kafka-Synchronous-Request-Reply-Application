package com.karaya.kafka.config;

import com.karaya.kafka.model.Result;
import com.karaya.kafka.model.Student;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

/**
 * @author Karaya_12
 * Define 2 Following Beans
 * KafkaTemplate & ReplyingKafkaTemplate
 */
@Configuration
public class KafkaConfig {

    @Value("${kafka.group.id}")
    private String groupId;

    @Value("${kafka.reply.topic}")
    private String replyTopic;

    /**
     * ReplyingKafkaTemplate<K, V, R> --> ReplyingKafkaTemplate<String, Student, Result>
     * K – Key type, V – Outbound Data Type, R – Reply Data Type
     * ReplyingKafkaTemplate offers a Return/Reply Object once the message is consumed by the Kafka listener from the other side.
     * Here send the 'Student' details to Kafka and get 'Result' as reply data type
     */
    @Bean
    public ReplyingKafkaTemplate<String, Student, Result> replyingKafkaTemplate(
            ProducerFactory<String, Student> pf, ConcurrentKafkaListenerContainerFactory<String, Result> factory) {
        ConcurrentMessageListenerContainer<String, Result> replyContainer = factory.createContainer(replyTopic);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(groupId);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    /**
     * Served as the reply template with producer factory having data type: ProducerFactory<String, Result>
     */
    @Bean
    public KafkaTemplate<String, Result> replyTemplate(
            ProducerFactory<String, Result> pf, ConcurrentKafkaListenerContainerFactory<String, Result> factory) {
        KafkaTemplate<String, Result> kafkaTemplate = new KafkaTemplate<>(pf);

        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);

        return kafkaTemplate;
    }
}
