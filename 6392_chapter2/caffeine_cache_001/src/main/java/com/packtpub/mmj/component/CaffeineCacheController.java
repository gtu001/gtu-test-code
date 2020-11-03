package com.packtpub.mmj.component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.benmanes.caffeine.cache.Cache;
import com.packtpub.mmj.lib.model.Customer;

@RestController
@RequestMapping("caffeine")
public class CaffeineCacheController {

    @Autowired
    CustomerService customerService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * http://localhost:8090/caffeine/getCustomer/123
     */
    @RequestMapping(value = "/getCustomer/{customerId}")
    public String getCustomer(@PathVariable(value = "customerId") String customerId) {
        Long customId = Long.parseLong(customerId);
        Customer customer = customerService.getCustomer(customId);
        StringBuilder sb = new StringBuilder();
        sb.append("hashcode : " + customer.hashCode() + "\r\n");
        sb.append("customer : " + ReflectionToStringBuilder.toString(customer, ToStringStyle.MULTI_LINE_STYLE) + "\r\n");
        return sb.toString();
    }

    /**
     * http://localhost:8090/caffeine/cachemanager/customer
     */
    @GetMapping("/cachemanager/{cacheName}")
    public String keys(@PathVariable("cacheName") String cacheName) {
        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(cacheName);
        com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
        return String.valueOf(nativeCache.asMap().keySet());

    }

    /**
     * http://localhost:8090/caffeine/caches
     */
    @GetMapping("/caches")
    public ResponseEntity<?> getCache() {
        Map<String, Object> cacheMap = cacheManager.getCacheNames().stream()//
                .collect(Collectors.toMap(Function.identity(), name -> {
                    Cache cache = (Cache) cacheManager.getCache(name).getNativeCache();
                    return cache.asMap();
                }));
        return ResponseEntity.ok(cacheMap);
    }
}
