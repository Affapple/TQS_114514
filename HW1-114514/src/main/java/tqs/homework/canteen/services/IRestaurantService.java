package tqs.homework.canteen.services;

import java.util.List;
import tqs.homework.canteen.entities.Restaurant;

public interface IRestaurantService {
    public Restaurant saveNewRestaurant(Restaurant newRestaurant);
    public List<Restaurant> getAllRestaurants();
    public Restaurant getRestaurantById(Long id);
    
}
