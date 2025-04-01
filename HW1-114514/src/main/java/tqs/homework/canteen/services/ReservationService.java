package tqs.homework.canteen.services;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.homework.canteen.DTOs.ReservationDTO;
import tqs.homework.canteen.DTOs.ReservationRequestDTO;
import tqs.homework.canteen.EnumTypes.ReservationStatus;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Reservation;
import tqs.homework.canteen.repositories.MealRepository;
import tqs.homework.canteen.repositories.ReservationRepository;

@Service
public class ReservationService implements IReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MealRepository mealRepository;


    public ReservationDTO createReservation(ReservationRequestDTO requestDTO) {
        Meal meal = mealRepository
            .findById(requestDTO.getMealId())
            .orElseThrow(
                () -> new NoSuchElementException("Meal with id \"" + requestDTO.getMealId() + "\" not found!")
            );

        Reservation reservation = new Reservation();
        reservation.setMeal(meal);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.generateCode();

        Reservation savedReservation = reservationRepository.save(reservation);
        //log.info("Created new reservation with code: {}", savedReservation.getCode());

        return savedReservation.asDTO();
    }

    public ReservationDTO getReservationByCode(String code) {
        Reservation reservation = reservationRepository
            .findById(code)
            .orElseThrow(
                () -> new NoSuchElementException("Reservation with code \""+code+"\" not found!")
            );

        return reservation.asDTO();
    }

    public ReservationDTO cancelReservation(String code) {
        Reservation reservation = reservationRepository
            .findById(code)
            .orElseThrow(
                () -> new NoSuchElementException("Reservation with code \""+code+"\" not found!")
            );

        if (reservation.getStatus() == ReservationStatus.USED) {
            throw new IllegalStateException("Cannot cancel a used reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation updatedReservation = reservationRepository.save(reservation);
        // log.info("Canceled reservation with code: {}", code);

        return updatedReservation.asDTO();
    }

    public ReservationDTO checkInReservation(String code) {
        Reservation reservation = reservationRepository
            .findById(code)
            .orElseThrow(
                () -> new NoSuchElementException("Reservation with code \""+code+"\" not found!")
            );

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Reservation is " + reservation.getStatus().getName());
        }

        reservation.setStatus(ReservationStatus.USED);
        Reservation updatedReservation = reservationRepository.save(reservation);
        //log.info("Checked in reservation with code: {}", code);

        return updatedReservation.asDTO();
    }
}
