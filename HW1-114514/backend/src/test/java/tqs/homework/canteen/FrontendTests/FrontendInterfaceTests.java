package tqs.homework.canteen.FrontendTests;

import tqs.homework.canteen.TestcontainersConfiguration;
import tqs.homework.canteen.FrontendTests.PageObjects.*;
import tqs.homework.canteen.services.RestaurantService;
import tqs.homework.canteen.entities.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
@DirtiesContext
public class FrontendInterfaceTests {
    private static final String FRONTEND_URL = "http://localhost:5173";
    
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("weatherapi.key", () -> "8a37c711ea644cafb77191906250604");
    }

    @Autowired
    private RestaurantService restaurantService;


    RestaurantsPage homePage;
    @BeforeEach
    public void setUp(FirefoxDriver driver) {
        RestaurantsPage homePage = new RestaurantsPage(driver, false);
        homePage.setFrontendUrl(FRONTEND_URL);
        homePage.navigateToHomePage();
        homePage.waitForRestaurants();

        restaurantService.saveNewRestaurant(new Restaurant("Castro", "Castro", 2));
        restaurantService.saveNewRestaurant(new Restaurant("Grelhados", "Grelhados", 2));
        restaurantService.saveNewRestaurant(new Restaurant("Santiago", "Santiago", 2));
    }

    /**
     * Given 3 restaurants,
     * when the user goes to the home page,
     * then the user sees the 3 restaurants
     */
    @Test
    public void testRestaurantsAreShown(FirefoxDriver driver) {
        assertThat(homePage.getNumberOfRestaurants(), is(3));
    }

    /**
     * Given a restaurant with 3 meals,
     * When user goes to the meals page
     * Then user sees the 3 meals
     */
    @Test
    public void test(FirefoxDriver driver) {
        homePage.goToMenusOfRestaurant("Castro");

    }

    /**
     * Given a restaurant with 3 meals,
     * When user Reserves a meal
     * Then a success message with the reservation details is shown
     */
    // @Test
    // public void test(@DockerBrowser(type = CHROME) WebDriver driver) {
    // }

    /**
     * When the user goes to the home page
     * Then the user sees weather information
     */
    // @Test
    // public void test(@DockerBrowser(type = CHROME) WebDriver driver) {
    // }

    /**
     * Given a reservationn,
     * When the user searches for the reservation in the home page
     * Then the reservation details is shown
     */

    /**
     * Given a restaurant and user is a worker
     * When user goes to the meals page
     * Then have the option to add a new menu and to see all reservations
     */

    /**
     * Given a restaurant and user is a worker
     * When user goes to the meals paage and add a menu
     * Then user can see the new menu
     */

    /**
     * Given a restaurant and user is a worker
     * When user goes to the meals page and reserve a menu
     * and user goes to the all reservations page
     * Then user can see their reservation in reservations list
     */

}
