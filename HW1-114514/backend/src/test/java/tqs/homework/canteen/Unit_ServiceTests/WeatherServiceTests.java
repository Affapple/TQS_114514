package tqs.homework.canteen.Unit_ServiceTests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import tqs.homework.canteen.Cache.Cache;
import tqs.homework.canteen.DTOs.CacheStats;
import tqs.homework.canteen.DTOs.Forecast;
import tqs.homework.canteen.services.WeatherService;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTests {
    
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private Cache<Forecast> cache;
    
    @InjectMocks
    private WeatherService weatherService;
    
    private LocalDate today = LocalDate.now();
    private LocalDate tomorrow = today.plusDays(1);
    private LocalDate nextWeek = today.plusDays(7);
    
    @BeforeEach
    public void setUp() {
        // Set the API key for testing
        ReflectionTestUtils.setField(weatherService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(weatherService, "restTemplate", restTemplate);
    }
    
    /**
     * Given a date range
     * When all dates are in the cache
     * Then the service should return forecasts from the cache without calling the API
     */
    @Test
    public void testGetWeatherFromCache() {
        Forecast todayForecast = new Forecast(today, 25.0, 15.0, 60, "Sunny", "sunny.png");
        Forecast tomorrowForecast = new Forecast(tomorrow, 26.0, 16.0, 65, "Partly cloudy", "partly-cloudy.png");
        
        when(cache.get(today.toString())).thenReturn(todayForecast);
        when(cache.get(tomorrow.toString())).thenReturn(tomorrowForecast);
        
        List<Forecast> result = weatherService.getWeather(today, tomorrow);
        
        assertThat(result, hasSize(2));
        assertThat(result, contains(todayForecast, tomorrowForecast));
        verify(restTemplate, never()).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class));
    }
    
    /**
     * Given a date range
     * When some dates are not in the cache
     * Then the service should fetch missing dates from the API and cache them
     */
    @Test
    public void testGetWeatherFromApiAndCache() {
        Forecast todayForecast = new Forecast(today, 25.0, 15.0, 60, "Sunny", "sunny.png");
        
        // Today is in cache, tomorrow is not
        when(cache.get(today.toString())).thenReturn(todayForecast);
        when(cache.get(tomorrow.toString())).thenReturn(null);
        
        Map<String, Object> apiResponse = createMockApiResponse(tomorrow);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
            .thenReturn(responseEntity);
        
        List<Forecast> result = weatherService.getWeather(today, tomorrow);
        
        assertThat(result, hasSize(2));
        assertThat(result.get(0), is(todayForecast));
        assertThat(result.get(1).getDate(), is(tomorrow));
        assertThat(result.get(1).getMaxTemp(), is(26.0));
        assertThat(result.get(1).getMinTemp(), is(16.0));
        assertThat(result.get(1).getAvgHumidity(), is(65));
        assertThat(result.get(1).getDescription(), is("Partly cloudy"));
        assertThat(result.get(1).getIcon(), is("partly-cloudy.png"));
        
        verify(cache).put(eq(tomorrow.toString()), any(Forecast.class));
    }
    
    /**
     * Given a date range
     * When the API returns an error
     * Then the service should return only cached data
     */
    @Test
    public void testGetWeatherApiError() {
        Forecast todayForecast = new Forecast(today, 25.0, 15.0, 60, "Sunny", "sunny.png");
        
        // Today is in cache, tomorrow is not
        when(cache.get(today.toString())).thenReturn(todayForecast);
        when(cache.get(tomorrow.toString())).thenReturn(null);
        
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
            .thenThrow(new RuntimeException("API Error"));
        
        List<Forecast> result = weatherService.getWeather(today, tomorrow);
        
        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(todayForecast));
    }
    
    /**
     * Given a date range
     * When the API returns an empty response
     * Then the service should return only cached data
     */
    @Test
    public void testGetWeatherEmptyApiResponse() {
        Forecast todayForecast = new Forecast(today, 25.0, 15.0, 60, "Sunny", "sunny.png");
        
        // Today is in cache, tomorrow is not
        when(cache.get(today.toString())).thenReturn(todayForecast);
        when(cache.get(tomorrow.toString())).thenReturn(null);
        
        Map<String, Object> emptyResponse = new HashMap<>();
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(emptyResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
            .thenReturn(responseEntity);
        
        List<Forecast> result = weatherService.getWeather(today, tomorrow);
        
        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(todayForecast));
    }
    
    /**
     * Given a service with multiple calls
     * When getCacheStats is called
     * Then it should return the correct cache statistics
     */
    @Test
    public void testGetCacheStats() {
        when(cache.getCacheHits()).thenReturn(5L);
        when(cache.getCacheMisses()).thenReturn(3L);
        
        // Make some calls to increment totalCalls
        weatherService.getWeather(today, today);
        weatherService.getWeather(tomorrow, tomorrow);
        weatherService.getWeather(nextWeek, nextWeek);
        
        CacheStats stats = weatherService.getCacheStats();
        
        assertThat(stats.getCacheHits(), is(5L));
        assertThat(stats.getCacheMisses(), is(3L));
        assertThat(stats.getTotalCalls(), is(3L));
    }
    
    /**
     * Helper method to create a mock API response
     */
    private Map<String, Object> createMockApiResponse(LocalDate date) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> forecast = new HashMap<>();
        List<Map<String, Object>> forecastDays = new ArrayList<>();
        
        Map<String, Object> dayComplete = new HashMap<>();
        dayComplete.put("date", date.toString());
        
        Map<String, Object> day = new HashMap<>();
        day.put("maxtemp_c", 26.0);
        day.put("mintemp_c", 16.0);
        day.put("avghumidity", 65);
        
        Map<String, Object> condition = new HashMap<>();
        condition.put("text", "Partly cloudy");
        condition.put("icon", "partly-cloudy.png");
        
        day.put("condition", condition);
        dayComplete.put("day", day);
        forecastDays.add(dayComplete);
        
        forecast.put("forecastday", forecastDays);
        response.put("forecast", forecast);
        
        return response;
    }
}
