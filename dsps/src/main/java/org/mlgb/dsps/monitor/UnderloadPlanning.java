package org.mlgb.dsps.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * The design blueprint for producing messages.
 * Input rate is almost 50 msg/s. 
 * @author Leo
 *
 */
public class UnderloadPlanning extends Planning{
    public List<Integer> delayMilis;
    public int type;
    
    public UnderloadPlanning() {
        this.type = PlanningFactory.UNDER_LOADED;
        this.delayMilis = new ArrayList<>();
        this.delayMilis.add(20);
    }
}
