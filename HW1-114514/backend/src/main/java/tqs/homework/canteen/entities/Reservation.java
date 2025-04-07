package tqs.homework.canteen.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;
import tqs.homework.canteen.EnumTypes.ReservationStatus;
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"meal"})
public class Reservation {
    @Id
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "reservation_status")
    private ReservationStatus status;

    @ManyToOne
    @JsonIgnore
    private Meal meal;

    public Reservation(String code, ReservationStatus status, Meal meal) {
        this.code = code;
        this.status = status;
        this.meal = meal;
    }
    
    public String generateCode() {
        code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return code;
    }
}