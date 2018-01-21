/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package tw.gov.moi.ae.jms;

import gtu.string.StringCompressUtil;
import gtu.xml.xstream.iisi.XmlParserImpl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * 訊息通報資料.
 * 
 * @author Sandy Chiu
 */
public class JmsMessage implements Serializable {

    public enum Priority {
        normal, monopoly, independent;;
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7020564663340787441L;

    /** 訊息編號(時間取至微秒). */
    private String messageId;

    /** 傳送時間. */
    private long sendDate;

    /** 傳送端系統代碼. */
    private String senderSystemId;

    /** 傳送端作業點代碼. */
    private String senderSiteId;

    /** 接收端系統代碼. */
    private String receiverSystemId;

    /** 接收端作業點代碼. */
    private String receiverSiteId;

    /** 訊息通報作業代碼. */
    private String jmsTaskId;

    /** 訊息通報次作業代碼. */
    private String jmsTaskIdTemp;

    /** 訊息內容. */
    private Object message;

    /** 交易代碼. */
    private String transationId;

    /** 描述. */
    private String description;

    /** 分類. */
    private String category;

    /** 筆數. */
    private int count;

    /** 呼叫者序號. */
    private String callerId;

    private List<String> syncKeys;

    private Priority priority = Priority.normal;

    private boolean needCheckMinTime = true;

    private String senderTaskCode = "";

    /**
     * 建構子.
     */
    protected JmsMessage() {
    }

    /**
     * 取得訊息編號(時間取至毫秒).
     * 
     * @return 訊息編號(時間取至毫秒)
     */
    @XmlElement
    public String getMessageId() {
        return this.messageId;
    }

    /**
     * 設定訊息編號(時間取至微秒).
     * 
     * @param messageId 訊息編號(時間取至微秒)
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * 取得傳送時間.
     * 
     * @return 傳送時間
     */
    @XmlElement
    public long getSendDate() {
        return this.sendDate;
    }

    /**
     * 設定傳送時間.
     * 
     * @param sendDate 傳送時間
     */
    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    /**
     * 取得訊息通報作業代碼.
     * 
     * @return 訊息通報作業代碼
     */
    @XmlElement
    public String getJmsTaskId() {
        return this.jmsTaskId;
    }

    /**
     * 設定訊息通報作業代碼.
     * 
     * @param jmsTaskId 訊息通報作業代碼
     */
    public void setJmsTaskId(String jmsTaskId) {
        this.jmsTaskId = jmsTaskId;
    }

    /**
     * 取得訊息通報次作業代碼.
     * 
     * @return 訊息通報次作業代碼
     */
    @XmlElement
    public String getJmsTaskIdTemp() {
        return this.jmsTaskIdTemp;
    }

    /**
     * 設定訊息通報次作業代碼.
     * 
     * @param jmsTaskIdTemp 訊息通報次作業代碼
     */
    public void setJmsTaskIdTemp(String jmsTaskIdTemp) {
        this.jmsTaskIdTemp = jmsTaskIdTemp;
    }

    /**
     * @param senderTaskCode the senderTaskCode to set
     */
    public void setSenderTaskCode(String senderTaskCode) {
        this.senderTaskCode = senderTaskCode;
    }

    /**
     * @return the senderTaskCode
     */
    public String getSenderTaskCode() {
        return this.senderTaskCode;
    }

    /**
     * 取得訊息內容.
     * 
     * @return 訊息內容
     */
    @XmlElement
    public Object getMessage() {
        return this.message;
    }

    /**
     * 設定訊息內容.
     * 
     * @param message 訊息內容
     */
    public void setMessage(Object message) {
        this.message = message;
    }

    /**
     * 取得交易代碼.
     * 
     * @return 交易代碼
     */
    @XmlElement
    public String getTransationId() {
        return this.transationId;
    }

    /**
     * 設定交易代碼.
     * 
     * @param transationId 交易代碼
     */
    public void setTransationId(String transationId) {
        this.transationId = transationId;
    }

