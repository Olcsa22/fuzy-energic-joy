package com.oliver.fuzzy.energic.joy.service;

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
import com.oliver.fuzzy.energic.joy.service.impl.MetricsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricsServiceImplTest {

    @Mock
    private MetricsRepository metricsRepository;
    @Mock
    private SensorRepository sensorRepository;
    @Mock
    private Mapper metricsEntityMapper;
    @InjectMocks
    private MetricsServiceImpl metricsService;

    private Sensor testSensor;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        testSensor = Sensor.builder().cityName("Budapest").id(1L).countryName("Hungary").build();
    }

    @Test
    void queryMetrics_withSensorIds_shouldReturnAverages() throws ValidationException {
        List<Object[]> averages = Collections.singletonList(new Object[]{25.5, 60.0});
        when(metricsRepository.findAveragesBySensorIdsAndDateRange(anyList(), any(Date.class), any(Date.class)))
                .thenReturn(averages);

        MetricsDTO result = metricsService.queryMetrics(
                List.of(1L), List.of(MetricType.TEMPERATURE, MetricType.HUMIDITY), 7);

        assertNotNull(result.getAverageTemperature());
        assertNotNull(result.getAverageHumidity());
        assertEquals(25.5, result.getAverageTemperature());
        assertEquals(60.0, result.getAverageHumidity());
    }

    @Test
    void queryMetrics_withNoSensorIds_shouldQueryAll() throws ValidationException {
        List<Object[]> averages = Collections.singletonList(new Object[]{20.0, 50.0});
        when(metricsRepository.findAveragesByDateRange(any(Date.class), any(Date.class)))
                .thenReturn(averages);

        MetricsDTO result = metricsService.queryMetrics(
                null, List.of(MetricType.TEMPERATURE), 7);

        assertNotNull(result.getAverageTemperature());
        assertNull(result.getAverageHumidity());
        verify(metricsRepository).findAveragesByDateRange(any(), any());
    }

    @Test
    void queryMetrics_withOnlyTemperature_shouldOnlyReturnTemperature() throws ValidationException {
        List<Object[]> averages = Collections.singletonList(new Object[]{25.0, 60.0});
        when(metricsRepository.findAveragesBySensorIdsAndDateRange(anyList(), any(Date.class), any(Date.class)))
                .thenReturn(averages);

        MetricsDTO result = metricsService.queryMetrics(
                List.of(1L), List.of(MetricType.TEMPERATURE), 7);

        assertNotNull(result.getAverageTemperature());
        assertNull(result.getAverageHumidity());
    }

    @Test
    void queryMetrics_withInvalidDay_shouldThrow() {
        assertThrows(ValidationException.class,
                () -> metricsService.queryMetrics(List.of(1L), List.of(MetricType.TEMPERATURE), -1));
    }

    @Test
    void queryMetrics_withZeroDays_shouldQueryFromStartOfToday() throws ValidationException {
        List<Object[]> averages = Collections.singletonList(new Object[]{20.0, 50.0});
        when(metricsRepository.findAveragesBySensorIdsAndDateRange(anyList(), any(Date.class), any(Date.class)))
                .thenReturn(averages);

        metricsService.queryMetrics(List.of(1L), List.of(MetricType.TEMPERATURE, MetricType.HUMIDITY), 0);

        verify(metricsRepository).findAveragesBySensorIdsAndDateRange(anyList(), any(Date.class), any(Date.class));
    }


    @SuppressWarnings("unchecked")
    @Test
    void getMetricsForSensor_shouldReturnDetailDTOs() {
        Metrics m1 = Metrics.builder().temperature(20.0).humidity(50.0).sensor(testSensor).createdOn(new Date()).build();
        Metrics m2 = Metrics.builder().temperature(25.0).humidity(55.0).sensor(testSensor).createdOn(new Date()).build();
        MetricDetailDTO dto1 = MetricDetailDTO.builder().temperature(20.0).humidity(50.0).sensorId(1L).createdOn(new Date()).build();
        MetricDetailDTO dto2 = MetricDetailDTO.builder().temperature(25.0).humidity(55.0).sensorId(1L).createdOn(new Date()).build();

        when(sensorRepository.existsById(1L)).thenReturn(true);
        when(metricsRepository.findBySensorIdOrderByCreatedOnDesc(1L)).thenReturn(List.of(m1, m2));
        when(metricsEntityMapper.transform(m1)).thenReturn(dto1);
        when(metricsEntityMapper.transform(m2)).thenReturn(dto2);

        List<MetricDetailDTO> result = metricsService.getMetricsForSensor(1L);

        assertEquals(2, result.size());
        assertEquals(20.0, result.get(0).getTemperature());
        verify(metricsEntityMapper, times(2)).transform(any());
    }

    @Test
    void getMetricsForSensor_withNonExistingSensor_shouldThrow() {
        when(sensorRepository.existsById(999L)).thenReturn(false);

        assertThrows(SensorNotFoundException.class, () -> metricsService.getMetricsForSensor(999L));
    }

    @Test
    void generateInitialMetrics_shouldCreateMetrics() throws ValidationException {
        when(metricsRepository.save(any(Metrics.class))).thenAnswer(i -> i.getArgument(0));

        metricsService.generateInitialMetrics(testSensor);

        verify(metricsRepository, atLeastOnce()).save(any(Metrics.class));
    }
}
