package tqs.cars.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import tqs.cars.entity.Car;

@Repository
public interface CarRepository {
    public List<Car> findAll();
    public Optional<Car> findByCarId(Long carId);
}
