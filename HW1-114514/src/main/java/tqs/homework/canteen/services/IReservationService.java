package tqs.homework.canteen.services;

import tqs.homework.canteen.DTOs.ReservationRequestDTO;
import tqs.homework.canteen.entities.Reservation;

public interface IReservationService {
    public Reservation createReservation(ReservationRequestDTO requestDTO);
    /** ReservationRequestDTO: mealId
     * Given an invalid mealId,
     * when the reservation is made
     * then an NoSuchElementException is thrown
     *
     * Given a valid mealId and free capacity,
     * when the meal is reserved
     * then a reservation is created and returned.
     * 
     * Given a valid mealId and no free capacity,
     * when the meal is reserved
     * then an IllegalStateException is thrown.
     */

    public Reservation getReservationByCode(String code);
    /**
     * Given an invalid code
     * when get reservation by code
     * then an NoSuchElementException is thrown
     *
     * Given a valid code
     * when get reservation by code
     * then a reservation is returned
     */
    public Reservation cancelReservation(String code);
    /**
     * Given an invalid code
     * when cancel reservation
     * then an NoSuchElementException is thrown
     * 
     * Given a valid code and a reservation with status ACTIVE
     * when cancel reservation
     * then the reservation is cancelled and the status is set to CANCELLED
     * 
     * Given a valid code and a reservation with status USED
     * when cancel reservation
     * then an IllegalStateException is thrown
     * 
     * Given a valid code and a reservation with status CANCELLED   
     * when cancel reservation
     * then an IllegalStateException is thrown
     */

    public Reservation checkInReservation(String code);
    /**
     * Given an invalid code
     * when check in reservation
     * then an NoSuchElementException is thrown
     *
     * Given a valid code and a reservation with status ACTIVE
     * when check in reservation
     * then the reservation is checked in and the status is set to USED
     * 
     * Given a valid code and a reservation with status USED
     * when check in reservation
     * then an IllegalStateException is thrown
     * 
     * Given a valid code and a reservation with status CANCELLED
     * when check in reservation
     * then an IllegalStateException is thrown
     */
}

