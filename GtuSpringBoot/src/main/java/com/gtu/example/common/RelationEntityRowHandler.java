package com.gtu.example.common;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.ManyToOne;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gtu.example.common.JqGridHandler.SimpleJdGridCreater;

public class RelationEntityRowHandler {

    private static final Logger log = LoggerFactory.getLogger(RelationEntityRowHandler.class);

    final Object masterEntity;
    final Class<?> entityClz;
    final String fieldName;
    final Class<?> detailClz;

    Object detailOrignObj;
    boolean isCollection;
    Object detailEntity;
    String detailEntityPkId;
    boolean isDetailEntityPkIdGeneratedValue;

    public RelationEntityRowHandler(Class entityClz, Object masterEntity, String fieldName) {
        this.masterEntity = masterEntity;
        this.entityClz = entityClz;
        this.fieldName = fieldName;

        Field field = FieldUtils.getDeclaredField(entityClz, fieldName, true);
        if (!SimpleJdGridCreater.isRelationField(field)) {
            detailClz = null;
            return;
        }

        if (Collection.class.isAssignableFrom(field.getType())) {
            detailClz = getFieldGenericType(field);
            isCollection = true;
        } else {
            detailClz = field.getType();
        }

        detailOrignObj = JqGridHandler.getFieldFromEntity(entityClz, masterEntity, field.getName());
        detailEntityPkId = RepositoryReflectionUtil.getEntityId(detailClz);
        isDetailEntityPkIdGeneratedValue = RepositoryReflectionUtil.isIdGeneratedValue(detailClz);
    }

    private void setBackToMasterEntity() {
        JqGridHandler.setFieldToEntity(entityClz, masterEntity, fieldName, detailOrignObj);
    }

    private void setDetailEntity2MaterEntity() {
        Field masterField = Stream.of(detailClz.getDeclaredFields()).filter(f -> f.getType() == entityClz && f.isAnnotationPresent(ManyToOne.class)).findAny().orElse(null);
        if (masterField != null) {
            JqGridHandler.setFieldToEntity(detailClz, detailEntity, masterField.getName(), masterEntity);
        }
    }

    private void setDetailEntityFromMap(Map<String, Object> entityMap) {
        entityMap.keySet().stream().forEach(key -> {
            try {
                if (detailEntityPkId.equals(key) && isDetailEntityPkIdGeneratedValue) {
                    return;
                }
                Object value = entityMap.get(key);
                JqGridHandler.setFieldToEntity(detailClz, detailEntity, key, value);
            } catch (Exception ex) {
                log.warn("[field not found][" + key + "]!!");
            }
        });
        log.info("detail : {}", ReflectionToStringBuilder.toString(this.detailEntity));
    }

    public void insert(Map<String, Object> entityMap) {
        this.detailEntity = PackageReflectionUtil.newInstanceDefault(detailClz, true);
        this.setDetailEntityFromMap(entityMap);
        this.setDetailEntity2MaterEntity();

        // 設定 detail
        if (isCollection) {
            Collection coll = null;
            if (detailOrignObj != null) {
                coll = (Collection) detailOrignObj;
            } else {
                coll = new ArrayList<>();
            }
            coll.add(this.detailEntity);
            detailOrignObj = coll;
        } else {
            detailOrignObj = this.detailEntity;
        }

        // 設定回 master entity
        setBackToMasterEntity();
    }

    public void update(Map<String, Object> entityMap, Object id) {
        // 找到對的Entity
        if (isCollection) {
            Collection coll = (Collection) detailOrignObj;
            for (Object entity : coll) {
                Object pkVal = JqGridHandler.getFieldFromEntity(detailClz, entity, detailEntityPkId);
                if (ObjectUtils.equals(id, pkVal)) {
                    this.detailEntity = entity;
                    break;
                }
            }
        } else {
            this.detailEntity = detailOrignObj;
        }

        // 塞值
        this.setDetailEntityFromMap(entityMap);
        this.setDetailEntity2MaterEntity();

        // 設定回 master entity
        setBackToMasterEntity();
    }

    public void delete(Object id) {
        // 找到對的Entity
        if (isCollection) {
            Collection coll = (Collection) detailOrignObj;
            for (Object entity : coll) {
                Object pkVal = JqGridHandler.getFieldFromEntity(detailClz, entity, detailEntityPkId);
                if (ObjectUtils.equals(id, pkVal)) {
                    this.detailEntity = entity;
                    break;
                }
            }

            coll.remove(this.detailEntity);
        } else {
            this.detailOrignObj = null;
        }

        // 設定回 master entity
        setBackToMasterEntity();
    }

    private Class<?> getFieldGenericType(Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> genericType = (Class<?>) type.getActualTypeArguments()[0];
        return genericType;
    }
}