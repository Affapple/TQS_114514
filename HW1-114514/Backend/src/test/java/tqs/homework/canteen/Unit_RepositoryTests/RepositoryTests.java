package tqs.homework.canteen.Unit_RepositoryTests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import tqs.homework.canteen.TestcontainersConfiguration;
import tqs.homework.canteen.EnumTypes.MealType;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.repositories.MealRepository;
import tqs.homework.canteen.repositories.MenuRepository;
import tqs.homework.canteen.repositories.ReservationRepository;
import tqs.homework.canteen.repositories.RestaurantRepository;

@DataJpaTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RepositoryTests {
    @Autowired RestaurantRepository restaurantRepository;
    @Autowired MenuRepository menuRepository;
    @Autowired MealRepository mealRepository;
    @Autowired ReservationRepository reservationRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void setup() {
        // Add exampels of restaurants, menus and meals
        Restaurant res = new Restaurant();
        res.setLocation("Aveiro");
        res.setCapacity(1000);
        res.setName("Castro");
        restaurantRepository.save(res);

        // Create a menu for today
        createNewMenu(res, LocalDate.now());
    }

	@Test
	void whenCheckIfMealExistsForToday_thenReturnTrue() {
        Restaurant res = restaurantRepository.findAll().getFirst();
        LocalDate date = LocalDate.now();

        assertTrue(
            menuRepository.existsMealOfThisType(
                res.getId(),
                date,
                MenuTime.LUNCH
            ), 
            "No meal for today's date was found although it was initialized!"
        );

        assertTrue(
            menuRepository.existsMealOfThisType(
                res.getId(),
                date,
                MenuTime.LUNCH,
                MealType.MEAT
            ), 
            "No meat meal for today's date was found although it was initialized!"
        );
    }

    @Test
    public void whenGetMenuInRange_thenCorrectResult() {
        // Used to test if ranges are working properly
        Restaurant res = restaurantRepository.findAll().getFirst();
        LocalDate today = LocalDate.now(),
                tomorrow = today.plusDays(1l),
                yesterday = today.minusDays(1);
        createNewMenu(res, tomorrow);

        assertThat(
            menuRepository.findByRestaurant_idAndDateBetween(res.getId(), yesterday.minusDays(1), yesterday),
            iterableWithSize(0)
        );

        assertThat(
            menuRepository.findByRestaurant_idAndDateBetween(res.getId(), yesterday, today),
            iterableWithSize(1)
        );

        assertThat(
            menuRepository.findByRestaurant_idAndDateBetween(res.getId(), today, tomorrow),
            iterableWithSize(2)
        );
    }


    Menu createNewMenu(Restaurant res, LocalDate date) {
        Menu menu = new Menu();
        menu.setTime(MenuTime.LUNCH);
        menu.setRestaurant(res);
        menu.setDate(date);

        Meal soup = new Meal();
        soup.setDescription("soup meal");
        soup.setType(MealType.SOUP);
        soup.setMenu(menu);

        Meal fish = new Meal();
        fish.setDescription("Fish meal");
        fish.setType(MealType.FISH);
        fish.setMenu(menu);

        Meal meat = new Meal();
        meat.setDescription("Meat meal");
        meat.setType(MealType.MEAT);
        meat.setMenu(menu);
            
        menuRepository.save(menu);
        mealRepository.saveAll(List.of(soup, fish, meat));

        return menu;
    }
}

