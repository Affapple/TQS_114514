package tqs.cars.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.cars.entity.Car;
import tqs.cars.services.CarManagerService;

@RestController
@RequestMapping("/api")
public class CarController {
    @Autowired
    CarManagerService carManagerService;

    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        // unimplemented method
        return new ResponseEntity<Car>(null);
    }

    @GetMapping("/cars")
    public List<Car> getAllCars() {
        // unimplemented method
        return null;
    }

    @GetMapping("/cars/{carId}")
    public ResponseEntity<Car> getCarById(Long carId) {
        // unimplemented method
        return new ResponseEntity<Car>(null);
    }

}
