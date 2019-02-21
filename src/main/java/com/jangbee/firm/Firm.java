package com.jangbee.firm;

import lombok.Data;
import javax.persistence.*;
import javax.persistence.Entity;

import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.*;

/**
 * Created by test on 2019-01-20.
 */
@Data
@Entity
@SqlResultSetMapping(
        name="groupDetailsMapping",
        classes={
                @ConstructorResult(
                        targetClass=FirmDto.ListResponse.class,
                        columns={
                                @ColumnResult(name="account_id"),
                                @ColumnResult(name="fname"),
                                @ColumnResult(name="phone_number"),
                                @ColumnResult(name="equi_list_str"),
                                @ColumnResult(name="address"),
                                @ColumnResult(name="introduction"),
                                @ColumnResult(name="thumbnail"),
                                @ColumnResult(name="distance", type=Long.class),
                        }
                )
        }
)
@org.hibernate.annotations.NamedNativeQuery(name="Firm.getNearFirm", query = "SELECT account_id, fname, phone_number, thumbnail, introduction, address, equi_list_str, ST_DISTANCE_SPHERE(POINT(?2, ?3), location) AS distance FROM firm WHERE equi_list_str like ?1 ORDER BY distance desc LIMIT ?4, ?5 ", resultSetMapping="groupDetailsMapping")
public class Firm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountId;
    private String fname;
    private String phoneNumber;
    private String equiListStr;
    private String address;
    private String addressDetail;
    private String sidoAddr;
    private String sigunguAddr;
    @Column(name = "location", columnDefinition = "POINT")
    private Point location;
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
