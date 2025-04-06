package tqs.homework.canteen.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CacheStats {
    private Long cacheHits;
    private Long cacheMisses;
    private Long totalCalls;
}
