package com.packtpub.mmj.lib.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {

    Long id;
    String email;
    String name;

    public Customer(Long id, String email, String name) {
        super();
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
