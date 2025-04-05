package tqs.homework.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.services.MenuService;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping
    public ResponseEntity<Menu> createNewMenu(
        @RequestBody MenuRequestDTO menuRequest
    ) {
        Menu menu = menuService.createNewMenu(menuRequest);
        
        return new ResponseEntity<>(
            menu,
            HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<Menu> addMealToMenu(
        @RequestBody MealDTO meal
    ) {
        return new ResponseEntity<>(
            menuService.addMeal(meal),
            HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{menuId}/{mealId}")
    public ResponseEntity<Void> deleteMealFromMenu(
        @PathVariable Long menuId, 
        @PathVariable Long mealId
    ) {
        menuService.deleteMeal(menuId, mealId);
        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }
}
