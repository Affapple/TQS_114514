package ies.carbox.api.RestAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.carbox.api.RestAPI.entity.TripInfo;
import ies.carbox.api.RestAPI.repository.TripInfoRepository;

/**
 * TripInfoService
 */
@Service
public class TripInfoService {
    TripInfoRepository tripInfoRepository;
    CacheService cacheService;

    @Autowired
    public TripInfoService(TripInfoRepository tripInfoRepository, CacheService cacheService) {
        this.tripInfoRepository = tripInfoRepository;
        this.cacheService = cacheService;
    }

    public List<TripInfo> getTripInfoByCarId(String carId) {
        List<TripInfo> trips;
        
        trips = tripInfoRepository.findByCarId(carId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                String.format("No trips found for car %s", carId)));

        cacheService.saveTrip(trips, carId);
        return trips;
    }

    public TripInfo getTripInfo(String tripId, String carId) throws IllegalArgumentException {
        List<TripInfo> trips = getTripInfoByCarId(carId);
        for (TripInfo trip : trips) {
            if (trip.getTripId().equals(tripId)) {
                return trip;
            }
        }
        throw new IllegalArgumentException(
                String.format("Trip %s not found for car %s", tripId, carId));
    }

    public TripInfo getLatestTripInfo(String carId) {
        // First, try cache.
        List<TripInfo> trips = cacheService.getCarTrips(carId);
        if (trips != null && !trips.isEmpty()) {
            System.out.println("Trips found in cache: " + trips);
    
            List<TripInfo> completedTrips = trips.stream()
                    .filter(trip -> trip.getTripEnd() != null)
                    .toList();
    
            if (!completedTrips.isEmpty()) {
                System.out.println("Returning last completed trip from cache: " + completedTrips.get(completedTrips.size() - 1));
                return completedTrips.get(completedTrips.size() - 1);
            }
        }
    
        // If not in cache, fetch from repository.
        trips = tripInfoRepository.findByCarId(carId)
                .orElseThrow(() -> new IllegalArgumentException("No trips found for carId: " + carId));
        
        List<TripInfo> completedTrips = trips.stream()
                .filter(trip -> trip.getTripEnd() != null)
                .toList();
    
        if (completedTrips.isEmpty()) {
            throw new IllegalArgumentException("No completed trips found for carId: " + carId);
        }
    

        cacheService.saveTrip(trips, carId);
        return completedTrips.get(completedTrips.size() - 1);
    }
}
