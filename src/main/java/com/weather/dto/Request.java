package com.weather.dto;

public record Request(String type,
                      String query,
                      String language) {
}
