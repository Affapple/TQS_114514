package tqs.cars.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import tqs.cars.entity.Car;
import tqs.cars.repository.CarRepository;

public class CarManagerServiceImpl implements CarManagerService {
    @Autowired
    CarRepository carRepository;

    @Override
    public void save(Car car) {
    }

    @Override
    public List<Car> getAllCars() {
        throw new UnsupportedOperationException("Unimplemented method 'getAllCars'");
    }

    @Override
    public Optional<Car> getCarDetails(Long carId) {
        throw new UnsupportedOperationException("Unimplemented method 'getCarDetails'");
    }
    
}
