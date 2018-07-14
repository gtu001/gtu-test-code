package com.gtu.example.springdata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ADDRESS_1")
//@EntityListeners(AddressEntityListener.class)
public class Address {

    @Id
    @GenericGenerator(name = "sequence_address_id", strategy = "com.gtu.example.springdata.dao.AddressIdGenerator")
    @GeneratedValue(generator = "sequence_address_id")
    @Column(name = "ADD_ID", unique = true, nullable = false)
    private String addressId;

    // @PrePersist // 替代字串pk方案 (拿掉@GeneratedValue)
    // private void ensureId() {
    // this.setAddressId(UUID.randomUUID().toString());
    // }

    private String city;
    private String road;

    public Address(String city, String road) {
        this.city = city;
        this.road = road;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }
}
