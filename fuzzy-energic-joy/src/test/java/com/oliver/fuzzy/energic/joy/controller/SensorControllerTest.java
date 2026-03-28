package com.oliver.fuzzy.energic.joy.controller;

import com.oliver.fuzzy.energic.joy.exception.SensorNotFoundException;
import com.oliver.fuzzy.energic.joy.exception.ValidationException;
import com.oliver.fuzzy.energic.joy.model.dto.MetricDetailDTO;
import com.oliver.fuzzy.energic.joy.model.dto.MetricsDTO;
import com.oliver.fuzzy.energic.joy.model.dto.SensorDTO;
import com.oliver.fuzzy.energic.joy.service.MetricsService;
import com.oliver.fuzzy.energic.joy.service.SensorService;
import com.oliver.fuzzy.energic.joy.util.Constants;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SensorController.class)
class SensorControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private SensorService sensorService;
    @MockitoBean private MetricsService metricsService;

    @Test
    void getAllSensors_shouldReturnSensorList() throws Exception {
        when(sensorService.findAll()).thenReturn(List.of(
                SensorDTO.builder().id(1L).cityName("Budapest").countryName("Hungary").build(),
                SensorDTO.builder().id(2L).cityName("London").countryName("UK").build()
        ));

        mockMvc.perform(get("/sensors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cityName").value("Budapest"));
    }

    @Test
    void getSensor_shouldReturnSensor() throws Exception {
        when(sensorService.findById(1L)).thenReturn(
                SensorDTO.builder().id(1L).cityName("Budapest").countryName("Hungary").build());

        mockMvc.perform(get("/sensors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cityName").value("Budapest"));
    }

    @Test
    void getSensor_notFound_shouldReturn404WithJson() throws Exception {
        when(sensorService.findById(999L)).thenThrow(new SensorNotFoundException(999L));

        mockMvc.perform(get("/sensors/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        String.format(Constants.Validaiton.SENSOR_NOT_FOUND, 999L)));
    }

    @Test
    void registerSensor_shouldReturnCreated() throws Exception {
        SensorDTO input = SensorDTO.builder().id(3L).cityName("Berlin").countryName("Germany").build();
        SensorDTO output = SensorDTO.builder().id(3L).cityName("Berlin").countryName("Germany").build();
        when(sensorService.register(any(SensorDTO.class))).thenReturn(output);

        mockMvc.perform(post("/sensors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.cityName").value("Berlin"));
    }

    @Test
    void registerSensor_withMissingFields_shouldReturn400WithJson() throws Exception {
        when(sensorService.register(any())).thenThrow(
                new ValidationException(Constants.Validaiton.SENSOR_ID_REQUIRED));

        mockMvc.perform(post("/sensors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Constants.Validaiton.SENSOR_ID_REQUIRED));
    }

    @Test
    void getSensorMetrics_shouldReturnMetricsList() throws Exception {
        when(metricsService.getMetricsForSensor(1L)).thenReturn(List.of(
                MetricDetailDTO.builder().id(1L).sensorId(1L).temperature(20.0).humidity(50.0).createdOn(new Date()).build()
        ));

        mockMvc.perform(get("/sensors/1/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].temperature").value(20.0));
    }

    @Test
    void queryMetrics_shouldReturnAverages() throws Exception {
        MetricsDTO metricsDTO = MetricsDTO.builder().averageTemperature(22.5).averageHumidity(55.0).build();
        when(metricsService.queryMetrics(anyList(), anyList(), eq(7))).thenReturn(metricsDTO);

        mockMvc.perform(get("/sensors/query-metrics")
                        .param("sensorIds", "1", "2")
                        .param("metrics", "TEMPERATURE", "HUMIDITY")
                        .param("days", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageTemperature").value(22.5))
                .andExpect(jsonPath("$.averageHumidity").value(55.0));
    }

    @Test
    void queryMetrics_withInvalidDays_shouldReturn400WithJson() throws Exception {
        when(metricsService.queryMetrics(any(), anyList(), eq(32)))
                .thenThrow(new ValidationException("Days must be between 0 and 31"));

        mockMvc.perform(get("/sensors/query-metrics")
                        .param("metrics", "TEMPERATURE")
                        .param("days", "32"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Days must be between 0 and 31"));
    }
}
