package tqs.homework.canteen.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
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
    private String description;

    @Column(name="meal_type")
    private MealType type;

    @ManyToOne
    private Menu menu;
    
    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}
