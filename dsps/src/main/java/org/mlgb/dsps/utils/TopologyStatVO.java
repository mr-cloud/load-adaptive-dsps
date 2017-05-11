package org.mlgb.dsps.utils;

public class TopologyStatVO {
    private String windowPretty;
    private String window;
    private long emitted;
    private long transferred;
    private String completeLatency;
    private long acked;
    private long failed;
    public String getWindowPretty() {
        return windowPretty;
    }
    public void setWindowPretty(String windowPretty) {
        this.windowPretty = windowPretty;
    }
    public String getWindow() {
        return window;
    }
    public void setWindow(String window) {
        this.window = window;
    }
    public long getEmitted() {
        return emitted;
    }
    public void setEmitted(long emitted) {
        this.emitted = emitted;
    }
    public long getTransferred() {
        return transferred;
    }
    public void setTransferred(long transferred) {
        this.transferred = transferred;
    }
    public String getCompleteLatency() {
        return completeLatency;
    }
    public void setCompleteLatency(String completeLatency) {
        this.completeLatency = completeLatency;
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
