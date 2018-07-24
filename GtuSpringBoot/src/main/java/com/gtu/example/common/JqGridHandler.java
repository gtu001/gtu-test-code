package com.gtu.example.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import com.google.common.base.Optional;
import com.gtu.example.common.JqGridHandler.JqReader.JqRow;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JqGridHandler {

    private static final Logger log = LoggerFactory.getLogger(JqGridHandler.class);

    public static class SimpleJdGridCreater<T> {

        Class<T> clz;
        List<ColModel> colModelLst;
        String entityId;
        List<String> entityIds = new ArrayList<String>();
        Class<?> pkClz;

        public SimpleJdGridCreater(Class<T> clz) {
            this.clz = clz;
        }

        private void handlePrimaryColumns(String entityId, Class<?> clz, List<Field> pksLst) {
            // 處理pks
            Field tempFld = FieldUtils.getDeclaredField(clz, entityId, true);
            if (tempFld == null) {
                return;
            }
            pkClz = tempFld.getType();
            if (!ClassUtils.isPrimitiveOrWrapper(pkClz) && //
                    pkClz != String.class //
            ) {
                for (Field fld : pkClz.getDeclaredFields()) {
                    entityIds.add(fld.getName());
                    pksLst.add(fld);
                }
            } else {
                entityIds.add(entityId);
                pksLst.add(FieldUtils.getDeclaredField(clz, entityId, true));
            }

            if (clz.getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
                this.handlePrimaryColumns(entityId, clz.getSuperclass(), pksLst);
            }
        }

        private boolean __isColumnExists(final String fieldName, List<ColModel> lst) {
            return lst.stream().filter(c -> c.getIndex().equals(fieldName)).collect(Collectors.counting()) > 0;
        }

        private void handleColumn(Class<?> clz, List<ColModel> lst) {
            for (Field f : clz.getDeclaredFields()) {
                ColModel col = new ColModel(f.getName(), f.getName());
                col.setEditable(true);
                if (__isColumnExists(f.getName(), lst)) {
                    continue;
                }
                lst.add(col);
            }
            if (clz.getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
                handleColumn(clz.getSuperclass(), lst);
            }
        }

        public JSONArray getColModel() {
            List<ColModel> lst = new ArrayList<ColModel>();

            entityId = RepositoryReflectionUtil.getEntityId(clz);

            List<Field> pkFld = new ArrayList<>();

            this.handlePrimaryColumns(entityId, clz, pkFld);
            for (Field f : pkFld) {
                ColModel col = new ColModel(f.getName(), f.getName());
                col.edithidden.setEdithidden(true);
                col.setEditable(true);
                if (__isColumnExists(f.getName(), lst)) {
                    continue;
                }
                lst.add(col);
            }

            // 處理欄位
            handleColumn(clz, lst);

            // 保留一份 重要!!
            this.colModelLst = lst;

            return JSONArray.fromObject(lst);
        }

        public <Entity, SubEntity> JqReader<Entity, SubEntity> getSimpleJqReader(//
                List<Entity> lst, @Nullable String[] columnOrdery, @Nullable String[] primaryKeyColumn, //
                @Nullable Character primaryKeyDelimitChar, int pageNo, int totalCount) {
            JqReader<Entity, SubEntity> reader = new JqReader<Entity, SubEntity>();

            List<JqRow<Entity>> rows = new ArrayList<>();
            for (int ii = 0; ii < lst.size(); ii++) {
                Entity t = lst.get(ii);
                JqRow<Entity> row = new JqRow<Entity>(t, columnOrdery, primaryKeyColumn, primaryKeyDelimitChar);
                rows.add(row);
            }
            reader.setRows(rows);

            reader.setTotal(totalCount);
            reader.setPage(pageNo);

            return reader;
        }

        public static JSONObject toJSONArray(JqReader reader) {
            return JSONObject.fromObject(reader);
        }
    }

    public static class EntityPrimaryKeySetter<T, ID> {
        Class<T> entityClz;
        Class<ID> entityPkClz;
        String entityId;
        ID eneityPkObj;
        T entity;

        public EntityPrimaryKeySetter(T entity, Object pkObj) {
            this.entity = entity;
            this.entityClz = (Class<T>) entity.getClass();
            this.entityId = RepositoryReflectionUtil.getEntityId(entityClz);
            log.info("> entityId = {}", entityId);

            try {
                this.entityPkClz = (Class<ID>) entityClz.getDeclaredField(entityId).getType();
                log.info("> entityPkClz = {}", entityPkClz);
            } catch (NoSuchFieldException | SecurityException e) {
                throw new RuntimeException("EntityPrimaryKeySetter <init> ERR : " + e.getMessage(), e);
            }

            boolean isPrimitiveOrWrapped = ClassUtils.isPrimitiveOrWrapper(entityPkClz);
            log.info("> isPrimitiveOrWrapped = {}", isPrimitiveOrWrapped);
            if (!isPrimitiveOrWrapped && !__isDoWithPrimitive(entityPkClz)) {
                // Model Bean
                eneityPkObj = (ID) PackageReflectionUtil.newInstanceDefault(entityPkClz, false);
                this.__processMapToBean(eneityPkObj, pkObj);
            } else {
                eneityPkObj = (ID) __primitiveConvert(pkObj, entityPkClz);
            }
            log.info("> eneityPkObj = {}", ReflectionToStringBuilder.toString(eneityPkObj));
        }

        private Object __primitiveConvert(Object value, Class<ID> targetClz) {
            if (value == null) {
                return null;
            }
            return ConvertUtils.convert(String.valueOf(value), targetClz);
        }

        private void __processMapToBean(Object eneityPkObj, Object pkObj) {
            if (pkObj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) pkObj;
                try {
                    BeanUtils.populate(eneityPkObj, map);
                } catch (Exception e) {
                    throw new RuntimeException("__processMapToBean ERR : " + e.getMessage(), e);
                }
            } else {
                throw new RuntimeException("Must be a Map : " + pkObj.getClass().getName());
            }
        }

        private boolean __isDoWithPrimitive(Class<?> entityPkClz) {
            if (entityPkClz == String.class) {
                return true;
            }
            return false;
        }

        public T apply() {
            try {
                log.info("apply  {} , {}, {}", entity, entityId, eneityPkObj);
                FieldUtils.writeDeclaredField(entity, entityId, eneityPkObj, true);
                return entity;
            } catch (IllegalAccessException e) {
                throw new RuntimeException("EntityPrimaryKeySetter <init> ERR : " + e.getMessage(), e);
            }
        }
    }

    // {
    // "total": "xxx",
    // "page": "yyy",
    // "records": "zzz",
    // "rows" : [
    // {"id" :"1", "cell" :["cell11", "cell12", "cell13"]},
    // {"id" :"2", "cell":["cell21", "cell22", "cell23"]},
    // ...
    // ]
    // }
    public static class JqReader<Entity, D> {

        private List<JqRow<Entity>> rows;
        private int page = 0;
        private int total = 0;
        private String records;
        private boolean repeatitems = true;
        private String cell;
        private String id;
        private String userdata;
        private SubGrid<D> subgrid;

        public static class JqRow<Entity> {
            String id;
            List<String> cell;
            @Transient
            Set<String> fieldNamesOrderedSet;

            public JqRow(Entity entity, @Nullable String[] fieldNamesOrdered, @Nullable String[] pkColumns, @Nullable Character primaryKeyDelimitChar) {
                this.cell = new ArrayList<>();

                if (fieldNamesOrdered == null) {
                    this.fieldNamesOrderedSet = Stream.of(entity.getClass().getDeclaredFields())//
                            .map(Field::getName)//
                            .collect(Collectors.toCollection(LinkedHashSet::new));

                    if (entity.getClass().getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
                        Set<String> parentCol = Stream.of(entity.getClass().getSuperclass().getDeclaredFields())//
                                .map(Field::getName)//
                                .collect(Collectors.toCollection(LinkedHashSet::new));
                        this.fieldNamesOrderedSet.addAll(parentCol);
                    }
                }

                List<String> pkArry = new ArrayList<>();

                for (String fname : fieldNamesOrderedSet) {
                    try {
                        Object val = FieldUtils.readDeclaredField(entity, fname, true);
                        String valStr = "" + Optional.fromNullable(val).or("");
                        this.cell.add(valStr);

                        if (pkColumns != null) {
                            if (ArrayUtils.contains(pkColumns, fname)) {
                                pkArry.add(valStr);
                            }
                        }
                    } catch (org.hibernate.exception.SQLGrammarException e) {
                        if (e.getMessage().contains("could not extract ResultSet")) {
                            this.cell.add("<ResultSet Error>");
                            log.error("JqRow <init> : " + fname + " -> " + e.getMessage());
                        } else {
                            this.cell.add("<Error>");
                            log.error("JqRow <init> : " + fname + " -> " + e.getMessage(), e);
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        try {
                            Class<?> parentClz = entity.getClass().getSuperclass();
                            Field field = FieldUtils.getDeclaredField(parentClz, fname, true);
                            Object val = field.get(entity);
                            String valStr = "" + Optional.fromNullable(val).or("");
                            this.cell.add(valStr);

                            if (pkColumns != null) {
                                if (ArrayUtils.contains(pkColumns, fname)) {
                                    pkArry.add(valStr);
                                }
                            }
                        } catch (Exception e2) {
                            this.cell.add("<Error>");
                            log.error("JqRow <init> : " + fname + " -> " + e2.getMessage(), e2);
                        }
                    } catch (Exception ex) {
                        this.cell.add("<Error>");
                        log.error("JqRow <init> : " + fname + " -> " + ex.getMessage(), ex);
                    }
                }

                this.id = StringUtils.join(pkArry, Optional.fromNullable(primaryKeyDelimitChar).or('^'));

                log.info("column name : {}", fieldNamesOrderedSet);
                log.info("value  name : {}", cell);
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public List<String> getCell() {
                return cell;
            }

            public void setCell(List<String> cell) {
                this.cell = cell;
            }

            public Set<String> getFieldNamesOrderedSet() {
                return fieldNamesOrderedSet;
            }

            public void setFieldNamesOrderedSet(Set<String> fieldNamesOrderedSet) {
                this.fieldNamesOrderedSet = fieldNamesOrderedSet;
            }
        }

        public static class SubGrid<D> {
            private List<D> rows;
            private boolean repeatitems = true;
            private String cell;

            public List<D> getRows() {
                return rows;
            }

            public void setRows(List<D> rows) {
                this.rows = rows;
            }

            public boolean isRepeatitems() {
                return repeatitems;
            }

            public void setRepeatitems(boolean repeatitems) {
                this.repeatitems = repeatitems;
            }

            public String getCell() {
                return cell;
            }

            public void setCell(String cell) {
                this.cell = cell;
            }
        }

        public List<JqRow<Entity>> getRows() {
            return rows;
        }

        public void setRows(List<JqRow<Entity>> rows) {
            this.rows = rows;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getRecords() {
            return records;
        }

        public void setRecords(String records) {
            this.records = records;
        }

        public boolean isRepeatitems() {
            return repeatitems;
        }

        public void setRepeatitems(boolean repeatitems) {
            this.repeatitems = repeatitems;
        }

        public String getCell() {
            return cell;
        }

        public void setCell(String cell) {
            this.cell = cell;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserdata() {
            return userdata;
        }

        public void setUserdata(String userdata) {
            this.userdata = userdata;
        }

        public SubGrid<D> getSubgrid() {
            return subgrid;
        }

        public void setSubgrid(SubGrid<D> subgrid) {
            this.subgrid = subgrid;
        }
    }

    public static class ColModel implements Serializable {
        private static final long serialVersionUID = 1L;
        String name; // 'alias',
        String label;
        String index;// 'alias',
        int width = 0;// 150,
        boolean editable = false;// false\
        String edittype = EdittypeEnum.TEXT.value;
        boolean sortable = true;

        public enum EdittypeEnum {
            TEXT("text"), //
            TEXTAREA("textarea"), //
            SELECT("select"), //
            CHECKBOX("checkbox"),//
            ;

            final String value;

            EdittypeEnum(String value) {
                this.value = value;
            }
        }

        boolean hidden = false;
        EditHiddenRule edithidden = new EditHiddenRule();

        public static class EditHiddenRule {
            private boolean edithidden = false;
            private boolean searchhidden = false;
            private boolean required = false;
            private boolean number = false;
            private Integer minValue;
            private Integer maxValue;
            private boolean email;

            public boolean isEdithidden() {
                return edithidden;
            }

            public void setEdithidden(boolean edithidden) {
                this.edithidden = edithidden;
            }

            public boolean isSearchhidden() {
                return searchhidden;
            }

            public void setSearchhidden(boolean searchhidden) {
                this.searchhidden = searchhidden;
            }

            public boolean isRequired() {
                return required;
            }

            public void setRequired(boolean required) {
                this.required = required;
            }

            public boolean isNumber() {
                return number;
            }

            public void setNumber(boolean number) {
                this.number = number;
            }

            public Integer getMinValue() {
                return minValue;
            }

            public void setMinValue(Integer minValue) {
                this.minValue = minValue;
            }

            public Integer getMaxValue() {
                return maxValue;
            }

            public void setMaxValue(Integer maxValue) {
                this.maxValue = maxValue;
            }

            public boolean isEmail() {
                return email;
            }

            public void setEmail(boolean email) {
                this.email = email;
            }
        }

        public ColModel(String name, String label) {
            this.name = name;
            this.label = label;
            this.index = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public String getEdittype() {
            return edittype;
        }

        public void setEdittype(String edittype) {
            this.edittype = edittype;
        }

        public boolean isSortable() {
            return sortable;
        }

        public void setSortable(boolean sortable) {
            this.sortable = sortable;
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        public EditHiddenRule getEdithidden() {
            return edithidden;
        }

        public void setEdithidden(EditHiddenRule edithidden) {
            this.edithidden = edithidden;
        }
    }
}
