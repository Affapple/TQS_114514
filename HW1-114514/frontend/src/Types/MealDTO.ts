import { MealType } from "@Types/MealType";

export interface MealDTO {
    menuId: number;
    description: string;
    type: MealType;
}

