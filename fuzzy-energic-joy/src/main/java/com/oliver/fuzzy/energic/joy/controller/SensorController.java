package com.oliver.fuzzy.energic.joy.controller;

import com.oliver.fuzzy.energic.joy.enums.MetricType;
import com.oliver.fuzzy.energic.joy.exception.ValidationException;
import com.oliver.fuzzy.energic.joy.model.dto.MetricDetailDTO;
import com.oliver.fuzzy.energic.joy.model.dto.MetricsDTO;
import com.oliver.fuzzy.energic.joy.model.dto.SensorDTO;
import com.oliver.fuzzy.energic.joy.service.MetricsService;
import com.oliver.fuzzy.energic.joy.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService service;
    private final MetricsService metricsService;

    @GetMapping
    public ResponseEntity<List<SensorDTO>> getAllSensors() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDTO> getSensor(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<SensorDTO> registerSensor(@RequestBody SensorDTO sensorDTO) throws ValidationException {
        SensorDTO dto = service.register(sensorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{sensorId}/metrics")
    public ResponseEntity<List<MetricDetailDTO>> getSensorMetrics(@PathVariable Long sensorId) {
        return ResponseEntity.ok(metricsService.getMetricsForSensor(sensorId));
    }

    @GetMapping("/query-metrics")
    public ResponseEntity<MetricsDTO> queryMetrics(
            @RequestParam(required = false) final List<Long> sensorIds,
            @RequestParam final List<MetricType> metrics,
            @RequestParam(defaultValue = "0") final int days) throws ValidationException {
        MetricsDTO result = metricsService.queryMetrics(sensorIds, metrics, days);
        return ResponseEntity.ok(result);
    }

}
