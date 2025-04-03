package tqs.homework.canteen.serviceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import tqs.homework.canteen.DTOs.ReservationRequestDTO;
import tqs.homework.canteen.EnumTypes.ReservationStatus;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Reservation;
import tqs.homework.canteen.repositories.MealRepository;
import tqs.homework.canteen.repositories.ReservationRepository;
import tqs.homework.canteen.services.ReservationService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReservationServiceTests {
  
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Meal testMeal;
    private Reservation testReservation;
    private ReservationRequestDTO testRequestDTO;

    /* METHOD: createReservation */
    /**
     * Given an invalid mealId,
     * when the reservation is made
     * then an NoSuchElementException is thrown
     */
    @Test
    void whenCreateReservationWithInvalidMealId_thenExceptionShouldBeThrown() {
        when(mealRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.createReservation(testRequestDTO);
        });
    }

    /**
     * Given a valid mealId and free capacity,
     * when the meal is reserved
     * then a reservation is created and returned.
     */ 
    @Test
    void whenCreateReservationWithValidMealIdAndFreeCapacity_thenReservationShouldBeCreated() {
        when(mealRepository.findById(1L)).thenReturn(java.util.Optional.of(testMeal));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);   
        
        Reservation reservation = reservationService.createReservation(testRequestDTO);
        assertEquals(testReservation, reservation);
    }

    /**
     * Given a valid mealId and no free capacity,
     * when the meal is reserved
     * then an IllegalStateException is thrown.
     */

    /* METHOD: getReservationByCode */
    /**
     * Given an invalid code
     * when get reservation by code
     * then an NoSuchElementException is thrown
     */
    /**
     * Given a valid code
     * when get reservation by code
     * then a reservation is returned
     */

    /* METHOD: cancelReservation */
    /**
     * Given an invalid code
     * when cancel reservation
     * then an NoSuchElementException is thrown
     */
    /**
     * Given a valid code and a reservation with status ACTIVE
     * when cancel reservation
     * then the reservation is cancelled and the status is set to CANCELLED
     */
    /**
     * Given a valid code and a reservation with status USED
     * when cancel reservation
     * then an IllegalStateException is thrown
     */
    /**
     * Given a valid code and a reservation with status CANCELLED   
     * when cancel reservation
     * then an IllegalStateException is thrown
     */

    /* METHOD: checkInReservation */
    /**
     * Given an invalid code
     * when check in reservation
     * then an NoSuchElementException is thrown
     */
    /**
     * Given a valid code and a reservation with status ACTIVE
     * when check in reservation
     * then the reservation is checked in and the status is set to USED
     */
    /**
     * Given a valid code and a reservation with status USED
     * when check in reservation
     * then an IllegalStateException is thrown
     */
    /**
     * Given a valid code and a reservation with status CANCELLED
     * when check in reservation
     * then an IllegalStateException is thrown
     */
}