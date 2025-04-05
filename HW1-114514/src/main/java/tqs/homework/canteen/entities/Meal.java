package tqs.homework.canteen.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;
import tqs.homework.canteen.EnumTypes.MealType;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"menu"})
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @Column(name="meal_type")
    private MealType type;

    @ManyToOne
    @JsonIgnore
    private Menu menu;
    
    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Reservation> reservations = new ArrayList<>();

    public Meal(String description, MealType type, Menu menu) {
        this.description = description;
        this.type = type;
        this.menu = menu;
    }
}
