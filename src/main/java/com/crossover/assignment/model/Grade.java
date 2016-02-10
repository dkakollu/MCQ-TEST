/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/
package com.crossover.assignment.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User test result data object.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = { "started", "finished", "score", "user", "exam" })
@SuppressWarnings("serial")
public class Grade extends BaseEntity {
    private Date started;
    private Date finished;

    private Integer score;

    @Basic
    @Column(name = "passed", columnDefinition = "BIT", length = 1)
    private Boolean passed; // in case when test exam "pass score" value will be
                            // changed after user was graded

    @ManyToOne
    private User user;

    @ManyToOne
    private TestExam exam;

    public Grade() {
        this(UUID.randomUUID());
    }

    public Grade(UUID uuid) {
        super(uuid);
    }

    public Grade(User user, TestExam exam, Date started, Date finished, Integer score, Boolean passed) {
        this(UUID.randomUUID());
        this.user = user;
        this.exam = exam;
        this.started = started;
        this.finished = finished;
        this.score = score;
        this.passed = passed;
    }
}
