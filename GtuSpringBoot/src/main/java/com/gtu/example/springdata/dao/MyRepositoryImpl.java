package com.gtu.example.springdata.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

@Profile("spring-data")
public class MyRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> {

    private final EntityManager entityManager;

    MyRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);

        // Keep the EntityManager around to used from the newly introduced
        // methods.
        this.entityManager = entityManager;
    }

    @Transactional
    public <S extends T> S save(S entity) {
        // implementation goes here
        return entity;
    }
}
