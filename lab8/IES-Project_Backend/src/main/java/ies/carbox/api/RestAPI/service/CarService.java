package ies.carbox.api.RestAPI.service;

import ies.carbox.api.RestAPI.entity.Car;
import ies.carbox.api.RestAPI.entity.CarLiveInfo;
import ies.carbox.api.RestAPI.entity.User;
import ies.carbox.api.RestAPI.repository.CarLiveInfoRepository;
import ies.carbox.api.RestAPI.repository.CarRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.carbox.api.RestAPI.repository.UserRepository;


/**
 * Service class for managing {@link Car} entities.
 *
 * <p>
 * This service provides methods to create, retrieve, update, and delete cars,
 * as well as to fetch cars associated with a specific user.
 * </p>
 *
 * <p>
 * Note: The methods are designed to interact with a repository layer
 * to handle data persistence and retrieval.
 * </p>
 */
@Service
public class CarService {
    CarRepository carRepository;
    CarLiveInfoRepository carLiveInfoRepository;
    CacheService cacheService;

    /**
     * Default constructor for the CarService.
     */
    @Autowired
    public CarService(CarRepository carRepository, CarLiveInfoRepository carLiveInfoRepository,
            CacheService cacheService) {
        this.carRepository = carRepository;
        this.carLiveInfoRepository = carLiveInfoRepository;
        this.cacheService = cacheService;
    }

    /**
     * Retrieves all cars associated with the current user.
     *
     * @param ecuIds List of ecuIds owned by the {@link User}
     *
     * @return a list of {@link Car} entities owned by the {@link User}
     */
    public List<Car> getAllUserCars(List<String> ecuIds) {
        List<Car> carList = new ArrayList<>();
        for (String ecuId : ecuIds) {
            // They MUST exist, otherwise something is very wrong
            try {
                Car car = cacheService.getCar(ecuId);
                if (car == null) {
                    car = carRepository.findByEcuId(ecuId).orElseThrow(
                            () -> new IllegalArgumentException("Car with ecuId \"" + ecuId + "\" does not exist!"));
                    cacheService.saveCar(car);
                }

                carList.add(car);
                CarLiveInfo latestStatus = getLatestCarData(car.getEcuId());
                if (latestStatus == null) {
                    continue;
                }

                car.setLocation(latestStatus.getLocation());
                car.setLocation(latestStatus.getLocation());
            } catch (NoSuchElementException elementException) {
                System.err.println("User add a non existent car! ecuID = \"" + ecuId + "\"");
                continue;
            }
        }

        return carList;
    }

    public CarLiveInfo getLatestCarData(String ecuId) {
        List<CarLiveInfo> carLiveInfos = carLiveInfoRepository.findByCarId(ecuId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Car has no info!"));
        LocalDateTime latestDate = LocalDateTime.MIN;
        CarLiveInfo latestInfo = null;

        for (CarLiveInfo carInfo : carLiveInfos) {
            if (0 < carInfo.getTimestamp().compareTo(latestDate)) {
                latestInfo = carInfo;
                latestDate = carInfo.getTimestamp();
            }
        }
        System.out.println(latestInfo);
        return latestInfo;
    }

    public List<CarLiveInfo> getCarDataAfterTimestamp(String ecuId, LocalDateTime timestamp) {
        List<CarLiveInfo> carLiveInfos = carLiveInfoRepository.findByCarId(ecuId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Car has no info!"));
        List<CarLiveInfo> carLiveInfosAfterTimestamp = new ArrayList<>();

        for (CarLiveInfo carInfo : carLiveInfos) {
            if (0 < carInfo.getTimestamp().compareTo(timestamp)) {
                carLiveInfosAfterTimestamp.add(carInfo);
            }
        }

        return carLiveInfosAfterTimestamp;
    }

    public Car getCarById(String carId) {
        // Implementation to retrieve a car by ID
        Car car = cacheService.getCar(carId);
        if (car != null) {
            return car;
        }
        car = carRepository
                .findByEcuId(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car with carId \"" + carId + "\" does not exist!"));
        cacheService.saveCar(car);
        return car;
    }

    // The following methods may not be necessary
    /**
     * Updates an existing {@link Car} entity in the database.
     *
     * @param car the {@link Car} entity with updated information
     */
    public void updateCar(Car car) {
        // Implementation to update the car entity
    }

    /**
     * Deletes a specific car by its ID.
     *
     * @param carId the ID of the car to delete
     */
    public void deleteCarById(String carId) {
        // Implementation to delete the car entity
    }

    /**
     * Retrieves statistics related to a specific car.
     *
     * <p>
     * This method may involve fetching various metrics or statistics
     * associated with the car, which could be represented by a separate class.
     * </p>
     *
     * @param carId the ID of the car for which to retrieve statistics
     */
    public void getCarStats(String carId) {
        // Implementation to retrieve car statistics
        // Potentially calls another service or utility class for stats
    }

//     public List<Car> getCarsByUserId(String email) {
//     User user = UserRepository.findByEmail(email)
//             .orElseThrow(() -> new IllegalArgumentException("User with email \"" + email + "\" does not exist!"));

//     List<String> ecuIds = user.getEcuIds(); 

//     return getAllUserCars(ecuIds);
// }

    /**
     * Retrieves all cars in the system.
     *
     * @return a list of all {@link Car} entities
     */
    public List<Car> getAllCars() {
        return carRepository.findAll(); // predefined query ainda pordia usar find all by year, etc
    }

}
