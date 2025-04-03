package tqs.homework.canteen.serviceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.EnumTypes.MealType;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.repositories.MealRepository;
import tqs.homework.canteen.services.MealService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MealServiceTests {
  
    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private MealService mealService;

    private Meal testMeal;
    private MealDTO testMealDTO;

    @BeforeEach
    public void setup() {
        testMeal = new Meal();
        testMeal.setId(1L);
        testMeal.setDescription("Test Meal");
        testMeal.setType(MealType.MEAT);

        testMealDTO = new MealDTO();
        testMealDTO.setId(1L);
        testMealDTO.setDescription("Test Meal");
        testMealDTO.setType(MealType.MEAT);
    }

    @Test
    void whenCreateNewMeal_thenMealShouldBeCreated() {
        when(mealRepository.save(any(Meal.class))).thenReturn(testMeal);

        Meal createdMeal = mealService.createNewMeal(testMealDTO);

        assertNotNull(createdMeal);
        assertEquals(testMealDTO.getDescription(), createdMeal.getDescription());
        assertEquals(testMealDTO.getType(), createdMeal.getType());
        verify(mealRepository).save(any(Meal.class));
    }
}
