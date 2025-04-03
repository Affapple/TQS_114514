package tqs.homework.canteen.serviceTests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.checkerframework.checker.units.qual.t;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @InjectMocks
    private MenuService menuService;

    private Restaurant testRestaurant;
    private Menu testMenu;
    private Meal testMeal;
    private MenuRequestDTO testMenuRequestDTO;
    private MealDTO testMealDTO;

    @BeforeEach
    public void setup() {
        testRestaurant = new Restaurant();
        testRestaurant.setId(1L);
        testRestaurant.setName("Test Restaurant");
        testRestaurant.setLocation("Test Location");
        testRestaurant.setCapacity(3);

        testMenu = new Menu();
        testMenu.setId(1L);
        testMenu.setDate(LocalDate.now());
        testMenu.setTime(MenuTime.LUNCH);
        testMenu.setRestaurant(testRestaurant);

        testMeal = new Meal();
        testMeal.setId(1L);
        testMeal.setDescription("Test Meal");
        testMeal.setType(MealType.MEAT);
        testMeal.setMenu(testMenu);

        testMealDTO = new MealDTO();
        testMealDTO.setId(1L);
        testMealDTO.setDescription("Test Meal");
        testMealDTO.setType(MealType.MEAT);

        testMenuRequestDTO = new MenuRequestDTO();
        testMenuRequestDTO.setRestaurantId(1L);
        testMenuRequestDTO.setDate(LocalDate.now());
        testMenuRequestDTO.setTime(MenuTime.LUNCH);
        testMenuRequestDTO.setOptions(Arrays.asList(testMealDTO));
    }

    @Test
    void whenCreateNewMenu_thenMenuShouldBeCreated() {
        when(restaurantRepository.findById(1L)).thenReturn(java.util.Optional.of(testRestaurant));
        when(menuRepository.save(any(Menu.class))).thenReturn(testMenu);

        Menu createdMenu = menuService.createNewMenu(testMenuRequestDTO);

        assertNotNull(createdMenu);
        assertEquals(testRestaurant, createdMenu.getRestaurant());
        assertEquals(testMenuRequestDTO.getDate(), createdMenu.getDate());
        assertEquals(testMenuRequestDTO.getTime(), createdMenu.getTime());
        verify(restaurantRepository).findById(1L);
        verify(menuRepository).save(any(Menu.class));
    }

    @Test
    void whenCreateNewMenuWithInvalidRestaurant_thenExceptionShouldBeThrown() {
        when(restaurantRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            menuService.createNewMenu(testMenuRequestDTO);
        });

        verify(restaurantRepository).findById(1L);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void whenAddMeal_thenMealShouldBeAdded() {
        when(menuRepository.findById(1L)).thenReturn(java.util.Optional.of(testMenu));
        when(menuRepository.save(any(Menu.class))).thenReturn(testMenu);

        Menu updatedMenu = menuService.addMeal(1, testMealDTO);

        assertNotNull(updatedMenu);
        assertThat(updatedMenu.getOptions(), hasItem(testMeal));
        verify(menuRepository).findById(1L);
        verify(menuRepository).save(any(Menu.class));
    }

    @Test
    void whenAddMealToInvalidMenu_thenExceptionShouldBeThrown() {
        when(menuRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            menuService.addMeal(1, testMealDTO);
        });
        verify(menuRepository).findById(1L);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void whenAddMeals_thenMealsShouldBeAdded() {
        List<Meal> newMeals = Arrays.asList(
            new Meal(null, "Soup", MealType.SOUP, null, null),
            new Meal(null, "Fish", MealType.FISH, null, null)
        );
        when(menuRepository.findById(1L)).thenReturn(java.util.Optional.of(testMenu));
        when(menuRepository.save(any(Menu.class))).thenReturn(testMenu);

        Menu updatedMenu = menuService.addMeals(1, newMeals);

        assertNotNull(updatedMenu);
        assertThat(updatedMenu.getOptions(), containsInAnyOrder(newMeals));
        verify(menuRepository).findById(1L);
        verify(menuRepository).save(any(Menu.class));
    }

    @Test
    void whenAddMealsToInvalidMenu_thenExceptionShouldBeThrown() {
        List<Meal> newMeals = Arrays.asList(
            new Meal(null, "Soup", MealType.SOUP, null, null),
            new Meal(null, "Fish", MealType.FISH, null, null)
        );
        when(menuRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            menuService.addMeals(1, newMeals);
        });
        verify(menuRepository).findById(1L);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void whenGetMeals_thenMealsShouldBeReturned() {
        when(menuRepository.findById(1L)).thenReturn(java.util.Optional.of(testMenu));

        List<Meal> foundMeals = menuService.getMeals(1);

        assertNotNull(foundMeals);
        assertEquals(testMenu.getOptions(), foundMeals);
        verify(menuRepository).findById(1L);
    }

    @Test
    void whenGetMealsFromInvalidMenu_thenExceptionShouldBeThrown() {
        when(menuRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            menuService.getMeals(1);
        });
        verify(menuRepository).findById(1L);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void whenAddMealOfRepeatedType_thenExceptionShouldBeThrown() {
        when(menuRepository.findById(1L)).thenReturn(java.util.Optional.of(testMenu));
        when(menuRepository.save(any(Menu.class))).thenReturn(testMenu);

        MealDTO repeatedMealDTO = new MealDTO();
        repeatedMealDTO.setId(2l);
        repeatedMealDTO.setDescription("Repeated Meat");
        repeatedMealDTO.setType(MealType.MEAT);

        assertThrows(IllegalStateException.class, () -> {
            menuService.addMeal(1, repeatedMealDTO);
        });
        verify(menuRepository).existsMealOfThisType(
            testRestaurant.getId(),
            testMenu.getDate(),
            testMenu.getTime(),
            repeatedMealDTO.getType()
        );
        verify(menuRepository, never()).save(any(Menu.class));
    }
}