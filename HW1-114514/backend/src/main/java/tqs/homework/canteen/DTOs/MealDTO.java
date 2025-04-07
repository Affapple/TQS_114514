package tqs.homework.canteen.DTOs;

import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tqs.homework.canteen.EnumTypes.MealType;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MealDTO {
    private Long menuId;
    private String description;
    private MealType type;
}
