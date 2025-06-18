package com.weather.weather_producer.openmeteo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.util.ArrayList;

@Service
public class OpenMeteoClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OpenMeteoClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.open-meteo.com/v1").build();
        this.objectMapper = new ObjectMapper();
    }

    public List<WeatherHourData> fetchHourlyWeather(double latitude, double longitude) {
        String endpoint = "/forecast";

        Mono<String> responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpoint)
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("forecast_hours", 1)
                        .queryParam("hourly", String.join(",",
                                "temperature_2m",
                                "pressure_msl",
                                "rain",
                                "precipitation",
                                "precipitation_probability",
                                "relative_humidity_2m",
                                "apparent_temperature",
                                "dew_point_2m",
                                "wind_speed_10m",
                                "cloud_cover",
                                "uv_index",
                                "is_day"))
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        String jsonResponse = responseMono.block(); // síncrono

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            return parseHourly(root.get("hourly"));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta JSON", e);
        }
    }

    private List<WeatherHourData> parseHourly(JsonNode hourly) {
        List<WeatherHourData> list = new ArrayList<>();
        List<String> times = toList(hourly.get("time"));
        List<Double> temperature2m = toDoubleList(hourly.get("temperature_2m"));
        List<Double> pressure = toDoubleList(hourly.get("pressure_msl"));
        List<Double> rain = toDoubleList(hourly.get("rain"));
        List<Double> precipitation = toDoubleList(hourly.get("precipitation"));
        List<Double> precipitationProb = toDoubleList(hourly.get("precipitation_probability"));
        List<Double> humidity = toDoubleList(hourly.get("relative_humidity_2m"));
        List<Double> apparentTemp = toDoubleList(hourly.get("apparent_temperature"));
        List<Double> dewPoint = toDoubleList(hourly.get("dew_point_2m"));
        List<Double> windSpeed = toDoubleList(hourly.get("wind_speed_10m"));
        List<Double> cloudCover = toDoubleList(hourly.get("cloud_cover"));
        List<Double> uvIndex = toDoubleList(hourly.get("uv_index"));
        List<Integer> isDay = toIntList(hourly.get("is_day"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        for (int i = 0; i < times.size(); i++) {
            LocalDateTime ldt = LocalDateTime.parse(times.get(i), formatter);
            Instant instant = ldt.toInstant(ZoneOffset.UTC);
            list.add(new WeatherHourData(
                    instant,
                    temperature2m.get(i),
                    pressure.get(i),
                    rain.get(i),
                    precipitation.get(i),
                    precipitationProb.get(i),
                    humidity.get(i),
                    apparentTemp.get(i),
                    dewPoint.get(i),
                    windSpeed.get(i),
                    cloudCover.get(i),
                    uvIndex.get(i),
                    isDay.get(i) == 1
            ));
        }


        return list;
    }

    private List<String> toList(JsonNode node) {
        List<String> list = new ArrayList<>();
        node.forEach(n -> list.add(n.asText()));
        return list;
    }

    private List<Double> toDoubleList(JsonNode node) {
        List<Double> list = new ArrayList<>();
        node.forEach(n -> list.add(n.asDouble()));
        return list;
    }

    private List<Integer> toIntList(JsonNode node) {
        List<Integer> list = new ArrayList<>();
        node.forEach(n -> list.add(n.asInt()));
        return list;
    }
}
