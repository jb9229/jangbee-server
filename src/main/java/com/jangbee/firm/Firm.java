package com.jangbee.firm;

import com.jangbee.stat.StatDto;
import lombok.Data;
import javax.persistence.*;
import javax.persistence.Entity;
import com.vividsolutions.jts.geom.Point;

/**
 * Created by test on 2019-01-20.
 */
@Data
@Entity
@SqlResultSetMappings({
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
                    @ColumnResult(name="rating", type=Byte.class),
                    @ColumnResult(name="rating_cnt", type=Integer.class),
                    @ColumnResult(name="model_year"),
            }
    )
}
),
@SqlResultSetMapping(
name="firmLocalCountMapping",
classes={
    @ConstructorResult(
            targetClass = StatDto.FirmLocalCount.class,
            columns = {
                    @ColumnResult(name = "sido"),
                    @ColumnResult(name = "firm_count", type = Integer.class),
                    @ColumnResult(name = "model"),
                }
            )
        }
    )
})

@NamedNativeQueries({
        @NamedNativeQuery(name="Firm.getNearFirm", query = "SELECT account_id, fname, phone_number, thumbnail, introduction, address, equi_list_str, ST_DISTANCE_SPHERE(POINT(?2, ?3), location) AS distance, rating, rating_cnt, model_year FROM firm WHERE equi_list_str = ?1 ORDER BY distance LIMIT ?4, ?5 ", resultSetMapping="groupDetailsMapping"),
        @NamedNativeQuery(name="Firm.getFirmLocalCount", query="SELECT f.sido_addr as sido, count(f.sido_addr) as firm_count, SUBSTRING_INDEX(f.equi_list_str, ' ', 1) as model FROM firm f where f.equi_list_str like ?1 group by f.sido_addr, f.equi_list_str order by f.equi_list_str, f.sido_addr", resultSetMapping="firmLocalCountMapping")
})
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
    private String workAlarmSido;
    private String workAlarmSigungu;
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
