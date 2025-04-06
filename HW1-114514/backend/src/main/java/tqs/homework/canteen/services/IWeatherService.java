package tqs.homework.canteen.services;

import java.time.LocalDate;
import java.util.List;

import tqs.homework.canteen.DTOs.Forecast;

public interface IWeatherService {
    List<Forecast> getWeather(LocalDate from, LocalDate to);
}
