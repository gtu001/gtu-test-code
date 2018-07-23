package com.gtu.example.common;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import com.gtu.example.controller.SpringDataDBMainController;

public class RepositoryReflectionUtil {

    private static final Logger log = LoggerFactory.getLogger(SpringDataDBMainController.class);

    public static <Entity> Map<Class<Entity>, Class[]> getRepositoryEntityMap(String daoPackagePath) {
        Reflections reflections = new Reflections(daoPackagePath);
        Set<Class<? extends CrudRepository>> respositories = reflections.getSubTypesOf(CrudRepository.class);

        Map<Class<Entity>, Class[]> repositMap = new LinkedHashMap<Class<Entity>, Class[]>();

        for (Class<? extends CrudRepository> clz : respositories) {
            // log.info("--------------------------------------------------");
            // log.info("respository = {}", clz);
            Class<?> entityClz = PackageReflectionUtil.getClassGenericClz(clz, 0);
            Class<?> entityIdClz = PackageReflectionUtil.getClassGenericClz(clz, 1);
            // log.info("entityClz = {}", entityClz);
            repositMap.put((Class<Entity>) entityClz, new Class[] { clz, entityIdClz });
        }
        return repositMap;
    }

    public static String getEntityId(Class<?> clz) {
        for (Field f : clz.getDeclaredFields()) {
            if (f.isAnnotationPresent(javax.persistence.Id.class)) {
                return f.getName();
            }
        }

        if (clz.getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
            for (Field f : clz.getSuperclass().getDeclaredFields()) {
                if (f.isAnnotationPresent(javax.persistence.Id.class)) {
                    return f.getName();
                }
            }
        }
        return null;
    }
}
