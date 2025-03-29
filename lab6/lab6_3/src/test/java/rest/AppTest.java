package rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class AppTest {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void whenFetchingAllTodos_thenAPIisAvailable() {
        when()
            .get("/todos")
        .then()
            .statusCode(200);
    }
    
    @Test
    public void whenQueryingForToDo4_thenTitleIsCorrect() {
        when()
            .get("/todos/4")
        .then()
            .statusCode(200)
            .body("title", equalTo("et porro tempora"));
    }

    @Test
    public void whenListingAllTodos_thenIds198and199ArePresent() {
        when()
            .get("/todos")
        .then()
            .statusCode(200)
            .body("id", hasItem(198))
            .body("id", hasItem(199));
    }

    @Test
    public void whenListingAllTodos_thenResponseTimeIsLessThan2Seconds() {
        when()
            .get("/todos")
        .then()
            .statusCode(200)
            .time(lessThan(2000L)); // Time in milliseconds
    }
}
