package com.gtu.example.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.MappedSuperclass;

import org.apache.commons.beanutils.ConvertUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.ClassUtils;

import com.gtu.example.controller.SpringDataDBMainController;

public class RepositoryReflectionUtil {

    private static final Logger log = LoggerFactory.getLogger(SpringDataDBMainController.class);

    public static Object findById(Class<? extends CrudRepository> repositoryClz, CrudRepository repository, Object value) {
        Class<?> paramClz = PackageReflectionUtil.getClassGenericClz(repositoryClz, 1);
        Method findById = null;
        for (Method m : repositoryClz.getMethods()) {
            if (m.getName().equals("findById")) {
                findById = m;
                break;
            }
        }

        if (ClassUtils.isPrimitiveOrWrapper(paramClz)) {
            value = ConvertUtils.convert(value, paramClz);
        } else if (paramClz == String.class) {
            value = value == null ? null : String.valueOf(value);
        } else {
            log.warn("尚無支援 : " + paramClz);
        }

        try {
            return ((Optional) findById.invoke(repository, value)).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("findById ERR : " + e.getMessage(), e);
        }
    }

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
        try {
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
            throw new Exception("查無Id!!");
        } catch (Exception ex) {
            String clzName = clz == null ? "<class is null>" : clz.getName();
            throw new RuntimeException("getEntityId Err : " + ex.getMessage() + " --> " + clzName, ex);
        }
    }
}
