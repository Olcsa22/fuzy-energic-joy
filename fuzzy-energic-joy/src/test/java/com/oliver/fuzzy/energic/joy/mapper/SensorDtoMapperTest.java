package com.oliver.fuzzy.energic.joy.mapper;

import com.oliver.fuzzy.energic.joy.model.Sensor;
import com.oliver.fuzzy.energic.joy.model.dto.SensorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensorDtoMapperTest {

    private SensorDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SensorDtoMapper();
    }

    @Test
    void transform_withFullDto_shouldMapAllFields() {
        SensorDTO dto = SensorDTO.builder()
                .id(10L)
                .cityName("Budapest")
                .countryName("Hungary")
                .build();

        Sensor sensor = mapper.transform(dto);

        assertEquals(10L, sensor.getId());
        assertEquals("Budapest", sensor.getCityName());
        assertEquals("Hungary", sensor.getCountryName());
    }

    @Test
    void transform_withNullMetadata_shouldMapNulls() {
        SensorDTO dto = SensorDTO.builder().build();

        Sensor sensor = mapper.transform(dto);

        assertNull(sensor.getId());
        assertNull(sensor.getCityName());
        assertNull(sensor.getCountryName());
    }
}
