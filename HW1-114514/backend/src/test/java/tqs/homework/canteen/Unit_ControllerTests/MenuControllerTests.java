package tqs.homework.canteen.Unit_ControllerTests;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.flywaydb.core.internal.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.DTOs.MenuRequestDTO;
import tqs.homework.canteen.EnumTypes.MealType;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.controller.MenuController;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.services.MenuService;

@WebMvcTest(MenuController.class)
public class MenuControllerTests {
    @Autowired
    public MockMvc mvc;
    
    @MockitoBean
    public MenuService menuService;

    /* POST: /api/v1/menu */
    /**
     * Given restaurant doesnt exists
     * when createNewMenu to restaurant
     * then return 404
     */

    @Test
    public void whenCreateNewMenuToRestaurant_thenReturn404() throws Exception {
        when(menuService.createNewMenu(Mockito.any(MenuRequestDTO.class)))
            .thenThrow(new NoSuchElementException("Restaurant not found!"));

        mvc.perform(
            post("/api/v1/menu")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                JsonUtils.toJson(new MenuRequestDTO(10L, new ArrayList<>(), LocalDate.now(), MenuTime.LUNCH))
            )
        )
            .andExpect(status().isNotFound());
    }
    /**
     * Given resttaurant exists
     * when menu for given date and time table dont exists
     * then create new menu and return 201
     */
    @Test
    public void whenCreateNewMenuToRestaurant_thenReturn201() throws Exception {
        when(menuService.createNewMenu(Mockito.any(MenuRequestDTO.class)))
            .thenReturn(new Menu());

        when(menuService.addMeals(Mockito.anyLong(), Mockito.anyList()))
            .thenReturn(new Menu());

        mvc.perform(
            post("/api/v1/menu")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                JsonUtils.toJson(
                    new MenuRequestDTO(10L, new ArrayList<>(), LocalDate.now(), MenuTime.LUNCH)
                )
            )
        )
        .andExpect(status().isCreated());
    }

    /**
     * Given resttaurant exists
     * when menu for given date and time table already exists
     * then return 400
     */
    @Test
    public void whenCreateNewMenuToRestaurant_thenReturn400() throws Exception {
        when(menuService.createNewMenu(Mockito.any(MenuRequestDTO.class)))
            .thenThrow(new IllegalArgumentException("Menu already exists!"));

        when(menuService.addMeals(Mockito.anyLong(), Mockito.anyList()))
            .thenReturn(new Menu());

        mvc.perform(
            post("/api/v1/menu")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                JsonUtils.toJson(new MenuRequestDTO(10L, new ArrayList<>(), LocalDate.now(), MenuTime.LUNCH))
            )
        )
            .andExpect(status().isBadRequest());
    }

    /* PUT: /api/v1/menu/{menu_id} */
    /**
     * Given menu doesnt exits
     * when add meal
     * then return 404
     */
    @Test
    public void whenAddMealToMenu_thenReturn404() throws Exception {
        when(menuService.addMeal(Mockito.any(MealDTO.class)))
            .thenThrow(new NoSuchElementException("Menu not found!"));

        when(menuService.addMeals(Mockito.anyLong(), Mockito.anyList()))
            .thenReturn(new Menu());

        mvc.perform(
            put("/api/v1/menu")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"menuId\": 1}" + JsonUtils.toJson(new MealDTO(10L, "Meal", MealType.MEAT))
            )
        ).andExpect(status().isNotFound());
    }

    /**
     * Given menu exist 
     *    and meal of type already exists
     * when add meal
     * then return 400
     */
    @Test
    public void whenAddMealToMenu_thenReturn400() throws Exception {
        when(menuService.addMeal(Mockito.any(MealDTO.class)))
            .thenThrow(new IllegalArgumentException("Meal already exists!"));

        mvc.perform(
            put("/api/v1/menu")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"menuId\": 1}" + JsonUtils.toJson(new MealDTO(10L, "Meal", MealType.MEAT))
            )
        )
            .andExpect(status().isBadRequest());
    }

    /**
     * Given menu exists
     *    and when meal of type doesnt exists
     * when add meal
     * then add meal and return 201
     */
    @Test
    public void whenAddMealToMenu_thenReturn201() throws Exception {
        when(menuService.addMeal(Mockito.any(MealDTO.class)))
            .thenReturn(new Menu());

        mvc.perform(
            put("/api/v1/menu")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"menuId\": 1}" + JsonUtils.toJson(new MealDTO(10L, "Meal", MealType.MEAT))
            )
        )
            .andExpect(status().isCreated());
    }

    /* DELETE: /api/v1/menu/{menu_id} */
    /**
     * Given menu doesnt exists
     * when delete menu
     * then return 404
     */
    @Test
    public void whenDeleteMenu_thenReturn404() throws Exception {
        Mockito.doThrow(new NoSuchElementException("Menu not found!"))
        .when(menuService).deleteMenu(Mockito.anyLong());

        mvc.perform(delete("/api/v1/menu/1"))
            .andExpect(status().isNotFound());
    }

     /* Given menu exists
     * when delete menu
     * then delete menu and return 200
     */
    @Test
    public void whenDeleteMenu_thenReturn200() throws Exception {
        Mockito.doNothing()
        .when(menuService).deleteMenu(Mockito.anyLong());

        mvc.perform(delete("/api/v1/menu/1"))
            .andExpect(status().isOk());
    }

    /* DELETE: /api/v1/menu/{menu_id}/{meal_id} */
    /**
     * Given menu exists and doesnt contain meal with given id
     * when delete meal from menu
     * then return 404
     */
    @Test
    public void whenDeleteMealThatDoesntExistFromMenu_thenReturn404() throws Exception {
        Mockito.doThrow(new NoSuchElementException("Menu not found!"))
        .when(menuService).deleteMeal(Mockito.anyLong(), Mockito.anyLong());

        mvc.perform(delete("/api/v1/menu/1/1"))
            .andExpect(status().isNotFound());
    }
    /**
     * Given menu exists and contains meal with given id
     * when delete meal from menu
     * then delete meal from menu and return 200
     */
    @Test
    public void whenDeleteMealFromMenu_thenReturn200() throws Exception {
        doNothing().when(menuService).deleteMeal(1L, 1L);

        mvc.perform(delete("/api/v1/menu/1/1"))
            .andExpect(status().isOk());
    }
     /**
      *  Given menu exists and doesnt contain meal with given id
      * when delete meal from menu
      * then return 404
      */
     @Test
     public void whenDeleteMealFromMenu_thenReturn404() throws Exception {
        Mockito.doThrow(new NoSuchElementException("Meal not found!"))
        .when(menuService).deleteMeal(Mockito.anyLong(), Mockito.anyLong());

        mvc.perform(delete("/api/v1/menu/1/1"))
            .andExpect(status().isNotFound());
    }
}
