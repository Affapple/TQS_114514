package ies.carbox.api.RestAPI.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ies.carbox.api.RestAPI.entity.User;
import ies.carbox.api.RestAPI.CONSTANTS;
import ies.carbox.api.RestAPI.entity.Car;
import ies.carbox.api.RestAPI.service.UserService;
import ies.carbox.api.RestAPI.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping(CONSTANTS.apiBase + "/admin")
public class AdminController {

        /**
     * Gets a list of all cars and their associated users. For admin purposes only.
     * 
     */
    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;


    @GetMapping("/users") // to get all the users through admin
    @Operation(
        summary = "Get all users",
        description = "Get a list of all users",
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "List of all users", 
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
        }
    )
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/user/{email}") // to get a specific user through admin
    @Operation(
        summary = "Get a specific user",
        description = "Get a specific user",
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Specific user", 
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
        }
    )

    public ResponseEntity<User> loadUserByUsername(@PathVariable String email) {
        User user = userService.loadUserByUsername(email);    //(email); // ! Add loadUserByUsername method in UserService
        return ResponseEntity.ok(user);
    }

    @GetMapping("/cars") // to get all the cars of a specific user through admin
    @Operation(
        summary = "Get all cars of a specific user",
        description = "Get a list of all cars of a specific user",
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "List of all cars of a specific user", 
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
        }
    )
    public ResponseEntity<List<Car>> getAllUserCar(@RequestParam(name = "email", required = false) String email) {
        System.out.println("Email was " + email);
        if (email == null) {
            List<Car> cars = carService.getAllCars();       // ! Add getAllCars method in CarService or add new service and repository for admin
            return ResponseEntity.ok(cars);
        }

        List<List<String>> userCars = userService.getListOfEcuIds(email);

        List<String> carsEcuId = new ArrayList<>();
        userCars.forEach((car) -> {carsEcuId.add(car.get(0));} );
        userCars.forEach(System.out::println);

        List<Car> cars = carService.getAllUserCars(carsEcuId);
        cars.forEach(System.out::println);

        return ResponseEntity.ok(cars);
    }
}