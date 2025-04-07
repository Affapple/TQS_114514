package tqs.homework.canteen.Cache;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Component
public class Cache<T> {
    @Setter
    @Getter
    @Value("${cache.ttl: #{3600000}}")
    private Long ttl;
    private HashMap<String, CacheEntry<T>> cache = new HashMap<>();

    @Getter
    private Long cacheHits = 0L;
    @Getter
    private Long cacheMisses = 0L;

    public void put(String key, T value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value cannot be null");
        }
        CacheEntry<T> entry = new CacheEntry<>(value, System.currentTimeMillis());
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
            cache.remove(key);
            return null;
        }
        
        cacheHits++;
        return entry.getValue();
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
        cacheHits = 0L;
        cacheMisses = 0L;
    }
}