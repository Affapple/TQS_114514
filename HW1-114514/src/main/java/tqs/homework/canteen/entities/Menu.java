package tqs.homework.canteen.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import tqs.homework.canteen.EnumTypes.MenuTime;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private MenuTime time;
    private Integer capacity;

    @ManyToOne
    private Restaurant restaurant;
    
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<Meal> options;
}
