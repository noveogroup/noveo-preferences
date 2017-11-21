package com.noveogroup.preferences.sample.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class User {
    public static final long NOT_AN_AGE = -1L;
    private String name;
    private long age;
}
