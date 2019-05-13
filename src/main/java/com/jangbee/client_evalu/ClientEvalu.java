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
    private String regiTelNumber;
    private String cliName;
    private String firmName;
    private String telNumber;
    private String telNumber2;
    private String telNumber3;
    private String firmNumber;
    private String equipment;
    private String local;
    private String amount;
    private String reason;
    private int likeCount;
    private int unlikeCount;
    @Temporal(TemporalType.DATE)
    private Date updateDate;
}
