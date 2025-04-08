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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import tqs.homework.canteen.DTOs.CacheStats;
import tqs.homework.canteen.DTOs.Forecast;
import tqs.homework.canteen.services.WeatherService;

@RestController
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins = "*")
@Tag(name = "Weather", description = "Weather forecast and cache statistics APIs")
public class WeatherController {
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    
    @Autowired
    private WeatherService weatherService;

    @Operation(summary = "Get weather forecast", description = "Retrieves weather forecast for the next 7 days")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved weather forecast"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @Operation(summary = "Get cache statistics", description = "Retrieves statistics about the weather forecast cache")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved cache statistics"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/cache/stats")
    public ResponseEntity<CacheStats> getCacheStats() {
        logger.info("Received request for weather cache statistics");
        
        CacheStats stats = weatherService.getCacheStats();
        logger.info("Successfully retrieved cache statistics: hits={}, misses={}, total calls={}",
            stats.getCacheHits(), stats.getCacheMisses(), stats.getTotalCalls());
        return ResponseEntity.ok(stats);
    }
}
