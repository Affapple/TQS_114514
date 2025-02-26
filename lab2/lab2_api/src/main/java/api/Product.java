package api;

public class Product {
    Integer id;
    String image,
           description,
           title,
           category;

    Double price;

    public Product(Integer id, String image, String description, String title, String category, Double price) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public Product() {}
}
