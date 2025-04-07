package tqs.homework.canteen.Unit_ControllerTests;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tqs.homework.canteen.DTOs.ReservationRequestDTO;
import tqs.homework.canteen.EnumTypes.ReservationStatus;
import tqs.homework.canteen.controller.ReservationController;
import tqs.homework.canteen.entities.Reservation;
import tqs.homework.canteen.services.ReservationService;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTests {
    @Autowired
    private MockMvc mvc;
    
    @MockitoBean
    private ReservationService reservationService;
    
    /* GET: /api/v1/reservations */
    /**
     * Given an invalid code
     * when get reservation by code
     * then an NoSuchElementException is thrown
     */
    @Test
    public void testGetReservationByCode_InvalidCode() throws Exception {
        when(reservationService.getReservationByCode("invalidCode")).thenThrow(new NoSuchElementException());

        mvc.perform(get("/api/v1/reservations/invalidCode"))
            .andExpect(status().isNotFound());
    }

    /**
     * Given a valid code
     * when get reservation by code
     * then a reservation is returned
     */
    @Test
    public void testGetReservationByCode_ValidCode() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setCode("validCode");
        when(reservationService.getReservationByCode("validCode")).thenReturn(reservation);

        mvc.perform(get("/api/v1/reservations/validCode"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("validCode"));
    }

    /* POST: /api/v1/reservations */
    /**
     * Given an invalid mealId,
     * when the reservation is made
     * then an NoSuchElementException is thrown
     */
    @Test
    public void testCreateReservation_InvalidMealId() throws Exception {
        when(reservationService.createReservation(Mockito.any(ReservationRequestDTO.class)))
        .thenThrow(new NoSuchElementException());

        mvc.perform(
            post("/api/v1/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"mealId\": 1}")
        )
            .andExpect(status().isNotFound());
    }

    /**
     *  Given a valid mealId and free capacity,
     * when the meal is reserved
     * then a reservation is created and returned.
     */
    @Test
    public void testCreateReservation_ValidMealId() throws Exception {
        ReservationRequestDTO requestDTO = new ReservationRequestDTO();
        requestDTO.setMealId(1L);

        Reservation reservation = new Reservation();
        reservation.setCode("12345678");
        when(reservationService.createReservation(Mockito.any(ReservationRequestDTO.class)))
        .thenReturn(reservation);
        
        mvc.perform(
            post("/api/v1/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"mealId\": 1}")
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("12345678"));
    }

    /**
     * Given a valid mealId and no free capacity,
     * when the meal is reserved
     * then an IllegalArgumentException is thrown.
     */
    @Test
    public void testCreateReservation_NoFreeCapacity() throws Exception {
        when(reservationService.createReservation(Mockito.any(ReservationRequestDTO.class)))
        .thenThrow(new IllegalArgumentException());

        mvc.perform(
            post("/api/v1/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"mealId\": 1}")
        )
            .andExpect(status().isBadRequest());
    }

    /* DELETE: /api/v1/reservations/{reservation_id} */
    /**
     * Given an invalid code
     * when cancel reservation
     * then an NoSuchElementException is thrown
     */
    @Test
    public void testCancelReservation_InvalidCode() throws Exception {
        when(reservationService.cancelReservation("invalidCode")).thenThrow(new NoSuchElementException());

        mvc.perform(delete("/api/v1/reservations/invalidCode"))
            .andExpect(status().isNotFound());
    }
    /**
     * Given a valid code and a reservation with status ACTIVE
     * when cancel reservation
     * then the reservation is cancelled and the status is set to CANCELLED
     */
    @Test
    public void testCancelReservation_ValidCode() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.CANCELLED);
        when(reservationService.cancelReservation("validCode")).thenReturn(reservation);

        mvc.perform(delete("/api/v1/reservations/validCode"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    /**
     * Given a valid code and a reservation with status USED
     * when cancel reservation
     * then an IllegalArgumentException is thrown
     */
    @Test
    public void testCancelReservation_UsedStatus() throws Exception {
        when(reservationService.cancelReservation("usedCode")).thenThrow(new IllegalArgumentException());

        mvc.perform(delete("/api/v1/reservations/usedCode"))
            .andExpect(status().isBadRequest());
    }

    /**
     * Given a valid code and a reservation with status CANCELLED   
     * when cancel reservation
     * then an IllegalArgumentException is thrown
     */
    @Test
    public void testCancelReservation_CancelledStatus() throws Exception {
        when(reservationService.cancelReservation("cancelledCode")).thenThrow(new IllegalArgumentException());

        mvc.perform(delete("/api/v1/reservations/cancelledCode"))
            .andExpect(status().isBadRequest());
    }


     /* PUT: /api/v1/reservations/{reservation_id} */
    /**
     * Given an invalid code
     * when check in reservation
     * then an NoSuchElementException is thrown
     */
    @Test
    public void testCheckInReservation_InvalidCode() throws Exception {
        when(reservationService.checkInReservation("invalidCode")).thenThrow(new NoSuchElementException());

        mvc.perform(put("/api/v1/reservations/invalidCode"))
            .andExpect(status().isNotFound());
    }

    /**
     * Given a valid code and a reservation with status ACTIVE
     * when check in reservation
     * then the reservation is checked in and the status is set to USED
     */
    @Test
    public void testCheckInReservation_ValidCode() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.USED);
        when(reservationService.checkInReservation("validCode")).thenReturn(reservation);

        mvc.perform(put("/api/v1/reservations/validCode"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("USED"));
    }

    /**
     * Given a valid code and a reservation with status USED
     * when check in reservation
     * then an IllegalArgumentException is thrown
     */
    @Test
    public void testCheckInReservation_UsedStatus() throws Exception {
        when(reservationService.checkInReservation("usedCode")).thenThrow(new IllegalArgumentException());

        mvc.perform(put("/api/v1/reservations/usedCode"))
            .andExpect(status().isBadRequest());
    }

    /**
     * Given a valid code and a reservation with status CANCELLED
     * when check in reservation
     * then an IllegalArgumentException is thrown
     */
    @Test
    public void testCheckInReservation_CancelledStatus() throws Exception {
        when(reservationService.checkInReservation("cancelledCode")).thenThrow(new IllegalArgumentException());

        mvc.perform(put("/api/v1/reservations/cancelledCode"))
            .andExpect(status().isBadRequest());
    }
}
