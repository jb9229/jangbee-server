package com.jangbee.firm;

/**
 * Created by test on 2019-01-24.
 */
public class FirmNotFoundException  extends RuntimeException {
    Long id;

    public FirmNotFoundException(Long id){
        this.id     =   id;
    }


    public Long getId() {
        return id;
    }

}
