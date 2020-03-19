package com.jangbee.accounts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class AccountDto {
    @Data
    public static class FirebaseUser {
        private Integer userType;
        private String expoPushToken;
        private String sid;
    }
}
