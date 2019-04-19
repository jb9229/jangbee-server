package com.jangbee.work;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by test on 2019-04-15.
 */
@Data
@Entity
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountId;
    private String equipment;
    private String address;
    private String addressDetail;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    private float period;
    private String detailRequest;
    @Column(name = "address_point", columnDefinition = "POINT")
    private Point addressPoint;
    private Double addrLongitude;
    private Double addrLatitude;
    private String matchedAccId;
    private WorkState workState;
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyNoticeTime;
    private Date selectNoticeTime;
}

enum WorkState {
    OPEN,
    SELECTED,
    MATCHED,
    WORKING,
    CLOSED,
}
