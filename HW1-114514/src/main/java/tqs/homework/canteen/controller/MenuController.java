package tqs.homework.canteen.controller;

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
import tqs.homework.canteen.DTOs.MenuDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {
    @PostMapping
    public ResponseEntity<MenuDTO> createNewMenu(MenuRequestDTO menuRequest) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/{menu_id}")
    public ResponseEntity<MenuDTO> addMealToMenu(
        @PathVariable Long menuId,
        @RequestBody MealDTO meal
    ) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{menu_id}")
    public ResponseEntity<MenuDTO> deleteMenu(@PathVariable Long menuId) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{menu_id}/{meal_id}")
    public ResponseEntity<MenuDTO> deleteMealFromMenu(
        @PathVariable Long menuId, 
        @PathVariable Long mealId
    ) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }
}
