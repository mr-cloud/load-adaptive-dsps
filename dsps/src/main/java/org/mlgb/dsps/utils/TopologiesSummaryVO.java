package org.mlgb.dsps.utils;

import java.util.List;

public class TopologiesSummaryVO {
    private List<TopologySummaryVO> topologies;
    private boolean schedulerDisplayResource;
    public List<TopologySummaryVO> getTopologies() {
        return topologies;
    }
    public void setTopologies(List<TopologySummaryVO> topologies) {
        this.topologies = topologies;
    }
    public boolean isSchedulerDisplayResource() {
        return schedulerDisplayResource;
    }
    public void setSchedulerDisplayResource(boolean schedulerDisplayResource) {
        this.schedulerDisplayResource = schedulerDisplayResource;
    }
    @Override
    public String toString() {
        return "Number of topogies: " + this.getTopologies().size();
    }
    
    
}
