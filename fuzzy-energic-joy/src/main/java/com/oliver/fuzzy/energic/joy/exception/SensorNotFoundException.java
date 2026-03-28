package com.oliver.fuzzy.energic.joy.exception;

import com.oliver.fuzzy.energic.joy.util.Constants;

public class SensorNotFoundException extends RuntimeException {

    public SensorNotFoundException(Long sensorId) {
        super(String.format(Constants.Validaiton.SENSOR_NOT_FOUND, sensorId));
    }

}
