package gtu.db.tradevan.test;

import java.util.LinkedHashMap;
import java.util.Map;

import gtu.db.tradevan.DBColumn_tradevan;

public class UserInfoDO {
    public static String TABLENAME = "user_info";
    private Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
    @DBColumn_tradevan(typeName = "VARCHAR", type = 12, size = 20, pk = true)
    public static final String USER_ID = "USER_ID";
    @DBColumn_tradevan(typeName = "VARCHAR", type = 12, size = 20, pk = false)
    public static final String PASSWORD = "PASSWORD";
    @DBColumn_tradevan(typeName = "VARCHAR", type = 12, size = 20, pk = false)
    public static final String EMAIL = "EMAIL";
    @DBColumn_tradevan(typeName = "VARCHAR", type = 12, size = 20, pk = false)
    public static final String TEL_PHONE = "TEL_PHONE";
    @DBColumn_tradevan(typeName = "VARCHAR", type = 12, size = 100, pk = false)
    public static final String ADDRESS = "ADDRESS";
    @DBColumn_tradevan(typeName = "DATE", type = 91, size = 10, pk = false)
    public static final String CREATE_DATE = "CREATE_DATE";
    @DBColumn_tradevan(typeName = "DATETIME", type = 93, size = 19, pk = false)
    public static final String LASTST_LOGIN_DATE = "LASTST_LOGIN_DATE";
    @DBColumn_tradevan(typeName = "VARCHAR", type = 12, size = 20, pk = false)
    public static final String USER_NAME = "USER_NAME";
    @DBColumn_tradevan(typeName = "INT", type = 4, size = 11, pk = false)
    public static final String LOGIN_TIMES = "LOGIN_TIMES";

    public java.lang.String getUserId() {
        return (java.lang.String) dataMap.get(USER_ID);
    }

    public void setUserId(java.lang.String userId) {
        dataMap.put(USER_ID, userId);
    }

    public java.lang.String getPassword() {
        return (java.lang.String) dataMap.get(PASSWORD);
    }

    public void setPassword(java.lang.String password) {
        dataMap.put(PASSWORD, password);
    }

    public java.lang.String getEmail() {
        return (java.lang.String) dataMap.get(EMAIL);
    }

    public void setEmail(java.lang.String email) {
        dataMap.put(EMAIL, email);
    }

    public java.lang.String getTelPhone() {
        return (java.lang.String) dataMap.get(TEL_PHONE);
    }

    public void setTelPhone(java.lang.String telPhone) {
        dataMap.put(TEL_PHONE, telPhone);
    }

    public java.lang.String getAddress() {
        return (java.lang.String) dataMap.get(ADDRESS);
    }

    public void setAddress(java.lang.String address) {
        dataMap.put(ADDRESS, address);
    }

    public java.sql.Date getCreateDate() {
        return (java.sql.Date) dataMap.get(CREATE_DATE);
    }

    public void setCreateDate(java.sql.Date createDate) {
        dataMap.put(CREATE_DATE, createDate);
    }

    public java.sql.Timestamp getLaststLoginDate() {
        return (java.sql.Timestamp) dataMap.get(LASTST_LOGIN_DATE);
    }

    public void setLaststLoginDate(java.sql.Timestamp laststLoginDate) {
        dataMap.put(LASTST_LOGIN_DATE, laststLoginDate);
    }

    public java.lang.String getUserName() {
        return (java.lang.String) dataMap.get(USER_NAME);
    }

    public void setUserName(java.lang.String userName) {
        dataMap.put(USER_NAME, userName);
    }

    public java.lang.Integer getLoginTimes() {
        return (java.lang.Integer) dataMap.get(LOGIN_TIMES);
    }

    public void setLoginTimes(java.lang.Integer loginTimes) {
        dataMap.put(LOGIN_TIMES, loginTimes);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserInfo");
        sb.append(", userId = " + this.getUserId());
        sb.append(", password = " + this.getPassword());
        sb.append(", email = " + this.getEmail());
        sb.append(", telPhone = " + this.getTelPhone());
        sb.append(", address = " + this.getAddress());
        sb.append(", createDate = " + this.getCreateDate());
        sb.append(", laststLoginDate = " + this.getLaststLoginDate());
        sb.append(", userName = " + this.getUserName());
        sb.append(", loginTimes = " + this.getLoginTimes());
        return sb.toString();
    }
}
