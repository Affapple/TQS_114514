package tqs.cars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import tqs.cars.entity.Car;
import tqs.cars.repository.CarRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase

// switch AutoConfigureTestDatabase with TestPropertySource to use a real database
// @TestPropertySource( locations = "application-integrationtest.properties")
class CarManagerTestIT {

    // will need to use the server port for the invocation url
    @LocalServerPort
    int randomServerPort;

    // a REST client that is test-friendly
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository repository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }


    @Test
    void whenPostCar_thenCreateCar() throws Exception {
        Car car = new Car("Mercedes", "Benz");

        var result = restTemplate.postForEntity(
            "/api/car", 
            car, 
            Car.class
        );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Car resultCar = result.getBody();
        assertThat(resultCar.getMaker()).isEqualTo(car.getMaker());
        assertThat(resultCar.getModel()).isEqualTo(car.getModel());
    }

   @Test
    void whenGetCar_thenRetrieveCar() {
        Car car = new Car("Mercedes", "Benz");
        var postResult = restTemplate.postForEntity(
            "/api/car", 
            car, 
            Car.class
        );

        Car createdCar = postResult.getBody();

        var getResult = restTemplate.getForEntity(
            "/api/car/" + createdCar.getCarId(),
            Car.class
        );

        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResult.getBody()).isNotNull();
        assertThat(getResult.getBody().getModel()).isEqualTo("Benz");
    }

    @Test
    void whenGetInvalidCar_thenGetNotFound() {
        var result = restTemplate.getForEntity("/api/car/10", Car.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void givenNoCar_whenGetCars_thenReturnEmptyList() {
        ResponseEntity<Car[]> result = restTemplate.getForEntity("/api/cars", Car[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().length).isEqualTo(0);
    }

    @Test
    void givenManyCars_whenGetCars_thenReturnList() {
        Car car1 = new Car("Mercedes", "Benz");
        Car car2 = new Car("Porsche", "Carrera");
        Car car3 = new Car("Dacia", "Logan");

        restTemplate.postForEntity("/api/car", car1, Car.class);
        restTemplate.postForEntity("/api/car", car2, Car.class);
        restTemplate.postForEntity("/api/car", car3, Car.class);

        ResponseEntity<Car[]> result = restTemplate.getForEntity("/api/cars", Car[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().length).isEqualTo(3);
    }

    @Test
    void whenGetValidReplacementCar_thenGetCar() {
        Car car = new Car("Mercedes", "Benz");
        restTemplate.postForEntity("/api/car", car, Car.class);

        ResponseEntity<Car> result = restTemplate.exchange(
            RequestEntity.post("/api/car/replacement")
                .contentType(MediaType.APPLICATION_JSON)
                .body(car), Car.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getModel()).isEqualTo(car.getModel());
        assertThat(result.getBody().getMaker()).isEqualTo(car.getMaker());
    }

    @Test
    void whenGetInvalidReplacementCar_thenGetNotFound() {
        Car car = new Car("Mercedes", "Benz");

        ResponseEntity<Car> result = restTemplate.exchange(
            RequestEntity.post("/api/car/replacement")
                .contentType(MediaType.APPLICATION_JSON)
                .body(car), Car.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
