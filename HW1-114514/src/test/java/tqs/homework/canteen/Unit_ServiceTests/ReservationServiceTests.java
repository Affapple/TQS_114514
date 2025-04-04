package tqs.homework.canteen.Unit_ServiceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import tqs.homework.canteen.DTOs.ReservationRequestDTO;
import tqs.homework.canteen.EnumTypes.MealType;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.EnumTypes.ReservationStatus;
import tqs.homework.canteen.entities.Meal;
import tqs.homework.canteen.entities.Menu;
import tqs.homework.canteen.entities.Reservation;
import tqs.homework.canteen.entities.Restaurant;
import tqs.homework.canteen.repositories.MealRepository;
import tqs.homework.canteen.repositories.MenuRepository;
import tqs.homework.canteen.repositories.ReservationRepository;
import tqs.homework.canteen.repositories.RestaurantRepository;
import tqs.homework.canteen.services.ReservationService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReservationServiceTests {
  
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ReservationService reservationService;


    /* METHOD: createReservation */
    /**
     * Given an invalid mealId,
     * when the reservation is made
     * then an NoSuchElementException is thrown
     */
    @Test
    public void whenCreateReservationWithInvalidMealId_thenExceptionShouldBeThrown() {
        when(mealRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        ReservationRequestDTO testRequestDTO = new ReservationRequestDTO(1L);
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
    public void whenCreateReservationWithValidMealIdAndFreeCapacity_thenReservationShouldBeCreated() {
        Menu menu = new Menu(
            LocalDate.now(),
            MenuTime.LUNCH,
            new Restaurant("Test Restaurant", "Test Location", 10)
        ); 
        Meal meal = new Meal("Test Meal", MealType.FISH, menu);
        meal.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setMeal(meal);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setCode("12345678");

        when(
            reservationRepository
              .save(any(Reservation.class))
        )
        .thenReturn(reservation);
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));

        Reservation reservationMade = reservationService.createReservation(
            new ReservationRequestDTO(1L)
        );

        assertThat(
            reservationMade,
            is(notNullValue())
        );
        assertThat(
            reservationMade.getStatus(),
            is(ReservationStatus.ACTIVE)
        );
    }

    /**
     * Given a valid mealId and no free capacity,
     * when the meal is reserved
     * then an IllegalStateException is thrown.
     */
    @Test
    public void whenCreateReservationWithValidMealIdAndNoFreeCapacity_thenExceptionShouldBeThrown() {
        Menu menu = new Menu(
            LocalDate.now(),
            MenuTime.LUNCH,
            new Restaurant("Test Restaurant", "Test Location", 0)
        ); 
        Meal meal = new Meal("Test Meal", MealType.FISH, menu);
        meal.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setMeal(meal);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setCode("12345678");

        when(
            reservationRepository
              .save(any(Reservation.class))
        )
        .thenReturn(reservation);
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));

        assertThrows(IllegalStateException.class, () -> {
            reservationService.createReservation(
                new ReservationRequestDTO(1L)
            );
        });
    }

    /* METHOD: getReservationByCode */
    /**
     * Given an invalid code
     * when get reservation by code
     * then an NoSuchElementException is thrown
     */
    @Test
    public void whenGetReservationByInvalidCode_thenExceptionShouldBeThrown() {
        when(reservationRepository.findById("12345678")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.getReservationByCode("12345678");
        });
    }
    /**
     * Given a valid code
     * when get reservation by code
     * then a reservation is returned
     */
    @Test
    public void whenGetReservationByValidCode_thenReservationShouldBeReturned() {
        Reservation reservation = new Reservation();
        reservation.setCode("12345678");
        reservation.setStatus(ReservationStatus.ACTIVE);

        when(reservationRepository.findById("12345678")).thenReturn(Optional.of(reservation));

        Reservation reservationReturned = reservationService.getReservationByCode("12345678");

        assertThat(reservationReturned, is(reservation));
    }

    /* METHOD: cancelReservation */
    /**
     * Given an invalid code
     * when cancel reservation
     * then an NoSuchElementException is thrown
     */
    @Test
    public void whenCancelReservationWithInvalidCode_thenExceptionShouldBeThrown() {
        when(reservationRepository.findById("12345678")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.cancelReservation("12345678");   
        });
    }

    /**
     * Given a valid code and a reservation with status ACTIVE
     * when cancel reservation
     * then the reservation is cancelled and the status is set to CANCELLED
     */
    @Test
    public void whenCancelReservationWithValidCodeAndActiveReservation_thenReservationShouldBeCancelled() {
        Menu menu = new Menu(
            LocalDate.now(),
            MenuTime.LUNCH,
            new Restaurant("Test Restaurant", "Test Location", 10)
        ); 
        Meal meal = new Meal("Test Meal", MealType.FISH, menu);
        meal.setId(1L);
        menu.setOptions(List.of(meal));

        Reservation reservation = new Reservation();
        reservation.setCode("12345678");
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setMeal(meal);

        when(reservationRepository.findById("12345678")).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Reservation reservationCancelled = reservationService.cancelReservation("12345678");

        assertThat(reservationCancelled.getStatus(), is(ReservationStatus.CANCELLED));
    }
    /**
     * Given a valid code and a reservation with status USED
     * when cancel reservation
     * then an IllegalStateException is thrown
     */
    @Test
    public void whenCancelReservationWithValidCodeAndUsedReservation_thenExceptionShouldBeThrown() {
        Reservation reservation = new Reservation();
        reservation.setCode("12345678");
        reservation.setStatus(ReservationStatus.USED);

        when(reservationRepository.findById("12345678")).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> {
            reservationService.cancelReservation("12345678");
        });
    }
    /**
     * Given a valid code and a reservation with status CANCELLED   
     * when cancel reservation
     * then an IllegalStateException is thrown
     */
    @Test
    public void whenCancelReservationWithValidCodeAndCancelledReservation_thenExceptionShouldBeThrown() {
        Reservation reservation = new Reservation();
        reservation.setCode("12345678");
        reservation.setStatus(ReservationStatus.CANCELLED);

        when(reservationRepository.findById("12345678")).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> {
            reservationService.cancelReservation("12345678");
        });
    }

    /* METHOD: checkInReservation */
    /**
     * Given an invalid code
     * when check in reservation
     * then an NoSuchElementException is thrown
     */
    @Test
    public void whenCheckInReservationWithInvalidCode_thenExceptionShouldBeThrown() {
        when(reservationRepository.findById("12345678")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.checkInReservation("12345678");
        });
    }

    /**
     * Given a valid code and a reservation with status ACTIVE
     * when check in reservation
     * then the reservation is checked in and the status is set to USED
     */
    @Test
    public void whenCheckInReservationWithValidCodeAndActiveReservation_thenReservationShouldBeCheckedIn() {
        Reservation reservation = new Reservation();
        reservation.setCode("12345678");
        reservation.setStatus(ReservationStatus.ACTIVE);

        when(reservationRepository.findById("12345678")).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        
        Reservation reservationCheckedIn = reservationService.checkInReservation("12345678");
        assertThat(reservationCheckedIn.getStatus(), is(ReservationStatus.USED));
    }

    /**
     * Given a valid code and a reservation with status USED
     * when check in reservation
     * then an IllegalStateException is thrown
     */
    @Test
    public void whenCheckInReservationWithValidCodeAndUsedReservation_thenExceptionShouldBeThrown() {
        Reservation reservation = new Reservation();
        reservation.setCode("12345678");
        reservation.setStatus(ReservationStatus.USED);

        when(reservationRepository.findById("12345678")).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> {
            reservationService.checkInReservation("12345678");
        });
    }
    /**
     * Given a valid code and a reservation with status CANCELLED
     * when check in reservation
     * then an IllegalStateException is thrown
     */
    @Test
    void whenCheckInReservationWithValidCodeAndCancelledReservation_thenExceptionShouldBeThrown() {
        Reservation reservation = new Reservation();
        reservation.setCode("12345678");
        reservation.setStatus(ReservationStatus.CANCELLED);

        when(reservationRepository.findById("12345678")).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> {
            reservationService.checkInReservation("12345678");
        });
    }


    /* METHOD: getAllReservations */
    /*
     * Given restaurant doesnt exist
     * when getAllReservations is called
     * then no such element exception is thrown
     */
    @Test
    public void whenGetAllReservationsWithInvalidRestaurantId_thenExceptionShouldBeThrown() {
        when(restaurantRepository.existsById(1L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.getAllReservations(1L, LocalDate.now(), MenuTime.LUNCH);
        });
    }
    /**
     * Given restaurant exists
     * when getAllReservations is called with date and time
     * then a list of reservations of the restaurant at the given date and time is returned
     */
    @Test
    public void whenGetAllReservationsWithValidRestaurantIdAndDateAndTime_thenReservationsShouldBeReturned() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository
         .findAllByRestaurant_idAndDateAndTime(1L, LocalDate.now(), MenuTime.LUNCH))
         .thenReturn(List.of(new Reservation()));


        List<Reservation> reservations = reservationService.getAllReservations(
            1L, 
            LocalDate.now(), 
            MenuTime.LUNCH
        );

        assertThat(reservations, is(notNullValue()));
        assertThat(reservations.size(), is(1));
    }
    /**
     * Given restaurant exists
     * when getAllReservations is called with date and null
     * then a list of reservations of the restaurant at the given date is returned
     */
    @Test
    public void whenGetAllReservationsWithValidRestaurantIdAndDateAndNullTime_thenReservationsShouldBeReturned() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository
         .findAllByRestaurant_idAndDate(1L, LocalDate.now()))
         .thenReturn(List.of(new Reservation()));

        List<Reservation> reservations = reservationService.getAllReservations(
            1L, 
            LocalDate.now(), 
            null
        );

        assertThat(reservations, is(notNullValue()));
        assertThat(reservations.size(), is(1));
    }
    /**
     * Given restaurant exists
     * when getAllReservations is called with null and time
     * then a list of reservations of the restaurant at the given time is returned
     */
    @Test
    public void whenGetAllReservationsWithValidRestaurantIdAndNullDateAndTime_thenReservationsShouldBeReturned() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository
         .findAllByRestaurant_id(1L))
         .thenReturn(List.of(new Reservation()));

        List<Reservation> reservations = reservationService.getAllReservations(
            1L, 
            null, 
            null
        );

        assertThat(reservations, is(notNullValue()));
        assertThat(reservations.size(), is(1));
    }
    /**
     * Given restaurant exists
     * when getAllReservations is called with null and null
     * then a list of reservations of the restaurant is returned
     */
}