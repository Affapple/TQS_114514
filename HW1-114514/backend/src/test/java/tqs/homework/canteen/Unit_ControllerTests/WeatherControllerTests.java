package tqs.homework.canteen.Unit_ControllerTests;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tqs.homework.canteen.controller.WeatherController;
import tqs.homework.canteen.DTOs.CacheStats;
import tqs.homework.canteen.DTOs.Forecast;
import tqs.homework.canteen.services.WeatherService;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private WeatherService weatherService;

    /**
     * Given the weather service returns forecasts
     * When getWeather is called
     * Then return 200 with the forecasts
     */
    @Test
    public void whenGetWeather_thenReturn200() throws Exception {
        List<Forecast> forecasts = new ArrayList<>();
        LocalDate today = LocalDate.now();
        forecasts.add(new Forecast(today, 25.0, 15.0, 60, "Sunny", "sunny.png"));
        forecasts.add(new Forecast(today.plusDays(1), 26.0, 16.0, 65, "Partly cloudy", "partly-cloudy.png"));
        
        when(weatherService.getWeather(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(forecasts);
        
        mvc.perform(get("/api/v1/weather/forecast")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].date", is(today.toString())))
            .andExpect(jsonPath("$[0].maxtemp", is(25.0)))
            .andExpect(jsonPath("$[0].mintemp", is(15.0)))
            .andExpect(jsonPath("$[0].avghumidity", is(60)))
            .andExpect(jsonPath("$[0].description", is("Sunny")))
            .andExpect(jsonPath("$[0].icon", is("sunny.png")))
            .andExpect(jsonPath("$[1].date", is(today.plusDays(1).toString())))
            .andExpect(jsonPath("$[1].maxtemp", is(26.0)))
            .andExpect(jsonPath("$[1].mintemp", is(16.0)))
            .andExpect(jsonPath("$[1].avghumidity", is(65)))
            .andExpect(jsonPath("$[1].description", is("Partly cloudy")))
            .andExpect(jsonPath("$[1].icon", is("partly-cloudy.png")));
    }

    /**
     * Given the weather service throws an exception
     * When getWeather is called
     * Then return 500
     */
    @Test
    public void whenGetWeather_thenReturn500() throws Exception {
        when(weatherService.getWeather(any(LocalDate.class), any(LocalDate.class)))
            .thenThrow(new RuntimeException("Service error"));
        
        mvc.perform(get("/api/v1/weather/forecast")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

    /**
     * Given the weather service returns cache stats
     * When getCacheStats is called
     * Then return 200 with the cache stats
     */
    @Test
    public void whenGetCacheStats_thenReturn200() throws Exception {
        CacheStats stats = new CacheStats(5L, 3L, 8L);
        
        when(weatherService.getCacheStats())
            .thenReturn(stats);
        
        mvc.perform(get("/api/v1/weather/cache/stats")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cacheHits", is(5)))
            .andExpect(jsonPath("$.cacheMisses", is(3)))
            .andExpect(jsonPath("$.totalCalls", is(8)));
    }
}