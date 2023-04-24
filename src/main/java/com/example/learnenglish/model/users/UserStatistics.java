package com.example.learnenglish.model.users;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_statistics")
@Data
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
    @Column(name = "repetitions_count")
    private Long repetitionsCount;
    @Column(name = "days_count")
    private Long daysInARowCount;
    @Column(name = "error_count")
    private Long errorCount;

    @OneToOne(mappedBy = "statistics",
            cascade = CascadeType.ALL)
    private User user;

    public UserStatistics() {
    }
}
