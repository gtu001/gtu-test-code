package gtu.quartz.ex3;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@Entity
@Table(name = "BCH_LOG_JOB_FINISH")
public class BchLogJobFinish {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BCH_LOG_JOB_FINISH_ID_SEQ")
    @SequenceGenerator(name = "BCH_LOG_JOB_FINISH_ID_SEQ", sequenceName = "BCH_LOG_JOB_FINISH_ID_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private long id;

    @Column(name = "BATCH_DATE")
    private String batchDate;

    @Column(name = "PROGRAM_ID")
    private String programId;

    @Column(name = "RETURN_CODE")
    private String returnCode;

    @Column(name = "START_DTTM")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime startDttm;

    @Column(name = "END_DTTM")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime endDttm;

    @Column(name = "SKIP_FLAG")
    private String skipFlag;

    @Column(name = "RETURN_DESC")
    private String returnDesc;

    @Column(name = "UPDATE_DTTM")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime updateDttm;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(String batchDate) {
        this.batchDate = batchDate;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public LocalDateTime getStartDttm() {
        return startDttm;
    }

    public void setStartDttm(LocalDateTime startDttm) {
        this.startDttm = startDttm;
    }

    public LocalDateTime getEndDttm() {
        return endDttm;
    }

    public void setEndDttm(LocalDateTime endDttm) {
        this.endDttm = endDttm;
    }

    public String getSkipFlag() {
        return skipFlag;
    }

    public void setSkipFlag(String skipFlag) {
        this.skipFlag = skipFlag;
    }

    public String getReturnDesc() {
        return returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }

    public LocalDateTime getUpdateDttm() {
        return updateDttm;
    }

    public void setUpdateDttm(LocalDateTime updateDttm) {
        this.updateDttm = updateDttm;
    }
}
