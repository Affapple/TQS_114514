package tqs.homework.canteen.Cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
class CacheEntry<T> {
    private T value;
    private long timestamp;
}