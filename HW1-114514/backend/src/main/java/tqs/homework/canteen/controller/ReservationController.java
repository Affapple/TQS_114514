package tqs.homework.canteen.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*")
public class ReservationController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(
        @RequestParam(required = true) Long restaurantId,
        @RequestParam(required = false) LocalDate date,
        @RequestParam(required = false) MenuTime time
    ) {
        logger.info("Received request to get all reservations for restaurantId={}, date={}, time={}", restaurantId, date, time);
        List<Reservation> reservations = reservationService.getAllReservations(restaurantId, date, time);
        logger.info("Found {} reservations", reservations.size());
        return new ResponseEntity<>(
            reservations,
            HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
        @RequestBody ReservationRequestDTO reservationRequestDTO
    ) {
        logger.info("Received request to create reservation: {}", reservationRequestDTO);
        Reservation reservation = reservationService.createReservation(reservationRequestDTO);
        logger.info("Reservation created successfully: {}", reservation);
        return new ResponseEntity<>(
            reservation,
            HttpStatus.CREATED
        );
    }


    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getReservation(
        @PathVariable String reservationId
    ) {
        logger.info("Received request to get reservation: {}", reservationId);
        Reservation reservation = reservationService.getReservationByCode(reservationId);
        logger.info("Reservation found: {}", reservation);
        return new ResponseEntity<>(
            reservation,
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Reservation> cancelReservation(
        @PathVariable String reservationId
    ) {
        logger.info("Received request to cancel reservation: {}", reservationId);
        Reservation reservation = reservationService.cancelReservation(reservationId);
        logger.info("Reservation cancelled successfully: {}", reservation);
        return new ResponseEntity<>(
            reservation,
            HttpStatus.OK
        );
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<Reservation> checkinReservation(
        @PathVariable String reservationId
    ) {
        logger.info("Received request to checkin reservation: {}", reservationId);
        Reservation reservation = reservationService.checkInReservation(reservationId);
        
        logger.info("Reservation checkedin successfully: {}", reservation);
        return new ResponseEntity<>(
            reservation,
            HttpStatus.OK
        );
    }
}
