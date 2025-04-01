package tqs.homework.canteen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.homework.canteen.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
}
