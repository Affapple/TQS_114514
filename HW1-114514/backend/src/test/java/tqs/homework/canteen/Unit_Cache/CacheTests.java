package tqs.homework.canteen.Unit_Cache;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import tqs.homework.canteen.Cache.Cache;

@ExtendWith(MockitoExtension.class)
public class CacheTests {
    private Cache<String> cache;

    @BeforeEach
    public void setUp() {
        cache = new Cache<>();
        cache.setTtl(3600000L); // Set a default TTL value for testing
        cache.clear();
    }
    
    /**
     * Given a key and a value
     * When put the kv-pair in the cache
     * then the value is stored in the cache
     */
    @Test
    public void testPut() {
        cache.put("key", "value");
        assertThat(cache.get("key"), is("value"));
    }

    /**
     * Given the key-value pairs (key, null) and (null, value)
     * When put both kv-pairs in the cache
     * then an IllegalArgumentException is thrown for both cases
     */
    @Test
    public void testPutNullValue() {
        assertThrows(IllegalArgumentException.class, () -> cache.put("key", null));
        assertThrows(IllegalArgumentException.class, () -> cache.put(null, "value"));
    }

    /**
     * Given a key that is in the cache
     * When get a value from the cache
     * then the value is returned, and the cache hit counter is incremented
     */
    @Test
    public void testGet() {
        cache.put("key", "value");
        assertThat(cache.get("key"), is("value"));
        assertThat(cache.getCacheHits(), is(1L));
    }

    /**
     * Given a key that is not in the cache
     * When get a value from the cache
     * then the value is returned, and the cache miss counter is incremented
     */
    @Test
    public void testGetNullValue() {
        assertThat(cache.get("key"), is(nullValue()));
        assertThat(cache.getCacheMisses(), is(1L));
    }

    /**
     * Given a key that may or may not be in the cache
     * When remove the key from the cache
     * then the key is removed from the cache
     */
    @Test
    public void testRemove() {
        cache.put("key", "value");
        cache.remove("key");

        assertThat(cache.get("key"), is(nullValue()));
    }

    /**
     * Given an expired key that is in the cache
     * When get the value from the cache
     * then null is returned, and the cache miss counter is incremented
     */
    @Test
    public void testGetExpiredKey() {
        cache.put("key", "value");
        cache.setTtl(0L);
        assertThat(cache.get("key"), is(nullValue()));
        assertThat(cache.getCacheMisses(), is(1L));
    }
}
