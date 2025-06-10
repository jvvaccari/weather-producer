package com.weather.weather_producer.controllers;

import com.weather.weather_producer.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class WeatherController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public String test() {
        return "API - Weather Producer";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, message);
        return "Mensagem enviada ao RabbitMQ: " + message;
    }
}
