package tqs.homework.canteen.controllerTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tqs.homework.canteen.controller.MenuController;
import tqs.homework.canteen.services.MenuService;

@WebMvcTest(MenuController.class)
public class MenuControllerTests {
    @Autowired
    private MockMvc mvc;
    
    @MockitoBean
    private MenuService menuService;
}
