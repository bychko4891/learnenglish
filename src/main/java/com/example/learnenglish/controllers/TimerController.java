package com.example.learnenglish.controllers;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.service.TimerStorage;
import com.example.learnenglish.service.UserWordLessonStatisticService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class TimerController {
    private final HttpSession session;
    private final UserWordLessonStatisticService userWordLessonStatisticService;


    @GetMapping("/start-timer")
    public void startTimer() {
        Long userId = (Long) session.getAttribute("userId");
        Long wordLessonId = (Long) session.getAttribute("wordLessonId");
        int totalPage = (int) session.getAttribute("totalPage");

        Timer timer = new Timer();
        String timerId = UUID.randomUUID().toString();
        int intervalInSeconds = (totalPage / 3 + 1) * 60;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                userWordLessonStatisticService.deleteWordLessonStatistic(userId, wordLessonId);
                TimerStorage.removeTimer(timerId);
                session.removeAttribute("timerId");
                System.out.println("Таймер завершено!");
            }
        }, intervalInSeconds * 1000);

        TimerStorage.addTimer(timerId, timer);
        session.setAttribute("timerId", timerId);
    }

    @GetMapping("/stop-timer")
    public void stopTimer() {
        Long userId = (Long) session.getAttribute("userId");
        Long wordLessonId = (Long) session.getAttribute("wordLessonId");
        String timerId = (String) session.getAttribute("timerId");

        if (timerId != null) {
            Timer timer = TimerStorage.getTimer(timerId);
            timer.cancel();
            userWordLessonStatisticService.deleteWordLessonStatistic(userId, wordLessonId);
            TimerStorage.removeTimer(timerId);
            session.removeAttribute("timerId");
        }
    }
}
