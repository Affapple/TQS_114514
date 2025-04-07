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

function getRestaurantById(id: number | string): Promise<AxiosResponse<Restaurant>> {
  const response = apiClient.get(`/restaurants/${id}`);
  return response;
}

function getRestaurantMenus(id: number, from?: Date, to?: Date): Promise<AxiosResponse<{menus: Menu[], hasMore: boolean}>> {
  console.log("from", from);
  console.log("to", to);
  const response = apiClient.get(`/restaurants/${id}/menus`, {
    params: {
      from: from ? toDateString(from) : undefined,
      to: to ? toDateString(to) : undefined,
    },
  });

  return response;
}

function toDateString(date: Date): string {
  return date.toISOString().split('T')[0];
}

export { getAllRestaurants, createRestaurant, getRestaurantById, getRestaurantMenus };
