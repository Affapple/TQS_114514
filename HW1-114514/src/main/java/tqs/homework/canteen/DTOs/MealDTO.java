package tqs.homework.canteen.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tqs.homework.canteen.EnumTypes.MealTime;
import tqs.homework.canteen.EnumTypes.MealType;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MealDTO {
    private Long id;
    private String description;
    private MealType mealType;
    private MealTime mealTime;
}
