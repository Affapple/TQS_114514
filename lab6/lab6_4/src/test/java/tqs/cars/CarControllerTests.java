package tqs.cars;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import tqs.cars.controller.CarController;
import tqs.cars.entity.Car;
import tqs.cars.services.CarManagerService;

@Import(TestcontainersConfiguration.class)
@WebMvcTest(CarController.class)
class CarControllerTests  {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarController controller;

    @MockitoBean
    CarManagerService service;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        //when(service.getCarDetails(anyLong())).thenReturn(Optional.of(car));
        Mockito.when(service.getAllCars())
            .thenReturn(new ArrayList<Car>());

        Mockito.when(service.save(Mockito.any(Car.class)))
            .thenAnswer(i -> {
                Car car = (Car) i.getArguments()[0];
                Long id = (long) car.hashCode();
                car.setCarId(id);

                Mockito.when(service.getCarDetails(Mockito.anyLong()))
                    .thenAnswer(invocation -> {
                        Long carId = invocation.getArgument(0);
                        return Optional.of(car);
                    });
                return car;
            });

    }

    @Test
    void whenPostCar_thenCreateCar() throws Exception {
        Car car = new Car("Mercedes", "Benz");

        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(car))
        .when()
            .post("/api/car")
        .then()
            .statusCode(201)
            .body("maker", is("Mercedes"))
            .body("model", is("Benz"));
    }

    @Test
    void whenPostCarAndGetCar_thenGetCar() throws Exception {
        Car car = new Car("Mercedes", "Benz");

        Integer carId = RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(car))
        .when()
            .post("/api/car")
        .then()
            .extract().path("carId");

        RestAssuredMockMvc.when()
            .get("/api/car/" + carId)
        .then()
            .statusCode(200)
            .body("model", is("Benz"));
    }

    @Test
    void whenGetInvalidCar_thenGetNotFound() throws Exception {
        RestAssuredMockMvc.when()
            .get("/api/car/10")
        .then()
            .statusCode(404);
    }

    @Test
    void givenNoCar_whenGetCars_thenReturnEmptyListOfCars() throws Exception {
        RestAssuredMockMvc
            .get("/api/cars")
        .then()
            .statusCode(200)
            .body("$.size()", is(0));

    }

    @Test
    void givenManyCars_whenGetCars_thenReturnListOfCars() throws Exception {
        Car car1 = new Car("Mercedes", "Benz");
        Car car2 = new Car("Porsche", "Carrera");
        Car car3 = new Car("Dacia", "Logan");

        List<Car> allCars = Arrays.asList(car1, car2, car3);
        Mockito.when(service.getAllCars())
            .thenReturn(allCars);

        RestAssuredMockMvc.when()
            .get("/api/cars")
        .then()
            .statusCode(200)
            .body("$.size()", is(3))
            .body("[0].model", is(car1.getModel()))
            .body("[1].model", is(car2.getModel()))
            .body("[2].model", is(car3.getModel()));
    }

    @Test
    void whenGetValidReplacementCar_thenGetCar() throws Exception {
        Car car = new Car("Mercedes", "Benz");

        Mockito.when(service.findReplacement(Mockito.any(Car.class)))
            .thenAnswer(i -> Optional.of(i.getArguments()[0]));

        RestAssuredMockMvc
        .given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(car))
        .when()
            .post("/api/car/replacement")
        .then()
            .statusCode(200)
            .body("model", is(car.getModel()))
            .body("maker", is(car.getMaker()));
    }

    @Test
    void whenGetInValidReplacementCar_thenGetNotFound() throws Exception {
        Car car = new Car("Mercedes", "Benz");
        Mockito.when(service.findReplacement(Mockito.any(Car.class)))
            .thenReturn(Optional.ofNullable(null));

        RestAssuredMockMvc
        .given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(car))
        .when()
            .post("/api/car/replacement")
        .then()
            .statusCode(404);
    }
}
