package com.oliver.fuzzy.energic.joy.mapper;

import com.oliver.fuzzy.energic.joy.model.Metrics;
import com.oliver.fuzzy.energic.joy.model.dto.MetricDetailDTO;
import org.springframework.stereotype.Component;

@Component
public class MetricsEntityMapper implements Mapper<Metrics, MetricDetailDTO> {
    @Override
    public MetricDetailDTO transform(Metrics metrics) {
        return MetricDetailDTO.builder()
                .id(metrics.getId())
                .sensorId(metrics.getSensor().getId())
                .temperature(metrics.getTemperature())
                .humidity(metrics.getHumidity())
                .createdOn(metrics.getCreatedOn())
                .build();
    }
}
