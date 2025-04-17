package ies.carbox.api.RestAPI.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ies.carbox.api.RestAPI.entity.Car;

import java.util.List;

/**
 * Repository interface for accessing and managing {@link Car} entities in the MongoDB database.
 *
 * <p>This interface extends {@link MongoRepository}, providing CRUD operations and custom query methods
 * for the {@link Car} entity, which represents car data within the application.</p>
 *
 * <p>The repository uses {@code Long} as the ID type for the {@link Car} entity.</p>
 */
@Repository
public interface CarRepository extends MongoRepository<Car, String> {
    Optional<Car> findByEcuId(String ecuId);
    List<Car> findByEcuIdIn(List<String> ecuIds); // para ir buscar varios carros com uma lista de ecuIds


}
