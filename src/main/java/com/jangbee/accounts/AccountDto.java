package com.jangbee.accounts;

import java.util.Date;
import javax.validation.constraints.Size;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class AccountDto {

    @Data
    public static class Create {
        @NotBlank
        @Size(min=5, max=100)
        @Email
        private String email;

        @NotBlank
        @Size(min=5, max = 45)
        private String password;

        @NotBlank
        @Size(max = 45)
        private String username;

        private String profilePhoto;

        private boolean female;

        private boolean single;

        private int birth;

        private String residence;

        private Double authMailKey;
    }

    @Data
    public static class Update {
        private String password;
        private String username;
    }


    @Data
    public static class Response{

        private Long id;
        private String email;
        private String username;
        private String profilePhoto;
        private boolean female;
        private boolean single;
        private int birth;
        private Date joined;
        private Date updated;
        private Double authMailKey;
    }


    @Data
    public static class RiceUpdate{
        private int riceTol;

        private int riceTemp;

        private int riceMonth;

        private int riceYear;
    }


    @Data
    public static class RiceResponse{
        private Long id;
        private String username;

        private int riceTol;
        private int riceTemp;
        private int riceMonth;
        private int riceYear;
    }

}
