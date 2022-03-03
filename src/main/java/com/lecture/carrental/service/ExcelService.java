package com.lecture.carrental.service;

import com.lecture.carrental.domain.User;
import com.lecture.carrental.helper.ExcelHelper;
import com.lecture.carrental.repository.CarRepository;
import com.lecture.carrental.repository.ReservationRepository;
import com.lecture.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class ExcelService {

    UserRepository userRepository;
    CarRepository carRepository;
    ReservationRepository reservationRepository;

    public ByteArrayInputStream loadUser() throws IOException {
        List<User> users = userRepository.findAll();

        return ExcelHelper.usersExcel(users);
    }
}
