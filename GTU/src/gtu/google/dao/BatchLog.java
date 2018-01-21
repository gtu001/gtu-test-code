package gtu.google.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "batchlog")
public class BatchLog implements Serializable {

    private static final long serialVersionUID = 7973488690098151262L;

    /** 批次作業執行記錄主鍵 */
    @Id
    private BatchLogKey key = new BatchLogKey();

    /** 批次作業名稱 */
    @Column(name = "job_name")
    private String jobName;

    /** 作業點代碼 */
    @Column(name = "site_id")
    private String siteId;

    @Transient
    private String notEqualSiteId;

    /** 完成時間 */
    @Column(name = "complete_time")
    private String completeTime;

    /** 狀態 */
    @Column(name = "status")
    private String status;

    /** 回傳代碼 */
    @Column(name = "rtn_code")
    private String rtnCode;

    /** 回傳訊息(代碼對映的中文:額外訊息) */
    @Column(name = "rtn_msg")
    private String rtnMsg;

    /** 是否有做訊息通知 1:有;0:沒有 */
    @Column(name = "is_notify")
    private String isNotify;

    /** 批次作業代碼 */
    @Column(name = "job_code")
    private String jobCode;

    /** 建立者 */
    @Column(name = "user_id")
    private String userId;

    /** 建立時間 */
    @Column(name = "create_time")
    private String createTime;

    /** 執行周期 */
    @Column(name = "cron_string")
    private String cronString;

    @Column(name = "completion_rate")
    private String completionRate;

    /** 作業類別(1:週期性;2:一次性;3:立即執行) */
    @Column(name = "job_type")
    private String jobType;

    @Column(name = "user_name")
    private String userName;

    @Transient
    private String nextFireTime;

    public String getIsNotify() {
        return isNotify;
    }

    public void setIsNotify(String isNotify) {
        this.isNotify = isNotify;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public BatchLogKey getKey() {
        return key;
    }

    public void setKey(BatchLogKey key) {
        this.key = key;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    public String isNotify() {
        return isNotify;
    }

    public void setNotify(String isNotify) {
        this.isNotify = isNotify;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCronString() {
        return cronString;
    }

    public void setCronString(String cronString) {
        this.cronString = cronString;
    }

    public String getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(String completionRate) {
        this.completionRate = completionRate;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(String nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public String getNotEqualSiteId() {
        return notEqualSiteId;
    }

    public void setNotEqualSiteId(String notEqualSiteId) {
        this.notEqualSiteId = notEqualSiteId;
    }

    @Override
    public String toString() {
        return "BatchLog [key=" + key + ", jobName=" + jobName + ", siteId=" + siteId + ", notEqualSiteId=" + notEqualSiteId + ", completeTime=" + completeTime + ", status=" + status + ", rtnCode="
                + rtnCode + ", rtnMsg=" + rtnMsg + ", isNotify=" + isNotify + ", jobCode=" + jobCode + ", userId=" + userId + ", createTime=" + createTime + ", cronString=" + cronString
                + ", completionRate=" + completionRate + ", jobType=" + jobType + ", userName=" + userName + ", nextFireTime=" + nextFireTime + "]";
    }

}
