package tqs.homework.canteen.services;

import tqs.homework.canteen.DTOs.ReservationRequestDTO;
import tqs.homework.canteen.entities.Reservation;

public interface IReservationService {
    public Reservation createReservation(ReservationRequestDTO requestDTO);
    public Reservation getReservationByCode(String code);
    public Reservation cancelReservation(String code);
    public Reservation checkInReservation(String code);
}

