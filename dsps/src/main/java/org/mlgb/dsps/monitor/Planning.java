package org.mlgb.dsps.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * The design blueprint for producing messages.
 * Default input rate is almost 150 msgs/s, 100 msgs/s, 30 msg/s. 
 * @author Leo
 *
 */
public class Planning {
    public List<Integer> delayMilis;
    public int type;
    
    public Planning() {
        super();
        this.type = PlanningFactory.DEFAULT_LOADED;
        this.delayMilis = new ArrayList<>();
        this.delayMilis.add(10);
        this.delayMilis.add(7);
        this.delayMilis.add(30);
    }
    
    public Planning (int type) {
        this();
        this.type = type;
    }
}
