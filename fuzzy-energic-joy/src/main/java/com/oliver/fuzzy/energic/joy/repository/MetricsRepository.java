package com.oliver.fuzzy.energic.joy.repository;

import com.oliver.fuzzy.energic.joy.model.Metrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MetricsRepository extends JpaRepository<Metrics, Long> {

    List<Metrics> findBySensorIdOrderByCreatedOnDesc(Long sensorId);

    @Query("SELECT AVG(m.temperature), AVG(m.humidity) FROM Metrics m " +
           "WHERE m.sensor.id IN :sensorIds AND m.createdOn BETWEEN :start AND :end")
    List<Object[]> findAveragesBySensorIdsAndDateRange(
            @Param("sensorIds") List<Long> sensorIds,
            @Param("start") Date start,
            @Param("end") Date end);

    @Query("SELECT AVG(m.temperature), AVG(m.humidity) FROM Metrics m " +
           "WHERE m.createdOn BETWEEN :start AND :end")
    List<Object[]> findAveragesByDateRange(
            @Param("start") Date start,
            @Param("end") Date end);
}
