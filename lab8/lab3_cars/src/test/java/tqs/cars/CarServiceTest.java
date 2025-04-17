package tqs.cars;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import tqs.cars.entity.Car;
import tqs.cars.repository.CarRepository;
import tqs.cars.services.CarManagerServiceImpl;


/**
 * Test scenario: verify the logic of the Service, mocking the response of the datasource
 * Results in standard unit test with mocks
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CarServiceTest {
  
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerServiceImpl carService;

    @BeforeEach
    void setUp() {
        Car car1 = new Car("Mercedes", "Benz");
        Car car2 = new Car("Porsche", "Carrera");
        Car car3 = new Car("Dacia", "Logan");
        car1.setCarId(10l);
        car2.setCarId(11l);
        car3.setCarId(12l);

        List<Car> allCars = Arrays.asList(car1, car2, car3);

        Mockito.when(carRepository.findByCarId(car1.getCarId())).thenReturn(Optional.of(car1));
        Mockito.when(carRepository.findByCarId(car2.getCarId())).thenReturn(Optional.of(car2));
        Mockito.when(carRepository.findByCarId(car3.getCarId())).thenReturn(Optional.of(car3));
        Mockito.when(carRepository.findByCarId(-1l)).thenReturn(Optional.ofNullable(null));
        
        Mockito
            .when(
                carRepository.findByMakerAndModel(
                    car1.getMaker(), car1.getModel()
                )
            )
            .thenReturn(Arrays.asList(car1));

        Mockito.when(carRepository.findAll()).thenReturn(allCars);
    }

    @Test
    void whenSaveCar_thenCarIsSaved() {
        Car car = new Car("Hyundai", "Civic");

        carService.save(car);
        Mockito.verify(carRepository, times(1)).save(car);
    }

    @Test
    void whenSearchExistingCar_thenFindCar() {
        assertThat(
            carService.getCarDetails(10l).get()
        ).extracting(Car::getModel).isEqualTo("Benz");
    }

    @Test
    void whenSearchNonExistingCar_thenReturnNull() {
        assertThat(
            carService.getCarDetails(-1l)
        ).isNotPresent();
    }

    @Test
    void whenGetAllCars_thenGetAllCars() {
        assertThat(
            carService.getAllCars()
        ).hasSize(3).extracting(Car::getCarId).contains(10l, 11l, 12l);
    }
    
    @Test
    void whenSearchExistingReplacement_thenGetReplacement() {
        Car car = new Car("Mercedes", "Benz");

        assertThat(
            carService.findReplacement(car).get()
        )
        .extracting(Car::getMaker, Car::getModel).contains(car.getMaker(), car.getModel());
    }


    @Test
    void whenSearchCarWithNoReplacement_thenReturnNull() {
        Car car = new Car("Porsche", "Carrera");

        assertThat(
            carService.findReplacement(car)
        ).isNotPresent();
    }
}
