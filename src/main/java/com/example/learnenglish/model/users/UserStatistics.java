package com.example.learnenglish.model.users;

import jakarta.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Columns;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user_statistics")

public class UserStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(name = "last_visit")
    private LocalDateTime lastVisit;
    @Column(name = "count_pair")
    private Long countDownloadPair;
    @Column(name = "training_time")
    private Long trainingTime;
    @ElementCollection
    @CollectionTable(name = "training_time_two_week",
            joinColumns = @JoinColumn(name = "user_statistics_id"))
    @OrderColumn
    private List<TrainingTimeUsersEmbeddable> trainingTimeTwoWeek = new ArrayList<>();
    @Column(name = "repetitions_count")
    private Long repetitionsCount;
    @Column(name = "repetitions_count_prev")
    private Long repetitionsCountPrev;
    @Column(name = "days_count")
    private Long daysInARowCount;
    @ElementCollection
    @CollectionTable(name = "training_days_mount",
            joinColumns = @JoinColumn(name = "user_statistics_id"))
    @Column(name = "training_day")
    private List<LocalDate> trainingDaysInMonth = new ArrayList<>();
    @Column(name = "error_count")
    private Long errorCount;

    @OneToOne(mappedBy = "statistics",
            cascade = CascadeType.ALL)
    private User user;

    public UserStatistics() {
    }
}
