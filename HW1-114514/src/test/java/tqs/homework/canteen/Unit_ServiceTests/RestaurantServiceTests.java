package tqs.homework.canteen.Unit_ServiceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    /*
     * Given a new restaurant
     * when save new restaurant
     * then the restaurant is saved
     */
    @Test
    public void whenSaveNewRestaurant_thenRestaurantIsSaved() {
        Restaurant newRestaurant = new Restaurant("Test Restaurant", "Test Location", 10);
        when(restaurantRepository.save(Mockito.any(Restaurant.class))).thenReturn(newRestaurant);

        Restaurant savedRestaurant = restaurantService.saveNewRestaurant(newRestaurant);
        assertEquals(newRestaurant.getName(), savedRestaurant.getName());
        assertEquals(newRestaurant.getLocation(), savedRestaurant.getLocation());
        assertEquals(newRestaurant.getCapacity(), savedRestaurant.getCapacity());
    }
    
    /**
     * Given all restaurants
     * when get all restaurants
     * then the list of restaurants is returned
     */
    @Test
    public void whenGetAllRestaurants_thenListOfRestaurantsIsReturned() {
        when(restaurantRepository.findAll())
        .thenReturn(
            Arrays.asList(
                new Restaurant("Test Restaurant", "Test Location", 10),
                new Restaurant("Another Test Restaurant", "Another Test Location", 20)
        ));

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        assertThat(restaurants, hasSize(2));
    }
    
    /**
     * Given a valid restaurant id
     * when get restaurant by id
     * then the restaurant is returned
     */
    @Test
    public void whenGetRestaurantById_thenRestaurantIsReturned() {
        Restaurant newRestaurant = new Restaurant("Test Restaurant", "Test Location", 10);
        when(restaurantRepository.findById(10L))
        .thenReturn(Optional.of(newRestaurant));

        Restaurant savedRestaurant = restaurantService.getRestaurantById(10L);
        assertEquals(newRestaurant.getName(), savedRestaurant.getName());
        assertEquals(newRestaurant.getLocation(), savedRestaurant.getLocation());
        assertEquals(newRestaurant.getCapacity(), savedRestaurant.getCapacity());
    }
    
    /**
     * Given an invalid restaurant id
     * when get restaurant by id
     * then an NoSuchElementException is thrown
     */
    @Test
    public void whenGetRestaurantById_thenNoSuchElementExceptionIsThrown() {
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            restaurantService.getRestaurantById(10L);
        });
    }
}
