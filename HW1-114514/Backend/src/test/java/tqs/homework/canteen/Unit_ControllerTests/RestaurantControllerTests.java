package tqs.homework.canteen.Unit_ControllerTests;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.flywaydb.core.internal.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tqs.homework.canteen.controller.RestaurantController;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.services.MenuService;
import tqs.homework.canteen.services.RestaurantService;

@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MenuService menuService;

    @MockitoBean
    private RestaurantService restaurantService;

    /* GET: /api/v1/restaurant */
    /**
     * Given restaurant "Castro", "Grelhados", "Santiago" exist
     * when getAllRestaurants is called
     * then a list of <Castro, Grelhados, Santiago> is returned
     */
    @Test
    public void whenGetAllRestaurants_thenListOfRestaurantsIsReturned() throws Exception {
        when(restaurantService.getAllRestaurants()).thenReturn(
            List.of(
                new Restaurant("Castro", "Castro", 10),
                new Restaurant("Grelhados", "Grelhados", 10),
                new Restaurant("Santiago", "Santiago", 10)
            )
        );

        mvc.perform(get("/api/v1/restaurant"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(3)));
    }


    /**
     * Given no restaurants exist
     * when getAllReservations is called
     * then an empty list is returned
     */
    @Test
    public void whenGetAllRestaurants_thenEmptyListIsReturned() throws Exception {
        when(restaurantService.getAllRestaurants()).thenReturn(List.of());

        mvc.perform(get("/api/v1/restaurant"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    /* POST: /api/v1/restaurant */
    /** 
     * Given a new restaurant
     * when save new restaurant
     * then the restaurant is saved and return 201
     */
    @Test
    public void whenCreateRestaurant_thenRestaurantIsSavedAndReturn201() throws Exception {
        when(restaurantService.saveNewRestaurant(Mockito.any(Restaurant.class)))
            .thenReturn(new Restaurant());

        mvc.perform(post("/api/v1/restaurant")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(new Restaurant("Castro", "Castro", 10)))
        )
        .andExpect(status().isCreated());
    }

    /* GET: /api/v1/restaurant/{restaurant_id} */
    /**
     * Given restaurant "Castro" with id 1 exists
     * when getRestaurant is called with id 1
     * then the restaurant "Castro" is returned and return 200
     * 
     * Given restaurants exists
     * when getRestaurant is called with an invalid id
     * then a body with status 404 is returned
     */
    @Test
    public void whenGetRestaurant_thenRestaurantIsReturnedAndReturn200() throws Exception {
        Restaurant restaurant = new Restaurant("Castro", "Castro", 10);
        restaurant.setId(1L);
        when(restaurantService.getRestaurantById(1L)).thenReturn(restaurant);

        mvc.perform(
            get("/api/v1/restaurant/1")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Castro"))
        .andExpect(jsonPath("$.location").value("Castro"))
        .andExpect(jsonPath("$.capacity").value(10));
    }

    /* GET: /api/v1/restaurant/{restaurant_id}/menus */
    /**
     * Given restaurant "Castro" with id 1 exists 
     *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
     * when getMenusOfRestaurant is called with id 1, 2025-04-01 and 2025-04-02
     * then a list of <Menu1, Menu2> is returned
     */
    @Test
    public void whenGetMenusOfRestaurant_thenListOfMenusIsReturned() throws Exception {
        when(
            menuService.getMenusOfRestaurantBetweenDates(
                1L, 
                LocalDate.of(2025, 4, 1), 
                LocalDate.of(2025, 4, 2))
        ).thenReturn(List.of(new Menu(), new Menu()));

        mvc.perform(
            get("/api/v1/restaurant/1/menus?from=2025-04-01&to=2025-04-02")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)));
    }

    /**
     * Given restaurant "Castro" with id 1 exists 
     *  and has no menus for the dates 2025-04-01 and 2025-04-02
     * when getMenusOfRestaurant is called with id 1, 2025-04-01 and 2025-04-02
     * then an empty list is returned
     */
    @Test
    public void whenGetMenusOfRestaurant_thenEmptyListIsReturned() throws Exception {
        when(
            menuService.getMenusOfRestaurantBetweenDates(
                1L, 
                LocalDate.of(2025, 3, 30), 
                LocalDate.of(2025, 4, 1))
        ).thenReturn(List.of());

        mvc.perform(
            get("/api/v1/restaurant/1/menus?from=2025-03-30&to=2025-04-01")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * Given restaurant "Castro" with id 1 exists 
     *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
     * when getMenusOfRestaurant is called with id 1, 2025-04-03 and 2025-04-04
     * then a list of <Menu2> is returned
     */
    @Test
    public void whenGetMenusOfRestaurantBetweenDates_thenListOfMenusIsReturned() throws Exception {
        when(
            menuService.getMenusOfRestaurantBetweenDates(
                1L, 
                LocalDate.of(2025, 4, 3), 
                LocalDate.of(2025, 4, 4))
        ).thenReturn(List.of(new Menu()));

        mvc.perform(
            get("/api/v1/restaurant/1/menus?from=2025-04-03&to=2025-04-04")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(1)));
    }

    /**
     * Given restaurant "Castro" with id 1 exists 
     *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
     * when getMenusOfRestaurant is called with id 1, null and 2025-04-01
     * then a list of <Menu1> is returned
     *
     */
    @Test
    public void whenGetMenusOfRestaurantBetweenDates2_thenListOfMenusIsReturned() throws Exception {
        when(
            menuService.getMenusOfRestaurantBetweenDates(
                1L, 
                null, 
                LocalDate.of(2025, 4, 1))
        ).thenReturn(List.of(new Menu()));

        mvc.perform(
            get("/api/v1/restaurant/1/menus?to=2025-04-01")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(1)));
    }

     /**
      * Given restaurant "Castro" with id 1 exists 
      *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
      * when getMenusOfRestaurant is called with id 1, null and null
      * then a list of <Menu1, Menu2> is returned
      */
    @Test
    public void whenGetMenusOfRestaurantBetweenDates3_thenListOfMenusIsReturned() throws Exception {
        when(
            menuService.getMenusOfRestaurantBetweenDates(1L, null, null)
        ).thenReturn(List.of(new Menu(), new Menu()));

        mvc.perform(
            get("/api/v1/restaurant/1/menus")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)));
    }
}