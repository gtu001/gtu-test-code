package com.gtu.example.common;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Transient;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.gtu.example.springdata.entity.GtuDBRelation;

public class DBRelationHelper {

    private static final Logger log = LoggerFactory.getLogger(DBRelationHelper.class);

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
        for (Field fld : entity.getClass().getDeclaredFields()) {
            try {
                if (fld.isAnnotationPresent(Transient.class) && fld.isAnnotationPresent(GtuDBRelation.class)) {

                    Object dependencyValue = FieldUtils.readDeclaredField(entity, fld.getName(), true);

                    if (Optional.ofNullable(dependencyValue).orElse("").toString().length() == 0) {
                        continue;
                    }

                    GtuDBRelation dbQuery = (GtuDBRelation) fld.getAnnotation(GtuDBRelation.class);

                    String setterName = dbQuery.setter();
                    String repositoryMethod = dbQuery.method();

                    if (StringUtils.isBlank(repositoryMethod)) {
                        repositoryMethod = "findById";
                    }

                    Map<Class<Object>, Class[]> map = getAllRepostiriesMap();

                    Optional opt = map.values().stream().map(cs -> cs[0]).filter(c -> c.getName().contains(dbQuery.repository())).findAny();
                    if (opt.isPresent()) {
                        Object respositoryUncheck = ctx.getBean((Class) opt.get());

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
    }

}
