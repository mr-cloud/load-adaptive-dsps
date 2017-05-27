package org.mlgb.dsps.util.vo;

public class RebalanceSuccessResultVO {
    private String topologyOperation;
    private String topologyId;
    private String status;
    public String getTopologyOperation() {
        return topologyOperation;
    }
    public void setTopologyOperation(String topologyOperation) {
        this.topologyOperation = topologyOperation;
    }
    public String getTopologyId() {
        return topologyId;
    }
    public void setTopologyId(String topologyId) {
        this.topologyId = topologyId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "rebalacne: " + this.status + "\n"
                + "topology: " + this.getTopologyId();
    }
    
}
