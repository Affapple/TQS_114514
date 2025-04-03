package tqs.homework.canteen.serviceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.repositories.RestaurantRepository;
import tqs.homework.canteen.services.RestaurantService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RestaurantServiceTests {
  
    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant testRestaurant;
    private Restaurant anotherRestaurant;

    @BeforeEach
    public void setup() {
        testRestaurant = new Restaurant();
        testRestaurant.setName("Test Restaurant");
        testRestaurant.setLocation("Test Location");
        testRestaurant.setCapacity(100);

        anotherRestaurant = new Restaurant();
        anotherRestaurant.setName("Another Restaurant");
        anotherRestaurant.setLocation("Another Location");
        anotherRestaurant.setCapacity(200);
    }

    @Test
    void whenSaveNewRestaurant_thenRestaurantShouldBeSaved() {
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(testRestaurant);

        Restaurant savedRestaurant = restaurantService.saveNewRestaurant(testRestaurant);

        assertNotNull(savedRestaurant);
        assertEquals(testRestaurant.getName(), savedRestaurant.getName());
        assertEquals(testRestaurant.getLocation(), savedRestaurant.getLocation());
        assertEquals(testRestaurant.getCapacity(), savedRestaurant.getCapacity());
        verify(restaurantRepository, times(1)).save(testRestaurant);
    }

    @Test
    void whenGetAllRestaurants_thenListOfRestaurantsShouldBeReturned() {
        List<Restaurant> expectedRestaurants = Arrays.asList(testRestaurant, anotherRestaurant);
        when(restaurantRepository.findAll()).thenReturn(expectedRestaurants);

        List<Restaurant> actualRestaurants = restaurantService.getAllRestaurants();

        assertNotNull(actualRestaurants);
        assertEquals(expectedRestaurants.size(), actualRestaurants.size());
        assertEquals(expectedRestaurants, actualRestaurants);
        verify(restaurantRepository, times(1)).findAll();
    }
}
