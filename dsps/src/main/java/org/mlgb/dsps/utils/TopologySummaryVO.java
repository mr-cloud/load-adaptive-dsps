package org.mlgb.dsps.utils;

public class TopologySummaryVO {
    private String id;
    private String name;
    private String status;
    private String uptime;
    private int uptimeSeconds;
    private int tasksTotal;
    private int workersTotal;
    private int executorsTotal;
    private int replicationCount;
    private double requestedMemOnHeap;
    private double requestedMemOffHeap;
    private double requestedTotalMem;
    private double requestedCpu;
    private double assignedMemOnHeap;
    private double assignedMemOffHeap;
    private double assignedTotalMem;
    private double assignedCpu;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getUptime() {
        return uptime;
    }
    public void setUptime(String uptime) {
        this.uptime = uptime;
    }
    public int getUptimeSeconds() {
        return uptimeSeconds;
    }
    public void setUptimeSeconds(int uptimeSeconds) {
        this.uptimeSeconds = uptimeSeconds;
    }
    public int getTasksTotal() {
        return tasksTotal;
    }
    public void setTasksTotal(int tasksTotal) {
        this.tasksTotal = tasksTotal;
    }
    public int getWorkersTotal() {
        return workersTotal;
    }
    public void setWorkersTotal(int workersTotal) {
        this.workersTotal = workersTotal;
    }
    public int getExecutorsTotal() {
        return executorsTotal;
    }
    public void setExecutorsTotal(int executorsTotal) {
        this.executorsTotal = executorsTotal;
    }
    public int getReplicationCount() {
        return replicationCount;
    }
    public void setReplicationCount(int replicationCount) {
        this.replicationCount = replicationCount;
    }
    public double getRequestedMemOnHeap() {
        return requestedMemOnHeap;
    }
    public void setRequestedMemOnHeap(double requestedMemOnHeap) {
        this.requestedMemOnHeap = requestedMemOnHeap;
    }
    public double getRequestedMemOffHeap() {
        return requestedMemOffHeap;
    }
    public void setRequestedMemOffHeap(double requestedMemOffHeap) {
        this.requestedMemOffHeap = requestedMemOffHeap;
    }
    public double getRequestedTotalMem() {
        return requestedTotalMem;
    }
    public void setRequestedTotalMem(double requestedTotalMem) {
        this.requestedTotalMem = requestedTotalMem;
    }
    public double getRequestedCpu() {
        return requestedCpu;
    }
    public void setRequestedCpu(double requestedCpu) {
        this.requestedCpu = requestedCpu;
    }
    public double getAssignedMemOnHeap() {
        return assignedMemOnHeap;
    }
    public void setAssignedMemOnHeap(double assignedMemOnHeap) {
        this.assignedMemOnHeap = assignedMemOnHeap;
    }
    public double getAssignedMemOffHeap() {
        return assignedMemOffHeap;
    }
    public void setAssignedMemOffHeap(double assignedMemOffHeap) {
        this.assignedMemOffHeap = assignedMemOffHeap;
    }
    public double getAssignedTotalMem() {
        return assignedTotalMem;
    }
    public void setAssignedTotalMem(double assignedTotalMem) {
        this.assignedTotalMem = assignedTotalMem;
    }
    public double getAssignedCpu() {
        return assignedCpu;
    }
    public void setAssignedCpu(double assignedCpu) {
        this.assignedCpu = assignedCpu;
    }
    @Override
    public String toString() {
        return "Topology summary:\n" + "id: " + this.id + "\n"
                + "name: " + this.name + "\n"
                + "status: " + this.status + "\n"
                + "executors: " + this.executorsTotal + "\n"
                + "tasks: " + this.tasksTotal;
    } 
    
}
