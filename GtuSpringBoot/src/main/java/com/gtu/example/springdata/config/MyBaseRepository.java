package com.gtu.example.springdata.config;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@Profile("spring-data")
//自訂Repository
@NoRepositoryBean
interface MyBaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

    Optional<T> findById(ID id);

    <S extends T> S save(S entity);
}