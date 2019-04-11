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
        private String obAccTokenExpDate;
        private String obAccTokenDiscDate;
        private String obAccessToken;
        private String obRefreshToken;
        private String obUserSeqNo;

        public Date parseObAccTokenExpDate() {
            if(obAccTokenExpDate == null || obAccTokenExpDate.isEmpty() ){return null;}

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                return dateFormat.parse(obAccTokenExpDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

        public Date parseObAccTokenDiscDate() {
            if(obAccTokenDiscDate == null || obAccTokenDiscDate.isEmpty() ){return null;}

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                return dateFormat.parse(obAccTokenDiscDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
