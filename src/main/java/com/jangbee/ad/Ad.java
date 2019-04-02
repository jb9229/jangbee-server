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
    public static final short ADTYPE_MAIN_FIRST = 1;
    public static final short ADTYPE_MAIN_SECONDE = 2;
    public static final short ADTYPE_MAIN_THIRD = 3;
    public static final short ADTYPE_EQUIPMENT_FIRST = 11;
    public static final short ADTYPE_EQUIPMENT_SECONDE = 12;
    public static final short ADTYPE_EQUIPMENT_THIRD = 13;
    public static final short ADTYPE_LOCAL_FIRST = 21;
    public static final short ADTYPE_LOCAL_SECONDE = 22;
    public static final short ADTYPE_LOCAL_THIRD = 23;
    public static final short ADTYPE_SEARCH_FIRST = 31;
    public static final short ADTYPE_SEARCH_SECONDE = 32;
    public static final short ADTYPE_SEARCH_THIRD = 33;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private short adType;
    @Enumerated(EnumType.ORDINAL)
    private AdLocation adLocation;
    private String accountId;
    private String equiTarget;
    private String sidoTarget;
    private String gugunTarget;
    private String title;
    private String subTitle;
    private String photoUrl;
    private String telNumber;
    private String fintechUseNum;
    private int price;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Temporal(TemporalType.DATE)
    private Date nextWithdrawDate;
}

enum AdType {
    MAIN_FIRST("메인광고_첫번째", 7),
    MAIN_SECONDE("메인광고_두번째", 5),
    MAIN_THIRD("메인광고_세번째", 5),
    TARGET_EQUI_FIRST("장비 타켓광고_첫번째", 1),
    TARGET_EQUI_SECONDE("장비 타켓광고_두번째", 2),
    TARGET_EQUI_THIRD("장비 타켓광고_세번째", 3),
    TARGET_LOCAL_FIRST("지역 타켓광고_첫번째", 1),
    TARGET_LOCAL_SECONDE("지역 타켓광고_두번째", 2),
    TARGET_LOCAL_THIRD("지역 타켓광고_세번째", 3);

    private String description;
    private int defaultPrice;

    AdType(String description, int price){
        this.description = description;
        this.defaultPrice = price;
    }
}

enum AdLocation {
    MAIN,
    EQUIPMENT,
    LOCAL,
    SEARCH,
}