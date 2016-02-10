/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/

package com.crossover.assignment.model;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Answer choice data object.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = { "text", "valid", "question" })
@SuppressWarnings("serial")
public class Answer extends BaseEntity {
    @Column(name = "a_text")
    private String text;

    @Column(name = "d_order")
    private Integer order;

    @Basic
    @Column(name = "is_valid", columnDefinition = "BIT", length = 1)
    private Boolean valid;

    @ManyToOne
    private Question question;

    public Answer() {
        this(UUID.randomUUID());
    }

    public Answer(UUID uuid) {
        super(uuid);
    }

    public Answer(Question question, String text, Integer order, Boolean valid) {
        this(UUID.randomUUID());
        this.question = question;
        this.text = text;
        this.order = order;
        this.valid = valid;
    }
}