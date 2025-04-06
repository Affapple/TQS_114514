import apiClient from "@api/ApiClient";
import { MealDTO } from "@Types/MealDTO";
import { MenuRequestDTO } from "@Types/MenuRequestDTO";

function createMenu(menuRequest: MenuRequestDTO) {
  console.log('Creating menu with request:', JSON.stringify(menuRequest, null, 2));
  const response = apiClient.post("/menu", menuRequest);
  return response;
}

function addMealToMenu(meal: MealDTO) {
  const response = apiClient.post("/menu/", meal);
  return response;
}

function deleteMenu(menuId: number) {
  const response = apiClient.delete(`/menu/${menuId}`);
  return response;
}

function deleteMealFromMenu(menuId: number, mealId: number) {
  const response = apiClient.delete(`/menu/${menuId}/${mealId}`);
  return response;
}


export { createMenu, addMealToMenu, deleteMealFromMenu, deleteMenu };
