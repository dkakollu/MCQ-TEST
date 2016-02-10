/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/
package com.crossover.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crossover.assignment.model.User;

/**
 * User data access object.
 */
public interface UsersRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
}
