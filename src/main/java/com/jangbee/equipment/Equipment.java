package com.jangbee.equipment;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by test on 2019-01-15.
 */

@Entity
@Data
public class Equipment {
    @Id
    @GeneratedValue
    private Long eId;

    private String eName;
}
