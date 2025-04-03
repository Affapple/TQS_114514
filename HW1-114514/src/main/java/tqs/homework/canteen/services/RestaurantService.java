package tqs.homework.canteen.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.repositories.RestaurantRepository;

@Service
public class RestaurantService implements IRestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant saveNewRestaurant(Restaurant newRestaurant) {
        return restaurantRepository.save(newRestaurant);
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRestaurantById'");
    }
}
