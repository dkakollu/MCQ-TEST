/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/
package com.crossover.assignment.model;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User data object.
 */
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true, exclude = { "fullname", "password" })
@SuppressWarnings("serial")
public class User extends BaseEntity {
    private String username;
    private String fullname;
    private String password;

    @OneToMany(mappedBy = "user", cascade = { CascadeType.REMOVE })
    private Set<Grade> grades;

    public User() {
        this(UUID.randomUUID());
    }

    public User(UUID uuid) {
        super(uuid);
    }

    public User(String username, String fullname, String password) {
        this(UUID.randomUUID());
        this.username = username;
        this.fullname = fullname;
        this.password = password;
    }
}
