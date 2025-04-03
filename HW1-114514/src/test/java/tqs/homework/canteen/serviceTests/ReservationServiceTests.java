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

    @BeforeEach
    public void setup() {
        testMeal = new Meal();
        testMeal.setId(1L);
        testMeal.setDescription("Test Meal");

        testReservation = new Reservation();
        testReservation.setCode("TEST1234");
        testReservation.setMeal(testMeal);
        testReservation.setStatus(ReservationStatus.ACTIVE);

        testRequestDTO = new ReservationRequestDTO();
        testRequestDTO.setMealId(1L);
    }

    @Test
    void whenCreateReservation_thenReservationShouldBeCreated() {
        when(mealRepository.findById(1L)).thenReturn(java.util.Optional.of(testMeal));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation createdReservation = reservationService.createReservation(testRequestDTO);

        assertNotNull(createdReservation);
        assertNotNull(createdReservation.getCode());
        assertEquals(8, createdReservation.getCode().length());
        assertEquals(ReservationStatus.ACTIVE, createdReservation.getStatus());
        assertEquals(testMeal, createdReservation.getMeal());
        verify(mealRepository).findById(1L);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void whenCreateReservationWithInvalidMealId_thenExceptionShouldBeThrown() {
        when(mealRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.createReservation(testRequestDTO);
        });
        verify(mealRepository).findById(1L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void whenGetReservationByCode_thenReservationShouldBeReturned() {
        when(reservationRepository.findById("TEST1234")).thenReturn(java.util.Optional.of(testReservation));

        Reservation foundReservation = reservationService.getReservationByCode("TEST1234");

        assertNotNull(foundReservation);
        assertEquals("TEST1234", foundReservation.getCode());
        assertEquals(testMeal, foundReservation.getMeal());
        assertEquals(ReservationStatus.ACTIVE, foundReservation.getStatus());
        verify(reservationRepository).findById("TEST1234");
    }

    @Test
    void whenGetReservationWithInvalidCode_thenExceptionShouldBeThrown() {
        when(reservationRepository.findById("INVALID")).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.getReservationByCode("INVALID");
        });
        verify(reservationRepository).findById("INVALID");
        verify(mealRepository, never()).findById(any());
    }

    @Test
    void whenCancelReservation_thenReservationShouldBeCancelled() {
        when(reservationRepository.findById("TEST1234")).thenReturn(java.util.Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation cancelledReservation = reservationService.cancelReservation("TEST1234");

        assertNotNull(cancelledReservation);
        assertEquals(ReservationStatus.CANCELLED, cancelledReservation.getStatus());
        verify(reservationRepository).findById("TEST1234");
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void whenCheckInReservation_thenReservationShouldBeCheckedIn() {
        when(reservationRepository.findById("TEST1234")).thenReturn(java.util.Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation checkedInReservation = reservationService.checkInReservation("TEST1234");

        assertNotNull(checkedInReservation);
        assertEquals(ReservationStatus.USED, checkedInReservation.getStatus());
        verify(reservationRepository).findById("TEST1234");
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void whenCheckInReservationWithInvalidStatus_thenExceptionShouldBeThrown() {
        when(reservationRepository.findById("TEST1234")).thenReturn(java.util.Optional.of(testReservation));

        assertThrows(IllegalStateException.class, () -> {
            reservationService.checkInReservation("TEST1234");
        });
        verify(reservationRepository).findById("TEST1234");
    }

    @Test
    void whenCreateReservationWithFullRestaurant_thenExceptionShouldBeThrown() {
        when(mealRepository.findById(1L)).thenReturn(java.util.Optional.of(testMeal));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        assertThrows(IllegalStateException.class, () -> {
            reservationService.createReservation(testRequestDTO);
        });
    }

    @Test
    void whenCancelInvalidReservation_thenExceptionShouldBeThrown() {
        when(reservationRepository.findById("INVALID")).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.cancelReservation("INVALID");
        });

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.checkInReservation("INVALID");
        });
    }
}