package tqs.homework.canteen.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    List<Reservation> findAllByRestaurant_idAndDateAndTime(Long restaurantId, LocalDate date, MenuTime time);
    List<Reservation> findAllByRestaurant_idAndDate(Long restaurantId, LocalDate date);
    List<Reservation> findAllByRestaurant_id(Long restaurantId);
}
