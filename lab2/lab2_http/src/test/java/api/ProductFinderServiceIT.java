package api;

import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ProductFinderServiceIT {
    private ProductFinderService productFinderService;

    @BeforeEach
    public void setUp() {
        productFinderService = new ProductFinderService(new HttpClient());
    }
    
    @Test
    public void whenFindProduct3_thenReturnProduct() {
        assertThat(
            productFinderService.findProductDetails(3).get().title,
            equalTo("Mens Cotton Jacket")
        );

    }

    @Test
    public void whenFindProduct300_thenReturnNoProduct() {
        assertThat(
            productFinderService.findProductDetails(300).isPresent(),
            is(false)
        );

    }
}
