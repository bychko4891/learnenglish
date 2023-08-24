package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.repository.LessonRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;

    }

    public Lesson getLesson(long id) {
        return lessonRepository.findById(id).get();
    }

    public Page<Lesson> getLessonsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lessonRepository.findAll(pageable);
    }

    public ResponseMessage saveLesson(Lesson lesson) {
        if (lesson.getId() != null) {
            Optional<Lesson> lessonOptional = lessonRepository.findById(lesson.getId());
            if (!lessonOptional.isPresent()) {
                lessonSave(lesson);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            } else {
                Lesson lessonFromBase = lessonOptional.get();
                lessonFromBase.setName(lesson.getName());
                lessonFromBase.setLessonInfo(lesson.getLessonInfo());
                lessonRepository.save(lessonFromBase);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            }
        } else {
            return new ResponseMessage(Message.ERROR_SERVER);
        }
    }

    public void lessonSave(Lesson lesson) {
        lessonRepository.save(lesson);
    }


    public Long countLessons() {
        return lessonRepository.count();
    }


}
