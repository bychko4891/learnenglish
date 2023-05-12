package com.example.learnenglish.service;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.LessonRepository;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
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
        lessonRepository.save(lesson);
    }
//    public void lessonEdit(Long lessonId){
//        Optional<Lesson> optionalUser = lessonRepository.findById(lessonId);
//        if (optionalUser.isPresent()) {
//            Lesson lesson = optionalUser.get();
//        lesson.setLessonInfo();
//            userRepository.save(user);
////            return userRepository.save(user);
//        } else {
//            throw new IllegalArgumentException("User with id " + userId + " not found");
//        }
//        lessonRepository.save(lesson);
//    }
}
