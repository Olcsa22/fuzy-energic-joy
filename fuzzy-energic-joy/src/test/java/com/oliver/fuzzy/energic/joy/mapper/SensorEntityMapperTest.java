package com.oliver.fuzzy.energic.joy.mapper;

import com.oliver.fuzzy.energic.joy.model.Sensor;
import com.oliver.fuzzy.energic.joy.model.dto.SensorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensorEntityMapperTest {

    private SensorEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SensorEntityMapper();
    }

    @Test
    void transform_shouldMapAllFields() {
        Sensor sensor = Sensor.builder()
                .id(42L)
                .cityName("London")
                .countryName("UK")
                .build();

        SensorDTO dto = mapper.transform(sensor);

        assertEquals(42L, dto.getId());
        assertEquals("London", dto.getCityName());
        assertEquals("UK", dto.getCountryName());
    }

    @Test
    void transform_withNullMetadata_shouldMapNulls() {
        Sensor sensor = Sensor.builder().build();

        SensorDTO dto = mapper.transform(sensor);

        assertNull(dto.getId());
        assertNull(dto.getCityName());
        assertNull(dto.getCountryName());
    }
}
