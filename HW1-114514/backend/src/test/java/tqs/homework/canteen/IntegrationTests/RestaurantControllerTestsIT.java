package tqs.homework.canteen.IntegrationTests;

import java.time.LocalDate;
import java.util.List;

import org.flywaydb.core.internal.util.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import tqs.homework.canteen.TestcontainersConfiguration;
import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.EnumTypes.MealType;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.services.MenuService;
import tqs.homework.canteen.services.RestaurantService;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
public class RestaurantControllerTestsIT {
    @LocalServerPort
    int randomServerPort;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantService restaurantService;


    Restaurant castro, grelhados, santiago;
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = randomServerPort;
        
        castro = restaurantService.saveNewRestaurant(new Restaurant("Castro", "Castro", 10));
        grelhados = restaurantService.saveNewRestaurant(new Restaurant("Grelhados", "Grelhados", 10));
        santiago = restaurantService.saveNewRestaurant(new Restaurant("Santiago", "Santiago", 10));

        /* CASTRO */
        menuService.createNewMenu(new MenuRequestDTO(
            castro.getId(), 
            List.of(
                new MealDTO(null, "Meal 1 Soup Lunch", MealType.SOUP),
                new MealDTO(null, "Meal 1 Meat Lunch", MealType.MEAT),
                new MealDTO(null, "Meal 1 Fish Lunch", MealType.FISH)
            ),
            LocalDate.of(2025, 4, 1),
            MenuTime.LUNCH
        ));
        menuService.createNewMenu(new MenuRequestDTO(
            castro.getId(), 
            List.of(
                new MealDTO(null, "Meal 2 Soup Dinner", MealType.SOUP),
                new MealDTO(null, "Meal 2 Meat Dinner", MealType.MEAT),
                new MealDTO(null, "Meal 2 Fish Dinner", MealType.FISH)
            ),
            LocalDate.of(2025, 4, 1),
            MenuTime.DINNER
        ));

        menuService.createNewMenu(new MenuRequestDTO(
            castro.getId(), 
            List.of(
                new MealDTO(null, "Meal 3 Soup Lunch", MealType.SOUP),
                new MealDTO(null, "Meal 3 Meat Lunch", MealType.MEAT),
                new MealDTO(null, "Meal 3 Fish Lunch", MealType.FISH)
            ),
            LocalDate.of(2025, 4, 2),
            MenuTime.LUNCH
        ));

        /* GRELHADOS */
        menuService.createNewMenu(new MenuRequestDTO(
            grelhados.getId(), 
            List.of(
                new MealDTO(null, "Meal 1 Soup Lunch", MealType.SOUP),
                new MealDTO(null, "Meal 1 Meat Lunch", MealType.MEAT),
                new MealDTO(null, "Meal 1 Fish Lunch", MealType.FISH)
            ),
            LocalDate.of(2025, 4, 1),
            MenuTime.LUNCH
        ));

