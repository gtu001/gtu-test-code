package com.gtu.example.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import com.google.common.base.Optional;
import com.gtu.example.common.JqGridHandler.JqReader.JqRow;
import com.gtu.example.controller.SpringDataDBMainController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JqGridHandler {

    private static final Logger log = LoggerFactory.getLogger(SpringDataDBMainController.class);

    public static class SimpleJdGridCreater<T> {

        Class<T> clz;

        public SimpleJdGridCreater(Class<T> clz) {
            this.clz = clz;
        }

        public JSONArray getColModel() {
            List<ColModel> lst = new ArrayList<ColModel>();

            String entityId = RepositoryReflectionUtil.getEntityId(clz);

            for (Field f : clz.getDeclaredFields()) {
                ColModel col = new ColModel(f.getName(), f.getName());

                // pk
                if (entityId != null && col.getName().equals(entityId)) {
                    col.edithidden.setEdithidden(true);
                }

                col.setEditable(true);

                lst.add(col);
            }
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
                    } catch (Exception e) {
                        log.info("JqRow <init> warning : " + fname + " -> " + e.getMessage());
                    }
                }

                this.id = StringUtils.join(pkArry, Optional.fromNullable(primaryKeyDelimitChar).or('^'));
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