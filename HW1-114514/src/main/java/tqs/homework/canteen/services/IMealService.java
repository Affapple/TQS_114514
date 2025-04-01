package tqs.homework.canteen.services;

import tqs.homework.canteen.DTOs.MealDTO;
import tqs.homework.canteen.entities.Meal;

public interface IMealService {
    public Meal createNewMeal(MealDTO meal);
}
