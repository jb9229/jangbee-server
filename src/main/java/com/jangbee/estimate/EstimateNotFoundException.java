package com.jangbee.estimate;

/**
 * Created by jeong on 2016-04-06.
 */
public class EstimateNotFoundException extends RuntimeException {
    Long id;

    public EstimateNotFoundException(Long id){
        this.id     =   id;
    }


    public Long getId() {
        return id;
    }

}