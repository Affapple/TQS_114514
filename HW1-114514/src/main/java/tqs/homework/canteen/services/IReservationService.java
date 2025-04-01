package tqs.homework.canteen.services;

import tqs.homework.canteen.DTOs.ReservationDTO;
import tqs.homework.canteen.DTOs.ReservationRequestDTO;

public interface IReservationService {
    public ReservationDTO createReservation(ReservationRequestDTO requestDTO);
    public ReservationDTO getReservationByCode(String code);
    public ReservationDTO cancelReservation(String code);
    public ReservationDTO checkInReservation(String code);
}

