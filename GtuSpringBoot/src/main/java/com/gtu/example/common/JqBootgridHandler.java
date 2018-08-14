package com.gtu.example.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.lang.Nullable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JqBootgridHandler {
    private static final Logger log = LoggerFactory.getLogger(JqBootgridHandler.class);

    public static class JqBootgridRequestInterpreter {
        public JqBootgridRequestInterpreter(HttpServletRequest request) {
            for (Enumeration enu = request.getParameterNames(); enu.hasMoreElements();) {
                String key = (String) enu.nextElement();
                if ("current".equals(key)) {
                    current = request.getParameter("current");
                } else if ("rowCount".equals(key)) {
                    String tmpRowCount = request.getParameter("rowCount");
                    if (StringUtils.isNumeric(tmpRowCount)) {
                        rowCount = Integer.parseInt(tmpRowCount);
                    }
                }

                if (key.matches("sort\\[\\w+\\]")) {
                    Pattern ptn = Pattern.compile("sort\\[(\\w+)\\]");
                    Matcher mth = ptn.matcher(key);
                    String sortColumn = "";
                    String sortType = request.getParameter(key);
                    if (mth.find()) {
                        sortColumn = mth.group(1);
                    }
                    if (StringUtils.isNotBlank(sortColumn)) {
                        sort = Pair.of(sortColumn, sortType);
                    }
                }

                if ("searchPhrase".equals(key)) {
                    httpFormToBean = HttpFormToBean.newInstance(request.getParameter(key));
                    searchPhrase = httpFormToBean.getFormMap();
                }
            }
        }

        private String current;
        private int rowCount;
        private Pair<String, String> sort;
        private Map<String, String> searchPhrase;
        private HttpFormToBean httpFormToBean;

        public String getCurrent() {
            return current;
        }

        public int getRowCount() {
            return rowCount;
        }

        public Pair<String, String> getSort() {
            return sort;
        }

        public Map<String, String> getSearchPhrase() {
            return searchPhrase;
        }

        public HttpFormToBean getHttpFormToBean() {
            return httpFormToBean;
        }
    }

    public static class JqBootgridModelHandler<T> {
        private List<T> lst;
        private List<Pair<String, String>> orignColumnDef;// FieldName,
                                                          // columnTitle
        private JqBootgridModel jqBootgridModel;
        private List<JqBootgridColumnDef> columnDef;

        public static List<JqBootgridColumnDef> getColumnDef(List<Pair<String, String>> columns) {
            List<JqBootgridColumnDef> columnDef = new ArrayList<>();
            for (Pair<String, String> def : columns) {
                JqBootgridColumnDef def2 = new JqBootgridColumnDef();
                def2.setId(def.getFirst());
                def2.setText(def.getSecond());
                columnDef.add(def2);
            }
            return columnDef;
        }

        public static <T> JqBootgridModel getJqBootgridModel(List<T> lst, List<JqBootgridColumnDef> columnDef) {
            JqBootgridModel model = new JqBootgridModel();
            for (Object bean : lst) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                for (JqBootgridColumnDef column : columnDef) {
                    String key = column.getId();
                    Object value = null;
                    try {
                        value = FieldUtils.readDeclaredField(bean, key, true);
                        if (value == null) {
                            value = "";
                        }
                        map.put(key, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                model.add(map);
            }
            return model;
        }

        public JqBootgridModelHandler(List<T> lst, Class<T> clz, @Nullable List<Pair<String, String>> columns) {
            this.lst = lst;
            this.jqBootgridModel = new JqBootgridModel();
            this.columnDef = new ArrayList<>();

            if (columns == null) {
                columns = Stream.of(clz.getDeclaredFields()).map(Field::getName).map(name -> Pair.of(name, name)).collect(Collectors.toList());
            }
            this.columnDef = this.getColumnDef(columns);
            this.jqBootgridModel = this.getJqBootgridModel(lst, columnDef);
        }

        public JSONObject getModel() {
            return JSONObject.fromObject(jqBootgridModel);
        }

        public JSONArray getColmnDef() {
            return JSONArray.fromObject(columnDef);
        }
    }

    public static class JqBootgridModel {
        private String current;
        private int rowCount;
        private int total;
        private List<Map<String, Object>> rows = new ArrayList<>();

        public boolean add(Map<String, Object> map) {
            if (rows == null) {
                rows = new ArrayList<>();
            }
            if (map == null) {
                return false;
            }
            rows.add(map);
            return true;
        }

        public JqBootgridModel() {
        }

        public String getCurrent() {
            return current;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public int getRowCount() {
            return rowCount;
        }

        public void setRowCount(int rowCount) {
            this.rowCount = rowCount;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<Map<String, Object>> getRows() {
            return rows;
        }

        public void setRows(List<Map<String, Object>> rows) {
            this.rows = rows;
        }
    }

    public static class JqBootgridColumnDef {
        private String id;
        private boolean identifier = false;
        private Map<String, String> converter = new HashMap<String, String>();
        private String text;
        private String align = "left";
        private String headerAlign = "left";
        private String cssClass = "";
        private String headerCssClass = "";
        private String formatter = null;
        private boolean searchable = true;
        private boolean sortable = true;
        private boolean visible = true;
        private boolean visibleInSelection = true;
        private String width = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIdentifier() {
            return identifier;
        }

        public void setIdentifier(boolean identifier) {
            this.identifier = identifier;
        }

        public Map<String, String> getConverter() {
            return converter;
        }

        public void setConverter(Map<String, String> converter) {
            this.converter = converter;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAlign() {
            return align;
        }

        public void setAlign(String align) {
            this.align = align;
        }

        public String getHeaderAlign() {
            return headerAlign;
        }

        public void setHeaderAlign(String headerAlign) {
            this.headerAlign = headerAlign;
        }

        public String getCssClass() {
            return cssClass;
        }

        public void setCssClass(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getHeaderCssClass() {
            return headerCssClass;
        }

        public void setHeaderCssClass(String headerCssClass) {
            this.headerCssClass = headerCssClass;
        }

        public String getFormatter() {
            return formatter;
        }

        public void setFormatter(String formatter) {
            this.formatter = formatter;
        }

        public boolean isSearchable() {
            return searchable;
        }

        public void setSearchable(boolean searchable) {
            this.searchable = searchable;
        }

        public boolean isSortable() {
            return sortable;
        }

        public void setSortable(boolean sortable) {
            this.sortable = sortable;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public boolean isVisibleInSelection() {
            return visibleInSelection;
        }

        public void setVisibleInSelection(boolean visibleInSelection) {
            this.visibleInSelection = visibleInSelection;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }
    }
}
