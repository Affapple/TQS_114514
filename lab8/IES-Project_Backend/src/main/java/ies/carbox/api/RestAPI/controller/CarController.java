package ies.carbox.api.RestAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import ies.carbox.api.RestAPI.entity.Car;
import ies.carbox.api.RestAPI.entity.CarLiveInfo;
import ies.carbox.api.RestAPI.entity.TripInfo;
import ies.carbox.api.RestAPI.entity.User;
import ies.carbox.api.RestAPI.service.CarService;
import ies.carbox.api.RestAPI.service.TripInfoService;
import ies.carbox.api.RestAPI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import ies.carbox.api.RestAPI.CONSTANTS;
import ies.carbox.api.RestAPI.service.AuthenticationService;
import ies.carbox.api.RestAPI.service.CacheService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * CarController provides endpoints for managing cars, including viewing, associating cars to users,
 * retrieving car data and trips, and removing car associations.
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(CONSTANTS.apiBase + "/vehicles")  // Base path for Car-related requests

public class CarController {

    private final CarService carService;
    private final UserService userService;
    private final TripInfoService tripInfoService;
    private final CacheService cacheService;
    
    @Autowired
    public CarController(CarService carService, UserService userService, TripInfoService tripInfoService, CacheService cacheService) {
        this.carService = carService;
        this.userService = userService;
        this.tripInfoService = tripInfoService;
        this.cacheService = cacheService;
    }

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Retrieves a list of cars owned by the specified user.
     *
     * @param user The User object representing the user whose cars are to be retrieved.
     * @return A list of Car objects associated with the user.
     */
    @GetMapping
    @Operation(summary = "Retrieve all cars owned by a specific user", description = "Retrieve a list of cars owned by a user")
    @ApiResponse(responseCode = "200", description = "Cars retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<List<Car>> getAllCars() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Gets the email of the user, no need for Body User user

        try {
            List<List<String>> data = userService.getListOfEcuIds(userEmail);
            if (data == null) {
                return ResponseEntity.noContent().build();
            }
            List<String> ecuIds = new ArrayList<>();
            for (List<String> car : data) {
                ecuIds.add(car.get(0));
            }
        
            List<Car> cars = carService.getAllUserCars(ecuIds);
            return ResponseEntity.ok(cars);
        } catch (UsernameNotFoundException e) {
            System.out.println("INFO: User \"" + userEmail + "\" not found");
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
         catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Associates a car with a user.
     *
     * @param user The User object containing user's email.
     * @param ecuId The ID of the car to be associated with the user.
     * @return The associated Car object on success.
     */
    @PostMapping("/{vehicleId}/{vehicle_name}")
    @Operation(summary = "Associate a car with a user", description = "Associates a car with the specified user")
    @ApiResponse(responseCode = "200", description = "Car associated with user successfully")
    @ApiResponse(responseCode = "404", description = "User or car not found")
    public ResponseEntity<Car> associateCarToUser(
        @Parameter(description = "ID of the car to be associated") @PathVariable(required = true, name = "vehicleId") String ecuId,
        @Parameter(description = "Name of the car to be associated") @PathVariable(required = true, name = "vehicle_name") String vehicleName
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); 
        
        try {
            Car car = carService.getCarById(ecuId);
            User user = userService.addUserCar(userEmail, ecuId, vehicleName);
            return ResponseEntity.ok(car);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Retrieves data for a specific car by its ID.
     *
     * @param vehicleId The ID of the car to retrieve data for.
     * @return The Car object containing data of the specified car.
     */
    @GetMapping("/{vehicleId}")
    @Operation(summary = "Retrieve car data by car ID", description = "Retrieve detailed car data by specifying car ID")
    @ApiResponse(responseCode = "200", description = "Car data retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Car not found")
    public ResponseEntity<Car> getCarById(
        @Parameter(description = "ID of the car to retrieve") @PathVariable(required = true, value = "vehicleId") String vehicleId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); 
        try {
            if (!userService.belongsToUser(vehicleId, userEmail)) {
                return ResponseEntity.notFound().build();
            }
            Car car = carService.getCarById(vehicleId);
            return ResponseEntity.ok(car);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves the name of the car by its ID.
     *
     * @param vehicleId The Car object containing the updated data.
     * @return A status message indicating the result of the update.
     */
    @GetMapping("/name/{vehicleId}")
    @Operation(summary = "Retrieve the name of a car by car ID", description = "Retrieve the name of a car by specifying car ID")
    @ApiResponse(responseCode = "200", description = "Car name retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Car not found")
    public ResponseEntity<String> getCarName(
        @Parameter(description = "ID of the car to retrieve the name for") @PathVariable(required = true, value = "vehicleId") String vehicleId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); 
        try {
            if (!userService.belongsToUser(vehicleId, userEmail)) {
                return ResponseEntity.notFound().build();
            }
            List<List<String>> car = userService.getListOfEcuIds(userEmail);
            for (List<String> c : car) {
                if (c.get(0).equals(vehicleId)) {
                    return ResponseEntity.ok((String) c.get(1));
                }
            }
            return ResponseEntity.notFound().build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


    /**Retrieves all live information gathered after a timestamp for a specified car.
    *
    * @param vehicleId The ID of the car for which to retrieve the live information.
    * @param timestamp The timestamp after which to retrieve the live information.
    * @return A list of CarLiveInfo objects containing the live data of the specified car.
    */
    @GetMapping("/live/{vehicleId}/{timestamp}")
    @Operation(summary = "Retrieve live information of a car by car ID and timestamp", description = "Retrieve live data for a specific car after a specified timestamp")
    @ApiResponse(responseCode = "200", description = "Car data retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Car not found")
    public ResponseEntity<List<CarLiveInfo>> getCarLiveInfo(
        @Parameter(description = "ID of the car to retrieve live data for") @PathVariable(required = true, value = "vehicleId") String vehicleId,
        @Parameter(description = "Timestamp after which to retrieve live data") @PathVariable(required = true, value = "timestamp") LocalDateTime timestamp
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); 
        try {
            if (!userService.belongsToUser(vehicleId, userEmail)) {
                return ResponseEntity.notFound().build();
            }
            List<CarLiveInfo> car = carService.getCarDataAfterTimestamp(vehicleId, timestamp);
            return ResponseEntity.ok(car);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves the latest live information for a specified car.
     *
     * @param vehicleId The ID of the car for which to retrieve the latest information.
     * @return The CarLiveInfo object containing the latest data of the specified car.
     */
    @GetMapping("/live/{vehicleId}")
    @Operation(summary = "Retrieve the latest live information of a car by car ID", description = "Retrieve the latest live data for a specific car")
    @ApiResponse(responseCode = "200", description = "Latest car data retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Car not found")
    public ResponseEntity<CarLiveInfo> getCarLatestInfo(
        @Parameter(description = "ID of the car to retrieve live data for") @PathVariable(required = true, value = "vehicleId") String vehicleId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); 
        try {
            if (!userService.belongsToUser(vehicleId, userEmail)) {
                return ResponseEntity.notFound().build();
            }
            CarLiveInfo car = carService.getLatestCarData(vehicleId);
            return ResponseEntity.ok(car);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
     
    }

    /**
     * Deletes the association of a car from a user.
     *
     * @param vehicleId The ID of the car to be dissociated.
     * @param user The User object containing user's email.
     * @return A status message indicating the result of the deletion.
     */
    @DeleteMapping("/{vehicleId}")
    @Operation(summary = "Dissociate a car from a user by car ID", description = "Removes the association of a car from a user")
    @ApiResponse(responseCode = "200", description = "Car dissociated from user successfully")
    @ApiResponse(responseCode = "404", description = "User or car not found")
    public ResponseEntity<String> deleteCar(
        @Parameter(description = "ID of the car to be removed from user") @PathVariable(required = true) String vehicleId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); 
        try {
            if (!userService.belongsToUser(vehicleId, userEmail)) {
                return ResponseEntity.notFound().build();
            }

            User user = userService.removeUserCar(userEmail, vehicleId);
            return ResponseEntity.ok("Car removed successfully");

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("ERROR: Error removing car from user");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error removing car from user");
        }
    }

    /**
     * Retrieves all trips made by a specific car.
     * Optionally, a specific trip can be retrieved by providing the trip ID.
     *
     * @param vehicleId The ID of the car to retrieve trips for.
     * @param tripId The optional ID of the trip to retrieve.
     * @return A list of TripInfo objects for the specified car.
     */
    @GetMapping("/trips/{vehicleId}")
    @Operation(summary = "Retrieve all trips made by a car", description = "Retrieve a list of trips associated with a specific car")
    @ApiResponse(responseCode = "200", description = "Trips retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Car not found or no trips available")
    public ResponseEntity<List<TripInfo>> getCarTrips(
        @Parameter(description = "ID of the car to retrieve trips for") @PathVariable(required = true, name = "vehicleId") String vehicleId,
        @Parameter(description = "Optional trip identifier") @RequestParam(required = false) String tripId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); 
        try {
            if (!userService.belongsToUser(vehicleId, userEmail)) {
                return ResponseEntity.notFound().build();
            }
            if (tripId != null) {
                TripInfo trip = tripInfoService.getTripInfo(tripId, vehicleId);
                return ResponseEntity.ok(List.of(trip));
            }
            List<TripInfo> trips = tripInfoService.getTripInfoByCarId(vehicleId);
            return ResponseEntity.ok(trips.stream().filter(trip -> trip.getTripEnd() != null).toList());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves the latest trip made by a car.
     *
     * @param vehicleId The ID of the car to retrieve the trip for.
     * @return The TripInfo object for the specified trip.
     */
    @GetMapping("/trips/latest/{vehicleId}")
    @Operation(summary = "Retrieve the latest trip made by a car", description = "Retrieve the latest trip made by a specific car")
    @ApiResponse(responseCode = "200", description = "Latest trip retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Car not found or no trips available")
    public ResponseEntity<TripInfo> getLatestTrip(
        @Parameter(description = "ID of the car to retrieve the latest trip for") @PathVariable(required = true, name = "vehicleId") String vehicleId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); 
        try {
            if (!userService.belongsToUser(vehicleId, userEmail)) {
                return ResponseEntity.notFound().build();
            }
            TripInfo trip = tripInfoService.getLatestTripInfo(vehicleId);
            return ResponseEntity.ok(trip);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
