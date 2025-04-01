package tqs.homework.canteen.DTOs;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MenuDTO {
    Long id;
    Long restaurantId;
    List<MealDTO> options;
    LocalDate date;
}
