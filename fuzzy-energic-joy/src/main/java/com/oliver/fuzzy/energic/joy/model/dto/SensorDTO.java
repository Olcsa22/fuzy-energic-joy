package com.oliver.fuzzy.energic.joy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDTO {


    protected Long id;
    protected String cityName;
    protected String countryName;

}
