package com.jangbee.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by test on 2015-12-20.
 */
@Setter
@Getter
public class Email {
    private String subject;
    private String content;
    private String receiver;
    private String sender;
}
