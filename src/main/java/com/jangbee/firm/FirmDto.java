package com.jangbee.firm;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;

/**
 * Created by test on 2019-01-20.
 */
public class FirmDto {
    @Data
    public static class Response {
        private Long id;
        private String accountId;
        private String fname;
        private String phoneNumber;
        private String equiListStr;
        private String address;
        private String addressDetail;
        private String sidoAddr;
        private String sigunguAddr;
        private Double addrLongitude;
        private Double addrLatitude;
        private String introduction;
        private String thumbnail;
        private String photo1;
        private String photo2;
        private String photo3;
        private String blog;
        private String homepage;
        private String sns;
        private Byte rating;
        private int ratingCnt;
        private String modelYear;
    }
    @Data
    public static class ListResponse {
        public ListResponse() {
        }
        public ListResponse(String accountId, String fname, String phoneNumber, String equiListStr, String address, String introduction, String thumbnail, Long distance, Byte rating, Integer ratingCnt, String modelYear) {
            this.accountId = accountId;
            this.fname = fname;
            this.phoneNumber = phoneNumber;
            this.equiListStr = equiListStr;
            this.address = address;
            this.introduction = introduction;
            this.thumbnail = thumbnail;
            this.distance = distance;
            this.rating = rating;
            this.ratingCnt = ratingCnt;
            this.modelYear = modelYear;
        }
        private String accountId;
        private String fname;
        private String phoneNumber;
        private String equiListStr;
        private String address;
//        private String sidoAddr;
//        private String sigunguAddr;
        private Long distance;
        private String introduction;
        private String thumbnail;
//        private String blog;
//        private String homepage;
//        private String sns;
        private Byte rating;
        private int ratingCnt;
        private String modelYear;
    }
    @Data
    public static class Create{
        @NotNull
        private String accountId;
        @NotBlank
        @Size(max=45)
        private String fname;

        @NotBlank
        @Size(max=45)
        private String phoneNumber;

        @NotBlank
        @Size(max=200)
        private String equiListStr;

        @NotBlank
        @Size(max=5)
        private String modelYear;

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
        private Double addrLongitude;

        @NotNull
        private Double addrLatitude;

        @Size(max=1000)
        private String introduction;

        @NotBlank
        @Size(max=250)
        private String thumbnail;

        @NotBlank
        @Size(max=250)
        private String photo1;

        @Size(max=250)
        private String photo2;

        @Size(max=250)
        private String photo3;

        @Size(max=250)
        private String blog;

        @Size(max=250)
        private String homepage;

        @Size(max=250)
        private String sns;
    }

    @Data
    public static class Update {

        @NotNull
        private Long id;

        @NotNull
        private String accountId;
        @NotBlank
        @Size(max=45)
        private String fname;

        @NotBlank
        @Size(max=45)
        private String phoneNumber;

        @NotBlank
        @Size(max=200)
        private String equiListStr;

        @NotBlank
        @Size(max=5)
        private String modelYear;

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
        private Double addrLongitude;

        @NotNull
        private Double addrLatitude;

        @Size(max=1000)
        private String introduction;

        @NotBlank
        @Size(max=250)
        private String thumbnail;

        @NotBlank
        @Size(max=250)
        private String photo1;

        @Size(max=250)
        private String photo2;

        @Size(max=250)
        private String photo3;

        @Size(max=250)
        private String blog;

        @Size(max=250)
        private String homepage;

        @Size(max=250)
        private String sns;
    }
}
