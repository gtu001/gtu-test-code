package com.gtu.example.springdata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "work_item")
public class WorkItem {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "work_job_id")
    private String workJobId;
    
    private String workName;

    private String description;

    public WorkItem(String workName, String description) {
        this.workName = workName;
        this.description = description;
    }

    public WorkItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkJobId() {
        return workJobId;
    }

    public void setWorkJobId(String workJobId) {
        this.workJobId = workJobId;
    }
}
