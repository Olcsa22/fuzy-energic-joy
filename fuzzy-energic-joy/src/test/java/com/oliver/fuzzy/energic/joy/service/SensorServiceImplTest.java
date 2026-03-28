package com.oliver.fuzzy.energic.joy.service;

import com.oliver.fuzzy.energic.joy.exception.SensorNotFoundException;
import com.oliver.fuzzy.energic.joy.exception.ValidationException;
import com.oliver.fuzzy.energic.joy.mapper.Mapper;
import com.oliver.fuzzy.energic.joy.model.Sensor;
import com.oliver.fuzzy.energic.joy.model.dto.SensorDTO;
import com.oliver.fuzzy.energic.joy.repository.SensorRepository;
import com.oliver.fuzzy.energic.joy.service.impl.SensorServiceImpl;
import com.oliver.fuzzy.energic.joy.util.Constants;
import com.oliver.fuzzy.energic.joy.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorServiceImplTest {

    @Mock
    private SensorRepository sensorRepository;
    @Mock
    private MetricsService metricsService;
    @Mock
    private Validator<SensorDTO> sensorDTOValidator;
    @Mock(name = "sensorMapper")
    private Mapper sensorMapper;
    @Mock(name = "dtoMapper")
    private Mapper dtoMapper;
    private SensorServiceImpl sensorService;

    private SensorDTO inputDto;
    private Sensor sensorEntity;
    private SensorDTO outputDto;


    @BeforeEach
    void setUp() {
        sensorService = new SensorServiceImpl(sensorRepository, sensorDTOValidator, sensorMapper, dtoMapper, metricsService);

        inputDto = SensorDTO.builder().id(1L).cityName("Budapest").countryName("Hungary").build();
        sensorEntity = Sensor.builder().id(1L).cityName("Budapest").countryName("Hungary").build();
        outputDto = SensorDTO.builder().id(1L).cityName("Budapest").countryName("Hungary").build();
    }

    @SuppressWarnings("unchecked")
    @Test
    void register_shouldValidateSaveAndGenerateMetrics() throws ValidationException {
        when(sensorRepository.existsById(1L)).thenReturn(false);
        when(sensorMapper.transform(any())).thenReturn(sensorEntity);
        when(sensorRepository.save(any(Sensor.class))).thenReturn(sensorEntity);
        when(dtoMapper.transform(any())).thenReturn(outputDto);

        SensorDTO result = sensorService.register(inputDto);

        verify(sensorDTOValidator).validate(inputDto);
        verify(sensorMapper).transform(inputDto);
        verify(sensorRepository).save(sensorEntity);
        verify(metricsService).generateInitialMetrics(sensorEntity);
        verify(dtoMapper).transform(sensorEntity);
        assertEquals(1L, result.getId());
        assertEquals("Budapest", result.getCityName());
    }

    @Test
    void register_whenValidationFails_shouldThrow() throws ValidationException {
        SensorDTO invalidDto = SensorDTO.builder().id(1L).cityName("  ").build();
        doThrow(new ValidationException(Constants.Validaiton.SENSOR_CITY_NAME))
                .when(sensorDTOValidator).validate(invalidDto);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> sensorService.register(invalidDto));
        assertEquals(Constants.Validaiton.SENSOR_CITY_NAME, ex.getMessage());
        verify(sensorRepository, never()).save(any());
    }


    @Test
    void register_withNullId_shouldThrow() throws ValidationException {
        SensorDTO noIdDto = SensorDTO.builder().cityName("Budapest").countryName("Hungary").build();
        doThrow(new ValidationException(Constants.Validaiton.SENSOR_ID_REQUIRED))
                .when(sensorDTOValidator).validate(noIdDto);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> sensorService.register(noIdDto));
        assertEquals(Constants.Validaiton.SENSOR_ID_REQUIRED, ex.getMessage());
    }

    @Test
    void register_withDuplicateId_shouldThrow() throws ValidationException {
        when(sensorRepository.existsById(1L)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> sensorService.register(inputDto));
        assertEquals(String.format(Constants.Validaiton.SENSOR_ALREADY_EXISTS, 1L), ex.getMessage());
        verify(sensorRepository, never()).save(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    void findAll_shouldReturnAllSensors() {
        Sensor s1 = Sensor.builder().id(1L).cityName("A").countryName("X").build();
        Sensor s2 = Sensor.builder().id(2L).cityName("B").countryName("Y").build();
        SensorDTO dto1 = SensorDTO.builder().id(1L).cityName("A").countryName("X").build();
        SensorDTO dto2 = SensorDTO.builder().id(2L).cityName("B").countryName("Y").build();

        when(sensorRepository.findAll()).thenReturn(List.of(s1, s2));
        when(dtoMapper.transform(s1)).thenReturn(dto1);
        when(dtoMapper.transform(s2)).thenReturn(dto2);

        List<SensorDTO> result = sensorService.findAll();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getCityName());
    }

    @SuppressWarnings("unchecked")
    @Test
    void findById_withExistingSensor_shouldReturnDto() {
        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensorEntity));
        when(dtoMapper.transform(sensorEntity)).thenReturn(outputDto);

        SensorDTO result = sensorService.findById(1L);

        assertEquals("Budapest", result.getCityName());
        verify(dtoMapper).transform(sensorEntity);
    }

    @Test
    void findById_withNonExistingSensor_shouldThrow() {
        when(sensorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(SensorNotFoundException.class, () -> sensorService.findById(999L));
    }
}
