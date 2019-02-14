package com.jangbee.ad;

import lombok.Data;

import javax.persistence.*;

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
    private Long firmId;
    private String photoUrl;
    private String title;
    private String subTitle;
    private String telNumber;
}

enum AdType {
    MAIN,
    EQUIPMENT,
    LOCAL;
}
