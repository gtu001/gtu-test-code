package gtu.quartz.ex3;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "LOG_SYS_BATCH")
public class LogSysBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_SYS_BATCH_SEQ")
    @SequenceGenerator(name = "LOG_SYS_BATCH_SEQ", sequenceName = "log_sys_batch_seq", allocationSize = 1)
    @Column(name = "ID")
    private long id;

    @Column(name = "BATCH_NAME")
    private String batchName;

    @Column(name = "BATCH_STEP_NAME")
    private String batchStepName;

    @Column(name = "BATCH_DATE")
    private String batchDate;

    @Column(name = "LOG_MESSAGE")
    private String logMessage;

    public LogSysBatch() {

    }

    public LogSysBatch(String batchName, String batchStepName, String batchDate, String logMessage) {
        this.batchName = batchName;
        this.batchStepName = batchStepName;
        this.batchDate = batchDate;
        this.logMessage = logMessage;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getBatchStepName() {
        return batchStepName;
    }

    public void setBatchStepName(String batchStepName) {
        this.batchStepName = batchStepName;
    }

    public String getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(String batchDate) {
        this.batchDate = batchDate;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
