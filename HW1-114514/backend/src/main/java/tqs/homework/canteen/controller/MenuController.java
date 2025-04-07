package tqs.homework.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.services.MenuService;

@RestController
@RequestMapping("/api/v1/menu")
@CrossOrigin(origins = "*")
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @PostMapping
    public ResponseEntity<Menu> createNewMenu(
        @RequestBody MenuRequestDTO menuRequest
    ) {
        logger.info("Received menu request: {}", menuRequest);
        Menu menu = menuService.createNewMenu(menuRequest);

        logger.info("Menu created: {}", menu);
        return new ResponseEntity<>(
            menu,
            HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<Menu> addMealToMenu(
        @RequestBody MealDTO meal
    ) {
        logger.info("Received add meal to menu request: {}", meal);
        Menu menu = menuService.addMeal(meal);

        logger.info("Menu added successfully: {}", menu);
        return new ResponseEntity<>(
            menu,
            HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        logger.info("Received delete menu request: menuId={}", menuId);
        menuService.deleteMenu(menuId);

        logger.info("Menu deleted successfully");
        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{menuId}/{mealId}")
    public ResponseEntity<Void> deleteMealFromMenu(
        @PathVariable Long menuId, 
        @PathVariable Long mealId
    ) {
        logger.info("Received delete meal from menu request: menuId={}, mealId={}", menuId, mealId);
        menuService.deleteMeal(menuId, mealId);

        logger.info("Meal deleted successfully");
        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }
}