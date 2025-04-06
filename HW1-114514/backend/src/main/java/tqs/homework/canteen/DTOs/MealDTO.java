package tqs.homework.canteen.DTOs;

import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tqs.homework.canteen.EnumTypes.MealType;
import tqs.homework.canteen.EnumTypes.MealTypeConverter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MealDTO {
    private Long menuId;
    private String description;
    @Convert(converter = MealTypeConverter.class)
    private MealType type;
}
