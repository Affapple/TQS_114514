package tqs.homework.canteen.IntegrationTests;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import tqs.homework.canteen.TestcontainersConfiguration;
import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.DTOs.ReservationRequestDTO;
import tqs.homework.canteen.EnumTypes.MealType;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.EnumTypes.ReservationStatus;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.entities.Reservation;
import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.repositories.ReservationRepository;
import tqs.homework.canteen.services.MenuService;
import tqs.homework.canteen.services.ReservationService;
import tqs.homework.canteen.services.RestaurantService;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservationControllerTestsIT {
    @LocalServerPort
    int randomServerPort;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }
    
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private ReservationRepository reservationRepository;

    Restaurant castro, grelhados, santiago;
    Reservation validReservation;
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = randomServerPort;
        
        castro = restaurantService.saveNewRestaurant(new Restaurant("Castro", "Castro", 2));
        grelhados = restaurantService.saveNewRestaurant(new Restaurant("Grelhados", "Grelhados", 2));
        santiago = restaurantService.saveNewRestaurant(new Restaurant("Santiago", "Santiago", 2));

        /* CASTRO */
        menuService.createNewMenu(new MenuRequestDTO(
            castro.getId(), 
            List.of(
                new MealDTO(null, "Meal 1 Soup Lunch", MealType.SOUP),
                new MealDTO(null, "Meal 1 Meat Lunch", MealType.MEAT),
                new MealDTO(null, "Meal 1 Fish Lunch", MealType.FISH)
            ),
            LocalDate.now(),
            MenuTime.LUNCH
        ));
        menuService.createNewMenu(new MenuRequestDTO(
            castro.getId(), 
            List.of(
                new MealDTO(null, "Meal 2 Soup Dinner", MealType.SOUP),
                new MealDTO(null, "Meal 2 Meat Dinner", MealType.MEAT),
                new MealDTO(null, "Meal 2 Fish Dinner", MealType.FISH)
            ),
            LocalDate.now(),
            MenuTime.DINNER
        ));

        Menu updatedCastroMenu = menuService.createNewMenu(new MenuRequestDTO(
            castro.getId(), 
            List.of(
                new MealDTO(null, "Meal 3 Soup Lunch", MealType.SOUP),
                new MealDTO(null, "Meal 3 Meat Lunch", MealType.MEAT),
                new MealDTO(null, "Meal 3 Fish Lunch", MealType.FISH)
            ),
            LocalDate.now().plusDays(1),
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
            LocalDate.now(),
            MenuTime.LUNCH
        ));

        menuService.createNewMenu(new MenuRequestDTO(
            grelhados.getId(), 
            List.of(
                new MealDTO(null, "Meal 2 Soup Dinner", MealType.SOUP),
                new MealDTO(null, "Meal 2 Meat Dinner", MealType.MEAT),
                new MealDTO(null, "Meal 2 Fish Dinner", MealType.FISH)
            ),
            LocalDate.now(),
            MenuTime.DINNER
        ));
        menuService.createNewMenu(new MenuRequestDTO(
            grelhados.getId(), 
            List.of(
                new MealDTO(null, "Meal 3 Soup Dinner", MealType.SOUP),
                new MealDTO(null, "Meal 3 Meat Dinner", MealType.MEAT),
                new MealDTO(null, "Meal 3 Fish Dinner", MealType.FISH)
            ),
            LocalDate.now().plusDays(1),
            MenuTime.DINNER
        ));

        validReservation = reservationService.createReservation(
            new ReservationRequestDTO(updatedCastroMenu.getOptions().getFirst().getId())
        );
    }
    
    /* GET: /api/v1/reservations */
    /**
     * Given an invalid code
     * when get reservation by code
     * then an NoSuchElementException is thrown
     */
    @Test
    public void testGetReservationByCode_InvalidCode() throws Exception {
        when()
            .get("/api/v1/reservations/invalidCode")
        .then()
            .statusCode(404);
    }

    /**
     * Given a valid code
     * when get reservation by code
     * then a reservation is returned
     */
    @Test
    public void testGetReservationByCode_ValidCode() throws Exception {
        when()
            .get("/api/v1/reservations/" + validReservation.getCode())
        .then()
            .statusCode(200)
            .body("code", is(validReservation.getCode()));
    }

    /* POST: /api/v1/reservations */
    /**
     * Given an invalid mealId,
     * when the reservation is made
     * then an NoSuchElementException is thrown
     */
    @Test
    public void testCreateReservation_InvalidMealId() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .body("{\"mealId\": -1}")
        .when()
            .post("/api/v1/reservations")
        .then()
            .statusCode(404);
    }

    /**
     *  Given a valid mealId and free capacity,
     * when the meal is reserved
     * then a reservation is created and returned.
     */
    @Test
    public void testCreateReservation_ValidMealId() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .body("{\"mealId\": " + validReservation.getMeal().getId() + "}")
        .when()
            .post("/api/v1/reservations")
        .then()
            .statusCode(201)
            .body("code", notNullValue());
    }

    /**
     * Given a valid mealId and no free capacity,
     * when the meal is reserved
     * then an IllegalArgumentException is thrown.
     */
    @Test
    public void testCreateReservation_NoFreeCapacity() throws Exception {
        // Valid reservation but after this one, the capacity will be 0
        given()
            .contentType(ContentType.JSON)
            .body("{\"mealId\": " + validReservation.getMeal().getId() + "}")
        .when()
            .post("/api/v1/reservations")
        .then()
            .statusCode(201);

        given()
            .contentType(ContentType.JSON)
            .body("{\"mealId\": " + validReservation.getMeal().getId() + "}")
        .when()
            .post("/api/v1/reservations")
        .then()
            .statusCode(400);
    }

    /* DELETE: /api/v1/reservations/{reservation_id} */
    /**
     * Given an invalid code
     * when cancel reservation
     * then an NoSuchElementException is thrown
     */
    @Test
    public void testCancelReservation_InvalidCode() throws Exception {
        when()
            .delete("/api/v1/reservations/invalidCode")
        .then()
            .statusCode(404);
    }
    /**
     * Given a valid code and a reservation with status ACTIVE
     * when cancel reservation
     * then the reservation is cancelled and the status is set to CANCELLED
     */
    @Test
    public void testCancelReservation_ValidCode() throws Exception {
        when()
            .delete("/api/v1/reservations/" + validReservation.getCode())
        .then()
            .statusCode(200)
            .body("status", is("CANCELLED"));
    }

    /**
     * Given a valid code and a reservation with status USED
     * when cancel reservation
     * then an IllegalArgumentException is thrown
     */
    @Test
    public void testCancelReservation_UsedStatus() throws Exception {
        validReservation.setStatus(ReservationStatus.USED);
        reservationRepository.save(validReservation);

        when()
            .delete("/api/v1/reservations/" + validReservation.getCode())
        .then()
            .statusCode(400);
    }

    /**
     * Given a valid code and a reservation with status CANCELLED   
     * when cancel reservation
     * then an IllegalArgumentException is thrown
     */
    @Test
    public void testCancelReservation_CancelledStatus() throws Exception {
        validReservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(validReservation);

        when()
            .delete("/api/v1/reservations/" + validReservation.getCode())
        .then()
            .statusCode(400);
    }


     /* PUT: /api/v1/reservations/{reservation_id} */
    /**
     * Given an invalid code
     * when check in reservation
     * then an NoSuchElementException is thrown
     */
    @Test
    public void testCheckInReservation_InvalidCode() throws Exception {
        when()
            .put("/api/v1/reservations/invalidCode")
        .then()
            .statusCode(404);
    }

    /**
     * Given a valid code and a reservation with status ACTIVE
     * when check in reservation
     * then the reservation is checked in and the status is set to USED
     */
    @Test
    public void testCheckInReservation_ValidCode() throws Exception {
        when()
            .put("/api/v1/reservations/" + validReservation.getCode())
        .then()
            .statusCode(200)
            .body("status", is("USED"));
    }

    /**
     * Given a valid code and a reservation with status USED
     * when check in reservation
     * then an IllegalArgumentException is thrown
     */
    @Test
    public void testCheckInReservation_UsedStatus() throws Exception {
        validReservation.setStatus(ReservationStatus.USED);
        reservationRepository.save(validReservation);

        when()
            .put("/api/v1/reservations/" + validReservation.getCode())
        .then()
            .statusCode(400);
    }

    /**
     * Given a valid code and a reservation with status CANCELLED
     * when check in reservation
     * then an IllegalArgumentException is thrown
     */
    @Test
    public void testCheckInReservation_CancelledStatus() throws Exception {
        validReservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(validReservation);

        when()
            .put("/api/v1/reservations/" + validReservation.getCode())
        .then()
            .statusCode(400);
    }
}
