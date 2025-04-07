package tqs.homework.canteen.DTOs;

import java.util.List;

import lombok.*;
import tqs.homework.canteen.entities.Menu;

@AllArgsConstructor
@Getter
@Setter
public class MenuListResponseDTO {
    private List<Menu> menus;
    private boolean hasMore;
}
