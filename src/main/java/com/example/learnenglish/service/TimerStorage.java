package com.example.learnenglish.service;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class TimerStorage {
    private static final Map<String, Timer> timers = new HashMap<>();

    public static void addTimer(String timerId, Timer timer) {
        timers.put(timerId, timer);
    }

    public static Timer getTimer(String timerId) {
        return timers.get(timerId);
    }

    public static void removeTimer(String timerId) {
        timers.remove(timerId);
    }
}
