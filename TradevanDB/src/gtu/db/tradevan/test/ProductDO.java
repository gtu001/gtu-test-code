package gtu.db.tradevan.test;

import java.util.LinkedHashMap;
import java.util.Map;

import gtu.db.tradevan.DBColumn_tradevan;

public class ProductDO {
    public static String TABLENAME = "product";
    private Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
    @DBColumn_tradevan(typeName = "DECIMAL", type = 3, size = 20, pk = true)
    public static final String LIST_ID = "LIST_ID";
    @DBColumn_tradevan(typeName = "VARCHAR", type = 12, size = 20, pk = false)
    public static final String GROUP_ID = "GROUP_ID";
    @DBColumn_tradevan(typeName = "VARCHAR", type = 12, size = 100, pk = false)
    public static final String NAME = "NAME";
    @DBColumn_tradevan(typeName = "VARCHAR", type = 12, size = 100, pk = false)
    public static final String TITLE = "TITLE";
    @DBColumn_tradevan(typeName = "DECIMAL", type = 3, size = 11, pk = false)
    public static final String PRICE = "PRICE";

    public java.math.BigDecimal getListId() {
        return (java.math.BigDecimal) dataMap.get(LIST_ID);
    }

    public void setListId(java.math.BigDecimal listId) {
        dataMap.put(LIST_ID, listId);
    }

    public java.lang.String getGroupId() {
        return (java.lang.String) dataMap.get(GROUP_ID);
    }

    public void setGroupId(java.lang.String groupId) {
        dataMap.put(GROUP_ID, groupId);
    }

    public java.lang.String getName() {
        return (java.lang.String) dataMap.get(NAME);
    }

    public void setName(java.lang.String name) {
        dataMap.put(NAME, name);
    }

    public java.lang.String getTitle() {
        return (java.lang.String) dataMap.get(TITLE);
    }

    public void setTitle(java.lang.String title) {
        dataMap.put(TITLE, title);
    }

    public java.math.BigDecimal getPrice() {
        return (java.math.BigDecimal) dataMap.get(PRICE);
    }

    public void setPrice(java.math.BigDecimal price) {
        dataMap.put(PRICE, price);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product");
        sb.append(", listId = " + this.getListId());
        sb.append(", groupId = " + this.getGroupId());
        sb.append(", name = " + this.getName());
        sb.append(", title = " + this.getTitle());
        sb.append(", price = " + this.getPrice());
        return sb.toString();
    }
}
