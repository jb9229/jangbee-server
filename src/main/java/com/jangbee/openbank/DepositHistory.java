package com.jangbee.openbank;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by test on 2019-07-05.
 */
@Data
@Entity
public class DepositHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bank_tran_id;  // "12345678901234567890"
    private String bank_tran_date;  // "20160310101921567"
    private String account_alias;  //"급여계좌"
    private String bank_name; // 오픈은행
    private String account_num_masked; // "000-1230000-***"
    private String print_content; // 쇼핑몰환불",
    private String account_holder_name; // "홍길동",
    private String tran_amt; // "10000“
}
