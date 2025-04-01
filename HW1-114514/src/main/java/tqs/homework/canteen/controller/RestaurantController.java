package tqs.homework.canteen.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tqs.homework.canteen.DTOs.MenuDTO;
import tqs.homework.canteen.entities.Restaurant;

@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{restaurant_id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/menus")
    public ResponseEntity<List<MenuDTO>> getMenusOfRestaurant(@RequestParam Integer days) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }
}
