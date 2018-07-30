package com.gtu.example.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import com.gtu.example.springdata.entity.DynamicDBRelation;

public class DBRelationHelper {

    private static final Logger log = LoggerFactory.getLogger(DBRelationHelper.class);

    public static Set<Class<?>> getAllTables() {
        Set<Class<?>> all = new LinkedHashSet<>();
        Set<Class<?>> s1 = PackageReflectionUtil.getSubClasses(new Reflections("com.gtu.example.springdata.entity"));
        s1 = s1.stream().filter(clz -> clz.isAnnotationPresent(Entity.class)).sorted(Comparator.comparing(Class::getSimpleName)).collect(Collectors.toCollection(LinkedHashSet::new));
        log.info("getAllTables material {}", s1.size());
        all.addAll(s1);
        log.info("getAllTables = {}", all.size());
        return all;
    }

    public static Map<Class<Object>, Class[]> getAllRepostiriesMap() {
        Map<Class<Object>, Class[]> all = new LinkedHashMap<>();
        Map<Class<Object>, Class[]> m1 = RepositoryReflectionUtil.getRepositoryEntityMap("com.gtu.example.springdata.dao_1");
        log.info("getAllRepostiriesMap material {}", m1.size());
        all.putAll(m1);
        log.info("getAllRepostiriesMap = {}", all.size());
        return all;
    }

    Object entity;
    ApplicationContext ctx;

    public DBRelationHelper(Object entity, ApplicationContext ctx) {
        this.entity = entity;
        this.ctx = ctx;
    }

    public void relationProcess() {
        log.info("## relationProcess ## start");
        for (Field fld : entity.getClass().getDeclaredFields()) {
            try {
                if (// fld.isAnnotationPresent(Transient.class) &&
                fld.isAnnotationPresent(DynamicDBRelation.class)) {

                    Object dependencyValue = FieldUtils.readDeclaredField(entity, fld.getName(), true);

                    if (Optional.ofNullable(dependencyValue).orElse("").toString().length() == 0) {
                        continue;
                    }

                    DynamicDBRelation dbQuery = (DynamicDBRelation) fld.getAnnotation(DynamicDBRelation.class);

                    String setterName = dbQuery.setter();
                    String repositoryMethod = dbQuery.method();

                    if (StringUtils.isBlank(repositoryMethod)) {
                        repositoryMethod = "findById";
                    }

                    Map<Class<Object>, Class[]> map = getAllRepostiriesMap();

                    Optional opt = map.values().stream().map(cs -> cs[0]).filter(c -> {
                        if (c.getSimpleName().equals(dbQuery.repository())) {
                            log.info("find repository = {}", c);
                            return true;
                        }
                        return false;
                    }).findAny();

                    if (opt.isPresent()) {
                        Object respositoryUncheck = ctx.getBean((Class) opt.get());

                        Class<?> paramClz = __getMethodInvokeParameterClz(respositoryUncheck, repositoryMethod);

                        if (ClassUtils.isPrimitiveOrWrapper(paramClz) || paramClz == String.class) {
                            dependencyValue = ConvertUtils.convert(dependencyValue, paramClz);
                        }

                        Object valueUncheck = MethodUtils.invokeMethod(respositoryUncheck, repositoryMethod, dependencyValue);

                        Object realValue = null;

                        if (valueUncheck instanceof Optional) {
                            realValue = ((Optional) valueUncheck).orElse(null);
                        } else {
                            realValue = valueUncheck;
                        }

                        log.info("write dependency : {} = {}", setterName, realValue);
                        MethodUtils.invokeMethod(entity, setterName, realValue);
                    }
                }
            } catch (Exception ex) {
                log.error("write dependency ERR : " + ex.getMessage(), ex);
            }
        }
        log.info("## relationProcess ## end");
    }

    private Class<?> __getMethodInvokeParameterClz(Object respositoryUncheck, final String repositoryMethod) {
        log.info("find method = {}", repositoryMethod);
        Optional<Method> opt = Stream.of(respositoryUncheck.getClass().getDeclaredMethods()).filter(m -> {
            if (m.getName().equals(repositoryMethod)) {
                return true;
            }
            return false;
        }).findAny();
        if (opt.isPresent()) {
            return opt.get().getParameterTypes()[0];
        }
        throw new RuntimeException("找不到對應method : " + respositoryUncheck.getClass().getName() + "." + repositoryMethod);
    }

}
