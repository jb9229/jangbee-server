package com.jangbee.work;

/**
 * Created by test on 2019-04-20.
 */
public enum WorkState {
    OPEN,
    SELECTED,
    MATCHED,
    WORKING,
    CLOSED;

    public boolean equals(WorkState compare){
        return this.name().equals(compare.name());
    }
}
