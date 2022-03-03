package com.lecture.carrental.controller;

import com.lecture.carrental.domain.Car;
import com.lecture.carrental.domain.Reservation;
import com.lecture.carrental.dto.ReservationDTO;
import com.lecture.carrental.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/reservations")
public class ReservationController {

    public ReservationService reservationService;

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.fetchAllReservations();

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }


    @GetMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        ReservationDTO reservation = reservationService.findById(id);

        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping("/admin/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getAllUserReservations(@RequestParam(value = "userId") Long userId) {
        List<ReservationDTO> reservations = reservationService.findAllByUserId(userId);

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> getUserReservationById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("id");

        ReservationDTO reservation = reservationService.findByIdAndUserId(id, userId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getUserReservationsByUserId(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("id");

        List<ReservationDTO> reservations = reservationService.findAllByUserId(userId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> makeReservation(HttpServletRequest request,
                                                                @RequestParam(value = "carId") Car carId,
                                                                @Valid @RequestBody Reservation reservation) {

        Long userId = (Long) request.getAttribute("id");
        reservationService.addReservation(reservation, userId, carId);

        Map<String, Boolean> map = new HashMap<>();
        map.put("Reservation added successfully!", true);

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/add/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> addReservation(@RequestParam (value = "userId") Long userId,
                                                               @RequestParam (value = "carId") Car carId,
                                                               @Valid @RequestBody Reservation reservation) {
        reservationService.addReservation(reservation, userId, carId);

        Map<String, Boolean> map = new HashMap<>();
        map.put("Reservation added successfully!", true);

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PutMapping("/admin/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> updateReservation(@RequestParam(value = "carId") Car carId,
                                                                  @RequestParam(value = "reservationId")
                                                                          Long reservationId,
                                                                  @Valid @RequestBody Reservation reservation) {

        reservationService.updateReservation(carId, reservationId, reservation);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteReservation(@PathVariable Long id) {

        reservationService.removeById(id);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/auth")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> checkCarAvailability (@RequestParam(value = "carId") Long carId,
                                                                     @RequestParam(value = "pickUpDateTime")
                                                                             @DateTimeFormat(pattern = "MM/dd/yyyy " +
                                                                                     "HH:mm:ss")
                                                                             LocalDateTime pickUptime,
                                                                     @RequestParam(value = "dropOffDateTime")
                                                                         @DateTimeFormat(pattern = "MM/dd/yyyy " +
                                                                                 "HH:mm:ss")
                                                                                 LocalDateTime dropOffTime) {

        boolean availability = reservationService.carAvailability(carId, pickUptime, dropOffTime);
        Double totalPrice = reservationService.totalPrice(pickUptime, dropOffTime, carId);

        Map<String, Object> map = new HashMap<>();
        map.put("isAvailable", !availability);
        map.put("totalPrice", totalPrice);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}
