package tqs.cars.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(of = {"carId", "maker", "model"})
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long carId;

    String maker;
    String model;

    public Car(String maker, String model) {
        this.maker = maker;
        this.model = model;
    }
} 
