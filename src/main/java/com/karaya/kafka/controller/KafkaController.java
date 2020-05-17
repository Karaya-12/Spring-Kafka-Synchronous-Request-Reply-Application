package com.karaya.kafka.controller;

import com.karaya.kafka.model.Result;
import com.karaya.kafka.model.Student;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @author Karaya_12
 * Accept posted Student details and pass the information to the Kafka server to get the calculated result.
 */
@Slf4j
@RestController
public class KafkaController {

    @Value("${kafka.request.topic}")
    private String requestTopic;

    private final ReplyingKafkaTemplate<String, Student, Result> replyingKafkaTemplate;

    @Autowired
    public KafkaController(ReplyingKafkaTemplate<String, Student, Result> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    /**
     * Expect: 'Student' Object
     * Return: Calculated 'Result' Object
     */
    @PostMapping(value = "/getResult")
    public ResponseEntity<Result> getObject(@RequestBody Student student)
            throws InterruptedException, ExecutionException {
        // ProducerRecord<Topic, Partition, Key, Outbound Data Type>()
        ProducerRecord<String, Student> record =
                new ProducerRecord<>(requestTopic, null, student.getRegNumber(), student);

        // RequestReplyFuture
        RequestReplyFuture<String, Student, Result> future = replyingKafkaTemplate.sendAndReceive(record);

        // ConsumerRecord response from RequestReplyFuture
        ConsumerRecord<String, Result> response = future.get();

        return new ResponseEntity<>(response.value(), HttpStatus.OK);
    }
}
