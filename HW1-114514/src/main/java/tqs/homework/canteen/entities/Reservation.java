package tqs.homework.canteen.entities;


import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tqs.homework.canteen.DTOs.ReservationDTO;
import tqs.homework.canteen.EnumTypes.ReservationStatus;


@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class Reservation {
    @Id
    private String code;
    private ReservationStatus status;
    
    @ManyToOne
    private Meal meal;

    @PostConstruct
    public String generateCode() {
        code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return code;
    }

    public ReservationDTO asDTO() {
        return new ReservationDTO(this.code, this.status);
    }
}