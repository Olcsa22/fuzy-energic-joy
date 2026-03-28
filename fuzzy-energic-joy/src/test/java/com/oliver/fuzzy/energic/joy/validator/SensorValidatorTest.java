package com.oliver.fuzzy.energic.joy.validator;

import com.oliver.fuzzy.energic.joy.exception.ValidationException;
import com.oliver.fuzzy.energic.joy.model.dto.SensorDTO;
import com.oliver.fuzzy.energic.joy.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensorValidatorTest {

    private SensorValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SensorValidator();
    }

    @Test
    void validate_withValidSensor_shouldNotThrow() {
        SensorDTO dto = SensorDTO.builder()
                .id(1L)
                .cityName("Budapest")
                .countryName("Hungary")
                .build();
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void validate_withNullObject_shouldThrow() {
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(null));
        assertEquals(Constants.Validaiton.SENSOR_DATA_REQUIRED, ex.getMessage());
    }

    @Test
    void validate_withNullId_shouldThrow() {
        SensorDTO dto = SensorDTO.builder()
                .cityName("Budapest")
                .countryName("Hungary")
                .build();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(dto));
        assertEquals(Constants.Validaiton.SENSOR_ID_REQUIRED, ex.getMessage());
    }

    @Test
    void validate_withBlankCityName_shouldThrow() {
        SensorDTO dto = SensorDTO.builder()
                .id(1L)
                .cityName("   ")
                .countryName("Hungary")
                .build();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(dto));
        assertEquals(Constants.Validaiton.SENSOR_CITY_NAME, ex.getMessage());
    }

    @Test
    void validate_withNullCityName_shouldThrow() {
        SensorDTO dto = SensorDTO.builder()
                .id(1L)
                .countryName("Hungary")
                .build();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(dto));
        assertEquals(Constants.Validaiton.SENSOR_CITY_NAME, ex.getMessage());
    }

    @Test
    void validate_withBlankCountryName_shouldThrow() {
        SensorDTO dto = SensorDTO.builder()
                .id(1L)
                .cityName("Budapest")
                .countryName("")
                .build();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(dto));
        assertEquals(Constants.Validaiton.SENSOR_COUNTRY_NAME, ex.getMessage());
    }

    @Test
    void validate_withNullCountryName_shouldThrow() {
        SensorDTO dto = SensorDTO.builder()
                .id(1L)
                .cityName("Budapest")
                .build();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(dto));
        assertEquals(Constants.Validaiton.SENSOR_COUNTRY_NAME, ex.getMessage());
    }

}
