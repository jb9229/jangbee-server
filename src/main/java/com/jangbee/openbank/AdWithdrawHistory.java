package com.jangbee.openbank;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by test on 2019-03-08.
 */
@Data
@Entity
public class AdWithdrawHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bank_tran_id;  // "12345678901234567890"
    private String api_tran_dtm;  // "20160310101921567"
    private String bank_name;    // "A0000"
    private String account_alias; // 급여계좌
    private String account_holder_name;    // "홍길동"
    private String tran_amt;    // "50000"
    private String print_content;
}
