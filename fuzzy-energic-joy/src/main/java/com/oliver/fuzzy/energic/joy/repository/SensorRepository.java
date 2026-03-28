package com.oliver.fuzzy.energic.joy.repository;

import com.oliver.fuzzy.energic.joy.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
