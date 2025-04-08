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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import tqs.homework.canteen.DTOs.MenuListResponseDTO;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.services.MenuService;
import tqs.homework.canteen.services.RestaurantService;

@RestController
@RequestMapping("/api/v1/restaurants")
@CrossOrigin(origins = "*")
@Tag(name = "Restaurant", description = "Restaurant management APIs")
public class RestaurantController {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private MenuService menuService;

    @Operation(summary = "Get all restaurants", description = "Retrieves a list of all available restaurants")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurants"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        logger.info("Received request to get all restaurants");
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        logger.info("Found {} restaurants", restaurants.size());
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @Operation(summary = "Create a new restaurant", description = "Creates a new restaurant with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurant created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(
        @Parameter(description = "Restaurant details to create", required = true)
        @RequestBody Restaurant restaurant
    ) {
        logger.info("Received request to create restaurant: {}", restaurant);
        Restaurant savedRestaurant = restaurantService.saveNewRestaurant(restaurant);
        logger.info("Restaurant created successfully: {}", savedRestaurant);
        return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
    }

    @Operation(summary = "Get restaurant by ID", description = "Retrieves a specific restaurant by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurant"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> getRestaurant(
        @Parameter(description = "ID of the restaurant to retrieve", required = true)
        @PathVariable Long restaurantId
    ) {
        logger.info("Received request to get restaurant: {}", restaurantId);
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        logger.info("Restaurant found: {}", restaurant);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @Operation(summary = "Get restaurant menus", description = "Retrieves menus for a specific restaurant within an optional date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved menus"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{restaurantId}/menus")
    public ResponseEntity<MenuListResponseDTO> getMenusOfRestaurant(
        @Parameter(description = "ID of the restaurant", required = true)
        @PathVariable Long restaurantId,
        @Parameter(description = "Start date for menu filtering (optional)")
        @RequestParam(required = false) LocalDate from,
        @Parameter(description = "End date for menu filtering (optional)")
        @RequestParam(required = false) LocalDate to
    ) {
        logger.info("Received request to get menus of restaurant: {} from {} to {}", restaurantId, from, to);
        
        List<Menu> menus = menuService.getMenusOfRestaurantBetweenDates(restaurantId, from, to);
        logger.info("Found {} menus", menus.size());
        
        boolean hasMore = false;
        if (to == null && menus.size() > 0) {
            to = menus.getLast().getDate();
            hasMore = menuService.hasMenusFrom(restaurantId, to.plusDays(1));
        }

        return new ResponseEntity<>(new MenuListResponseDTO(
            menus,
            hasMore
        ), HttpStatus.OK);
    }
}
