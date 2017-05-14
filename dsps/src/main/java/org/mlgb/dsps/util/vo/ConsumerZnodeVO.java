package org.mlgb.dsps.util.vo;

public class ConsumerZnodeVO {
    private ZnodeTopologyVO topology;
    private long offset;
    private int partition;
    private ZnodeBrokerVO broker;
    private String topic;
    public ZnodeTopologyVO getTopology() {
        return topology;
    }
    public void setTopology(ZnodeTopologyVO topology) {
        this.topology = topology;
    }
    public long getOffset() {
        return offset;
    }
    public void setOffset(long offset) {
        this.offset = offset;
    }
    public int getPartition() {
        return partition;
    }
    public void setPartition(int partition) {
        this.partition = partition;
    }
    public ZnodeBrokerVO getBroker() {
        return broker;
    }
    public void setBroker(ZnodeBrokerVO broker) {
        this.broker = broker;
    }
    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    
}
