package tqs.homework.canteen.services;

import java.util.List;

import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Menu;

public interface IMenuService {
    public Menu createNewMenu(MenuRequestDTO menuRequest);
    public Menu addMeal(Integer menuId, MealDTO meal);
    public Menu addMeals(Integer menuId, List<Meal> meals);
    public Menu getMeals(Integer menuId);
}
