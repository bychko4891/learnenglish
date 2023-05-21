package com.example.learnenglish.model.users;

import jakarta.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Columns;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
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

//    @Column(name = "last_visit")
//    private LocalDateTime lastVisit; // текстове поле

//    @Column(name = "count_pair")
//    private Integer countDownloadPair; // к-сть текстів текстове поле

    @ElementCollection
    @CollectionTable(name = "study_time_in_two_weeks",
            joinColumns = @JoinColumn(name = "user_statistics_id"))
    @Column(name = "amount_of_time_per_day")
    private List<Integer> studyTimeInTwoWeeks = new ArrayList<>(); // графік || -----

    @Column(name = "training_time_start_end")
    private LocalDateTime trainingTimeStartEnd; // не виводиться

    @Column(name = "repetitions_count")
    private Integer repetitionsCount;  // 1 в круговий графік ||

    @Column(name = "repetitions_count_prev") //  1 в стовбці графік з двух стовпців ||-----
    private Integer repetitionsCountPrev;

    @Column(name = "repetitions_count_now") // 2 стовбець графік  || ----
    private Integer repetitionsCountNow;

    @Column(name = "days_count")
    private Integer daysInARowCount; // к-сть днів підряд - текстове поле

    @ElementCollection
    @CollectionTable(name = "training_days_mount",
            joinColumns = @JoinColumn(name = "user_statistics_id"))
    @Column(name = "training_day")
    private List<LocalDate> trainingDaysInMonth = new ArrayList<>(); // виводиться в календар || -----

    @Column(name = "error_count")
    private Integer errorCount; // 2 в круговий графік

    @OneToOne(mappedBy = "statistics",
            cascade = CascadeType.ALL)
    private User user;

    public UserStatistics() {
    }
}
