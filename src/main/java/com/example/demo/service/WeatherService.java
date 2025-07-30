package com.example.demo.service;

@Component
public class WeatherService{

  @Value("${weather.api.key}")
  private String apiKey;
  
  private static final String API="";

  @Autowired
  private RestTemplate restTemplate;
  
  public WeatherResponse getWeather(String city){
    String finalAPI = API.replace("CITY",city).replace("API_KEY",apiKey);
    ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
    WeatherResponse body = response.getBody();
    return body;
  }
}
      
