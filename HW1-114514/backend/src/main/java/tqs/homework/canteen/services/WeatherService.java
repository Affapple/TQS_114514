package tqs.homework.canteen.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import tqs.homework.canteen.Cache.Cache;
import tqs.homework.canteen.DTOs.CacheStats;
import tqs.homework.canteen.DTOs.Forecast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

@Service
public class WeatherService implements IWeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${weatherapi.key}")
    private String apiKey;
    private final String BASE_URL = "http://api.weatherapi.com/v1";
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private final Cache<Forecast> cache = new Cache<>();
    private Long totalCalls = 0L;

    @Override
    public List<Forecast> getWeather(LocalDate from, LocalDate to) {
        totalCalls++;
        List<Forecast> result = new ArrayList<>();
        LocalDate startFetch = null;
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            Forecast forecast = cache.get(date.toString());
            if (forecast == null) {
                startFetch = date; 
                break;
            }
            result.add(forecast);
        }
        logger.info("Fetched {} forecasts from cache, from {} to {}", result.size(), from, to);

        if (startFetch != null) {
            logger.info("Fetching weather forecast to the api from {} to {}", startFetch, to);
            List<Forecast> newForecasts = fetchWeatherFromApi(startFetch, to);
            logger.info("Fetched {} forecasts from the api", newForecasts.size());
            for (Forecast forecast : newForecasts) {
                cache.put(forecast.getDate().toString(), forecast);
            }
            result.addAll(newForecasts);
        }
        return result;
    }

    private List<Forecast> fetchWeatherFromApi(LocalDate from, LocalDate to) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/forecast.json")
                .queryParam("key", apiKey)
                .queryParam("q", "Aveiro")
                .queryParam("days", to.getDayOfMonth() - from.getDayOfMonth() + 1)
                .queryParam("aqi", "no")
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        List<Forecast> result = new ArrayList<>();
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> forecast = (Map<String, Object>) responseBody.get("forecast");
                List<Map<String, Object>> forecastDays = (List<Map<String, Object>>) forecast.get("forecastday");

                if (forecastDays != null && !forecastDays.isEmpty()) {
                    for (Map<String, Object> dayComplete : forecastDays) {
                        String date = (String) dayComplete.get("date");

                        Map<String, Object> day = (Map<String, Object>) dayComplete.get("day");
                        Map<String, Object> condition = (Map<String, Object>) day.get("condition");

                        Forecast forecastDTO = new Forecast(
                                LocalDate.parse(date),
                                (double) day.get("maxtemp_c"),
                                (double) day.get("mintemp_c"),
                                (int) day.get("avghumidity"),
                                (String) condition.get("text"),
                                (String) condition.get("icon"));

                        result.add(forecastDTO);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
        }
        return result;
    }

    public CacheStats getCacheStats() {
        return new CacheStats(cache.getCacheHits(), cache.getCacheMisses(), totalCalls);
    }
}