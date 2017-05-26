package org.mlgb.dsps.util.vo;


public class BoltVO implements Comparable<BoltVO>{
    private String boltId;
    private String capacity;  // feature
    private String processLatency;
    private String executeLatency;
    private int executors;  // feature
    private int tasks;  // feature
    private long acked;
    private long failed;
    private String lastError;
    private int errorLapsedSecs;
    private String errorWorkerLogLink;
    private long emitted;
    public String getBoltId() {
        return boltId;
    }
    public void setBoltId(String boltId) {
        this.boltId = boltId;
    }
    public String getCapacity() {
        return capacity;
    }
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
    public String getProcessLatency() {
        return processLatency;
    }
    public void setProcessLatency(String processLatency) {
        this.processLatency = processLatency;
    }
    public String getExecuteLatency() {
        return executeLatency;
    }
    public void setExecuteLatency(String executeLatency) {
        this.executeLatency = executeLatency;
    }
    public int getExecutors() {
        return executors;
    }
    public void setExecutors(int executors) {
        this.executors = executors;
    }
    public int getTasks() {
        return tasks;
    }
    public void setTasks(int tasks) {
        this.tasks = tasks;
    }
    public long getAcked() {
        return acked;
    }
    public void setAcked(long acked) {
        this.acked = acked;
    }
    public long getFailed() {
        return failed;
    }
    public void setFailed(long failed) {
        this.failed = failed;
    }
    public String getLastError() {
        return lastError;
    }
    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
    public int getErrorLapsedSecs() {
        return errorLapsedSecs;
    }
    public void setErrorLapsedSecs(int errorLapsedSecs) {
        this.errorLapsedSecs = errorLapsedSecs;
    }
    public String getErrorWorkerLogLink() {
        return errorWorkerLogLink;
    }
    public void setErrorWorkerLogLink(String errorWorkerLogLink) {
        this.errorWorkerLogLink = errorWorkerLogLink;
    }
    public long getEmitted() {
        return emitted;
    }
    public void setEmitted(long emitted) {
        this.emitted = emitted;
    }
    @Override
    public int compareTo(BoltVO o) {
        // DESC order.
        return Double.compare(Double.parseDouble(o.capacity), Double.parseDouble(this.capacity));
    }
    
}
