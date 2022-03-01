package com.lecture.carrental.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lecture.carrental.domain.enumeration.ReservationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car carId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss", timezone = "Turkey")
    @NotNull(message = "Please enter the pick up time of the reservation")
    @Column(nullable = false)
    private LocalDateTime pickUpTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss", timezone = "Turkey")
    @NotNull(message = "Please enter the drop off time of the reservation")
    @Column(nullable = false)
    private LocalDateTime dropOffTime;

    @Size(max = 150)
    @NotNull(message = "Please enter the pick up location of the reservation")
    @Column(length = 150, nullable = false)
    private String pickUpLocation;

    @Size(max = 150)
    @NotNull(message = "Please enter the drop off location of the reservation")
    @Column(length = 150, nullable = false)
    private String dropOffLocation;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private Double totalPrice;


    public Long getTotalHours(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {

        return ChronoUnit.HOURS.between(pickUpTime, dropOffTime);
    }

}
