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
    private boolean firmRegister;
    private String equipment;
    private String phoneNumber;
    private String address;
    private String addressDetail;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date guaranteeTime;

    public void calEndDate() {
        if(startDate == null){return;}

        long periodDate = (long)(period < 1 ? 0 : period-1);
        long periodTime = periodDate * 24 * 60 * 60 * 1000;
        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + periodTime);

        setEndDate(endDate);
    }

    public String getPeriodStr() {
        if (Float.compare(period, (float)0.3) == 0){ return "오전"; }
        if (Float.compare(period, (float)0.8) == 0){ return "오후"; }

        return (int)period+"일";
    }
}
