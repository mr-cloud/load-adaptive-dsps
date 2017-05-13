package org.mlgb.dsps.util;

public class SpoutVO {
    private String spoutId;
    private int executors;
    private long emitted;
    private String completeLatency;
    private long transferred;
    private int tasks;
    private String lastError;
    private int errorLapsedSecs;
    private String errorWorkerLogLink;
    private long acked;
    private long failed;
    public String getSpoutId() {
        return spoutId;
    }
    public void setSpoutId(String spoutId) {
        this.spoutId = spoutId;
    }
    public int getExecutors() {
        return executors;
    }
    public void setExecutors(int executors) {
        this.executors = executors;
    }
    public long getEmitted() {
        return emitted;
    }
    public void setEmitted(long emitted) {
        this.emitted = emitted;
    }
    public String getCompleteLatency() {
        return completeLatency;
    }
    public void setCompleteLatency(String completeLatency) {
        this.completeLatency = completeLatency;
    }
    public long getTransferred() {
        return transferred;
    }
    public void setTransferred(long transferred) {
        this.transferred = transferred;
    }
    public int getTasks() {
        return tasks;
    }
    public void setTasks(int tasks) {
        this.tasks = tasks;
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
    
}
