package tqs.cars;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import tqs.cars.entity.Car;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CarControllerTests  {
    @LocalServerPort
    int randomServerPort;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = randomServerPort;
    }

    @Test
    void whenPostCar_thenCreateCar() throws Exception {
        Car car = new Car("Mercedes", "Benz");

        given()
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

        Integer carId = given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(car))
        .when()
            .post("/api/car")
        .then()
            .extract().path("carId");

        when()
            .get("/api/car/" + carId)
        .then()
            .statusCode(200)
            .body("model", is("Benz"));
    }

    @Test
    void whenGetInvalidCar_thenGetNotFound() throws Exception {
        when()
            .get("/api/car/10")
        .then()
            .statusCode(404);
    }

    @Test
    void givenNoCar_whenGetCars_thenReturnEmptyListOfCars() throws Exception {
        when()
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

        for (Car car : List.of(car1, car2, car3)) {
            given()
                .contentType(ContentType.JSON)
                .body(JsonUtils.toJson(car))
            .when()
                .post("/api/car")
            .then()
                .statusCode(201);
        }

        when()
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
        Car carReplacement = new Car("Mercedes", "Benz");
        given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(carReplacement))
        .when()
            .post("/api/car");

        given()
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
        given()
            .contentType(ContentType.JSON)
            .body(JsonUtils.toJson(car))
        .when()
            .post("/api/car/replacement")
        .then()
            .statusCode(404);
    }
}
