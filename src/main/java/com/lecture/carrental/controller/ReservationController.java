package com.lecture.carrental.controller;

import com.lecture.carrental.domain.Car;
import com.lecture.carrental.domain.Reservation;
import com.lecture.carrental.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/reservations")
public class ReservationController {

    public ReservationService reservationService;

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
}
