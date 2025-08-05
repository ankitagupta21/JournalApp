package com.example.demo.service;

import com.example.demo.api.response.WeatherResponse;
import com.example.demo.cache.AppCache;
import com.example.demo.contants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService{

  @Value("${weather.api.key}")
  private String apiKey;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private AppCache appCache;

  public WeatherResponse getWeather(String city){
    String finalAPI = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.CITY,city).replace(Placeholders.API_KEY,apiKey);
    ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
    response.getStatusCode();
    WeatherResponse body = response.getBody();
    return body;
  }
}
      
