package tqs.homework.canteen.IntegrationTests;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.flywaydb.core.internal.util.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;

import tqs.homework.canteen.TestcontainersConfiguration;
import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.EnumTypes.MealType;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.services.MenuService;
import tqs.homework.canteen.services.RestaurantService;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MenuControllerTestsIT {
    @LocalServerPort
    int randomServerPort;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }
    
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private MenuService menuService;

    Restaurant castro, grelhados, santiago;
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = randomServerPort;
        
        castro = restaurantService.saveNewRestaurant(new Restaurant("Castro", "Castro", 2));
        grelhados = restaurantService.saveNewRestaurant(new Restaurant("Grelhados", "Grelhados", 2));
        santiago = restaurantService.saveNewRestaurant(new Restaurant("Santiago", "Santiago", 2));

        menuService.createNewMenu(new MenuRequestDTO(
            castro.getId(), 
            List.of(
                new MealDTO(null, "Meal 1 Soup Lunch", MealType.SOUP),
                new MealDTO(null, "Meal 1 Meat Lunch", MealType.MEAT)
            ),
            LocalDate.now(),
            MenuTime.LUNCH
        ));
    }


    /* POST: /api/v1/menu */
    /**
     * Given restaurant doesnt exists
     * when createNewMenu to restaurant
     * then return 404
     */

    @Test
    public void testCreateMenu_RestaurantDoesntExist() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .body(
                JsonUtils.toJson(
                    new MenuRequestDTO(-1L, new ArrayList<>(), LocalDate.now(), MenuTime.LUNCH)
                )
            )
        .when()
            .post("/api/v1/menu")
        .then()
            .statusCode(404);
    }

    /**
     * Given resttaurant exists
     * when menu for given date and time table dont exists
     * then create new menu and return 201
     */
    @Test
    public void testCreateMenu_RestaurantExists() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(new MenuRequestDTO(
                castro.getId(),
                List.of(
                    new MealDTO(null, "Meal 3 Soup Dinner", MealType.SOUP),
                    new MealDTO(null, "Meal 3 Meat Dinner", MealType.MEAT),
                    new MealDTO(null, "Meal 3 Fish Dinner", MealType.FISH)
                ), 
                LocalDate.now().plusDays(5),
                MenuTime.LUNCH
            )))
        .when()
            .post("/api/v1/menu")
        .then()
            .log().all()
            .statusCode(201);
    }

    /**
     * Given resttaurant exists
     * when menu for given date and time table already exists
     * then return 400
     */
    @Test
    public void testCreateMenu_MenuAlreadyExists() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(new MenuRequestDTO(
                castro.getId(),
                new ArrayList<>(),
                LocalDate.now(), 
                MenuTime.LUNCH
            )))
        .when()
            .post("/api/v1/menu")
        .then()
            .log().all()
            .statusCode(400);
    }

    /* PUT: /api/v1/menu/{menu_id} */
    /**
     * Given menu doesnt exits
     * when add meal
     * then return 404
     */
    @Test
    public void testAddMealToMenu_MenuDoesntExist() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(new MealDTO(-1L, "Meal", MealType.MEAT)))
        .when()
            .put("/api/v1/menu")
        .then()
            .statusCode(404);
    }

    /**
     * Given menu exist 
     *    and meal of type already exists
     * when add meal
     * then return 400
     */
    @Test
    public void testAddMealToMenu_MealAlreadyExists() throws Exception {
        Menu existingMenu = menuService.getMenusByRestaurantId(castro.getId()).getFirst();
        given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(new MealDTO(existingMenu.getId(), "Meal", MealType.MEAT)))
        .when()
            .put("/api/v1/menu")
        .then()
            .statusCode(400);
    }

    /**
     * Given menu exists
     *    and when meal of type doesnt exists
     * when add meal
     * then add meal and return 201
     */
    @Test
    public void testAddMealToMenu_MealDoesntExist() throws Exception {
        Menu existingMenu = menuService.getMenusByRestaurantId(castro.getId()).getFirst();

        given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(new MealDTO(existingMenu.getId(), "Meal", MealType.FISH)))
        .when()
            .put("/api/v1/menu")
        .then()
            .statusCode(201);
    }

    /* DELETE: /api/v1/menu/{menu_id} */
    /**
     * Given menu doesnt exists
     * when delete menu
     * then return 404
     */
    @Test
    public void testDeleteMenu_MenuDoesntExist() throws Exception {
        when()
            .delete("/api/v1/menu/-1")
        .then()
            .statusCode(404);
    }

     /* Given menu exists
     * when delete menu
     * then delete menu and return 200
     */
    @Test
    public void testDeleteMenu_MenuExists() throws Exception {
        Menu existingMenu = menuService.getMenusByRestaurantId(castro.getId()).getFirst();
        when()
            .delete("/api/v1/menu/" + existingMenu.getId())
        .then()
            .statusCode(200);
    }

    /* DELETE: /api/v1/menu/{menu_id}/{meal_id} */
    /**
     * Given menu exists and doesnt contain meal with given id
     * when delete meal from menu
     * then return 404
     */
    @Test
    public void testDeleteMealFromMenu_MealDoesntExist() throws Exception {
        Menu existingMenu = menuService.getMenusByRestaurantId(castro.getId()).getFirst();

        when()
            .delete("/api/v1/menu/" + existingMenu.getId() + "/-1")
        .then()
            .statusCode(404);
    }
    /**
     * Given menu exists and contains meal with given id
     * when delete meal from menu
     * then delete meal from menu and return 200
     */
    @Test
    public void testDeleteMealFromMenu_MealExists() throws Exception {
        Menu existingMenu = menuService.getMenusByRestaurantId(castro.getId()).getFirst();
        Meal existingMeal = menuService.getMeals(existingMenu.getId()).getFirst();

        when()
            .delete("/api/v1/menu/" + existingMenu.getId() + "/" + existingMeal.getId())
        .then()
            .statusCode(200);
    }
     /**
      * Given menu doesn't exists
      * when delete meal from menu
      * then return 404
      */
     @Test
     public void testDeleteMealFromMenu_MenuDoesntExist() throws Exception {
        when()
            .delete("/api/v1/menu/-1/-1")
        .then()
            .statusCode(404);
    }
}
