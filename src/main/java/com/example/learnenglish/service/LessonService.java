package com.example.learnenglish.service;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.repository.LessonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

//    public List<Lesson> lessonsListToAdminPage() {
//        return (List<Lesson>) lessonRepository.findAll();
//    }
    public Page<Lesson> getLessonsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lessonRepository.findAll(pageable);
    }

    public void lessonEdit(Lesson lesson) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lesson.getId());
        if (!optionalLesson.isPresent()) {
            throw new IllegalArgumentException("Lesson with id " + lesson.getId() + " not found");
        } else {
            Optional<Lesson> lessonOptional = lessonRepository.findById(lesson.getId());
            if (lessonOptional.isPresent()) {
                Lesson lessonFromBase = lessonOptional.get();
                lessonFromBase.setName(lesson.getName());
                lessonFromBase.setLessonInfo(lesson.getLessonInfo());
                lessonRepository.save(lessonFromBase);
            }
        }
    }

    public boolean createLesson(Lesson lesson) {
        System.out.println(lesson.toString());
        lessonRepository.save(lesson);
        return true;
    }


}
