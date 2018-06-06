/*
 * ===========================================================================
 * IBM Confidential
 * AIS Source Materials
 *
 *
 * (C) Copyright IBM Corp. 2013.
 *
 * ===========================================================================
 */
package gtu.spring.jdbc.crud;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Entity
 * 
 * @author $author$
 */
@Entity
@Table(name = "DIC")
@IdClass(DicEntityPk.class)
public class DicEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 排序
     */
    @Basic
    @Column(name = "INDEX_NO")
    private Integer indexNo;

    /**
     * 鍵值
     */
    @Id
    @Column(name = "KEY")
    private String key;

    /**
     * 語系
     */
    @Id
    @Column(name = "LOCALE")
    private String locale;

    /**
     * 保留欄位
     */
    @Basic
    @Column(name = "RESERVE1")
    private String reserve1;

    /**
     * 類別
     */
    @Id
    @Column(name = "TYPE")
    private String type;

    /**
     * 值
     */
    @Basic
    @Column(name = "VALUE")
    private String value;

    /**
     * 取得排序
     * 
     * @return BigDecimal 排序
     */
    public Integer getIndexNo() {
        return this.indexNo;
    }

    /**
     * 設定排序
     * 
     * @param indexNo
     *            要設定的排序
     */
    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    /**
     * 取得鍵值
     * 
     * @return String 鍵值
     */
    public String getKey() {
        return this.key;
    }

    /**
     * 設定鍵值
     * 
     * @param key
     *            要設定的鍵值
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 取得語系
     * 
     * @return String 語系
     */
    public String getLocale() {
        return this.locale;
    }

    /**
     * 設定語系
     * 
     * @param locale
     *            要設定的語系
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * 取得保留欄位
     * 
     * @return String 保留欄位
     */
    public String getReserve1() {
        return this.reserve1;
    }

    /**
     * 設定保留欄位
     * 
     * @param reserve1
     *            要設定的保留欄位
     */
    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    /**
     * 取得類別
     * 
     * @return String 類別
     */
    public String getType() {
        return this.type;
    }

    /**
     * 設定類別
     * 
     * @param type
     *            要設定的類別
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 取得值
     * 
     * @return String 值
     */
    public String getValue() {
        return this.value;
    }

    /**
     * 設定值
     * 
     * @param value
     *            要設定的值
     */
    public void setValue(String value) {
        this.value = value;
    }
}
