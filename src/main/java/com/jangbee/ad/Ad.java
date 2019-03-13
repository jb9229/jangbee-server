package com.jangbee.ad;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by test on 2019-02-14.
 */
@Data
@Entity
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.ORDINAL)
    private AdType adType;
    private Byte adOrder;
    private String accountId;
    private String equiTarget;
    private String sidoTarget;
    private String gugunTarget;
    private String photoUrl;
    private String title;
    private String subTitle;
    private String telNumber;
    private String fintechUseNum;
    private Integer adPrice;
    @Temporal(TemporalType.DATE)
    private Date nextWithdrawDate;
}

enum AdType {
    MAIN,
    EQUIPMENT,
    LOCAL;
}