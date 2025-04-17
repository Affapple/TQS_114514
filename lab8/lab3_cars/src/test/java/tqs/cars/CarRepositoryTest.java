package tqs.cars;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;

import tqs.cars.entity.Car;
import tqs.cars.repository.*;

@DataJpaTest
class CarRepositoryTest {

    // get a test-friendly Entity Manager
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    void whenAddCar_thenCarExistsInDb() {
        // arrange a new employee and insert into db
        Car car = new Car("Mercedes", "Benz");
        entityManager.persistAndFlush(car); //ensure data is persisted at this point

        // test the query method
        List<Car> allCars = carRepository.findAll();
        assertThat(allCars).hasSize(1).extracting(Car::getModel).contains(car.getModel());
    }

    @Test
    void whenSearchUnexistentCar_thenReturnsNull() {
        assertThat(
            carRepository.findByCarId(-1l)
        ).isNotPresent();
    }

    @Test
    void whenNoCarAdded_thenListIsEmpty() {
        assertThat(
            carRepository.findAll()
        ).isEmpty();
    }
}
