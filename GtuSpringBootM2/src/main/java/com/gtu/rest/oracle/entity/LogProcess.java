package com.gtu.rest.oracle.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the LOG_PROCESS database table.
 * 
 */
@Entity
@Table(name = "LOG_PROCESS", schema = "IVTLXIVP01FUND")
public class LogProcess implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "LOG_PROCESS_TX_ID", sequenceName = "ISEQ$$_93726")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_PROCESS_TX_ID")
    @Column(name = "TX_ID")
    private long txId;

    @Column(name = "BRANCH_ID")
    private String branchId;

    private String description;

    @Column(name = "TX_DATE")
    private Timestamp txDate;

    @Column(name = "TX_TYPE")
    private String txType;

    @Column(name = "USER_ID")
    private String userId;

    public LogProcess() {
    }

    public long getTxId() {
        return this.txId;
    }

    public void setTxId(long txId) {
        this.txId = txId;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTxDate() {
        return this.txDate;
    }

    public void setTxDate(Timestamp txDate) {
        this.txDate = txDate;
    }

    public String getTxType() {
        return this.txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}