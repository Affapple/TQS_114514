package api;

import java.util.Optional;

import com.google.gson.Gson;

public class ProductFinderService {
    Gson JsonParser = new Gson();
    
    String API_PRODUCTS;
    ISimpleHttpClient httpClient;


    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
        API_PRODUCTS = "https://fakestoreapi.com/products";
    }


    public Optional<Product> findProductDetails(Integer productId) {
        String response = httpClient.doHttpGet(API_PRODUCTS + "/" + productId);

        Product product = parseJson(response);

        if (product == null) {
            return Optional.empty();
        }

        return Optional.of(product);
    }

    private Product parseJson(String response) {
       Product product = JsonParser.fromJson(response, Product.class);

       return product.id == null ? null : product;
    }
}
