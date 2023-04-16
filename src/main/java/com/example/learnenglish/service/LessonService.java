package com.example.learnenglish.service;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.repository.LessonRepository;
import org.springframework.stereotype.Service;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }
    public Lesson findById(Integer id) {
        return lessonRepository.findById(id).get();
    }
}
