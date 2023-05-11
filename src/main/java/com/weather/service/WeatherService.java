package com.weather.service;

import com.weather.constant.Constants;
import com.weather.dto.WeatherDto;
import com.weather.dto.WeatherResponse;
import com.weather.model.WeatherEntity;
import com.weather.repository.WeatherRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class WeatherService {
    Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;

    public WeatherService(WeatherRepository weatherRepository, RestTemplate restTemplate) {
        this.weatherRepository = weatherRepository;
        this.restTemplate = restTemplate;
    }

    @Cacheable(cacheNames = "weather", key = "#city")
    public WeatherDto getWeatherByCityName(String city){
        Optional<WeatherEntity> weatherEntityOptional = weatherRepository.findFirstByRequestedCityNameOrderByResponseLocalTimeDesc(city);

        return weatherEntityOptional.map(weather -> {
            if(weatherEntityOptional.get().getUpdatedTime().isBefore(LocalDateTime.now().minusMinutes(30))){
                return WeatherDto.convert(getWeatherFromWeatherStack(city));
            }

            return WeatherDto.convert(weather);
        }).orElseGet(()-> WeatherDto.convert(getWeatherFromWeatherStack(city)));
    }

    private WeatherEntity getWeatherFromWeatherStack(String city){
        WeatherResponse weatherResponse = restTemplate.getForObject(getWeatherStackApiUrl(city), WeatherResponse.class);

        return saveWeatherEntity(city, weatherResponse);
    }

    private String getWeatherStackApiUrl(String city){
        return Constants.API_URL + Constants.API_ACCESS_PARAM + Constants.API_KEY + Constants.API_QUERY_PARAM + city;
    }

    private WeatherEntity saveWeatherEntity(String city, WeatherResponse weatherResponse){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        WeatherEntity weatherEntity = new WeatherEntity(
                city,
                weatherResponse.location().name(),
                weatherResponse.location().country(),
                weatherResponse.current().temperature(),
                LocalDateTime.now(),
                LocalDateTime.parse(weatherResponse.location().localTime(),dateTimeFormatter));

        return weatherRepository.save(weatherEntity);
    }


    @PostConstruct
    @Scheduled(fixedDelayString = "30 * 60 * 1000")
    @CacheEvict(cacheNames = "weather",allEntries = true)
    public void clearCache(){
        logger.info("Cache cleared.");
    }
}
