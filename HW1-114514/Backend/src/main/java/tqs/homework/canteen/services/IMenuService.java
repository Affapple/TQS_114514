package tqs.homework.canteen.services;

import java.time.LocalDate;
import java.util.List;

import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Menu;

public interface IMenuService {
    public Menu createNewMenu(MenuRequestDTO menuRequest);
    public Menu addMeal(MealDTO meal);
    public Menu addMeals(Long menuId, List<MealDTO> meals);
    public List<Meal> getMeals(Long menuId);
    public List<Menu> getMenusByRestaurantId(Long restaurantId);
    public List<Menu> getMenusOfRestaurantBetweenDates(Long restaurantId, LocalDate from, LocalDate to);
    public void deleteMenu(Long menuId);
    public void deleteMeal(Long menuId, Long mealId);
}
