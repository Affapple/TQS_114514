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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import tqs.homework.canteen.DTOs.ReservationRequestDTO;
import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.entities.Reservation;
import tqs.homework.canteen.services.ReservationService;

@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin(origins = "*")
@Tag(name = "Reservation", description = "Reservation management APIs")
public class ReservationController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    @Autowired
    private ReservationService reservationService;

    @Operation(summary = "Get all reservations", description = "Retrieves all reservations with optional filtering by restaurant, date, and time")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved reservations"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(
        @Parameter(description = "ID of the restaurant", required = true)
        @RequestParam(required = true) Long restaurantId,
        @Parameter(description = "Date to filter reservations (optional)")
        @RequestParam(required = false) LocalDate date,
        @Parameter(description = "Time to filter reservations (optional)")
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

    @Operation(summary = "Create reservation", description = "Creates a new reservation with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reservation created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Meal not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Reservation> createReservation(
        @Parameter(description = "Reservation details to create", required = true)
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

    @Operation(summary = "Get reservation by ID", description = "Retrieves a specific reservation by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved reservation"),
        @ApiResponse(responseCode = "404", description = "Reservation not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getReservation(
        @Parameter(description = "ID of the reservation to retrieve", required = true)
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

    @Operation(summary = "Cancel reservation", description = "Cancels a specific reservation by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservation cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Reservation not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Reservation> cancelReservation(
        @Parameter(description = "ID of the reservation to cancel", required = true)
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

    @Operation(summary = "Check-in reservation", description = "Marks a reservation as checked-in")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservation checked-in successfully"),
        @ApiResponse(responseCode = "404", description = "Reservation not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{reservationId}")
    public ResponseEntity<Reservation> checkinReservation(
        @Parameter(description = "ID of the reservation to check-in", required = true)
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
