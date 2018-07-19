package com.gtu.example.common;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.data.repository.CrudRepository;

public class RepositoryReflectionUtil {

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
}
