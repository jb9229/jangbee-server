package com.jangbee.client_evalu;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by test on 2019-03-28.
 */
@Data
@Entity
public class ClientEvaluLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountId;
    private Long evaluId;
    private boolean evaluLike;
    private String reason;
    @Temporal(TemporalType.DATE)
    private Date updateDate;
}
