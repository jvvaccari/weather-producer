package com.weather.weather_producer.controllers;

import com.weather.weather_producer.services.WeatherProducerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherListenerController {

    private final WeatherProducerService weatherProducerService;

    public WeatherListenerController(WeatherProducerService weatherProducerService){
        this.weatherProducerService = weatherProducerService;
    }

    @GetMapping
    public String test() {
        return "API - Weather Producer";
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestBody String message) {
        weatherProducerService.sendMessage(message);
        return "Mensagem enviada com sucesso!";
    }
}
