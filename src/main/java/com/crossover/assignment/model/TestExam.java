/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/
package com.crossover.assignment.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Test exam data object.
 */
@Entity
@Table(name = "test_exam")
@Data
@EqualsAndHashCode(callSuper = true, exclude = { "name", "description", "passScore", "totalScore", "duration", "questions", "grades" })
@SuppressWarnings("serial")
public class TestExam extends BaseEntity {
    private String name;
    private String description;

    @Column(name = "pass_score")
    private Integer passScore;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "exam_duration")
    private Integer duration;

    @OneToMany(mappedBy = "exam", cascade = { CascadeType.MERGE, CascadeType.REMOVE })
    private List<Question> questions;

    @OneToMany(mappedBy = "exam", cascade = { CascadeType.REMOVE })
    private Set<Grade> grades;

    public TestExam() {
        this(UUID.randomUUID());
    }

    public TestExam(UUID uuid) {
        super(uuid);
    }

    public TestExam(String name, String description, Integer passScore, Integer totalScore, Integer duration) {
        this(UUID.randomUUID());
        this.name = name;
        this.description = description;
        this.passScore = passScore;
        this.totalScore = totalScore;
        this.duration = duration;
    }
}
