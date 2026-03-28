package com.oliver.fuzzy.energic.joy.repository;

import com.oliver.fuzzy.energic.joy.model.Metrics;
import com.oliver.fuzzy.energic.joy.model.Sensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MetricsRepositoryTest {

    @Autowired
    private MetricsRepository metricsRepository;
    @Autowired
    private SensorRepository sensorRepository;

    private Sensor sensor1;
    private Sensor sensor2;

    @BeforeEach
    void setUp() {
        metricsRepository.deleteAll();
        sensorRepository.deleteAll();

        sensor1 = sensorRepository.save(Sensor.builder().id(1L).cityName("Budapest").countryName("Hungary").build());
        sensor2 = sensorRepository.save(Sensor.builder().id(2L).cityName("London").countryName("UK").build());

        // Metrics for sensor1
        saveMetric(25.0, 60.0, sensor1, daysAgo(1));
        saveMetric(27.0, 65.0, sensor1, daysAgo(2));
        saveMetric(15.0, 40.0, sensor1, daysAgo(10));

        // Metrics for sensor2
        saveMetric(20.0, 50.0, sensor2, daysAgo(1));
        saveMetric(22.0, 55.0, sensor2, daysAgo(3));
    }

    @Test
    void findBySensorIdOrderByCreatedOnDesc_shouldReturnOrderedResults() {
        List<Metrics> results = metricsRepository.findBySensorIdOrderByCreatedOnDesc(sensor1.getId());

        assertEquals(3, results.size());
        assertTrue(results.get(0).getCreatedOn().after(results.get(1).getCreatedOn())
                || results.get(0).getCreatedOn().equals(results.get(1).getCreatedOn()));
    }

    @Test
    void findAveragesBySensorIdsAndDateRange_shouldReturnCorrectAverages() {
        Date start = daysAgo(3);
        Date end = new Date();

        List<Object[]> results = metricsRepository.findAveragesBySensorIdsAndDateRange(
                List.of(sensor1.getId()), start, end);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        Object[] row = results.get(0);
        // Should include 25.0 and 27.0 temperature readings (avg = 26.0)
        Number avgTemp = (Number) row[0];
        assertNotNull(avgTemp);
        assertEquals(26.0, avgTemp.doubleValue(), 0.1);
    }

    @Test
    void findAveragesByDateRange_shouldIncludeAllSensors() {
        Date start = daysAgo(5);
        Date end = new Date();

        List<Object[]> results = metricsRepository.findAveragesByDateRange(start, end);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        Object[] row = results.get(0);
        Number avgTemp = (Number) row[0];
        assertNotNull(avgTemp);
        // Should include sensor1 (25.0, 27.0) and sensor2 (20.0, 22.0) = avg 23.5
        assertEquals(23.5, avgTemp.doubleValue(), 0.1);
    }

    @Test
    void findAveragesBySensorIdsAndDateRange_noResults_shouldReturnNullAverages() {
        Date start = daysAgo(100);
        Date end = daysAgo(90);

        List<Object[]> results = metricsRepository.findAveragesBySensorIdsAndDateRange(
                List.of(sensor1.getId()), start, end);

        // Aggregate with no matching rows returns a list with one row of nulls
        assertNotNull(results);
        assertFalse(results.isEmpty());
        Object[] row = results.get(0);
        assertNull(row[0]);
        assertNull(row[1]);
    }

    private void saveMetric(double temp, double humidity, Sensor sensor, Date createdOn) {
        metricsRepository.save(Metrics.builder()
                .temperature(temp)
                .humidity(humidity)
                .sensor(sensor)
                .createdOn(createdOn)
                .build());
    }

    private Date daysAgo(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -days);
        return cal.getTime();
    }
}
