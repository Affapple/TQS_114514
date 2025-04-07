package tqs.homework.canteen.services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.repositories.MealRepository;
import tqs.homework.canteen.repositories.MenuRepository;
import tqs.homework.canteen.repositories.RestaurantRepository;

@Service
public class MenuService implements IMenuService {
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MealRepository mealRepository;

    @Override
    public Menu addMeal(MealDTO meal) {
        Menu menu = menuRepository.findById(meal.getMenuId())
            .orElseThrow(() -> new NoSuchElementException("Menu with id \"" + meal.getMenuId() + "\" not found!")
        );
        
        List<Meal> options = menu.getOptions();
        if ( options.stream().anyMatch( m -> m.getType().equals(meal.getType()) ) ) {
            throw new IllegalArgumentException("Meal of type \"" + meal.getType() + "\" already exists in menu with id \"" + meal.getMenuId() + "\"!");
        }

        logger.info("Adding meal to menu: {}", meal);
        Meal newMeal = mealRepository.save(new Meal(meal.getDescription(), meal.getType(), menu));
        options.add(newMeal);
        menu.setOptions(options);
        return menuRepository.save(menu);
    }

    @Override
    public Menu addMeals(Long menuId, List<MealDTO> meals) {
        if (!menuRepository.existsById(menuId)) {
            throw new NoSuchElementException("Menu with id \"" + menuId + "\" not found!");
        }

        for (MealDTO meal : meals) {
            meal.setMenuId(menuId);
            try {
                addMeal(meal);
            } catch (IllegalArgumentException e) 
            {
                logger.error("Error adding meal to menu: {}", e.getMessage(), e);
            }
        }
        return menuRepository.findById(menuId).get();
    }

    @Override
    public Menu createNewMenu(MenuRequestDTO menuRequest) {
        Restaurant restaurant = restaurantRepository.findById(menuRequest.getRestaurantId())
            .orElseThrow(() -> new NoSuchElementException("Restaurant with id \"" + menuRequest.getRestaurantId() + "\" not found!"));

        List<Menu> menus = restaurant.getMenus();
        if (menus.stream().anyMatch(m -> m.getDate().equals(menuRequest.getDate()) && m.getTime().equals(menuRequest.getTime()))) {
            throw new IllegalArgumentException("Menu with date \"" + menuRequest.getDate() + "\" and time \"" + menuRequest.getTime().getName() + "\" already exists in restaurant with id \"" + menuRequest.getRestaurantId() + "\"!");
        }

        logger.info("Creating new menu: {}", menuRequest);
        Menu menu = menuRepository.save(
            new Menu(menuRequest.getDate(), menuRequest.getTime(), restaurant)
        );

        return addMeals(menu.getId(), menuRequest.getOptions());
    }

    @Override
    public List<Meal> getMeals(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
            .orElseThrow(() -> new NoSuchElementException("Menu with id \"" + menuId + "\" not found!"));

        return menu.getOptions();
    }

    @Override
    public List<Menu> getMenusByRestaurantId(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new NoSuchElementException("Restaurant with id \"" + restaurantId + "\" not found!");
        }

        return menuRepository.findByRestaurant_id(restaurantId);
    }

    @Override
    public List<Menu> getMenusOfRestaurantBetweenDates(Long restaurantId, LocalDate from, LocalDate to) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new NoSuchElementException("Restaurant with id \"" + restaurantId + "\" not found!");
        }
        if (from == null) {
            from = LocalDate.now();
        }
        if (to == null) {
            to = LocalDate.now().plusDays(7);
        }

        return menuRepository.findByRestaurant_idAndDateBetween(restaurantId, from, to);
    }

    @Override
    public void deleteMenu(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new NoSuchElementException("Menu with id \"" + menuId + "\" not found!");
        }

        menuRepository.deleteById(menuId);
    }

    @Override
    public void deleteMeal(Long menuId, Long mealId) {
        if (!menuRepository.existsById(menuId)) {
            throw new NoSuchElementException("Menu with id \"" + menuId + "\" not found!");
        }
        if (!mealRepository.existsByMenu_idAndId(menuId, mealId)) {
            throw new NoSuchElementException("Meal with id \"" + mealId + "\" not found in menu with id \"" + menuId + "\"!");
        }

        mealRepository.deleteById(mealId);
    }
}
