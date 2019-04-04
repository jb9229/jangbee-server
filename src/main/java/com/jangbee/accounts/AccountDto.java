package com.jangbee.accounts;

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
        Integer userType;
        String expoPushToken;
        @Temporal(TemporalType.DATE)
        private Date obAccTokenExpDate;
        @Temporal(TemporalType.DATE)
        private Date obAccTokenDiscDate;
        private String obAccessToken;
        private String obRefreshToken;
        private String obUserSeqNo;
    }
}
