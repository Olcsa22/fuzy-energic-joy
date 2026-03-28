package com.oliver.fuzzy.energic.joy.service.impl;

import com.oliver.fuzzy.energic.joy.exception.SensorNotFoundException;
import com.oliver.fuzzy.energic.joy.exception.ValidationException;
import com.oliver.fuzzy.energic.joy.mapper.Mapper;
import com.oliver.fuzzy.energic.joy.model.Sensor;
import com.oliver.fuzzy.energic.joy.model.dto.SensorDTO;
import com.oliver.fuzzy.energic.joy.repository.SensorRepository;
import com.oliver.fuzzy.energic.joy.service.MetricsService;
import com.oliver.fuzzy.energic.joy.service.SensorService;
import com.oliver.fuzzy.energic.joy.util.Constants;
import com.oliver.fuzzy.energic.joy.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorServiceImpl  implements SensorService {

    private final SensorRepository sensorRepository;
    private final Validator<SensorDTO> sensorDTOValidator;
    private final Mapper<SensorDTO, Sensor> sensorMapper;
    private final Mapper<Sensor, SensorDTO> dtoMapper;
    private final MetricsService metricsService;

    @Override
    @Transactional
    public SensorDTO register(SensorDTO sensorDTO) throws ValidationException {
        sensorDTOValidator.validate(sensorDTO);

        if (sensorRepository.existsById(sensorDTO.getId())) {
            throw new ValidationException(
                    String.format(Constants.Validaiton.SENSOR_ALREADY_EXISTS, sensorDTO.getId()));
        }

        Sensor entity = sensorMapper.transform(sensorDTO);
        entity = sensorRepository.save(entity);
        metricsService.generateInitialMetrics(entity);
        return dtoMapper.transform(entity);
    }

    @Override
    public List<SensorDTO> findAll() {
        return sensorRepository.findAll().stream()
                .map(dtoMapper::transform)
                .toList();
    }

    @Override
    public SensorDTO findById(Long id) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new SensorNotFoundException(id));
        return dtoMapper.transform(sensor);
    }

}
