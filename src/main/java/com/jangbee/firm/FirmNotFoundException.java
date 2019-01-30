package com.jangbee.firm;

/**
 * Created by test on 2019-01-24.
 */
public class FirmNotFoundException  extends RuntimeException {
    String accountId;

    public FirmNotFoundException(String accountId){
        this.accountId     =   accountId;
    }


    public String getAccountId() {
        return accountId;
    }

}
