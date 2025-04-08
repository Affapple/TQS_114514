package tqs.homework.canteen.FrontendTests;

import tqs.homework.canteen.TestcontainersConfiguration;
import tqs.homework.canteen.FrontendTests.PageObjects.*;

import tqs.homework.canteen.services.*;
import tqs.homework.canteen.entities.*;
import tqs.homework.canteen.EnumTypes.*;
import tqs.homework.canteen.DTOs.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.isNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import io.github.bonigarcia.seljup.SeleniumJupiter;
    
@Import(TestcontainersConfiguration.class)
@ExtendWith(SeleniumJupiter.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
    properties = {
        "server.port=8080"
    }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FrontendInterfaceTests {
    private static final String FRONTEND_URL = "http://localhost:5173";
    
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("weatherapi.key", () -> "8a37c711ea644cafb77191906250604");
    }

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private MenuService menuService;

    @Autowired
    private ReservationService reservationService;

    RestaurantsPage homePage;
    
    @BeforeEach
    public void setupTests(FirefoxDriver driver) {
        restaurantService.saveNewRestaurant(new Restaurant("Castro", "Castro", 2));
        restaurantService.saveNewRestaurant(new Restaurant("Grelhados", "Grelhados", 2));
        restaurantService.saveNewRestaurant(new Restaurant("Santiago", "Santiago", 2));
        homePage = new RestaurantsPage(driver, false);
        homePage.setFrontendUrl(FRONTEND_URL);
        homePage.navigateToHomePage();
    }

    /**
     * Given 3 restaurants,
     * when the user goes to the home page,
     * then the user sees the 3 restaurants
     */
    @Test
    public void testRestaurantsAreShown() {
        homePage.waitForRestaurants();
        assertThat(homePage.getNumberOfRestaurants(), is(3));
    }

    /**
     * Given a restaurant with 4 meals,
     * When user goes to the meals page
     * Then user sees the 4 meals
     */
    @Test
    public void testMenusAreShown() {
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now(),
            MenuTime.LUNCH
        );
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now(),
            MenuTime.DINNER
        );
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now().plusDays(1),
            MenuTime.LUNCH
        );
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now().plusDays(1),
            MenuTime.DINNER
        );

        MenusPage menusPage = homePage.goToMenusOfRestaurant("Castro");
        assertThat(menusPage.getNumberOfMeals(), is(4));
    }

    /**
     * Given a restaurant with 3 meals,
     * When user Reserves a meal
     * Then a success message with the reservation details is shown
     */
    @Test
    public void testMakeReservationSuccess() {
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now(),
            MenuTime.LUNCH
        );
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now(),
            MenuTime.DINNER
        );
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now().plusDays(1),
            MenuTime.LUNCH
        );
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now().plusDays(1),
            MenuTime.DINNER
        );

        homePage.waitForRestaurants();
        MenusPage menusPage = homePage.goToMenusOfRestaurant("Castro");
        ReservationRequestPage reservationRequestPage = menusPage.reserveMeal(LocalDate.now().toString(), MenuTime.LUNCH);
        assertThat(reservationRequestPage.isSucessfulReservation(), is(true));
    }

    /**
     * When the user goes to the home page
     * Then the user sees weather information
     */
    @Test
    public void testWeatherIsShown() {
        homePage.waitForRestaurants();
        assertThat(homePage.isWeatherDisplayed(), is(true));
    }


    /**
     * Given a reservationn,
     * When the user searches for the reservation in the home page
     * Then the reservation details is shown
     */
    @Test
    public void testSearchReservation() {
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now(),
            MenuTime.LUNCH
        );

        Reservation reservation = reservationService.createReservation(new ReservationRequestDTO(
            restaurantService
                .getAllRestaurants().get(0) // Get first restaurant
                .getMenus().getFirst() // Get first menu
                .getOptions().getFirst().getId() // get first meal
        ));
        
        SearchReservationsPage searchReservationsPage = homePage.goToSearchReservation();
        ReservationDataPage reservationDataPage = searchReservationsPage.search(reservation.getCode());
        assertThat(reservationDataPage.getReservationCode(), is(reservation.getCode()));
        assertThat(reservationDataPage.getReservationStatus(), is(ReservationStatus.ACTIVE.name()));
    }

    /**
     * Given a reservationn,
     * When the user searches for the reservation in the home page
     *  and user cancels the reservation
     * Then the reservation is cancelled
     */
    @Test
    public void testCancelReservation() {
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now(),
            MenuTime.LUNCH
        );

        Reservation reservation = reservationService.createReservation(new ReservationRequestDTO(
            restaurantService
                .getAllRestaurants().get(0) // Get first restaurant
                .getMenus().getFirst() // Get first menu
                .getOptions().getFirst().getId() // get first meal
        ));
        
        SearchReservationsPage searchReservationsPage = homePage.goToSearchReservation();
        ReservationDataPage reservationDataPage = searchReservationsPage.search(reservation.getCode());
        assertThat(reservationDataPage.getReservationCode(), is(reservation.getCode()));
        assertThat(reservationDataPage.getReservationStatus(), is(ReservationStatus.ACTIVE.name()));
    }

    /**
     * Given a restaurant and user is a worker
     * When user goes to the meals page
     * Then have the option to add a new menu and to see all reservations
     * and When user goes to the meals page and add a menu
     * Then user can see the new menu after refreshing the page
     */
    @Test
    public void testAddMenu() {
        homePage.switchMode();

        MenusPage menusPage = homePage.goToMenusOfRestaurant("Castro");
        assertThat(menusPage.hasMenuAddOption(), is(true));

        WorkerAddMenuPage addMenuPage = menusPage.addMenu();

        menusPage = addMenuPage.completeMenu();
        menusPage.refreshPage();

        assertThat(menusPage.getNumberOfMeals(), is(1));
    }

    /**
     * Given a restaurant and user is a worker
     * When user goes to the meals page and reserve a menu
     * and user goes to the all reservations page
     * Then user can see their reservation in reservations list
     */
    @Test
    public void testReservationsAreShown() {
        homePage.switchMode();
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now(),
            MenuTime.LUNCH
        );

        MenusPage menusPage = homePage.goToMenusOfRestaurant("Castro");
        ReservationRequestPage reservationRequestPage = menusPage.reserveMeal(LocalDate.now().toString(), MenuTime.LUNCH);
        menusPage = reservationRequestPage.closeModal();

        WorkerAllReservationsPage searchReservationsPage = menusPage.viewReservations();
        assertThat(searchReservationsPage.getNumberOfReservations(), is(1));
    }

    /**
     * Given a restaurant and user is a worker
     * When user goes to the meals page and reserve a menu
     *  and user goes to the all reservations page
     *  and user checkin the reservation
     * then the status of the reservation is USED
     */
    @Test
    public void testWorkerCheckinReservation() {
        homePage.switchMode();
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now(),
            MenuTime.LUNCH
        );

        MenusPage menusPage = homePage.goToMenusOfRestaurant("Castro");
        ReservationRequestPage reservationRequestPage = menusPage.reserveMeal(LocalDate.now().toString(), MenuTime.LUNCH);
        String reservationCode = reservationRequestPage.getReservationCode();
        menusPage = reservationRequestPage.closeModal();

        WorkerAllReservationsPage allReservationsPage = menusPage.viewReservations();
        assertThat(allReservationsPage.getNumberOfReservations(), is(1));

        allReservationsPage.checkinReservation(reservationCode);

        allReservationsPage.refreshPage();

        assertThat(
            allReservationsPage.getStatusOfReservation(reservationCode),
            is(ReservationStatus.USED.name())
        );
    }

    /**
     * Given a restaurant and user is a worker
     * When user goes to the meals page and reserve a menu
     *  and user goes to the all reservations page
     *  and user cancels the reservation
     * then the status of the reservation is CANCELLED
     */
    @Test
    public void testWorkerCancelReservation() {
        homePage.switchMode();
        createMeals(
            restaurantService.getAllRestaurants().get(0).getId(),
            LocalDate.now(),
            MenuTime.LUNCH
        );

        MenusPage menusPage = homePage.goToMenusOfRestaurant("Castro");
        ReservationRequestPage reservationRequestPage = menusPage.reserveMeal(LocalDate.now().toString(), MenuTime.LUNCH);
        String reservationCode = reservationRequestPage.getReservationCode();
        menusPage = reservationRequestPage.closeModal();

        WorkerAllReservationsPage allReservationsPage = menusPage.viewReservations();
        assertThat(allReservationsPage.getNumberOfReservations(), is(1));

        allReservationsPage.cancelReservation(reservationCode);

        allReservationsPage.refreshPage();

        assertThat(
            allReservationsPage.getStatusOfReservation(reservationCode),
            is(ReservationStatus.CANCELLED.name())
        );
    }

     private void createMeals(Long restaurantId, LocalDate date, MenuTime time) {
        menuService.createNewMenu(new MenuRequestDTO(
            restaurantId,
            List.of(
                new MealDTO(null, "Meal 1", MealType.MEAT),
                new MealDTO(null, "Meal 2", MealType.FISH),
                new MealDTO(null, "Meal 3", MealType.SOUP)
            ),
            date,
            time
        ));
     }
}
