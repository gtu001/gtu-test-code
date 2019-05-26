package com.gtu.rest.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the LOG_SERVICE database table.
 * 
 */
@Entity
@Table(name = "LOG_SERVICE")
public class LogService implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "LOG_SERVICE_TX_ID", sequenceName = "ISEQ$$_93729")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_SERVICE_TX_ID")
    @Column(name = "TX_ID")
    private long txId;

    @Column(name = "CHANNEL_IP")
    private String channelIp;

    @Column(name = "FINAL")
    private String finalData;

    @Column(name = "\"INDEX\"")
    private BigDecimal index;

    @Column(name = "LOCAL_IP")
    private String localIp;

    @Column(name = "MSGID")
    private String msgid;

    @Column(name = "PAGE_NUM")
    private BigDecimal pageNum;

    @Column(name = "REQ_DATA")
    private String reqData;

    @Column(name = "REQ_DATETIME")
    private Timestamp reqDatetime;

    @Column(name = "RES_DATA")
    private String resData;

    @Column(name = "RES_DATETIME")
    private Timestamp resDatetime;

    @Column(name = "RETURN_CODE")
    private String returnCode;

    @Column(name = "RETURN_DESC")
    private String returnDesc;

    @Column(name = "SOURCE_CHANNEL")
    private String sourceChannel;

    @Column(name = "TX_CTF")
    private String txCtf;

    @Column(name = "TX_OP")
    private String txOp;

    @Column(name = "TXNSEQ")
    private String txnseq;

    public LogService() {
    }

    public String getFinalData() {
        return finalData;
    }

    public void setFinalData(String finalData) {
        this.finalData = finalData;
    }

    public long getTxId() {
        return this.txId;
    }

    public void setTxId(long txId) {
        this.txId = txId;
    }

    public String getChannelIp() {
        return this.channelIp;
    }

    public void setChannelIp(String channelIp) {
        this.channelIp = channelIp;
    }

    public BigDecimal getIndex() {
        return this.index;
    }

    public void setIndex(BigDecimal index) {
        this.index = index;
    }

    public String getLocalIp() {
        return this.localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getMsgid() {
        return this.msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public BigDecimal getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(BigDecimal pageNum) {
        this.pageNum = pageNum;
    }

    public String getReqData() {
        return this.reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    public Timestamp getReqDatetime() {
        return this.reqDatetime;
    }

    public void setReqDatetime(Timestamp reqDatetime) {
        this.reqDatetime = reqDatetime;
    }

    public String getResData() {
        return this.resData;
    }

    public void setResData(String resData) {
        this.resData = resData;
    }

    public Timestamp getResDatetime() {
        return this.resDatetime;
    }

    public void setResDatetime(Timestamp resDatetime) {
        this.resDatetime = resDatetime;
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnDesc() {
        return this.returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }

    public String getSourceChannel() {
        return this.sourceChannel;
    }

    public void setSourceChannel(String sourceChannel) {
        this.sourceChannel = sourceChannel;
    }

    public String getTxCtf() {
        return this.txCtf;
    }

    public void setTxCtf(String txCtf) {
        this.txCtf = txCtf;
    }

    public String getTxOp() {
        return this.txOp;
    }

    public void setTxOp(String txOp) {
        this.txOp = txOp;
    }

    public String getTxnseq() {
        return this.txnseq;
    }

    public void setTxnseq(String txnseq) {
        this.txnseq = txnseq;
    }

}