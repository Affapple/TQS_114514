package tqs.homework.canteen.services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.repositories.MenuRepository;
import tqs.homework.canteen.repositories.RestaurantRepository;

@Service
public class MenuService implements IMenuService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public Menu addMeal(MealDTO meal) {
        Menu menu = menuRepository.findById(meal.getMenuId())
            .orElseThrow(() -> new NoSuchElementException("Menu with id \"" + meal.getMenuId() + "\" not found!")
        );
        
        List<Meal> options = menu.getOptions();
        if (options.stream().anyMatch(
                m -> m.getType().equals(meal.getType())
        )) {
            throw new IllegalArgumentException("Meal of type \"" + meal.getType() + "\" already exists in menu with id \"" + meal.getMenuId() + "\"!");
        }

        options.add(new Meal(meal.getDescription(), meal.getType(), menu));
        menu.setOptions(options);
        return menuRepository.save(menu);
    }

    @Override
    public Menu addMeals(Long menuId, List<MealDTO> meals) {
        Menu menu = menuRepository.findById(menuId)
            .orElseThrow(() -> new NoSuchElementException("Menu with id \"" + menuId + "\" not found!"));

        List<Meal> options = menu.getOptions();
        for (MealDTO meal : meals) {
            try {
                addMeal(meal);
            } catch (IllegalArgumentException e) {
                // Ignore meals that already exist in the menu
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

        Menu menu = new Menu(menuRequest.getDate(), menuRequest.getTime(), restaurant);
        return menuRepository.save(menu);
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
            from = LocalDate.MIN;
        }
        if (to == null) {
            to = LocalDate.MAX;
        }

        return menuRepository.findByRestaurant_idAndDateBetween(restaurantId, from, to);
    }
}
