package tqs.homework.canteen.Cache;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class Cache<T> {
    
    @Value("${cache.ttl}")
    private Long ttl;
    private HashMap<String, CacheEntry<T>> cache = new HashMap<>();

    @Getter
    private Long cacheHits = 0L;
    @Getter
    private Long cacheMisses = 0L;

    public void put(String key, T value) {
        CacheEntry<T> entry = new CacheEntry<>(value, System.currentTimeMillis() + ttl);
        cache.put(key, entry);
    }
    
    public T get(String key) {
        if (!cache.containsKey(key)) {
            cacheMisses++;
            return null;
        }
        CacheEntry<T> entry = cache.get(key);
        if (ttl < System.currentTimeMillis() - entry.getTimestamp()) {
            cacheMisses++;
            return null;
        }
        
        cacheHits++;
        return entry.getValue();
    }

    public void remove(String key) {
        cache.remove(key);
    }
}