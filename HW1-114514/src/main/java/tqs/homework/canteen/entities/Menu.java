package tqs.homework.canteen.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import tqs.homework.canteen.EnumTypes.MenuTime;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private List<Meal> options = new ArrayList<>();

    public Menu(LocalDate date, MenuTime time, Restaurant restaurant) {
        this.date = date;
        this.time = time;
        this.restaurant = restaurant;
        this.capacity = restaurant.getCapacity();
    }
}
