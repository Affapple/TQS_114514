package tqs.homework.canteen.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.homework.canteen.DTOs.CacheStats;
import tqs.homework.canteen.DTOs.Forecast;
import tqs.homework.canteen.services.WeatherService;

@RestController
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins = "*")
public class WeatherController {
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/forecast")
    public ResponseEntity<List<Forecast>> getWeather() {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(6);
        logger.info("Fetching weather forecast from {} to {}", from, to);
        
        try {
            List<Forecast> forecasts = weatherService.getWeather(from, to);
            return ResponseEntity.ok(forecasts);
        } catch (Exception e) {
            logger.error("Error retrieving weather forecast: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/cache/stats")
    public ResponseEntity<CacheStats> getCacheStats() {
        logger.info("Received request for weather cache statistics");
        
        CacheStats stats = weatherService.getCacheStats();
        logger.info("Successfully retrieved cache statistics: hits={}, misses={}, total calls={}",
            stats.getCacheHits(), stats.getCacheMisses(), stats.getTotalCalls());
        return ResponseEntity.ok(stats);
    }
}
