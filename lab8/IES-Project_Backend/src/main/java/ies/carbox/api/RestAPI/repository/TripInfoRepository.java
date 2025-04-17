package ies.carbox.api.RestAPI.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ies.carbox.api.RestAPI.entity.TripInfo;

/**
 * TripInfoRepository
 */
@Repository
public interface TripInfoRepository extends MongoRepository<TripInfo, String> {
    Optional<List<TripInfo>> findByCarId(String carId);
    Optional<TripInfo> findByCarIdAndTripId(String carId, String tripId);
}
