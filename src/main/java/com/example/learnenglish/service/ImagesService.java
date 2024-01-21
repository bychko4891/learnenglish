package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Image;
import com.example.learnenglish.repository.ImagesRepository;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service
@Data
public class ImagesService {

    private final ImagesRepository imagesRepository;


    public Page<Image> getImages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return imagesRepository.findAll(pageable, true);
    }


}