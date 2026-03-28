package com.oliver.fuzzy.energic.joy.service;

import com.oliver.fuzzy.energic.joy.exception.ValidationException;
import com.oliver.fuzzy.energic.joy.model.dto.SensorDTO;

import java.util.List;

public interface SensorService {
    SensorDTO register(SensorDTO sensorDTO) throws ValidationException;
    List<SensorDTO> findAll();
    SensorDTO findById(Long id);
}
