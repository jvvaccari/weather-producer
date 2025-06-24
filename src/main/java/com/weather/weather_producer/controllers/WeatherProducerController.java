package com.weather.weather_producer.controllers;

import com.weather.weather_producer.services.WeatherProducerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;



@RestController
@RequestMapping("/weather")
public class WeatherProducerController {

    private final WeatherProducerService weatherProducerService;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherProducerController(WeatherProducerService weatherProducerService,ObjectMapper objectMapper){
        this.weatherProducerService = weatherProducerService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String test() {
        return "API - Weather Producer";
    }

    @Scheduled(fixedRate = 30000)
    public void fetchAndStoreWeather(
    ) {
        // Polo Norte

        //double latitude = 90.0;
        //double longitude = 0.0;

        // Deserto do Saara

        double latitude = 23.4162;
        double longitude = 25.6628;

        var data = weatherProducerService.getWeather(latitude, longitude);

        try {
            String jsonData = objectMapper.writeValueAsString(data);
            weatherProducerService.sendToConsumer(jsonData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
