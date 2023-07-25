package com.example.learnenglish.service;

import com.example.learnenglish.repository.WordLessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordLessonService {

    private final WordLessonRepository wordLessonRepository;

}