        menuService.createNewMenu(new MenuRequestDTO(
            grelhados.getId(), 
            List.of(
                new MealDTO(null, "Meal 2 Soup Dinner", MealType.SOUP),
                new MealDTO(null, "Meal 2 Meat Dinner", MealType.MEAT),
                new MealDTO(null, "Meal 2 Fish Dinner", MealType.FISH)
            ),
            LocalDate.of(2025, 4, 1),
            MenuTime.DINNER
        ));
        menuService.createNewMenu(new MenuRequestDTO(
            grelhados.getId(), 
            List.of(
                new MealDTO(null, "Meal 3 Soup Dinner", MealType.SOUP),
                new MealDTO(null, "Meal 3 Meat Dinner", MealType.MEAT),
                new MealDTO(null, "Meal 3 Fish Dinner", MealType.FISH)
            ),
            LocalDate.of(2025, 4, 1),
            MenuTime.DINNER
        ));
    }

    /* GET: /api/v1/restaurant */
    /**
     * Given restaurant "Castro", "Grelhados", "Santiago" exist
     * when getAllRestaurants is called
     * then a list of <Castro, Grelhados, Santiago> is returned
     */
    @Test
    public void whenGetAllRestaurants_thenListOfRestaurantsIsReturned() throws Exception {
        when()
            .get("/api/v1/restaurant")
        .then()
            .statusCode(200)
            .body("$", is(arrayContainingInAnyOrder(
                hasProperty("id", is(1L)),
                hasProperty("id", is(2L)),
                hasProperty("id", is(3L))
            )));
    }

    /* POST: /api/v1/restaurant */
    /** 
     * Given a new restaurant
     * when save new restaurant
     * then the restaurant is saved and return 201
     */
    @Test
    public void whenCreateRestaurant_thenRestaurantIsSavedAndReturn201() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(new Restaurant("Porto", "Porto", 10)))
        .when()
            .post("/api/v1/restaurant")
        .then()
            .statusCode(201);
    }

    /* GET: /api/v1/restaurant/{restaurant_id} */
    /**
     * Given restaurant "Castro" exists
     * when getRestaurant is called with id 1
     * then the restaurant "Castro" is returned and return 200
     * 
     * Given restaurants exists
     * when getRestaurant is called with an invalid id
     * then a body with status 404 is returned
     */
    @Test
    public void whenGetRestaurant_thenRestaurantIsReturnedAndReturn200() throws Exception {
        when()
            .get("/api/v1/restaurant/" + castro.getId())
        .then()
            .statusCode(200)
            .body("id", is(castro.getId()))
            .body("name", is(castro.getName()))
            .body("location", is(castro.getLocation()))
            .body("capacity", is(castro.getCapacity()));
    }

    /* GET: /api/v1/restaurant/{restaurant_id}/menus */
    /**
     * Given restaurant "Castro" exists 
     *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
     * when getMenusOfRestaurant with from set as 2025-04-01 and to set as 2025-04-02
     * then a list of <Menu1, Menu2> is returned
     */
    @Test
    public void whenGetMenusOfRestaurant_thenListOfMenusIsReturned() throws Exception {
        when()
            .get("/api/v1/restaurant/" + castro.getId() + "/menus?from=2025-04-01&to=2025-04-02")
        .then()
            .statusCode(200)
            .body("$.size()", is(2))
            .body("$", is(arrayContainingInAnyOrder(
                hasProperty("id", is(castro.getMenus().get(0).getId())),
                hasProperty("id", is(castro.getMenus().get(1).getId()))
            )));
    }

    /**
     * Given restaurant "Castro" exists 
     *  and has no menus for the dates 2025-04-01 and 2025-04-02
     * when getMenusOfRestaurant with from set as 2025-03-30 and to set as 2025-04-01
     * then an empty list is returned
     */
    @Test
    public void whenGetMenusOfRestaurant_thenEmptyListIsReturned() throws Exception {
        when()
            .get("/api/v1/restaurant/" + castro.getId() + "/menus?from=2025-03-30&to=2025-04-01")
        .then()
            .statusCode(200)
            .body("$.size()", is(0));
    }

    /**
     * Given restaurant "Castro" exists 
     *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
     * when getMenusOfRestaurant with from set as 2025-04-03 and to set as 2025-04-04
     * then a list of <Menu2> is returned
     */
    @Test
    public void whenGetMenusOfRestaurantBetweenDates_thenListOfMenusIsReturned() throws Exception {
        when()
            .get("/api/v1/restaurant/" + castro.getId() + "/menus?from=2025-04-03&to=2025-04-04")
        .then()
            .statusCode(200)
            .body("$.size()", is(1))
            .body("$", is(arrayContainingInAnyOrder(
                hasProperty("id", is(castro.getMenus().get(1).getId()))
            )));
    }

    /**
     * Given restaurant "Castro" exists  
     *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
     * when getMenusOfRestaurant with to set as 2025-04-01
     * then a list of <Menu1> is returned
     *
     */
    @Test
    public void whenGetMenusOfRestaurantBetweenDates2_thenListOfMenusIsReturned() throws Exception {
        when()
            .get("/api/v1/restaurant/" + castro.getId() + "/menus?to=2025-04-01")
        .then()
            .statusCode(200)
            .body("$.size()", is(1))
            .body("$", is(arrayContainingInAnyOrder(
                hasProperty("id", is(castro.getMenus().get(0).getId()))
            )));
    }

     /**
      * Given restaurant "Castro" exists 
      *  and has 2 menus for the dates 2025-04-01 and 2025-04-02
      * when getMenusOfRestaurant with from and to set as null
      * then a list of <Menu1, Menu2> is returned
      */
    @Test
    public void whenGetMenusOfRestaurantBetweenDates3_thenListOfMenusIsReturned() throws Exception {
        when()
            .get("/api/v1/restaurant/" + castro.getId() + "/menus")
        .then()
            .statusCode(200)
            .body("$.size()", is(2))
            .body("$", is(arrayContainingInAnyOrder(
                hasProperty("id", is(castro.getMenus().get(0).getId())),
                hasProperty("id", is(castro.getMenus().get(1).getId()))
            )));
    }
}