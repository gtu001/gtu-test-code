package com.gtu.example.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;

public class PackageReflectionUtil {

    private static final Logger log = LoggerFactory.getLogger(PackageReflectionUtil.class);

    public static Set<String> getSubClassPaths(Reflections reflections) {
        Set<String> lst = new LinkedHashSet<>();
        Set<Class<? extends Object>> respositories = reflections.getSubTypesOf(java.lang.Object.class);
        for (Object v : respositories) {
            // System.out.println("--" + v);
        }
        Store store = reflections.getStore();
        // System.out.println(FieldUtils.readDeclaredField(store, "storeMap",
        // true));
        Set<String> set = store.keySet();
        for (String v : set) {
            // System.out.println("--" + v);
            Multimap<String, String> map = store.get(v);
            // System.out.println("size = " + map.size());
            for (String k1 : map.keys()) {
                Collection<String> values = map.get(k1);
                // System.out.println(values);
                lst.addAll(values);
            }
        }
        return lst;
    }

    public static Set<Class<?>> getSubClasses(Reflections reflections) {
        return getSubClassPaths(reflections).stream().map(str -> {
            try {
                return Class.forName(str);
            } catch (Exception ex) {
                throw new RuntimeException("getSubClasses ERR : " + ex.getMessage(), ex);
            }
        }).collect(Collectors.toCollection(HashSet::new));
    }

    public static <T> Class<T> getClassGenericClz(Class<T> clz, int genericIndex) {
        ParameterizedType type = (ParameterizedType) clz.getGenericInterfaces()[0];
        Type t2 = type.getActualTypeArguments()[genericIndex];
        Class<T> rtnRawClz = null;
        if (t2 instanceof TypeVariable) {
            log.info("is TypeVariable = {}", t2);
        } else if (t2 instanceof ParameterizedType) {
            ParameterizedType t3 = (ParameterizedType) t2;
            rtnRawClz = (Class) t3.getRawType();
        } else if (t2 instanceof Class) {
            rtnRawClz = (Class) t2;
        }
        return rtnRawClz;
    }

    public static void showPackageInfo(String packagePath) {
        log.info("# showPackageInfo start ..");
        Reflections reflections = new Reflections(packagePath);
        Store store = reflections.getStore();

        for (String index : store.keySet()) {
            for (String clzStr : store.get(index).keySet()) {
                log.info("classPathStr = {} ", clzStr);
            }
            Multimap<String, String> map = store.get(index);
            log.info("map = {}", map);
        }

        log.info("# showPackageInfo end ..");
    }

    public static Object newInstanceDefault(Class entityClz, boolean debug) {
        Object entity = null;
        try {
            entity = entityClz.newInstance();
        } catch (Exception ex) {
            if (debug)
                ex.printStackTrace();
            try {
                Constructor cons = entityClz.getConstructor(new Class[0]);
                entity = cons.newInstance(new Object[0]);
            } catch (Exception e1) {
                if (debug)
                    e1.printStackTrace();
                try {
                    Constructor cons = entityClz.getDeclaredConstructor(new Class[0]);
                    cons.setAccessible(true);
                    entity = cons.newInstance(new Object[0]);
                } catch (Exception e2) {
                    throw new RuntimeException("newInstanceDefault ERR : " + e2.getMessage(), e2);
                }
            }
        }
        return entity;
    }
}
