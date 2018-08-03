package com.gtu.example.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.hibernate.collection.internal.PersistentBag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import com.google.common.base.Optional;
import com.gtu.example.common.JqGridHandler.JqReader.JqRow;

import net.minidev.json.annotate.JsonIgnore;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JqGridHandler {

    public static void main(String[] args) throws NoSuchFieldException, SecurityException {
        // Field f =
        // PersonnelSpecification.class.getDeclaredField("personnelSpecificationProperties");
        //
        // System.out.println(ReflectionToStringBuilder.toString(f,
        // ToStringStyle.MULTI_LINE_STYLE));
        // System.out.println(ReflectionToStringBuilder.toString(f.getType(),
        // ToStringStyle.MULTI_LINE_STYLE));
    }

    private static final Logger log = LoggerFactory.getLogger(JqGridHandler.class);

    public static class SimpleJdGridCreater<T> {

        Class<T> clz;
        List<ColModel> colModelLst;
        String entityId;
        List<String> entityIds = new ArrayList<String>();
        Class<?> pkClz;
        boolean ignoreComplex;

        public SimpleJdGridCreater(Class<T> clz, boolean ignoreComplex) {
            this.clz = clz;
            this.ignoreComplex = ignoreComplex;
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

        public static boolean isRelationField(Field f) {
            if (f.isAnnotationPresent(ManyToMany.class) || //
                    f.isAnnotationPresent(OneToMany.class) || //
                    f.isAnnotationPresent(ManyToOne.class) || //
                    f.isAnnotationPresent(OneToOne.class) //
            ) {
                return true;
            }
            return false;
        }

        private void handleColumn(Class<?> clz, List<ColModel> lst) {
            for (Field f : clz.getDeclaredFields()) {

                // 是否忽略複雜欄位
                if (ignoreComplex) {
                    if (isRelationField(f)) {
                        continue;
                    }
                }

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

        public <Entity, SubEntity> JqReader<Entity, SubEntity> getSimpleJqReader(//
                Collection<Entity> lst, //
                @Nullable Character primaryKeyDelimitChar, int pageNo, int totalCount) {
            JqReader<Entity, SubEntity> reader = new JqReader<Entity, SubEntity>();

            String[] columnOrdery = this.colModelLst.stream().map(c -> c.getIndex()).toArray(String[]::new);
            String[] primaryKeyColumn = entityIds.stream().toArray(String[]::new);

            List<JqRow<Entity>> rows = new ArrayList<>();
            for (Entity t : lst) {
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
        private SubGrid subgrid;

        public static class JqRow<Entity> {
            String id;
            List<String> cell;
            @Transient
            @JsonIgnore
            Set<String> fieldNamesOrderedSet;
            Map<String, Object> loaclMap;
            Entity entity;

            private void _setValue(String fieldName, Object value, String[] pkColumns, List<String> pkArry) {
                String valStr = "" + Optional.fromNullable(value).or("");
                this.cell.add(valStr);
                this.loaclMap.put(fieldName, valStr);
                if (pkColumns != null) {
                    if (ArrayUtils.contains(pkColumns, fieldName)) {
                        pkArry.add(valStr);
                    }
                }
            }

            public JqRow(Entity entity, @Nullable String[] fieldNamesOrdered, @Nullable String[] pkColumns, @Nullable Character primaryKeyDelimitChar) {
                this.cell = new ArrayList<>();
                this.loaclMap = new LinkedHashMap<String, Object>();
                this.entity = entity;

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
                } else {
                    this.fieldNamesOrderedSet = Stream.of(fieldNamesOrdered).collect(Collectors.toCollection(LinkedHashSet::new));
                }

                List<String> pkArry = new ArrayList<>();

                if (entity != null) {
                    for (String fname : fieldNamesOrderedSet) {
                        try {
                            Object val = FieldUtils.readDeclaredField(entity, fname, true);
                            this._setValue(fname, val, pkColumns, pkArry);
                        } catch (org.hibernate.exception.SQLGrammarException e) {
                            if (e.getMessage().contains("could not extract ResultSet")) {
                                this._setValue(fname, "<ResultSet Error>", null, null);
                                log.error("JqRow <init> : " + fname + " -> " + e.getMessage());
                            } else {
                                this._setValue(fname, "<Error>", null, null);
                                log.error("JqRow <init> : " + fname + " -> " + e.getMessage(), e);
                            }
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            try {
                                Class<?> parentClz = getMappedSuperclassFromEntity(entity);
                                log.info("MappedSuperclass = {}", parentClz);
                                log.info("entity = {}, {}", entity, entity.getClass().getName());

                                Object val = getFieldFromEntity(parentClz, entity, fname);
                                this._setValue(fname, val, pkColumns, pkArry);
                            } catch (Exception e2) {
                                this.cell.add("<Error>");
                                log.error("JqRow <init> : " + fname + " -> " + e2.getMessage(), e2);
                            }
                        } catch (Exception ex) {
                            this.cell.add("<Error>");
                            log.error("JqRow <init> : " + fname + " -> " + ex.getMessage(), ex);
                        }
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

            public Map<String, Object> getLoaclMap() {
                return loaclMap;
            }

            public void setLoaclMap(Map<String, Object> loaclMap) {
                this.loaclMap = loaclMap;
            }
        }

        public static class SubGrid {
            private String root = "rows";
            private String row = "row";
            private boolean repeatitems = true;
            private String cell;

            // {root: "rows", row: "row", repeatitems: true, cell: "cell"}

            public boolean isRepeatitems() {
                return repeatitems;
            }

            public void setRepeatitems(boolean repeatitems) {
                this.repeatitems = repeatitems;
            }

            public String getCell() {
                return cell;
            }

            public String getRoot() {
                return root;
            }

            public void setRoot(String root) {
                this.root = root;
            }

            public String getRow() {
                return row;
            }

            public void setRow(String row) {
                this.row = row;
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

        public SubGrid getSubgrid() {
            return subgrid;
        }

        public void setSubgrid(SubGrid subgrid) {
            this.subgrid = subgrid;
        }
    }

    public static class SubgridHandler {

        private SimpleJdGridCreater handler;

        private String fieldName;
        private JSONArray colModel;
        private JSONObject rowReader;
        private Collection relationLst;

        private JSONArray loaclLst;

        public SubgridHandler(Object entity, String fieldName, EntityManager entityManager) {
            this.fieldName = fieldName;
            this.colModel = new JSONArray();
            this.rowReader = new JSONObject();

            Field relationField = FieldUtils.getDeclaredField(entity.getClass(), fieldName, true);

            Object relationObject;
            try {
                relationObject = relationField.get(entity);
            } catch (Exception e) {
                log.error("SubgridHandler <init> ERR : " + e.getMessage(), e);
                return;
            }

            int processType = 1;

            // verify
            if (relationObject instanceof org.hibernate.collection.internal.PersistentBag) {
                PersistentBag tmpBag = (PersistentBag) relationObject;
                boolean loadOk = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(tmpBag);
                if ((loadOk && tmpBag.empty()) || //
                        !loadOk) {
                    try {
                        relationObject = StreamSupport.stream(//
                                Spliterators.spliteratorUnknownSize(tmpBag.iterator(), Spliterator.ORDERED), //
                                false).collect(Collectors.toList());
                        processType = 1;
                    } catch (Exception ex) {
                        processType = -1;
                        log.error("[SubgridHandler] retry failed : " + ex.getMessage(), ex);
                    }
                }
            }

            // 一般處理
            switch (processType) {
            case 1:
                Class<?> fieldOrignClz = relationField.getType();
                if (Collection.class.isAssignableFrom(fieldOrignClz)) {
                    fieldOrignClz = getFieldGenericType(relationField);
                    relationLst = (Collection) relationObject;

                    // 如果取步道
                    if (fieldOrignClz == null && !relationLst.isEmpty()) {
                        fieldOrignClz = relationLst.iterator().next().getClass();
                    }
                } else {
                    relationLst = new ArrayList();
                    if (relationObject != null) {
                        relationLst.add(relationObject);
                    }
                }

                log.info("relationObject = {}", relationObject);

                handler = new SimpleJdGridCreater(fieldOrignClz, true);

                colModel = handler.getColModel();
                rowReader = SimpleJdGridCreater.toJSONArray(handler.getSimpleJqReader(relationLst, null, 1, relationLst.size()));
                break;
            case -1:
                log.info("subgrid : {} 物件為空!!", fieldName);
                break;
            }
        }

        public JSONArray getColModel() {
            return colModel;
        }

        public JSONObject getRowReader() {
            return rowReader;
        }

        public String getFieldName() {
            return fieldName;
        }

        private static Class<?> getFieldGenericType(Field field) {
            ParameterizedType type = (ParameterizedType) field.getGenericType();
            Class<?> genericType = (Class<?>) type.getActualTypeArguments()[0];
            return genericType;
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

    public static Class<?> getMappedSuperclassFromEntity(Object entity) {
        for (Class<?> superClz = entity.getClass();; superClz = superClz.getSuperclass()) {
            if (superClz != null && superClz.isAnnotationPresent(MappedSuperclass.class)) {
                return superClz;
            } else if (superClz == null) {
                return null;
            }
        }
    }

    public static Object getFieldFromEntity(Class indicateClz, Object entity, String fieldName) {
        try {
            Field field = FieldUtils.getDeclaredField(indicateClz, fieldName, true);
            return field.get(entity);
        } catch (Exception ex) {
            String tmpFieldName = StringUtils.capitalize(fieldName);
            String[] invokeMethodNames = new String[] { "get" + tmpFieldName, "is" + tmpFieldName };
            for (String fname : invokeMethodNames) {
                try {
                    return MethodUtils.invokeMethod(entity, fname, new Object[0]);
                } catch (Exception ex1) {
                    log.info("找不到method : {} - {}", fname, ex1.getMessage());
                }
            }
            throw new RuntimeException("無法取得此欄位 : " + fieldName, ex);
        }
    }

    private static Object __primitiveConvert(Object value, Class targetClz) {
        if (value == null) {
            return null;
        }
        if (targetClz == String.class) {
            return String.valueOf(value);
        }
        if (ClassUtils.isPrimitiveOrWrapper(targetClz)) {
            return ConvertUtils.convert(String.valueOf(value), targetClz);
        }
        return value;
    }

    public static void setFieldToEntity(Class indicateClz, Object entity, String fieldName, Object value) {
        try {
            Field field = FieldUtils.getDeclaredField(indicateClz, fieldName, true);
            value = __primitiveConvert(value, field.getType());
            field.set(entity, value);
            return;
        } catch (Exception ex) {
            String methodName = "set" + StringUtils.capitalize(fieldName);
            for (Method mth : entity.getClass().getMethods()) {
                if (mth.getName().equals(methodName) && mth.getParameterCount() == 1) {
                    value = __primitiveConvert(value, mth.getParameterTypes()[0]);
                    try {
                        mth.invoke(entity, value);
                        return;
                    } catch (Exception e) {
                        log.info("找不到method : {} - {}", fieldName, ex.getMessage());
                    }
                }
            }
            throw new RuntimeException("無法取得此欄位 : " + fieldName, ex);
        }
    }
}
