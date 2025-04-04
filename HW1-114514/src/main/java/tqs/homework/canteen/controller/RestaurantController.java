package tqs.homework.canteen.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.homework.canteen.DTOs.MenuDTO;
import tqs.homework.canteen.entities.Restaurant;

@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        /*
         * Given restaurant "Castro", "Grelhados", "Santiago" exist
         * when getAllRestaurants is called
         * then a list of <Castro, Grelhados, Santiago> is returned
         * 
         * Given no restaurants exist
         * when getAllRestaurants is called
         * then an empty list is returned
         */
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{restaurant_id}")
    public ResponseEntity<Restaurant> getRestaurant(
        @PathVariable Long restaurantId
    ) {
        /*
         * Given restaurant "Castro" exists
         * when getRestaurant is called with id 1
         * then the restaurant "Castro" is returned
         * 
         * Given restaurants exists
         * when getRestaurant is called with an invalid id
         * then a body with status 404 is returned
         */
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{restaurant_id}/menus")
    public ResponseEntity<List<MenuDTO>> getMenusOfRestaurant(
        @PathVariable Integer restaurantId,
        @RequestBody LocalDate from,
        @RequestBody LocalDate to
    ) {
        /*
         * Given restaurant "Castro" exists 
         *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
         * when getMenusOfRestaurant is called with id 1, 2025-04-01 and 2025-04-02
         * then a list of <Menu1, Menu2> is returned
         * 
         * Given restaurant "Castro" exists 
         *  and has no menus for the dates 2025-04-01 and 2025-04-02
         * when getMenusOfRestaurant is called with id 1, 2025-04-01 and 2025-04-02
         * then an empty list is returned
         * 
         * Given restaurant "Castro" exists 
         *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
         * when getMenusOfRestaurant is called with id 1, 2025-03-30 and 2025-04-01
         * then a list of <Menu1> is returned
         * 
         * Given restaurant "Castro" exists 
         *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
         * when getMenusOfRestaurant is called with id 1, 2025-04-03 and 2025-04-04
         * then a list of <Menu2> is returned
         * 
         * Given restaurant "Castro" exists 
         *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
         * when getMenusOfRestaurant is called with id 1, 2025-04-02 and null
         * then a list of <Menu2> is returned
         * 
         * Given restaurant "Castro" exists 
         *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
         * when getMenusOfRestaurant is called with id 1, null and 2025-04-01
         * then a list of <Menu1> is returned
         * 
         * Given restaurant "Castro" exists 
         *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
         * when getMenusOfRestaurant is called with id 1, null and null
         * then a list of <Menu1, Menu2> is returned
         */
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }
}
