package com.oliver.fuzzy.energic.joy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricDetailDTO {

    private Long id;
    private Long sensorId;
    private Double temperature;
    private Double humidity;
    private Date createdOn;

}
