package com.oliver.fuzzy.energic.joy;

import com.oliver.fuzzy.energic.joy.util.Constants;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Test
    @Order(1)
    void registerSensor_shouldCreateSensorWithProvidedId() throws Exception {
        String body = """
                {"id":100,"cityName":"Budapest","countryName":"Hungary"}""";

        mockMvc.perform(post("/sensors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.cityName").value("Budapest"))
                .andExpect(jsonPath("$.countryName").value("Hungary"));
    }

    @Test
    @Order(2)
    void registerSensor_withEmptyBody_shouldReturn400WithMessage() throws Exception {
        mockMvc.perform(post("/sensors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Constants.Validaiton.SENSOR_ID_REQUIRED));
    }

    @Test
    @Order(3)
    void registerSensor_withoutCityName_shouldReturn400WithMessage() throws Exception {
        mockMvc.perform(post("/sensors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":200,"countryName":"Hungary"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Constants.Validaiton.SENSOR_CITY_NAME));
    }

    @Test
    @Order(4)
    void registerSensor_withoutCountryName_shouldReturn400WithMessage() throws Exception {
        mockMvc.perform(post("/sensors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":200,"cityName":"Budapest"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Constants.Validaiton.SENSOR_COUNTRY_NAME));
    }

    @Test
    @Order(5)
    void registerSensor_withBlankCity_shouldReturn400WithMessage() throws Exception {
        mockMvc.perform(post("/sensors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":200,"cityName":"   ","countryName":"Hungary"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Constants.Validaiton.SENSOR_CITY_NAME));
    }

    @Test
    @Order(6)
    void registerSensor_withDuplicateId_shouldReturn400WithMessage() throws Exception {
        mockMvc.perform(post("/sensors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":100,"cityName":"Berlin","countryName":"Germany"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        String.format(Constants.Validaiton.SENSOR_ALREADY_EXISTS, 100L)));
    }

    @Test
    @Order(7)
    void getAllSensors_shouldReturnRegisteredSensors() throws Exception {
        mockMvc.perform(get("/sensors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)));
    }

    @Test
    @Order(8)
    void registerAndGetSensorById_shouldReturnSensor() throws Exception {
        mockMvc.perform(post("/sensors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":200,"cityName":"Berlin","countryName":"Germany"}"""))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/sensors/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200))
                .andExpect(jsonPath("$.cityName").value("Berlin"))
                .andExpect(jsonPath("$.countryName").value("Germany"));
    }

    @Test
    @Order(9)
    void getSensorById_nonExistent_shouldReturn404WithMessage() throws Exception {
        mockMvc.perform(get("/sensors/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        String.format(Constants.Validaiton.SENSOR_NOT_FOUND, 99999L)));
    }

    @Test
    @Order(10)
    void getSensorMetrics_shouldReturnGeneratedMetrics() throws Exception {
        // Sensor 100 was registered in test 1 and has auto-generated metrics
        mockMvc.perform(get("/sensors/100/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)));
    }

    @Test
    @Order(11)
    void queryMetrics_forSpecificSensor_shouldReturnAverages() throws Exception {
        mockMvc.perform(get("/sensors/query-metrics")
                        .param("sensorIds", "100")
                        .param("metrics", "TEMPERATURE", "HUMIDITY")
                        .param("days", "31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageTemperature").exists())
                .andExpect(jsonPath("$.averageHumidity").exists());
    }

    @Test
    @Order(12)
    void queryMetrics_allSensors_shouldWork() throws Exception {
        mockMvc.perform(get("/sensors/query-metrics")
                        .param("metrics", "TEMPERATURE")
                        .param("days", "31"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(13)
    void queryMetrics_withOnlyTemperature_shouldReturnTemperatureOnly() throws Exception {
        mockMvc.perform(get("/sensors/query-metrics")
                        .param("sensorIds", "100")
                        .param("metrics", "TEMPERATURE")
                        .param("days", "31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageTemperature").exists())
                .andExpect(jsonPath("$.averageHumidity").doesNotExist());
    }

    @Test
    @Order(14)
    void getSensorMetrics_forNonExistentSensor_shouldReturn404() throws Exception {
        mockMvc.perform(get("/sensors/99999/metrics"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        String.format(Constants.Validaiton.SENSOR_NOT_FOUND, 99999L)));
    }
}
