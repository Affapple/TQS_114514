import { getAllRestaurants } from "@api/Restaurant";
import { Restaurant } from "@Types/Restaurant";
import { Dispatch, SetStateAction, useEffect, useState } from "react";
import styles from "./styles.module.css";


interface RestaurantsProps {
  setSelectedRestaurant: Dispatch<SetStateAction<number>>;
  setTitle: Dispatch<SetStateAction<string>>;
}

export default function Restaurants(
  {setSelectedRestaurant, setTitle}: RestaurantsProps 
) {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);

  useEffect(() => {
    setTitle("Restaurantes");

    const fetchRestaurants = async () => {
      const response = await getAllRestaurants();

      if (response.status === 200) {
        setRestaurants(response.data);
      }
    };

    fetchRestaurants();
  }, []);

  return (
    <div className={styles.restaurants}>
      {restaurants.length > 0 ? (
        restaurants.map((restaurant) => (
          <div key={restaurant.id} className={styles.restaurant}
            onClick={() => {
            setSelectedRestaurant(restaurant.id);
          }}
        >
          <h3>{restaurant.name}</h3>
          <p className={styles.restaurantInfo}>
            <span>{restaurant.location}</span>
            <span>{restaurant.capacity} lugares</span>
          </p>
        </div>
      ))
      ) : (
        <div className={styles.noRestaurants}>
          <span>No restaurants found</span>
        </div>
      )}
    </div>
  )
}
