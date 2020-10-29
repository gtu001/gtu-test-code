package com.packtpub.mmj.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.packtpub.mmj.lib.model.Customer;

@Service
// @CacheConfig(cacheNames = { "customer" })
public class DefaultCustomerService implements CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCustomerService.class);

    @Override
    // @Cacheable(cacheManager = "cacheManager")
    @Cacheable(value = "customer", key = "#customerID")
    public Customer getCustomer(Long customerID) {
        LOG.info("[1] Trying to get customer information for id {} ", customerID);
        return getCustomerData(customerID);
    }

    private Customer getCustomerData(final Long id) {
        LOG.info("[2] Trying to get customer information for id {} ", id);
        Customer customer = new Customer(id, "testemail@test.com", "Test Customer");
        return customer;
    }
}