package com.weather.weather_producer.openmeteo;

import java.time.Instant;

public record WeatherHourData(
        Instant time,
        double temperature2m,
        double pressureMsl,
        double rain,
        double precipitation,
        double precipitationProbability,
        double relativeHumidity2m,
        double apparentTemperature,
        double dewPoint2m,
        double windSpeed10m,
        double cloudCover,
        double uvIndex,
        boolean isDay,
        double latitude,
        double longitude
) {
}