    /**
     * 取得描述.
     * 
     * @return 描述
     */
    @XmlElement
    public String getDescription() {
        return this.description;
    }

    /**
     * 設定描述.
     * 
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 取得分類.
     * 
     * @return 分類
     */
    @XmlElement
    public String getCategory() {
        return this.category;
    }

    /**
     * 設定分類.
     * 
     * @param category 分類
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 取得傳送端系統代碼.
     * 
     * @return 傳送端系統代碼
     */
    @XmlElement
    public String getSenderSystemId() {
        return this.senderSystemId;
    }

    /**
     * 設定傳送端系統代碼.
     * 
     * @param senderSystemId 傳送端系統代碼
     */
    public void setSenderSystemId(String senderSystemId) {
        this.senderSystemId = senderSystemId;
    }

    /**
     * 取得接收端系統代碼.
     * 
     * @return 接收端系統代碼
     */
    @XmlElement
    public String getReceiverSystemId() {
        return this.receiverSystemId;
    }

    /**
     * 設定接收端系統代碼.
     * 
     * @param receiverSystemId 接收端系統代碼
     */
    public void setReceiverSystemId(String receiverSystemId) {
        this.receiverSystemId = receiverSystemId;
    }

    /**
     * 取得傳送端作業點代碼.
     * 
     * @return 傳送端作業點代碼
     */
    @XmlElement
    public String getSenderSiteId() {
        return this.senderSiteId;
    }

    /**
     * 設定傳送端作業點代碼.
     * 
     * @param senderSiteId 傳送端作業點代碼
     */
    public void setSenderSiteId(String senderSiteId) {
        this.senderSiteId = senderSiteId;
    }

    /**
     * 取得接收端作業點代碼.
     * 
     * @return 接收端作業點代碼
     */
    @XmlElement
    public String getReceiverSiteId() {
        return this.receiverSiteId;
    }

    /**
     * 設定接收端作業點代碼.
     * 
     * @param receiverSiteId 接收端作業點代碼
     */
    public void setReceiverSiteId(String receiverSiteId) {
        this.receiverSiteId = receiverSiteId;
    }

    /**
     * 取得筆數.
     * 
     * @return 筆數
     */
    @XmlElement
    public int getCount() {
        return this.count;
    }

    /**
     * 設定筆數.
     * 
     * @param count 筆數
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 取得呼叫者序號
     * 
     * @return 呼叫者序號
     */
    @XmlElement
    public String getCallerId() {
        return this.callerId;
    }

    /**
     * 設定呼叫者序號
     * 
     * @param callerId 呼叫者序號
     */
    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getMessageXML() {
        if (this.message instanceof String) {
            String x = (String) this.message;
            if (x.startsWith("<") && x.endsWith(">")) {
                return x;
            } else if (x.startsWith("H4sIAAAA")) { // ziped
                return StringCompressUtil.uncompress(x);
            }
        }
        final XmlParserImpl xmlParserImpl = new XmlParserImpl();
        final String objAsXML = xmlParserImpl.genXmlString(this.message);
        return objAsXML;
    }

    public boolean isOld() {
        return true;
    }

    /**
     * 同一組 KeySet 相關的 MQ 同時間只有一個可被執行。
     * 
     * @return the syncKeys
     */
    public List<String> getSyncKeys() {
        return this.syncKeys;
    }

    /**
     * @param syncKeys the syncKeys to set
     */
    public void setSyncKeys(List<String> syncKeys) {
        this.syncKeys = Collections.unmodifiableList(syncKeys);
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * @return the priority
     */
    public Priority getPriority() {
        return this.priority;
    }

    /**
     * @return the needCheckMinTime
     */
    public boolean isNeedCheckMinTime() {
        return this.needCheckMinTime;
    }

    /**
     * @param needCheckMinTime the needCheckMinTime to set
     */
    public void setNeedCheckMinTime(boolean needCheckMinTime) {
        this.needCheckMinTime = needCheckMinTime;
    }
}
