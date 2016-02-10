/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/
package com.crossover.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crossover.assignment.model.Grade;
import com.crossover.assignment.model.TestExam;
import com.crossover.assignment.model.User;

/**
 * User test result data access object.
 */
@Repository
public interface GradesRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByExamOrderByStartedDesc(TestExam exam);

    Grade findByExamAndUser(TestExam exam, User user);
}
