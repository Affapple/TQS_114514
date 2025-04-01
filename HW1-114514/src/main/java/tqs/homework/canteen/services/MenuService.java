package tqs.homework.canteen.services;

import java.util.List;

import org.springframework.stereotype.Service;

import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Menu;

@Service
public class MenuService implements IMenuService {

    @Override
    public Menu addMeal(Integer menuId, MealDTO meal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu addMeals(Integer menuId, List<Meal> meals) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu createNewMenu(MenuRequestDTO menuRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu getMeals(Integer menuId) {
        // TODO Auto-generated method stub
        return null;
    }

    
}
