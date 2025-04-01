package tqs.homework.canteen.services;

import tqs.homework.canteen.entities.Restaurant;

public interface IRestaurantService {
    public Restaurant saveNewRestaurant(Restaurant newRestaurant);
    public Restaurant getAllRestaurants();
}
