package tqs.homework.canteen.Unit_ServiceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import tqs.homework.canteen.repositories.MealRepository;
import tqs.homework.canteen.services.MealService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MealServiceTests {
  
    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private MealService mealService;

    @BeforeEach
    public void setup() {
    }
}
