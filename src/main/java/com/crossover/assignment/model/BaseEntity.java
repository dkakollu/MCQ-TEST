/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/
package com.crossover.assignment.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.Assert;

/**
 * Base class for all JPA Entities
 *
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class BaseEntity extends AbstractPersistable<Long> {
    /**
     * All objects will have a unique UUID which allows for the decoupling from
     * DB generated ids
     *
     */
    @Column(length = 48)
    private String uuid;

    public BaseEntity() {
        this(UUID.randomUUID());
    }

    public BaseEntity(UUID guid) {
        Assert.notNull(guid, "UUID is required");
        setUuid(guid.toString());
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * In most instances we can rely on the UUID to identify the object.
     * Subclasses may want a user friendly identifier for constructing easy to
     * read urls
     *
     * @return Object unique identifier for the object
     */
    public Object getIdentifier() {
        return getUuid().toString();
    }
}