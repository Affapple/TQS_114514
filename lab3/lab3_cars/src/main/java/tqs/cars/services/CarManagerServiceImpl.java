package tqs.cars.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.cars.entity.Car;
import tqs.cars.repository.CarRepository;

@Service
public class CarManagerServiceImpl implements CarManagerService {
    CarRepository carRepository;

    @Autowired
    public CarManagerServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Car save(Car car) {
        Car result = carRepository.saveAndFlush(car);
        return result;
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Optional<Car> getCarDetails(Long carId) {
        return carRepository.findByCarId(carId);
    }

    @Override
    public Optional<Car> findReplacement(Car car) {
        List<Car> matches = carRepository.findByMakerAndModel(car.getMaker(), car.getModel());

        if (matches.isEmpty()) {
            return Optional.ofNullable(null);
        }

        return Optional.of(matches.getFirst());
    }
}
