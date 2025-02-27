package tqs.cars.services;

import java.util.List;
import java.util.Optional;

import tqs.cars.entity.Car;

public interface CarManagerService {

    public void save(Car car);
    public List<Car> getAllCars();
    public Optional<Car> getCarDetails(Long carId);
}
