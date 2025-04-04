package tqs.homework.canteen.services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.homework.canteen.DTOs.ReservationRequestDTO;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.EnumTypes.ReservationStatus;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.entities.Reservation;
import tqs.homework.canteen.repositories.MealRepository;
import tqs.homework.canteen.repositories.MenuRepository;
import tqs.homework.canteen.repositories.ReservationRepository;
import tqs.homework.canteen.repositories.RestaurantRepository;

@Service
public class ReservationService implements IReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private MenuRepository menuRepository;


    public Reservation createReservation(ReservationRequestDTO requestDTO) {
        Meal meal = mealRepository
            .findById(requestDTO.getMealId())
            .orElseThrow(
                () -> new NoSuchElementException("Meal with id \"" + requestDTO.getMealId() + "\" not found!")
            );
    
        Menu menu = meal.getMenu();
        if (menu.getCapacity() <= 0) {
            throw new IllegalStateException("Cannot create reservation. Restaurant is full!");
        }

        menu.setCapacity(menu.getCapacity() - 1);
        menuRepository.save(menu);

        Reservation reservation = new Reservation();
        reservation.setMeal(meal);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.generateCode();

        Reservation savedReservation = reservationRepository.save(reservation);
        //log.info("Created new reservation with code: {}", savedReservation.getCode());

        return savedReservation;
    }

    public Reservation getReservationByCode(String code) {
        Reservation reservation = reservationRepository
            .findById(code)
            .orElseThrow(
                () -> new NoSuchElementException("Reservation with code \""+code+"\" not found!")
            );

        return reservation;
    }

    public Reservation cancelReservation(String code) {
        Reservation reservation = reservationRepository
            .findById(code)
            .orElseThrow(
                () -> new NoSuchElementException("Reservation with code \""+code+"\" not found!")
            );

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Reservation with code \""+code+"\" cannot be cancelled! Only active reservations can be cancelled.");
        }

        Menu menu = reservation.getMeal().getMenu();
        menu.setCapacity(menu.getCapacity() + 1);
        menuRepository.save(menu);

        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation updatedReservation = reservationRepository.save(reservation);
        // log.info("Canceled reservation with code: {}", code);

        return updatedReservation;
    }

    public Reservation checkInReservation(String code) {
        Reservation reservation = reservationRepository
            .findById(code)
            .orElseThrow(
                () -> new NoSuchElementException("Reservation with code \""+code+"\" not found!")
            );

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Cannot check-in. Reservation is " + reservation.getStatus().getName());
        }

        reservation.setStatus(ReservationStatus.USED);
        Reservation updatedReservation = reservationRepository.save(reservation);
        //log.info("Checked in reservation with code: {}", code);

        return updatedReservation;
    }

    @Override
    public List<Reservation> getAllReservations(Long restaurantId, LocalDate date, MenuTime time) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new NoSuchElementException("Restaurant with id \"" + restaurantId + "\" not found!");
        }
        
        if(date == null) {
            return reservationRepository.findAllByRestaurant_id(restaurantId);
        }

        if(time == null) {
            return reservationRepository.findAllByRestaurant_idAndDate(restaurantId, date);
        }

        return reservationRepository.findAllByRestaurant_idAndDateAndTime(restaurantId, date, time);
    }
}
