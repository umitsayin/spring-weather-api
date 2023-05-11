package com.weather.dto;

public record WeatherResponse(
        Request request,
        Location location,
        WeatherDto current
) {
}
