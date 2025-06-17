package com.weather.weather_producer.controllers;

import com.weather.weather_producer.services.WeatherProducerService;
import com.weather.weather_producer.openmeteo.WeatherHourData;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

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
        weatherProducerService.sendMessage(message);
        return "Mensagem enviada com sucesso!";
    }

    @GetMapping("/forecast")
    public List<WeatherHourData> getWeather(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        return weatherProducerService.getWeather(lat, lon);
    }
}
