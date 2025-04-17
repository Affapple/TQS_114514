package ies.carbox.api.RestAPI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ies.carbox.api.RestAPI.entity.CarLiveInfo;

/**
 * Repository interface for accessing and managing {@link CarLiveInfo} entities in the MongoDB database.
 *
 * <p>This interface extends {@link MongoRepository}, providing CRUD operations and custom query methods
 * for the {@link CarLiveInfo} entity, which represents real-time information about cars.</p>
 *
 * <p>The repository uses {@link CarLiveInfoId} as the ID type for the {@link CarLiveInfo} entity.</p>
 */
@Repository
public interface CarLiveInfoRepository extends MongoRepository<CarLiveInfo, Long> {

    public Optional<List<CarLiveInfo>> findByCarId(String carId);
}
