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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.services.MenuService;

@RestController
@RequestMapping("/api/v1/menu")
@CrossOrigin(origins = "*")
@Tag(name = "Menu", description = "Menu and meal management APIs")
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @Operation(summary = "Create a new menu", description = "Creates a new menu with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Menu created successfully"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Menu> createNewMenu(
        @Parameter(description = "Menu details to create", required = true)
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

    @Operation(summary = "Add meal to menu", description = "Adds a new meal to an existing menu")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Meal added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Menu not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public ResponseEntity<Menu> addMealToMenu(
        @Parameter(description = "Meal details to add", required = true)
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

    @Operation(summary = "Delete menu", description = "Deletes a menu by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Menu not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(
        @Parameter(description = "ID of the menu to delete", required = true)
        @PathVariable Long menuId
    ) {
        logger.info("Received delete menu request: menuId={}", menuId);
        menuService.deleteMenu(menuId);

        logger.info("Menu deleted successfully");
        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }

    @Operation(summary = "Delete meal from menu", description = "Deletes a specific meal from a menu")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Meal deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Menu or meal not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{menuId}/{mealId}")
    public ResponseEntity<Void> deleteMealFromMenu(
        @Parameter(description = "ID of the menu", required = true)
        @PathVariable Long menuId,
        @Parameter(description = "ID of the meal to delete", required = true)
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