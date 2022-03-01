package com.lecture.carrental.service;

import com.lecture.carrental.domain.Car;
import com.lecture.carrental.domain.Reservation;
import com.lecture.carrental.domain.enumeration.ReservationStatus;
import com.lecture.carrental.exception.BadRequestException;
import com.lecture.carrental.repository.CarRepository;
import com.lecture.carrental.repository.ReservationRepository;
import com.lecture.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class ReservationService {

//    @Autowired
    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final CarRepository carRepository;

//    public ReservationService(ReservationRepository reservationRepository) {
//        this.reservationRepository = reservationRepository;
//    }

    public void addReservation(Reservation reservation, Long userId, Car carId) throws BadRequestException {
        boolean checkStatus = carAvailability(carId.getId(), reservation.getPickUpTime(),
                reservation.getDropOffTime());

    }

    public boolean carAvailability(Long carId, LocalDateTime pickUpTime, LocalDateTime dropOffTime){
        List<Reservation> checkStatus = reservationRepository
                .checkStatus(carId, pickUpTime, dropOffTime,
                        ReservationStatus.DONE, ReservationStatus.CANCELED);

        return checkStatus.size() > 0;
    }


}
