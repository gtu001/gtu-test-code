/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.functors.PrototypeFactory;
import org.apache.commons.collections.map.LazyMap;

/**
 * @author tsaicf
 */
public class AeMapUtils {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <K, E> Map<K, List<E>> newLazyMapToList() {
        return newLazyMapToList(new HashMap());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <K, E> Map<K, List<E>> newLazyMapToList(Map<K, List<E>> map) {
        return LazyMap.decorate(map, PrototypeFactory.getInstance(new ArrayList()));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <K, E> Map<K, E> newLazyMapToPrototype(E prototype) {
        return LazyMap.decorate(new ConcurrentHashMap(), PrototypeFactory.getInstance(prototype));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <K, E> Map<K, E> newLinkedLazyMapToPrototype(E prototype) {
        return LazyMap.decorate(new LinkedHashMap(), PrototypeFactory.getInstance(prototype));
    }

    public static <K, V> Map<K, V> newSyncMap() {
        return new ConcurrentHashMap<K, V>();
    }

}
