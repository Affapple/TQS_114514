package tqs.homework.canteen.services;

import java.time.LocalDate;
import java.util.List;

import tqs.homework.canteen.DTOs.ReservationRequestDTO;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.entities.Reservation;

public interface IReservationService {
    public List<Reservation> getAllReservations(Long restaurantId, LocalDate date, MenuTime time);
    public Reservation createReservation(ReservationRequestDTO requestDTO);
    public Reservation getReservationByCode(String code);
    public Reservation cancelReservation(String code);
    public Reservation checkInReservation(String code);
}