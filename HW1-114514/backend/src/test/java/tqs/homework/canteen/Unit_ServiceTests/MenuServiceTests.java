package tqs.homework.canteen.Unit_ServiceTests;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.EnumTypes.MealType;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.repositories.MealRepository;
import tqs.homework.canteen.repositories.MenuRepository;
import tqs.homework.canteen.repositories.RestaurantRepository;
import tqs.homework.canteen.services.MenuService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MenuServiceTests {
  
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    public void setup() {
    }

    /* METHOD: addMeal */
    /**
     * Given menu doesnt exits
     * when add meal
     * then throw NoSuchElement
     */
    @Test
    public void whenAddMealToNonExistentMenu_thenThrowError() {
        Long nonExistentMenuID = 10L;
        when(menuRepository.findById(nonExistentMenuID)).thenReturn(Optional.empty());

        MealDTO mealDTO = new MealDTO(nonExistentMenuID, "New tasty meal", MealType.MEAT);

        assertThrowsExactly(
            NoSuchElementException.class,
            () -> menuService.addMeal(mealDTO),
            "No error thrown when added meal to a non-existent menu!"
        );
    }

    /*
     * Given menu exist 
     *    and meal of type already exists
     * when add meal
     * then throw illegal Argument exception
     */
    @Test
    public void whenAddMealOfSameType_thenThrowError() {
        Long existentMenuID = 1L;
        Menu existentMenu = new Menu(
            existentMenuID,
            LocalDate.now(),
            MenuTime.LUNCH,
            10,
            new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>()),
            new ArrayList<>()
        );

        when(menuRepository.findById(existentMenuID)).thenReturn(Optional.of(existentMenu));
        when(menuRepository.save(Mockito.any(Menu.class))).thenReturn(existentMenu);
        when(mealRepository.save(Mockito.any(Meal.class))).thenReturn(new Meal("New tasty meal", MealType.MEAT, existentMenu));

        MealDTO mealDTO = new MealDTO(existentMenuID, "New tasty meal", MealType.MEAT);
        menuService.addMeal(mealDTO);

        MealDTO repeatedMealDTO = new MealDTO(existentMenuID, "Another tasty meal", MealType.MEAT);

        assertThrowsExactly(
            IllegalArgumentException.class,
            () -> menuService.addMeal(repeatedMealDTO),
            "No error thrown when added meal of same type!"
        );
    }


    /*
     * Given menu exists
     *    and when meal of type doesnt exists
     * when add meal
     * then add meal
     */
    @Test
    public void whenAddMealToExistentMenu_thenAddMeal() {
        Long existentMenuID = 1L;
        Menu existentMenu = new Menu(
            existentMenuID,
            LocalDate.now(),
            MenuTime.LUNCH,
            10,
            new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>()),
            new ArrayList<>()
        );

        when(menuRepository.findById(existentMenuID)).thenReturn(Optional.of(existentMenu));
        when(menuRepository.save(Mockito.any(Menu.class))).thenReturn(existentMenu);
        when(mealRepository.save(Mockito.any(Meal.class))).thenReturn(new Meal("New tasty meal", MealType.MEAT, existentMenu));


        MealDTO mealDTO = new MealDTO(existentMenuID, "New tasty meal", MealType.MEAT);

        Menu updatedMenu = menuService.addMeal(mealDTO);
        Meal addedMeal = updatedMenu.getOptions().getFirst();

        assertEquals(mealDTO.getDescription(), addedMeal.getDescription());
        assertEquals(mealDTO.getType(), addedMeal.getType());
        assertEquals(existentMenu, addedMeal.getMenu());
        assertThat(addedMeal.getReservations(), is(empty()));
    }


    /* METHOD: createNewMenu */
    /**
     * Given restaurant doesnt exists
     * when createNewMenu to restaurant
     * then throw NoSuchElement
     */
    @Test
    public void whenCreateMenuInNonExistentRestaurant_thenThrowError() {
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

        MenuRequestDTO menuRequest = new MenuRequestDTO(
            10l,
            new ArrayList<>(),
            LocalDate.now(),
            MenuTime.LUNCH
        );

        assertThrowsExactly(
            NoSuchElementException.class,
            () -> menuService.createNewMenu(menuRequest),
            "No error thrown when attempting to create new menu at non-existent restaurant!"
        );
    }
    /**
     * Given resttaurant exists
     * when menu for given date and time table dont exists
     * then create new menu
     */
    @Test
    public void whenAddingNewMenu_thenMenuIsAdded() {
        Restaurant restaurant = new Restaurant(10l, "Test Restaurant", "Test Location", 10, new ArrayList<>());
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(Mockito.any(Restaurant.class))).thenReturn(restaurant);

        Menu existentMenu = new Menu(LocalDate.now(), MenuTime.LUNCH, restaurant);
        existentMenu.setId(1L);
        when(menuRepository.save(Mockito.any(Menu.class))).thenReturn(existentMenu);
        when(mealRepository.save(Mockito.any(Meal.class))).thenReturn(new Meal("New tasty meal", MealType.MEAT, existentMenu));
        when(menuRepository.findById(existentMenu.getId())).thenReturn(Optional.of(existentMenu));
        when(menuRepository.existsById(existentMenu.getId())).thenReturn(true);

        MenuRequestDTO menuRequest = new MenuRequestDTO(
            10l,
            new ArrayList<>(),
            LocalDate.now(),
            MenuTime.LUNCH
        );
        Menu newMenu = menuService.createNewMenu(menuRequest);
        assertThat(
            newMenu,
            is(not(nullValue()))
        );
        assertThat(newMenu.getRestaurant(), is(restaurant));
        assertThat(newMenu.getOptions(), hasSize(0));
    }

    /**
     * Given resttaurant exists
     * when menu for given date and time table already exists
     * then throw IllegalArgException
     */
    @Test
    public void whenAddingNewAlreadyExistingMenu_thenThrowError() {
        Restaurant restaurant = new Restaurant(10l, "Test Restaurant", "Test Location", 10, new ArrayList<>());
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));

        restaurant
          .getMenus()
          .add(
            new Menu(LocalDate.now(), MenuTime.LUNCH, restaurant)
        );

        MenuRequestDTO repeatedMenuRequest = new MenuRequestDTO(
            10l,
            new ArrayList<>(),
            LocalDate.now(),
            MenuTime.LUNCH
        );

        assertThrowsExactly(
            IllegalArgumentException.class,
            () -> menuService.createNewMenu(repeatedMenuRequest),
            "No error thrown when attempting to create a menu of the same type and date at restaurant!"
        );
    }


    /* METHOD: getMeals */ 
    /**
     * Given menu doesnt exists when get meals
     * then throw NoSuchElement
     */
    @Test
    public void whenGettingMealsOfNonExistentMenu_thenThrowError() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrowsExactly(
            NoSuchElementException.class,
            () -> menuService.getMeals(anyLong()),
             "No error thrown when attempting to get meals of non-existent menu!"
        );
    }

    /*
     * Given menu exists
     *  and menu it has no meals
     * when get meals
     * then return empty list
     */
    @Test
    public void whenGettingMealsOfMenuWithNoMeals_thenReturnEmptyList() {
        Menu menu = new Menu(
            1L,
            LocalDate.now(),
            MenuTime.LUNCH,
            10,
            new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>()), 
            new ArrayList<>()
        );
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));

        assertThat(
            menuService.getMeals(1L),
            is(empty())
        );
    }
    /*
     * Given menu exists
     *  and it has meals 
     * when get meals
     * then return list of meals
     */
    @Test
    public void whenGettingMealsOfMenuWithMeals_thenReturnListOfMeals() {
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>());
        Menu menu = new Menu(
            1L,
            LocalDate.now(),
            MenuTime.LUNCH,
            10,
            restaurant,
            new ArrayList<>()
        );
        when(menuRepository.save(Mockito.any(Menu.class)))
            .thenReturn(menu);
        when(menuRepository.findById(1l))
            .thenReturn(Optional.of(menu));
        when(menuRepository.existsById(1L)).thenReturn(true);
        when(mealRepository.save(Mockito.any(Meal.class)))
            .thenAnswer( invocation -> {
                    Meal meal = invocation.getArgument(0);
                    meal.setMenu(menu);
                    return meal;
                }
            );

        Menu newMenu = menuService.addMeal(new MealDTO(1L, "Meal 1", MealType.MEAT));

        System.out.println("New menu:" + newMenu);
        System.out.println("\n\nSECOND MEAL");
        menuService.addMeal(new MealDTO(1L, "Meal 2", MealType.FISH));

        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));

        List<Meal> meals = menuService.getMeals(1L);
        assertThat(
            meals,
            is(not(empty()))
        );
        assertThat(meals.size(), is(2));
    }

    /* getMenusByRestaurantId */ 
    /**
     * Given resaurant doesnt exists
     * when get menus
     * then throw NoSuchElement
     */
    @Test
    public void whenGettingMealsOfNonExistentRestaurant_thenThrowError() {
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrowsExactly(
            NoSuchElementException.class,
            () -> menuService.getMenusByRestaurantId(anyLong()),
            "No error thrown when attempting to get menus of non-existent restaurant!"
        );
    }
    /* Given restaurant exists
     *  and restaurant has no menus
     * when get menus
     * then return empty list
     */
    @Test
    public void whenGettingMenusOfRestaurantWithNoMenus_thenReturnEmptyList() {
        List<Menu> menus = new ArrayList<>();
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant", "Test Location", 10, menus);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        when(menuRepository.findByRestaurant_id(1L)).thenReturn(menus);

        assertThat(
            menuService.getMenusByRestaurantId(1L),
            is(empty())
        );
    }

    /* Given restaurant exists
     *  and it has menus
     * when get menus
     * then return list of menus
     */
    @Test
    public void whenGettingMenusOfRestaurantWithMenus_thenReturnListOfMenus() {
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>());
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.existsById(1L)).thenReturn(true);

        Menu menu1 = new Menu(1L, LocalDate.now(), MenuTime.LUNCH, 10, restaurant, new ArrayList<>());
        Menu menu2 = new Menu(2L, LocalDate.now(), MenuTime.DINNER, 10, restaurant, new ArrayList<>());

        when(menuRepository.findByRestaurant_id(1L)).thenReturn(List.of(menu1, menu2));

        assertThat(
            menuService.getMenusByRestaurantId(1L),
            hasSize(2)
        );
    }

    /* getMenusOfRestaurantBetweenDate */
    /**
     * Given resaurant doesnt exists
     * when get meals
     * then throw NoSuchElement
     */
    @Test
    public void whenGettingMenusOfNonExistentRestaurantByDate_thenThrowError() {
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrowsExactly(
            NoSuchElementException.class,
            () -> menuService.getMenusOfRestaurantBetweenDates(
                10L,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
            ),
            "No error thrown when attempting to get meals of non-existent restaurant!"
        );
    }
    /* Given restaurant exists
     *  and restaurant has no menus in the time range 
     * when get meals
     * then return empty list
     */
    @Test
    public void whenGettingMenusOfRestaurantWithNoMenusInTimeRange_thenReturnEmptyList() {
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>());
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));

        assertThrowsExactly(
            NoSuchElementException.class,
            () -> menuService.getMenusOfRestaurantBetweenDates(
                10L,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
            ),
            "No error thrown when attempting to get menus of restaurant with no menus in time range!"
        );
    }

    /* Given restaurant exists
     *  and it has menus in the time rang
     * when get meals
     * then return list of menus
     */
    @Test
    public void whenGettingMenusOfRestaurantWithMenusInTimeRange_thenReturnListOfMenus() {
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>());
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.existsById(anyLong())).thenReturn(true);
        Menu menu1 = new Menu(1L, LocalDate.now(), MenuTime.LUNCH, 10, restaurant, new ArrayList<>());
        Menu menu2 = new Menu(2L, LocalDate.now(), MenuTime.DINNER, 10, restaurant, new ArrayList<>());
        when(
            menuRepository
              .findByRestaurant_idAndDateBetween(
                anyLong(),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class)
            )
        ).thenReturn(List.of(menu1, menu2));
        
        when(menuRepository.findByRestaurant_id(anyLong())).thenReturn(List.of(menu1, menu2));

        assertThat(
            menuService.getMenusOfRestaurantBetweenDates(1L, LocalDate.now(), LocalDate.now().plusDays(1)),
            hasSize(2)
        );
    }
    /*
     * Given restaurant exists
     *  and <from> is null
     * when get meals until <to>
     * then return all list of menus before <to>
     */
    @Test
    public void whenGettingMenusOfRestaurantWithMenusInTimeRangeFromNull_thenReturnListOfMenus() {
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>());
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.existsById(anyLong())).thenReturn(true);
        
        Menu menu1 = new Menu(1L, LocalDate.now(), MenuTime.LUNCH, 10, restaurant, new ArrayList<>());
        Menu menu2 = new Menu(2L, LocalDate.now().plusDays(1), MenuTime.DINNER, 10, restaurant, new ArrayList<>());
        when(
            menuRepository
              .findByRestaurant_idAndDateBetween(
                anyLong(),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class)
            )
        ).thenReturn(List.of(menu1));
        
        when(menuRepository.findByRestaurant_id(anyLong())).thenReturn(List.of(menu1, menu2));

        assertThat(
            menuService.getMenusOfRestaurantBetweenDates(1L, null, LocalDate.now()),
            hasSize(1)
        );
    }
    /* Given restaurant exists
     *  and <to> is null
     * when get meals from <from>
     * then return all list of menus starting <from> until forever
     */
    @Test
    public void whenGettingMenusOfRestaurantWithMenusInTimeRangeToNull_thenReturnListOfMenus() {
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>());
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.existsById(anyLong())).thenReturn(true);

        Menu menu1 = new Menu(1L, LocalDate.now(), MenuTime.LUNCH, 10, restaurant, new ArrayList<>());
        Menu menu2 = new Menu(2L, LocalDate.now().plusDays(1), MenuTime.DINNER, 10, restaurant, new ArrayList<>());
        when(menuRepository.findByRestaurant_id(anyLong())).thenReturn(List.of(menu1, menu2));        
        when(
            menuRepository
              .findByRestaurant_idAndDateBetween(
                anyLong(),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class)
            )
        ).thenReturn(List.of(menu2));
        
        when(menuRepository.findByRestaurant_id(anyLong())).thenReturn(List.of(menu1, menu2));

        assertThat(
            menuService.getMenusOfRestaurantBetweenDates(1L, LocalDate.now().plusDays(1), null),
            hasSize(1)
        );
    }


    /**
     * Given menu dont exists
     * when add multiple meals
     * then throw NoSuchElement
     */
    @Test
    public void whenAddingMultipleMealsToNonExistentMenu_thenThrowError() {
        when(menuRepository.existsById(anyLong())).thenReturn(false);

        assertThrowsExactly(
            NoSuchElementException.class,
            () -> menuService.addMeals(1L, List.of(new MealDTO(1L, "Meal 1", MealType.MEAT))),
            "No error thrown when attempting to add multiple meals to non-existent menu!"
        );
    }
    /**
     * Given menu exists
     * when add multiple meals
     * then add all meals
     */
    @Test
    public void whenAddingMultipleMeals_thenAllMealsAreAdded() {
        Menu menu = new Menu(1L, LocalDate.now(), MenuTime.LUNCH, 10, new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>()), new ArrayList<>());

        when(menuRepository.existsById(anyLong())).thenReturn(true);
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        when(menuRepository.save(Mockito.any(Menu.class))).thenReturn(menu);
        when(mealRepository.save(Mockito.any(Meal.class))).thenAnswer( invocation -> {
            Meal meal = invocation.getArgument(0);
            meal.setMenu(menu);
            return meal;
        });

        menuService.addMeals(
            1L, 
            List.of(
                new MealDTO(null, "Meal 1", MealType.MEAT),
                new MealDTO(null, "Meal 2", MealType.FISH)
            )
        );        

        
        assertThat(
            menu.getOptions(),
            hasSize(2)
        );
        assertThat(
            menu.getOptions().getFirst().getDescription(),
            is("Meal 1")
        );
        assertThat(
            menu.getOptions().getLast().getDescription(),
            is("Meal 2")
        );
    }


    /**
     * Given menu exists
     * when add repeated meal
     * then meal is not added to menu
     */
    @Test
    public void whenAddingRepeatedMeal_thenRepeatedMealIsNotAdded() {
        Menu menu = new Menu(1L, LocalDate.now(), MenuTime.LUNCH, 10, new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>()), new ArrayList<>());

        when(menuRepository.existsById(anyLong())).thenReturn(true);
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        when(menuRepository.save(Mockito.any(Menu.class))).thenReturn(menu);
        when(mealRepository.save(Mockito.any(Meal.class))).thenAnswer( invocation -> {
            Meal meal = invocation.getArgument(0);
            meal.setMenu(menu);
            return meal;
        });

              
        menuService.addMeals(
            1L, 
            List.of(
                new MealDTO(null, "Meal 1", MealType.MEAT), 
                new MealDTO(null, "Meal 2", MealType.MEAT)
            )
        );        

        assertThat(
            menu.getOptions(),
            hasSize(1)
        );
    }


    /**
     * Given Menu doesnt exists
     * when delete menu 
     * then throw NoSuchElement
     */
    @Test
    public void whenDeletingNonExistentMenu_thenThrowError() {
        when(menuRepository.existsById(anyLong())).thenReturn(false);

        assertThrowsExactly(
            NoSuchElementException.class,
            () -> menuService.deleteMenu(1L),
            "No error thrown when attempting to delete non-existent menu!"
        );
    }

    /**
     * Given menu exists
     * when delete menu
     * then delete menu
     */
    @Test
    public void whenDeletingMenu_thenMenuIsDeleted() {
        Menu menu = new Menu(1L, LocalDate.now(), MenuTime.LUNCH, 10, new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>()), new ArrayList<>());

        when(menuRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(menuRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> menuService.deleteMenu(1L));
    }

    /**
     * Given menu doesnt exists
     * when delete meal
     * then throw NoSuchElement
     */
    @Test
    public void whenDeletingMealFromNonExistentMenu_thenThrowError() {
        when(menuRepository.existsById(anyLong())).thenReturn(false);
        
        assertThrowsExactly(
            NoSuchElementException.class,
            () -> menuService.deleteMeal(1L, 1L),
            "No error thrown when attempting to delete meal from non-existent menu!"
        );
    }

    /**
     * Given menu doesnt exists
     * when delete meal
     * then Throw NoSuchElement
     */
    @Test
    public void whenDeletingMealFromExistentMenu_thenThrowError() {
        when(menuRepository.existsById(anyLong())).thenReturn(false);
        
        assertThrowsExactly(
            NoSuchElementException.class,
            () -> menuService.deleteMeal(1L, 1L),
            "No error thrown when attempting to delete meal from non-existent menu!"
        );
    }

    /**
     * Given menu exists
     * when delete meal
     * then delete meal
     */
    @Test
    public void whenDeletingMeal_thenMealIsDeleted() {
        Menu menu = new Menu(1L, LocalDate.now(), MenuTime.LUNCH, 10, new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>()), new ArrayList<>());

        when(menuRepository.existsById(anyLong())).thenReturn(true);
        when(mealRepository.existsByMenu_idAndId(anyLong(), anyLong())).thenReturn(true);
        doNothing().when(mealRepository).deleteById(anyLong());
        
        assertDoesNotThrow(() -> menuService.deleteMeal(1L, 1L));
    }

    /**
     * Given menu exists
     * when delete unexistent meal
     * then throw NoSuchElement
     */
    @Test
    public void whenDeletingUnexistentMeal_thenThrowError() {
        Menu menu = new Menu(1L, LocalDate.now(), MenuTime.LUNCH, 10, new Restaurant(1L, "Test Restaurant", "Test Location", 10, new ArrayList<>()), new ArrayList<>());

        when(menuRepository.existsById(anyLong())).thenReturn(true);
        when(mealRepository.existsByMenu_idAndId(anyLong(), anyLong())).thenReturn(false);

        assertThrowsExactly(
            NoSuchElementException.class,
            () -> menuService.deleteMeal(1L, 1L),
            "No error thrown when attempting to delete unexistent meal!"
        );
    }


    /* METHOD: hasMenusFrom */
    @Test
    public void whenHasMenusFrom_thenReturnTrue() {
        when(menuRepository.existsByRestaurant_idAndDateFrom(anyLong(), Mockito.any(LocalDate.class))).thenReturn(true);

        assertTrue(menuService.hasMenusFrom(1L, LocalDate.now()));
    }

    @Test
    public void whenHasMenusFrom_thenReturnFalse() {
        when(menuRepository.existsByRestaurant_idAndDateFrom(anyLong(), Mockito.any(LocalDate.class))).thenReturn(false);

        assertFalse(menuService.hasMenusFrom(1L, LocalDate.now()));
    }
}