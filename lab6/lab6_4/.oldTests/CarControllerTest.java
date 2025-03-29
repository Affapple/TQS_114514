package tqs.cars;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import tqs.cars.entity.Car;
import tqs.cars.controller.CarController;
import tqs.cars.services.CarManagerService;

@WebMvcTest(CarController.class)
public class CarControllerTest {
    @Autowired
    private MockMvc mvc;
    
    @MockitoBean
    private CarManagerService carService;

    @Test
    void whenPostCar_thenCreateCar() throws Exception {
        Car car = new Car("Mercedes", "Benz");

        when(carService.save(any())).thenReturn(car);

        mvc.perform(
            post("/api/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(car))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.maker", is("Mercedes")))
        .andExpect(jsonPath("$.model", is("Benz")));
    }

    @Test
    void whenPostCarAndGetCar_thenGetCar() throws Exception {
        Car car = new Car("Mercedes", "Benz");

        when(carService.save(car)).thenReturn(car);
        when(
            carService.getCarDetails(any())
        )
        .thenReturn(Optional.of(car));

        mvc.perform(
            post("/api/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(car))
        );

        mvc.perform(
            get("/api/car/" + car.getCarId())
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.model", is("Benz")));
    }

    @Test
    void whenGetInvalidCar_thenGetNotFound() throws Exception {
        when(
            carService.getCarDetails(any())
        )
        .thenReturn(Optional.ofNullable(null));

        mvc.perform(
            get("/api/car/10")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void givenNoCar_whenGetCars_thenReturnEmptyListOfCars() throws Exception {
        when(
            carService.getAllCars()
        ).thenReturn(
           new ArrayList<Car>() 
        );

        mvc.perform(
            get("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));

        verify(carService, times(1)).getAllCars();
    }

    @Test
    void givenManyCars_whenGetCars_thenReturnListOfCars() throws Exception {
        Car car1 = new Car("Mercedes", "Benz");
        Car car2 = new Car("Porsche", "Carrera");
        Car car3 = new Car("Dacia", "Logan");

        List<Car> allCars = Arrays.asList(car1, car2, car3);

        when(
            carService.getAllCars()
        ).thenReturn(
           allCars 
        );

        mvc.perform(
            get("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].model", is(car1.getModel())))
        .andExpect(jsonPath("$[1].model", is(car2.getModel())))
        .andExpect(jsonPath("$[2].model", is(car3.getModel())));

        verify(carService, times(1)).getAllCars();
    }
    
    @Test
    void whenGetValidReplacementCar_thenGetCar() throws Exception {
        Car car = new Car("Mercedes", "Benz");
        when(
            carService.findReplacement(any())
        )
        .thenReturn(Optional.of(new Car("Mercedes", "Benz")));

        mvc.perform(
            post("/api/car/replacement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(car))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.model", is(car.getModel())))
        .andExpect(jsonPath("$.maker", is(car.getMaker())));
    }
    
    @Test
    void whenGetInValidReplacementCar_thenGetNotFound() throws Exception {
        Car car = new Car("Mercedes", "Benz");
        when(
            carService.findReplacement(any())
        )
        .thenReturn(Optional.ofNullable(null));

        mvc.perform(
            post("/api/car/replacement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(car))
        )
        .andExpect(status().isNotFound());
    }
}
