package com.lecture.carrental.repository;

import com.lecture.carrental.domain.Reservation;
import com.lecture.carrental.domain.User;
import com.lecture.carrental.domain.enumeration.ReservationStatus;
import com.lecture.carrental.dto.ReservationDTO;
import com.lecture.carrental.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

//    @Query("SELECT new com.lecture.carrental.dto.ReservationDTO(r) FROM Reservation r")
    List<ReservationDTO> findAllBy();

    Optional<ReservationDTO> findByIdOrderById(Long id) throws ResourceNotFoundException;

//    @Query("SELECT new com.lecture.carrental.dto.ReservationDTO(r) FROM Reservation r WHERE r.userId = ?1")
    List<ReservationDTO> findAllByUserId(User userId);

//    @Query("SELECT new com.lecture.carrental.dto.ReservationDTO(r) FROM Reservation r WHERE r.id = ?1 and r.userId = ?2")
    Optional<ReservationDTO> findByIdAndUserId(Long id, User user) throws ResourceNotFoundException;

    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN fetch r.carId cd " +
            "LEFT JOIN fetch cd.image img " +
            "LEFT JOIN fetch r.userId uid " +
            "WHERE (cd.id = ?1 and r.status <> ?4 and r.status <> ?5 and ?2 BETWEEN r.pickUpTime and r.dropOffTime) or " +
            "(cd.id = ?1 and r.status <> ?4 and r.status <> ?5 and ?3 BETWEEN r.pickUpTime and r.dropOffTime)")
    List<Reservation> checkStatus(Long carId, LocalDateTime pickUpTime, LocalDateTime dropOffTime,
                                  ReservationStatus done, ReservationStatus canceled);
}
