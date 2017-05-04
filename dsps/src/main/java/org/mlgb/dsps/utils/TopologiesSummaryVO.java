package org.mlgb.dsps.utils;

import java.util.List;

public class TopologiesSummaryVO {
    private List<TopologyProfileVO> topologies;
    private boolean schedulerDisplayResource;
    public List<TopologyProfileVO> getTopologies() {
        return topologies;
    }
    public void setTopologies(List<TopologyProfileVO> topologies) {
        this.topologies = topologies;
    }
    public boolean isSchedulerDisplayResource() {
        return schedulerDisplayResource;
    }
    public void setSchedulerDisplayResource(boolean schedulerDisplayResource) {
        this.schedulerDisplayResource = schedulerDisplayResource;
    }
    
}
