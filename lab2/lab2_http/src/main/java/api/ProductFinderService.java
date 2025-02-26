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
        try {
            String response = httpClient.doHttpGet(API_PRODUCTS + "/" + productId);

            Product product = parseJson(response);

            return Optional.ofNullable(product);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return Optional.empty(); 
        }
    }

    private Product parseJson(String response) {
       Product product = JsonParser.fromJson(response, Product.class);
    
        if (product == null) {
            return null;
        }
        if (product.id == null){
            return null;
        }

       return product;
    }
}
