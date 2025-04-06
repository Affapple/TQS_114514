import { Meal } from "@Types/Meal";
import { MenuTime } from "@Types/MenuTime";

export interface Menu {
  id: number;
  name: string;
  description: string;
  capacity: number;
  options: Meal[];
  time: MenuTime;
  date: Date;
}
