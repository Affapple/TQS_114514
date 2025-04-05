package tqs.homework.canteen.DTOs;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tqs.homework.canteen.EnumTypes.MenuTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MenuRequestDTO {
    private Long restaurantId;
    private List<MealDTO> options;
    private LocalDate date;
    private MenuTime time;
}
