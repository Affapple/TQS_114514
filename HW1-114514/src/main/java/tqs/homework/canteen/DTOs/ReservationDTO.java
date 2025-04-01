package tqs.homework.canteen.DTOs;

import lombok.*;
import tqs.homework.canteen.EnumTypes.ReservationStatus;

@Getter
@AllArgsConstructor
public class ReservationDTO {
    String code;
    ReservationStatus status;
}
