import { MenuTime } from "@Types/MenuTime";
import { MealDTO } from "@Types/MealDTO";

export interface MenuRequestDTO {
    restaurantId: number;
    date: Date;
    options: MealDTO[];
    time: MenuTime;
}

