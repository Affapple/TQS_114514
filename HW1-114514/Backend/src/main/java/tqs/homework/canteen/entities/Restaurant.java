package tqs.homework.canteen.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Menu> menus = new ArrayList<>();

    public Restaurant(String name, String location, Integer capacity) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }
}
