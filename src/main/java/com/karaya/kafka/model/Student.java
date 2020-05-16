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
public class Student {

    private String regNumber;
    private String name;
    private String grade;
}
