package org.mlgb.dsps.utils;

import java.util.List;

public class TopologyProfileVO {
    private String id;
    private String name;
    private String status;
    private String uptime;
    private int uptimeSeconds;
    private int tasksTotal;
    private int workersTotal;
    private int executorsTotal;
    private int replicationCount;
    private int msgTimeout;
    private String windowHint;
    private List<TopologyStatVO> topologyStats;
    private List<SpoutVO> spouts;
    private List<BoltVO> bolts;
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
    public int getMsgTimeout() {
        return msgTimeout;
    }
    public void setMsgTimeout(int msgTimeout) {
        this.msgTimeout = msgTimeout;
    }
    public String getWindowHint() {
        return windowHint;
    }
    public void setWindowHint(String windowHint) {
        this.windowHint = windowHint;
    }
    public List<TopologyStatVO> getTopologyStats() {
        return topologyStats;
    }
    public void setTopologyStats(List<TopologyStatVO> topologyStats) {
        this.topologyStats = topologyStats;
    }
    public List<SpoutVO> getSpouts() {
        return spouts;
    }
    public void setSpouts(List<SpoutVO> spouts) {
        this.spouts = spouts;
    }
    public List<BoltVO> getBolts() {
        return bolts;
    }
    public void setBolts(List<BoltVO> bolts) {
        this.bolts = bolts;
    }
    @Override
    public String toString() {
        return "Topology profile:\n" + "id: " + this.id + "\n"
                + "name: " + this.name + "\n"
                + "status: " + this.status + "\n"
                + "executors: " + this.executorsTotal + "\n"
                + "tasks: " + this.tasksTotal
                + "#spouts: " + this.spouts.size()
                + "#bolts: " + this.bolts.size();
    } 
}


