package org.mlgb.dsps.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * The design blueprint for producing messages.
 * Input rate is almost 150 msgs/s. 
 * @author Leo
 *
 */
public class OverloadPlanning extends Planning{
    public List<Integer> delayMilis;
    public int type;
    
    public OverloadPlanning() {
        this.type = PlanningFactory.OVER_LOADED;
        this.delayMilis = new ArrayList<>();
        this.delayMilis.add(7);
    }
    
}
