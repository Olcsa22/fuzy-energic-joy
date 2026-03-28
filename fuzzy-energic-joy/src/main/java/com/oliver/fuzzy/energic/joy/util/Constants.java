package com.oliver.fuzzy.energic.joy.util;

public final class Constants {

    private Constants(){
        throw new RuntimeException("Utility class can not be instantiated");
    }

    public static final class Validaiton{
        public static final String SENSOR_DATA_REQUIRED = "Sensor data is required";
        public static final String SENSOR_ID_REQUIRED = "Sensor id is required";
        public static final String SENSOR_CITY_NAME = "City name can not be blank";
        public static final String SENSOR_COUNTRY_NAME = "Country name can not be blank";
        public static final String SENSOR_ALREADY_EXISTS = "Sensor with id %d already exists";
        public static final String SENSOR_NOT_FOUND = "Sensor not found with id: %d";
        public static final String DAYS_NEGATIVE = "Number of days can not be negative";
    }

}
