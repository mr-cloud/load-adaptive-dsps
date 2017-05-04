package org.mlgb.dsps.utils;

public class ClusterSummaryVO {
    private String stormVersion;
    private int supervisors;
    private int slotsTotal;
    private int slotsUsed;
    private int slotsFree;
    private int executorsTotal;
    private int tasksTotal;
    public String getStormVersion() {
        return stormVersion;
    }
    public void setStormVersion(String stormVersion) {
        this.stormVersion = stormVersion;
    }
    public int getSupervisors() {
        return supervisors;
    }
    public void setSupervisors(int supervisors) {
        this.supervisors = supervisors;
    }
    public int getSlotsTotal() {
        return slotsTotal;
    }
    public void setSlotsTotal(int slotsTotal) {
        this.slotsTotal = slotsTotal;
    }
    public int getSlotsUsed() {
        return slotsUsed;
    }
    public void setSlotsUsed(int slotsUsed) {
        this.slotsUsed = slotsUsed;
    }
    public int getSlotsFree() {
        return slotsFree;
    }
    public void setSlotsFree(int slotsFree) {
        this.slotsFree = slotsFree;
    }
    public int getExecutorsTotal() {
        return executorsTotal;
    }
    public void setExecutorsTotal(int executorsTotal) {
        this.executorsTotal = executorsTotal;
    }
    public int getTasksTotal() {
        return tasksTotal;
    }
    public void setTasksTotal(int tasksTotal) {
        this.tasksTotal = tasksTotal;
    }
    
}
