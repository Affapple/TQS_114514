import { Menu } from "@Types/Menu";
import { Restaurant } from "@Types/Restaurant";
import apiClient from "@api/ApiClient";
import { AxiosResponse } from "axios";


function getAllRestaurants(): Promise<AxiosResponse<Restaurant[]>> {
  const response = apiClient.get("/restaurants");
  return response;
}

function createRestaurant(restaurant: Restaurant): Promise<AxiosResponse<Restaurant>> {
  const response = apiClient.post("/restaurants", restaurant);
  return response;
}

function getRestaurantById(id: number): Promise<AxiosResponse<Restaurant>> {
  const response = apiClient.get(`/restaurants/${id}`);
  return response;
}

function getRestaurantMenus(id: number, from?: Date, to?: Date): Promise<AxiosResponse<Menu[]>> {
  const response = apiClient.get(`/restaurants/${id}/menus`, {
    params: {
      from: from?.toISOString(),
      to: to?.toISOString(),
    },
  });

  return response;
}

export { getAllRestaurants, createRestaurant, getRestaurantById, getRestaurantMenus };
