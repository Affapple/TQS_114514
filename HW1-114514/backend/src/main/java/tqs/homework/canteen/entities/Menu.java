package tqs.homework.canteen.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringExclude;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;
import tqs.homework.canteen.EnumTypes.MenuTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"restaurant"})
@Table(name = "menu", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"restaurant_id", "date", "time"})
})
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "menu_time")
    private MenuTime time;
    
    private Integer capacity;
    

    @ManyToOne
    @JsonIgnore
    @ToStringExclude
    private Restaurant restaurant;
    
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Meal> options = new ArrayList<>();

    public Menu(LocalDate date, MenuTime time, Restaurant restaurant) {
        this.date = date;
        this.time = time;
        this.restaurant = restaurant;
        this.capacity = restaurant.getCapacity();
    }
}
