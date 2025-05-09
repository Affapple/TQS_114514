package tqs.cars.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.cars.entity.Car;
import tqs.cars.services.CarManagerService;

@RestController
@RequestMapping("/api")
public class CarController {
    CarManagerService carService;

    @Autowired
    public CarController(CarManagerService carService) {
        this.carService = carService;
    }

    @PostMapping("/car")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car result = carService.save(car);

        if (result == null) {
            return new ResponseEntity<>(
                result,
                HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
            result,
            HttpStatus.CREATED
        );
    }

    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping("/car/replacement")
    public ResponseEntity<Car> getReplacementCar(@RequestBody Car car) {
        Optional<Car> replacement = carService.findReplacement(car);

        if (replacement.isEmpty()) {
            return new ResponseEntity<>(
                (Car) null,
                HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
            replacement.get(),
            HttpStatus.OK
        );
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<Car> getCarById(
        @PathVariable Long carId
    ) {
        try {
            Car result = carService.getCarDetails(carId).orElseThrow(NoSuchElementException::new);

            return new ResponseEntity<>(
                result,
                HttpStatus.OK
            );
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                (Car) null,
                HttpStatus.NOT_FOUND
            );
        }
    }
}
