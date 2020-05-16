package com.karaya.kafka.model;

import lombok.*;

/**
 * @author Karaya_12
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private String name;
    private String percentage;
    private String replyResult;
}
