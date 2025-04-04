package tqs.homework.canteen.Unit_ControllerTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tqs.homework.canteen.controller.RestaurantController;
import tqs.homework.canteen.services.MenuService;

@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTests {
    @Autowired
    private MockMvc mvc;
    
    @MockitoBean
    private MenuService menuService;
}
