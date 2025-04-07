package tqs.homework.canteen.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.services.MenuService;
import tqs.homework.canteen.services.RestaurantService;

@RestController
@RequestMapping("/api/v1/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private MenuService menuService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        logger.info("Received request to get all restaurants");
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        logger.info("Found {} restaurants", restaurants.size());
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(
        @RequestBody Restaurant restaurant
    ) {
        logger.info("Received request to create restaurant: {}", restaurant);
        Restaurant savedRestaurant = restaurantService.saveNewRestaurant(restaurant);
        logger.info("Restaurant created successfully: {}", savedRestaurant);
        return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> getRestaurant(
        @PathVariable Long restaurantId
    ) {
        logger.info("Received request to get restaurant: {}", restaurantId);
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        logger.info("Restaurant found: {}", restaurant);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/menus")
    public ResponseEntity<List<Menu>> getMenusOfRestaurant(
        @PathVariable Long restaurantId,
        @RequestParam(required = false) LocalDate from,
        @RequestParam(required = false) LocalDate to
    ) {
        logger.info("Received request to get menus of restaurant: {}", restaurantId);
        List<Menu> menus = menuService.getMenusOfRestaurantBetweenDates(restaurantId, from, to);
        logger.info("Found {} menus", menus.size());
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }
}
