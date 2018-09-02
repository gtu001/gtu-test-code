
package com.gtu.example.springdata.dao_1;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

import com.gtu.example.springdata.entity.Car;

@Profile({ "spring-data", "servers" })
// @Repository//網路說這東西沒有用
public interface CarRepository extends CrudRepository<Car, Long> {
}
