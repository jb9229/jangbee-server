package com.jangbee.estimate;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by test on 2016-02-13.
 */
public class EstimateDto {

    @Data
    public static class Response{
        private Long id;

        private String userName;
        private String mvDate;
        private int amount;

        private String cmAddress;
        private String cmAddressDetail;
        private String cmRegidentType;
        private int cmFloor;
        private int cmSpace;
        private String cmWorkCondition;

        private String nmAddress;
        private String nmAddressDetail;
        private String nmRegidentType;
        private int nmFloor;
        private int nmSpace;
        private String nmWorkCondition;

        private boolean bed;
        private String bedType;
        private boolean airconditioner;
        private String airconditionerType;
        private boolean drawer;
        private String drawerType;
        private boolean sofa;
        private String sofaType;
        private boolean tv;
        private String tvType;
        private boolean piano;
        private String pianoType;
        private boolean waterpurifier;
        private String waterpurifierType;
        private boolean bidet;
        private String bidetType;

        private String entrPhoto;
        private String lrPhoto;
        private String kchPhoto;
        private String rm1Photo;
        private String rm2Photo;
        private String rm3Photo;
        private String rm4Photo;
        private String rm5Photo;

        private String clientAsk;
    }

    @Data
    public static class Create {
        @NotBlank
        @Size(max=45)
        private String userName;
        @NotBlank
        @Size(max=45)
        private String mvDate;
        private int amount;


        @NotBlank
        private String cmAddress;
        @NotBlank
        private String cmAddressDetail;
        private String cmRegidentType;
        private int cmFloor;
        private int cmSpace;
        @Size(max=1000)
        private String cmWorkCondition;

        @NotBlank
        private String nmAddress;
        @NotBlank
        private String nmAddressDetail;
        private String nmRegidentType;
        private int nmFloor;
        private int nmSpace;

        @Size(max=1000)
        private String nmWorkCondition;

        private boolean bed;
        private String bedType;
        private boolean airconditioner;
        private String airconditionerType;
        private boolean drawer;
        private String drawerType;
        private boolean sofa;
        private String sofaType;
        private boolean tv;
        private String tvType;
        private boolean piano;
        private String pianoType;
        private boolean waterpurifier;
        private String waterpurifierType;
        private boolean bidet;
        private String bidetType;

        private String entrPhoto;
        private String lrPhoto;
        private String kchPhoto;
        private String rm1Photo;
        private String rm2Photo;
        private String rm3Photo;
        private String rm4Photo;
        private String rm5Photo;

        private String clientAsk;
    }
}
