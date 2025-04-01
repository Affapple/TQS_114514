package tqs.homework.canteen.DTOs;

import java.time.LocalDate;
import java.util.List;

public class MenuRequestDTO {
    Long restaurantId;
    List<MealDTO> options;
    LocalDate date;
}
