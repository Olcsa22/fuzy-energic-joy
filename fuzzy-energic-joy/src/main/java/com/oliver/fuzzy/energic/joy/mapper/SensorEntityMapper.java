package com.oliver.fuzzy.energic.joy.mapper;

import com.oliver.fuzzy.energic.joy.model.Sensor;
import com.oliver.fuzzy.energic.joy.model.dto.SensorDTO;
import org.springframework.stereotype.Component;

@Component
public class SensorEntityMapper implements Mapper<Sensor, SensorDTO> {
    @Override
    public SensorDTO transform(Sensor fromObject) {
        SensorDTO dto = SensorDTO.builder()
                                 .id(fromObject.getId())
                                 .cityName(fromObject.getCityName())
                                 .countryName(fromObject.getCountryName())
                                 .build();
        return dto;
    }
}
