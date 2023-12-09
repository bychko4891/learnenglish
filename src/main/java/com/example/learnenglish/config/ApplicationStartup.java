package com.example.learnenglish.config;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup {
    private final JdbcTemplate jdbcTemplate;

    public ApplicationStartup(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @EventListener(ApplicationReadyEvent.class)
    public void clearUserSessions() {
//        jdbcTemplate.update("DELETE FROM spring_session");
//        jdbcTemplate.update("DELETE FROM spring_session_attributes");
    }
}