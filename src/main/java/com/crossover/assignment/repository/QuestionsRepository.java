/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/
package com.crossover.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crossover.assignment.model.Question;

/**
 * Test exam question data access object.
 */
@Repository
public interface QuestionsRepository extends JpaRepository<Question, Long> {
}
