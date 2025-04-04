package tqs.homework.canteen.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tqs.homework.canteen.DTOs.ReservationRequestDTO;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.entities.Reservation;
import tqs.homework.canteen.services.ReservationService;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(
        @RequestBody(required = true) Long restaurantId,
        @RequestBody(required = false) LocalDate date,
        @RequestBody(required = false) MenuTime time
    ) {
        return new ResponseEntity<>(
            reservationService.getAllReservations(restaurantId, date, time),
            HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
        @RequestBody ReservationRequestDTO reservationRequestDTO
    ) {
        return new ResponseEntity<>(
            reservationService.createReservation(reservationRequestDTO),
            HttpStatus.CREATED
        );
    }


    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getReservation(
        @PathVariable String reservationId
    ) {
        return new ResponseEntity<>(
            reservationService.getReservationByCode(reservationId),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Reservation> cancelReservation(
        @PathVariable String reservationId
    ) {
        return new ResponseEntity<>(
            reservationService.cancelReservation(reservationId),
            HttpStatus.OK
        );
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<Reservation> checkinReservation(
        @PathVariable String reservationId
    ) {
        return new ResponseEntity<>(
            reservationService.checkInReservation(reservationId),
            HttpStatus.OK
        );
    }
}