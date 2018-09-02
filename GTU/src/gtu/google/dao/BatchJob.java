package gtu.google.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "batchjob")
public class BatchJob implements Serializable {

    private static final long serialVersionUID = -3405506581362157068L;

    /** 批次作業序號 */
    @Id
    @Column(name = "job_id")
    private String jobId;

    /** 批次作業代碼 */
    @Column(name = "job_code")
    private String jobCode;

    /** 批次作業名稱 */
    @Column(name = "job_name")
    private String jobName;

    /** 作業類別(1:週期性;2:一次性) */
    @Column(name = "job_type")
    private String jobType;

    /** 執行週期 */
    @Column(name = "cron_string")
    private String cronstring;

    /** 批次作業執行程式 */
    @Column(name = "clazz")
    private String clazz;

    /** 參數 */
    @Column(name = "data")
    private String data;

    @Column(name = "executant")
    private String executant;

    /** 上次執行時間 */
    @Column(name = "last_fire_time")
    private String lastFireTime;

    /** 下次執行時間 */
    @Column(name = "next_fire_time")
    private String nextFireTime;

    /** 執行優先順利 */
    @Column(name = "priority")
    private String priority;

    @Column(name = "notice_role_id")
    private String noticeRoleId;

    @Column(name = "notice_site_id")
    private String noticeSiteId;

    @Column(name = "notice_org_type")
    private String noticeOrgType;

    @Column(name = "notice_user_id")
    private String noticeUserId;

    /** 建立時間 */
    @Column(name = "create_time")
    private String createTime;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCronstring() {
        return cronstring;
    }

    public void setCronstring(String cronstring) {
        this.cronstring = cronstring;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getExecutant() {
        return executant;
    }

    public void setExecutant(String executant) {
        this.executant = executant;
    }

    public String getLastFireTime() {
        return lastFireTime;
    }

    public void setLastFireTime(String lastFireTime) {
        this.lastFireTime = lastFireTime;
    }

    public String getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(String nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getNoticeRoleId() {
        return noticeRoleId;
    }

    public void setNoticeRoleId(String noticeRoleId) {
        this.noticeRoleId = noticeRoleId;
    }

    public String getNoticeSiteId() {
        return noticeSiteId;
    }

    public void setNoticeSiteId(String noticeSiteId) {
        this.noticeSiteId = noticeSiteId;
    }

    public String getNoticeOrgType() {
        return noticeOrgType;
    }

    public void setNoticeOrgType(String noticeOrgType) {
        this.noticeOrgType = noticeOrgType;
    }

    public String getNoticeUserId() {
        return noticeUserId;
    }

    public void setNoticeUserId(String noticeUserId) {
        this.noticeUserId = noticeUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
