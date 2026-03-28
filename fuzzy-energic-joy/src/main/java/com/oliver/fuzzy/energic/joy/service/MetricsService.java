package com.oliver.fuzzy.energic.joy.service;

import com.oliver.fuzzy.energic.joy.enums.MetricType;
import com.oliver.fuzzy.energic.joy.exception.ValidationException;
import com.oliver.fuzzy.energic.joy.model.Sensor;
import com.oliver.fuzzy.energic.joy.model.dto.MetricDetailDTO;
import com.oliver.fuzzy.energic.joy.model.dto.MetricsDTO;

import java.util.List;

public interface MetricsService {
    void generateInitialMetrics(Sensor sensor) throws ValidationException;
    MetricsDTO queryMetrics(List<Long> sensorIds, List<MetricType> metrics, int days) throws ValidationException;
    List<MetricDetailDTO> getMetricsForSensor(Long sensorId);
}
