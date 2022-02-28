package com.lecture.carrental.service;

import com.lecture.carrental.domain.Car;
import com.lecture.carrental.domain.FileDB;
import com.lecture.carrental.dto.CarDTO;
import com.lecture.carrental.exception.BadRequestException;
import com.lecture.carrental.exception.ResourceNotFoundException;
import com.lecture.carrental.repository.CarRepository;
import com.lecture.carrental.repository.FileDBRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;
    private final FileDBRepository fileDBRepository;
    private final static String IMAGE_NOT_FOUND_MSG = "image with id %s not found";
    private final static String CAR_NOT_FOUND_MSG = "car with id %d not found";

    public List<CarDTO> fetchAllCars() {
        return carRepository.findAllCar();
    }

    public CarDTO findById(Long id) throws ResourceNotFoundException {
        return carRepository.findByIdOrderById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(CAR_NOT_FOUND_MSG, id)));
    }

    public void add(Car car, String imageId) throws BadRequestException {
        FileDB fileDB = fileDBRepository.findById(imageId).orElseThrow(
                () -> new ResourceNotFoundException(String.format(IMAGE_NOT_FOUND_MSG, imageId)));

        Set<FileDB> fileDBs = new HashSet<>();
        fileDBs.add(fileDB);

        car.setImage(fileDBs);

        carRepository.save(car);
    }

    public void updateCar(Long id, String imageId, Car car) throws BadRequestException {
        FileDB fileDB = fileDBRepository.findById(imageId).get();

        Car carInfo = carRepository.getById(id);

        if (carInfo.getBuiltIn()) {
            throw new BadRequestException("You dont have permission to update car!");
        }

//        car.setBuiltIn(false);

        Set<FileDB> fileDBS = new HashSet<>();
        fileDBS.add(fileDB);

        car.setImage(fileDBS);

        car.setId(id);

        carRepository.save(car);
    }

    public void removeById(Long id) throws BadRequestException {
        Car car = carRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(CAR_NOT_FOUND_MSG, id)));

        if (car.getBuiltIn()) {
            throw new BadRequestException("You dont have permission to delete car!");
        }

        carRepository.deleteById(id);
    }
}
