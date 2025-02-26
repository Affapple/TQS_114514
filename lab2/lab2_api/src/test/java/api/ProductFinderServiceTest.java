package api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@ExtendWith(MockitoExtension.class)
public class ProductFinderServiceTest {
    @Mock
    ISimpleHttpClient httpClient;

    @InjectMocks
    private ProductFinderService productFinderService;

    @Test
    public void whenFindProduct3_thenReturnProduct() {
        Mockito.when(
            httpClient.doHttpGet("https://fakestoreapi.com/products/3")
        ).thenReturn(
            """
            {
                "id": 3,
                "title": "Mens Cotton Jacket",
                "price": 35,
                "description": "great outerwear",
                "category": "Jacket",
                "image": "http://example.com"
            }
            """
        );

        assertThat(
            productFinderService.findProductDetails(3).get().title,
            equalTo("Mens Cotton Jacket")
        );

    }

    @Test
    public void whenFindProduct300_thenReturnNoProduct() {
        Mockito.when(
            httpClient.doHttpGet("https://fakestoreapi.com/products/300")
        ).thenReturn(
            """
            {
                "error": "Product not found"
            }
            """
        );

        assertThat(
            productFinderService.findProductDetails(300).isPresent(),
            is(false)
        );

    }
}
