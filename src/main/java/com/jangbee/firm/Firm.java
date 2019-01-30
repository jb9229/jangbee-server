package com.jangbee.firm;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by test on 2019-01-20.
 */
@Data
@Entity
public class Firm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountId;
    private String fname;
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
}
