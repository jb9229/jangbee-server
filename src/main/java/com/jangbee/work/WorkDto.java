package com.jangbee.work;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by test on 2019-04-15.
 */
public class WorkDto {

    public static boolean isOverAcceptTime(WorkState workState, Date selectNoticeTime) {
        if (workState != null && workState.equals(WorkState.SELECTED) && selectNoticeTime != null) {
            Date beforThreeHour = new Date();
            beforThreeHour.setTime(beforThreeHour.getTime() - (2*24*60*1000));
            if (selectNoticeTime.before(beforThreeHour)){
                return true;
            }
        }
        return false;
    }

    @Data
    public static class FirmResponse {
        private Long id;
        private String accountId;
        private boolean firmRegister;
        private String equipment;
        private String address;
        private String addressDetail;
        private String sidoAddr;
        private String sigunguAddr;
        private Date startDate;
        private Date endDate;
        private Date selectNoticeTime;
        private float period;
        private String detailRequest;
        private Short modelYearLimit;
        private String licenseLimit;
        private Short nondestLimit;
        private Short careerLimit;
        private Double addrLongitude;
        private Double addrLatitude;
        private String matchedAccId;
        private WorkState workState;
        private boolean applied;
        private long applicantCount;
        private boolean firmEstimated;
        private boolean overAcceptTime;
        private boolean guarTimeExpire;
    }

    @Data
    public static class ClientResponse {
        private Long id;
        private boolean firmRegister;
        private String equipment;
        private String phoneNumber;
        private String address;
        private String addressDetail;
        private Date startDate;
        private Date endDate;
        private Date selectNoticeTime;
        private float period;
        private String detailRequest;
        private Short modelYearLimit;
        private String licenseLimit;
        private Short nondestLimit;
        private Short careerLimit;
        private Double addrLongitude;
        private Double addrLatitude;
        private String matchedAccId;
        private WorkState workState;
        private boolean applied;
        private long applicantCount;
        private boolean firmEstimated;
        private boolean overAcceptTime;
        private boolean guarTimeExpire;
    }

    @Data
    public static class FirmMatchedResponse {
        private Long id;
        private boolean firmRegister;
        private String equipment;
        private String phoneNumber;
        private String address;
        private String addressDetail;
        private Date startDate;
        private Date endDate;
        private Date selectNoticeTime;
        private float period;
        private String detailRequest;
        private Short modelYearLimit;
        private String licenseLimit;
        private Short nondestLimit;
        private Short careerLimit;
        private Double addrLongitude;
        private Double addrLatitude;
        private String matchedAccId;
        private WorkState workState;
        private boolean applied;
        private long applicantCount;
        private boolean firmEstimated;
        private boolean overAcceptTime;
    }

    @Data
    public static class Create{
        @NotBlank
        private String accountId;

        private boolean firmRegister;

        @NotBlank
        @Size(max=25)
        private String equipment;

        @NotBlank
        @Size(max=45)
        private String phoneNumber;

        @NotBlank
        @Size(max=100)
        private String address;

        @Size(max=45)
        private String addressDetail;

        @NotBlank
        @Size(max=45)
        private String sidoAddr;

        @NotBlank
        @Size(max=45)
        private String sigunguAddr;

        @NotNull
        private Date startDate;

        @NotNull
        private Float period;

        private Integer guaranteeTime;

        @NotNull
        @Size(max=500)
        private String detailRequest;

        private Short modelYearLimit;
        private String licenseLimit;
        private Short nondestLimit;
        private Short careerLimit;

        @NotNull
        private Double addrLongitude;

        @NotNull
        private Double addrLatitude;
    }

    @Data
    public static class Update {
        @NotNull
        private Long workId;

        @NotBlank
        @Size(max=45)
        private String phoneNumber;

        @NotNull
        @Size(max=500)
        private String detailRequest;
    }

    @Data
    public static class Apply {
        @NotNull
        private long workId;
        @NotBlank
        private String accountId;
        private String authToken;
        private String fintechUseNum;
        private boolean coupon;
    }

    @Data
    public static class Select {
        @NotNull
        private long workId;
        @NotBlank
        private String accountId;
    }


    @Data
    public static class Accept {
        @NotNull
        private long workId;
        @NotBlank
        private String accountId;
        private boolean coupon;
    }

    @Data
    public static class Abandon {
        @NotNull
        private long workId;
        @NotBlank
        private String matchedAccId;
    }
}
