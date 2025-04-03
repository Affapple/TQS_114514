package tqs.homework.canteen.services;

import java.time.LocalDate;
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
        /**
         * Given menu doesnt exits
         * when add meal
         * then throw NoSuchElement
         *
         *
         * Given menu exist 
         *    and meal of type already exists
         * when add meal
         * then throw illegal Argument exception
         *  
         *
         * Given menu exists
         *    and when meal of type doesnt exists
         * when add meal
         * then add meal
         */
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu addMeals(Integer menuId, List<Meal> meals) {
         /**
          * Wrapper of addMeal for a list
          */
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu createNewMenu(MenuRequestDTO menuRequest) {
        /**
         * Given restaurant doesnt exists
         * when createNewMenu to restaurant
         * then throw NoSuchElement
         *
         * Given resttaurant exists
         * when menu for given date and time table already exists
         * then throw IllegalArgException
         * 
         * Given resttaurant exists
         * when menu for given date and time table dont exists
         * then create new menu
         */
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Meal> getMeals(Integer menuId) {
        /**
         * Given menu doesnt exists
         * when get meals
         * then throw NoSuchElement
         *
         * Given menu exists
         *  and menu it has no meals
         * when get meals
         * then throw NoSuchElement
         *
         * Given menu exists
         *  and it has meals 
         * when get meals
         * then return list of meals
         */
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Meal> getMealsByRestaurantId(Long restaurantId) {
        /**
         * Given resaurant doesnt exists
         * when get meals
         * then throw NoSuchElement
         *
         * Given restaurant exists
         *  and restaurant has no menus
         * when get meals
         * then throw NoSuchElement
         *
         * Given restaurant exists
         *  and it has menus
         * when get meals
         * then return list of menus
         */
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Meal> getMealsOfRestaurantBetweenDates(Long restaurantId, LocalDate from, LocalDate to) {
        /**
         * Given resaurant doesnt exists
         * when get meals
         * then throw NoSuchElement
         *
         *
         * Given restaurant exists
         *  and restaurant has no menus in the time range 
         * when get meals
         * then throw NoSuchElement
         *
         * Given restaurant exists
         *  and it has menus in the time rang
         * when get meals
         * then return list of menus
         *
         * Given restaurant exists
         *  and <from> is null
         * when get meals until <to>
         * then return all list of menus before <to>
         *
         * Given restaurant exists
         *  and <to> is null
         * when get meals from <from>
         * then return all list of menus starting <from> until forever
         */
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMealsOfRestaurantBetweenDates'");
    }
}
