package com.weather.weather_producer.controllers;

import com.weather.weather_producer.services.WeatherProducerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/weather")
public class WeatherProducerController {

    private final WeatherProducerService weatherProducerService;

    public WeatherProducerController(WeatherProducerService weatherProducerService){
        this.weatherProducerService = weatherProducerService;
    }

    @GetMapping
    public String test() {
        return "API - Weather Producer";
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestBody String message) {
        return "Mensagem enviada com sucesso!";
    }

    @Scheduled(fixedRate = 45000)
    public void fetchAndStoreWeather(
    ) {
        double latitude = 90.0;
        double longitude = 0.0;
        var parsedData = weatherProducerService.getWeather(latitude, longitude).toString();
        weatherProducerService.sendToConsumer(parsedData);
    }
}
