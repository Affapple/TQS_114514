package tqs.homework.canteen.entities;


import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tqs.homework.canteen.DTOs.ReservationDTO;
import tqs.homework.canteen.EnumTypes.ReservationStatus;


@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"meal"})
public class Reservation {
    @Id
    private String code;
    private ReservationStatus status;
    
    @ManyToOne
    @JsonIgnore
    private Meal meal;

    public String generateCode() {
        code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return code;
    }

    public ReservationDTO asDTO() {
        return new ReservationDTO(this.code, this.status);
    }
}