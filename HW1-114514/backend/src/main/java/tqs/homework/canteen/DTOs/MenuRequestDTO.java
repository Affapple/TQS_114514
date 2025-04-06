package tqs.homework.canteen.DTOs;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Convert;
import lombok.*;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.EnumTypes.MenuTimeConverter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MenuRequestDTO {
    private Long restaurantId;
    private List<MealDTO> options;
    private LocalDate date;
    @Convert(converter = MenuTimeConverter.class)   
    private MenuTime time;
}