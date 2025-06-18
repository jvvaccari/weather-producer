package com.weather.weather_producer.services;

import com.weather.weather_producer.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.weather.weather_producer.openmeteo.OpenMeteoClient;
import com.weather.weather_producer.openmeteo.WeatherHourData;
import java.util.List;


@Service
public class WeatherProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final OpenMeteoClient openMeteoClient;

    public WeatherProducerService(RabbitTemplate rabbitTemplate,OpenMeteoClient openMeteoClient) {
        this.rabbitTemplate = rabbitTemplate;
        this.openMeteoClient = openMeteoClient;
    }

    public void sendToConsumer(String data) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, data);
    }

    public List<WeatherHourData> getWeather(double lat, double lon) {
        return openMeteoClient.fetchHourlyWeather(lat, lon);
    }

}
