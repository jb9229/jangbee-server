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
    @Data
    public static class Response {
        private Long id;
        private String equipment;
        private String address;
        private String addressDetail;
        private Date startDate;
        private Date selectNoticeTime;
        private float period;
        private String detailRequest;
        private Double addrLongitude;
        private Double addrLatitude;
        private String matchedAccId;
        private WorkState workState;
        private boolean applied;
        private long applicantCount;
        private boolean firmEstimated;
        private boolean overAcceptTime;

        public Object getEndDate() {
            if(startDate == null){return null;}
            long periodDate = (long)(period < 1 ? 1 : period);
            long periodTime = periodDate * 24 * 60* 1000;
            Date endDate = new Date();
            endDate.setTime(startDate.getTime() + periodTime);

            return endDate;
        }

        public boolean isOverAcceptTime() {
            if (workState != null && workState.equals(WorkState.SELECTED) && selectNoticeTime != null) {
                Date beforThreeHour = new Date();
                beforThreeHour.setTime(beforThreeHour.getTime() - (3*24*60*1000));
                if (selectNoticeTime.before(beforThreeHour)){
                    return true;
                }
            }
            return false;
        }
    }

    @Data
    public static class Create{
        @NotBlank
        private String accountId;

        @NotBlank
        @Size(max=25)
        private String equipment;

        @NotBlank
        @Size(max=100)
        private String address;

        @Size(max=45)
        private String addressDetail;

        @NotNull
        private Date startDate;

        @NotNull
        private Float period;

        @NotNull
        @Size(max=500)
        private String detailRequest;

        @NotNull
        private Double addrLongitude;

        @NotNull
        private Double addrLatitude;
    }

    @Data
    public static class Apply {
        @NotNull
        private long workId;
        @NotBlank
        private String accountId;
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
    }
}
