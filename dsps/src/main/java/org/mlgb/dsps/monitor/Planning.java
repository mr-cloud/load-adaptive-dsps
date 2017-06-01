package org.mlgb.dsps.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * The design blueprint for producing messages.
 * Default input rate is 1/(10ms) = 10^2 msg/s. 
 * @author Leo
 *
 */
public class Planning {
    public long messagesPerInterval;
    public List<Integer> delayMilis;
    public int type;
    
    public Planning() {
        super();
        this.messagesPerInterval = 10;
        this.delayMilis = new ArrayList<>();
        this.delayMilis.add(10);
    }
    
    public Planning (int type) {
        this();
        this.type = type;
    }
}
