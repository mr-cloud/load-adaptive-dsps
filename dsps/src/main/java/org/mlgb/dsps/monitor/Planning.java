package org.mlgb.dsps.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * The design blueprint for producing messages. 
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
        this.delayMilis.add(100);
        this.delayMilis.add(300);
        this.delayMilis.add(1000);
    }
    
    public Planning (int type) {
        this();
        this.type = type;
    }
}
