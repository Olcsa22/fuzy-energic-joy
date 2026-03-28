package com.oliver.fuzzy.energic.joy.service.impl;

import com.oliver.fuzzy.energic.joy.enums.MetricType;
import com.oliver.fuzzy.energic.joy.exception.SensorNotFoundException;
import com.oliver.fuzzy.energic.joy.exception.ValidationException;
import com.oliver.fuzzy.energic.joy.mapper.Mapper;
import com.oliver.fuzzy.energic.joy.model.Metrics;
import com.oliver.fuzzy.energic.joy.model.Sensor;
import com.oliver.fuzzy.energic.joy.model.dto.MetricDetailDTO;
import com.oliver.fuzzy.energic.joy.model.dto.MetricsDTO;
import com.oliver.fuzzy.energic.joy.repository.MetricsRepository;
import com.oliver.fuzzy.energic.joy.repository.SensorRepository;
import com.oliver.fuzzy.energic.joy.service.MetricsService;
import com.oliver.fuzzy.energic.joy.util.DateUtil;
import com.oliver.fuzzy.energic.joy.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class MetricsServiceImpl implements MetricsService {

    private final MetricsRepository metricsRepository;
    private final SensorRepository sensorRepository;
    private final Mapper<Metrics, MetricDetailDTO> metricsEntityMapper;

    @Override
    public void generateInitialMetrics(Sensor sensor) throws ValidationException {

        Random random = new Random();
        int numberOfMetrics = random.nextInt(10);

        for(int i = 0; i <= numberOfMetrics; i++) {

            double temperature = Math.round((-10 + (45 * random.nextDouble())) * 100.0) / 100.0; // -10 to 35
            double humidity = Math.round((20 + (70 * random.nextDouble())) * 100.0) / 100.0;     // 20 to 90
            int daysOld = random.nextInt(60);
            Date creationDate = DateUtil.getDateDaysBeforeNow(daysOld);
            saveMetric(temperature, humidity, sensor, creationDate);
        }
    }

    @Override
    public MetricsDTO queryMetrics(List<Long> sensorIds, List<MetricType> metrics, int days) throws ValidationException {

        Date end = new Date();
        Date start = DateUtil.getDateDaysBeforeNow(days);

        List<Object[]> results;
        if (sensorIds == null || sensorIds.isEmpty()) {
            results = metricsRepository.findAveragesByDateRange(start, end);
        } else {
            results = metricsRepository.findAveragesBySensorIdsAndDateRange(sensorIds, start, end);
        }

        MetricsDTO dto = new MetricsDTO();
        if (results != null && !results.isEmpty()) {
            Object[] row = results.get(0);
            if (metrics.contains(MetricType.TEMPERATURE) && row[0] != null) {
                dto.setAverageTemperature(Math.round(((Number) row[0]).doubleValue() * 100.0) / 100.0);
            }
            if (metrics.contains(MetricType.HUMIDITY) && row[1] != null) {
                dto.setAverageHumidity(Math.round(((Number) row[1]).doubleValue() * 100.0) / 100.0);
            }
        }

        return dto;
    }

    @Override
    public List<MetricDetailDTO> getMetricsForSensor(Long sensorId) {
        if (!sensorRepository.existsById(sensorId)) {
            throw new SensorNotFoundException(sensorId);
        }
        return metricsRepository.findBySensorIdOrderByCreatedOnDesc(sensorId).stream()
                .map(metricsEntityMapper::transform)
                .toList();
    }

    //Normally this method would not exist in this form. But since we need to generate some dummy data for our sensors, I keep it simple
    //In a better use the data would come from an endpoint, or some kind of message queue, etc. in a DTO, would be validated, mapped, and saved.
    private void saveMetric(double temp, double humidity, Sensor sensor, Date creationDate){
        Metrics metrics = Metrics.builder()
                .temperature(temp)
                .humidity(humidity)
                .sensor(sensor)
                .createdOn(creationDate)
                .build();

        metricsRepository.save(metrics);
    }

}
