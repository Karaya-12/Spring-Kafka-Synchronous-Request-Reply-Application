package com.karaya.kafka.messaging;

import com.karaya.kafka.model.Result;
import com.karaya.kafka.model.Student;

import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Karaya_12
 * Kafka Listener Class
 * Which receives the request messages and responds to the reply topic with calculated student’s 'Result' details.
 */
@Slf4j
@Component
public class StudentResultCalculator {

    /**
     * @KafkaListener --> Subscribe to the request Kafka topic
     * @SendTo --> Enable the listener method the capability to send a response back to another reply topic
     */
    @KafkaListener(topics = "${kafka.request.topic}", groupId = "${kafka.group.id}")
    @SendTo
    public Result handle(Student student) {
        log.info("Calculating Reply Result Based on Received Student Info...");

        // Randomly calculates student result and percentage
        double total = ThreadLocalRandom.current().nextDouble(2.5, 9.9);
        String replyResult = (total > 3.5) ? "Pass" : "Fail";
        String replyPercentage = String.valueOf(total * 10).substring(0, 4) + "%";

        Result result = new Result();
        result.setName(student.getName());
        result.setReplyResult(replyResult);
        result.setPercentage(replyPercentage);
        log.info("Student: " + student.getName() + ", Percentage: " + replyPercentage + ", Status:" + replyResult);

        return result;
    }
}
