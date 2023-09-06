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
        double totalPage = (int) session.getAttribute("totalPage") + 1.0;

        Timer timer = new Timer();
        String timerId = UUID.randomUUID().toString();
        double intervalInSecondsDouble = (totalPage / 3.0) * 60;
        int intervalInSeconds = (int) intervalInSecondsDouble * 1000;
        System.out.println(totalPage + " | " + intervalInSeconds);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                userWordLessonStatisticService.deleteWordLessonStatistic(userId, wordLessonId);
                TimerStorage.removeTimer(timerId);
                timer.cancel();
            }
        }, intervalInSeconds);

        TimerStorage.addTimer(timerId, timer);
        session.setAttribute("timerId", timerId);
    }

    @GetMapping("/stop-timer")
    public void stopTimer() {
        Long userId = (Long) session.getAttribute("userId");
        Long wordLessonId = (Long) session.getAttribute("wordLessonId");
        String timerId = (String) session.getAttribute("timerId");
        try {
            if (timerId != null) {
                userWordLessonStatisticService.deleteWordLessonStatistic(userId, wordLessonId);
                TimerStorage.removeTimer(timerId);
                Timer timer = TimerStorage.getTimer(timerId);
                timer.cancel();
                session.removeAttribute("timerId");
            }
        } catch (NullPointerException e) {
            session.removeAttribute("timerId");
        }
    }
}
