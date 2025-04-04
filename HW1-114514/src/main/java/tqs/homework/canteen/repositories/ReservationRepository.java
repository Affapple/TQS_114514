package tqs.homework.canteen.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    @Query("""
        SELECT reservation FROM Reservation reservation
        WHERE reservation.meal.menu.restaurant.id = :restaurantId
        AND reservation.meal.menu.date = :date
        AND reservation.meal.menu.time = :time
    """)
    List<Reservation> findAllByRestaurant_idAndDateAndTime(Long restaurantId, LocalDate date, MenuTime time);

    @Query("""
        SELECT reservation FROM Reservation reservation
        WHERE reservation.meal.menu.restaurant.id = :restaurantId
        AND reservation.meal.menu.date = :date
    """)
    List<Reservation> findAllByRestaurant_idAndDate(Long restaurantId, LocalDate date);

    @Query("""
        SELECT reservation FROM Reservation reservation
        WHERE reservation.meal.menu.restaurant.id = :restaurantId
    """)
    List<Reservation> findAllByRestaurant_id(Long restaurantId);
}
