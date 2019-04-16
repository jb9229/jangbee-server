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
        private float period;
        private String detailRequest;
        private Double addrLongitude;
        private Double addrLatitude;
        private String matchedAccId;
        private WorkState workState;
    }

    @Data
    public static class Create{
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
}
