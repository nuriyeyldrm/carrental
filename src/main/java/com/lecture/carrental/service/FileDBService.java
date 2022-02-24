package com.lecture.carrental.service;

import com.lecture.carrental.domain.FileDB;
import com.lecture.carrental.repository.FileDBRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FileDBService {

    private final FileDBRepository fileDBRepository;

    public FileDB getFileById(String id) {
        return fileDBRepository.findById(id).get();
    }


}
