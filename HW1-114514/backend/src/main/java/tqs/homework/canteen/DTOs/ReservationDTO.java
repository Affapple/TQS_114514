package tqs.homework.canteen.DTOs;

import jakarta.persistence.Convert;
import lombok.*;
import tqs.homework.canteen.EnumTypes.ReservationStatus;
import tqs.homework.canteen.EnumTypes.ReservationStatusConverter;

@Getter
@AllArgsConstructor
public class ReservationDTO {
    String code;
    @Convert(converter = ReservationStatusConverter.class)
    ReservationStatus status;
}
