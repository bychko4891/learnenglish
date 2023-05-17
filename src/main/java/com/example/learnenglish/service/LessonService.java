package com.example.learnenglish.service;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Lesson findById(long id) {
        return lessonRepository.findById(id).get();
    }
    public List<Lesson> lessonsListToAdminPage(){
        return (List<Lesson>) lessonRepository.findAll();
    }
    public void lessonSave(Lesson lesson){
        Optional<Lesson> optionalLesson = lessonRepository.findById(lesson.getId());
        if (!optionalLesson.isPresent()) {
            throw new IllegalArgumentException("Lesson with id " + lesson.getId() + " not found");
        }else {
            lessonRepository.save(lesson);
        }
    }
}
