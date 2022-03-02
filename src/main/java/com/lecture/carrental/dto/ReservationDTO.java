package com.lecture.carrental.dto;

import com.lecture.carrental.domain.Reservation;
import com.lecture.carrental.domain.enumeration.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class ReservationDTO {
    
    private Long id;
    
    private CarDTO carId;
    
    private Long userId;
    
    private LocalDateTime pickUpTime;
    
    private LocalDateTime dropOffTime;
    
    private String pickUpLocation;
    
    private String dropOffLocation;
    
    private ReservationStatus status;
    
    private Double totalPrice;

    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.carId = new CarDTO(reservation.getCarId());
        this.userId = reservation.getUserId().getId();
        this.pickUpTime = reservation.getPickUpTime();
        this.dropOffTime = reservation.getDropOffTime();
        this.pickUpLocation = reservation.getPickUpLocation();
        this.dropOffLocation = reservation.getDropOffLocation();
        this.status = reservation.getStatus();
        this.totalPrice = reservation.getTotalPrice();
    }
}
