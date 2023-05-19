package com.example.learnenglish.model.users;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class TrainingTimeUsersEmbeddable {

    @Column(name = "training_time_start")
    private LocalDateTime trainingTimeStart;

    @Column(name = "training_time_end")
    private LocalDateTime trainingTimeEnd;

    public LocalDateTime getTrainingTimeStart() {
        return trainingTimeStart;
    }

    public void setTrainingTimeStart(LocalDateTime trainingTimeStart) {
        this.trainingTimeStart = trainingTimeStart;
    }

    public LocalDateTime getTrainingTimeEnd() {
        return trainingTimeEnd;
    }

    public void setTrainingTimeEnd(LocalDateTime trainingTimeEnd) {
        this.trainingTimeEnd = trainingTimeEnd;
    }
}
