package tqs.homework.canteen.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import tqs.homework.canteen.EnumTypes.MealTime;
import tqs.homework.canteen.EnumTypes.MealType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Menu menu;

    private String description;
    private MealType mealType;
    private MealTime mealTime;
    
    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}
