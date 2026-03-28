package com.oliver.fuzzy.energic.joy.mapper;

import com.oliver.fuzzy.energic.joy.model.Sensor;
import com.oliver.fuzzy.energic.joy.model.dto.SensorDTO;
import org.springframework.stereotype.Component;

@Component
public class SensorDtoMapper implements Mapper<SensorDTO, Sensor> {
    @Override
    public Sensor transform(SensorDTO fromObject) {
        Sensor sensor = Sensor.builder()
                .id(fromObject.getId())
                .countryName(fromObject.getCountryName())
                .cityName(fromObject.getCityName())
                .build();
        return sensor;
    }
}
