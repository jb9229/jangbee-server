package com.jangbee.client_evalu;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by test on 2019-03-26.
 */
@Data
@Entity
public class ClientEvalu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountId;
    private String cliName;
    private String telNumber;
    private String reason;
    private int likeCount;
    private int unlikeCount;
    @Temporal(TemporalType.DATE)
    private Date updateDate;
}