package ies.carbox.api.RestAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;

import ies.carbox.api.RestAPI.entity.Car;
import ies.carbox.api.RestAPI.entity.CarLiveInfo;
import ies.carbox.api.RestAPI.entity.TripInfo;
import ies.carbox.api.RestAPI.entity.User;

/**
 * CacheRepository
 */
@Service
public class CacheService {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    private static ObjectMapper mapper = JsonMapper.builder()
                                            .addModule(new JavaTimeModule())
                                            .build();

    @Value("${cache.general.expiration-time}")
    private long ttl;

    private static String tripKey = "_trips";
    private static String liveDataKey = "_livedata";


    public void saveTrip(TripInfo trip, String carId) {
        String key = carId + tripKey;
        boolean setExpire = !redisTemplate.hasKey(key);

        try {
            var json = mapper.writeValueAsString(trip);
            System.out.println("INFO: Saving trip from car \"" + carId + "\": " + json);
            redisTemplate.opsForList().rightPush(key, json);

            if (setExpire)
                redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            System.out.println("ERROR: Error saving car trip to cache");
            e.printStackTrace();
        }
    }
    public void saveTrip(List<TripInfo> trips, String carId) {
        trips.forEach((trip) -> saveTrip(trip, carId));
    }
    public List<TripInfo> getCarTrips(String carId) {
        String key = carId + tripKey;

        try {
            List<String> tripsJson = redisTemplate.opsForList().range(key, 0, -1);

            List<TripInfo> trips = new ArrayList<>();
            if (tripsJson == null) {
                System.out.println("INFO: Trips fetched from cache was null (key = " + key + ")");
                return trips;
            }

            for (String trip : tripsJson) {
                trips.add(
                    mapper.readValue(trip, TripInfo.class)
                );
            }

            System.out.println("Fetched trips from cache (key = " + key + ")");
            trips.forEach(System.out::println);
            return trips;
        } catch (Exception e) {
            System.out.println("ERROR: Error fetching user trips from cache");
            e.printStackTrace();
            return null;
        }
    }

    public void saveUser(User user) {
        String key = user.getEmail();

        try {
            System.err.println("DEBUG: User= " + user);
            var json = mapper.writeValueAsString(user);
            System.out.println("INFO: Saving User:" + json);
            redisTemplate.opsForValue().set(key, json);
            redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
        } catch (JsonProcessingException e) {
            System.out.println("ERROR: Error saving user to cache");
            e.printStackTrace();
        }
    }
    public User getUser(String email) {
        String key = email;

        try {
            System.out.println("Email: " + email);
            String userJson = redisTemplate.opsForValue().get(key);

            if (userJson == null) {
                System.out.println("INFO: User not found in cache (key = " + key + ")");
                return null;
            }

            User user = mapper.readValue(userJson, User.class);
            System.out.println("INFO: Fetched user from cache using key \"" + key + "\": " + user);
            return user;
        } catch (Exception e) {
            System.out.println("ERROR: Error fetching user from cache");
            e.printStackTrace();
        }
        return null;
    }
    public void deleteUser(String email) {
        String key = email;
        redisTemplate.delete(key);
    }

    public void saveLiveData(CarLiveInfo live_data, String email) {
        String key = email + liveDataKey;
        boolean setExpire = !redisTemplate.hasKey(key);

        try {
            var json = mapper.writeValueAsString(live_data);
            System.out.println("INFO: Saving live_data_point from user \"" + email + "\": " + json);
            redisTemplate.opsForList().rightPush(key, json);

            if (setExpire)
                redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
        } catch (JsonProcessingException e) {
            System.out.println("ERROR: Error saving live_data_point to cache");
            e.printStackTrace();
        }
    }
    public void saveLiveData(List<CarLiveInfo> live_datas, String email) {
        live_datas.forEach((live_data) -> saveLiveData(live_data, email));
    }
    public List<CarLiveInfo> getLiveData(String ecuId) {
        String key = ecuId + liveDataKey;

        try {
            List<String> liveDataJson = redisTemplate.opsForList().range(key, 0, -1);

            List<CarLiveInfo> liveData = new ArrayList<>();
            if (liveDataJson == null) {
                System.out.println("INFO: User live data fetched from cache was null (key = " + key + ")");
                return liveData;
            }

            for (String trip : liveDataJson) {
                liveData.add(
                    mapper.readValue(trip, CarLiveInfo.class)
                );
            }

            System.out.println("Fetched live data from cache (key = " + key + ")");
            liveData.forEach(System.out::println);

            return liveData;
        } catch (Exception e) {
            System.out.println("ERROR: Error fetching user live data from cache");
            e.printStackTrace();
        }
        return null;
    }


    public void saveCar(Car car) {
        String key = car.getEcuId();

        try {
            var json = mapper.writeValueAsString(car);
            System.out.println("INFO: Saving Car:" + json);
            redisTemplate.opsForValue().set(key, json);
            redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println("ERROR: Error saving car to cache");
            e.printStackTrace();
        }
    }
    public Car getCar(String ecuId) {
        String key = ecuId;

        try {
            String carJson = redisTemplate.opsForValue().get(key);
            if (carJson == null) {
                System.out.println("INFO: User not found in cache (key = " + key + ")");
                return null;
            }
            Car car = mapper.readValue(carJson, Car.class);

            System.out.println("INFO: Fetched car from cache using key \"" + key + "\": " + car);
            return car;
        } catch (JsonProcessingException e) {
            System.out.println("INFO: No car with ecuID = \"" + ecuId + "\" found in cache");
        } catch (Exception e) {
            System.out.println("ERROR: Error fetching car from cache");
            e.printStackTrace();
        }

        return null;
    }
}
