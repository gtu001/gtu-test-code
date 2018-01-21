package gtu.google.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang.math.LongRange;

@Embeddable
public class BatchLogKey implements Serializable {

    private static final long serialVersionUID = 7973488690098151262L;

    /** 批次作業序號 */
    @Column(name = "job_id")
    private String jobId;

    /** 開始執行時間 */
    @Column(name = "fire_time")
    private String fireTime;

    @Transient
    private LongRange fireTimeRange;

    public LongRange getFireTimeRange() {
        return fireTimeRange;
    }

    public void setFireTimeRange(LongRange fireTimeRange) {
        this.fireTimeRange = fireTimeRange;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getFireTime() {
        return fireTime;
    }

    public void setFireTime(String fireTime) {
        this.fireTime = fireTime;
    }

}
