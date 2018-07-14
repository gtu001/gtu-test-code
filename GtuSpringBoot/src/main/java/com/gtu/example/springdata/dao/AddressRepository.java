
package com.gtu.example.springdata.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

import com.gtu.example.springdata.entity.Address;

@Profile("spring-data")
// @Repository//網路說這東西沒有用
public interface AddressRepository extends CrudRepository<Address, Long> {
}
