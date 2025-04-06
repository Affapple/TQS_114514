package tqs.homework.canteen.DTOs;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.EnumTypes.MenuTimeConverter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MenuDTO {
    Long id;
    Long restaurantId;
    List<MealDTO> options;
    LocalDate date;
    @Convert(converter = MenuTimeConverter.class)
    MenuTime time;
}
