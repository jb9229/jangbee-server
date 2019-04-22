package com.jangbee.firm_evalu;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by test on 2019-04-18.
 */
@Data
@Entity
public class FirmEvalu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long workId;
    private String accountId;
    private String firmAccountId;
    private byte rating;
    private String comment;
}
