package com.jangbee.coupon;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by test on 2019-05-23.
 */
@Entity
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountId;
    @Enumerated(EnumType.ORDINAL)
    private CouponType type;
    private int cpCount;
    private int availCashback;
}
