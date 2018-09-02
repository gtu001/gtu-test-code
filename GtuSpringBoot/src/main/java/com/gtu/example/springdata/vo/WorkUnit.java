package com.gtu.example.springdata.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkUnit {
    @JsonProperty("work-unit")
    String workUnit;
    @JsonProperty("name")
    String name;

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}