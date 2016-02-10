/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/
package com.crossover.assignment.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Test exam question data object.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = { "title", "description", "multiple", "order", "exam" })
@SuppressWarnings("serial")
public class Question extends BaseEntity {
    private String title;
    private String description;

    @Basic
    @Column(name = "multiple", columnDefinition = "BIT", length = 1)
    private Boolean multiple;

    @Column(name = "d_order")
    private Integer order;

    @ManyToOne
    private TestExam exam;

    @OneToMany(mappedBy = "question", cascade = { CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    private List<Answer> answers;

    public Question() {
        this(UUID.randomUUID());
    }

    public Question(UUID uuid) {
        super(uuid);
    }

    public Question(TestExam exam, String title, String description, Boolean multiple, Integer order) {
        this(UUID.randomUUID());
        this.exam = exam;
        this.title = title;
        this.description = description;
        this.multiple = multiple;
        this.order = order;
    }
}
