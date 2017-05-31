package org.mlgb.dsps.monitor;
/**
 * 
 * @author Leo
 *
 */
public class PlanningFactory {
    public static final int DEFAULT_LOADED = 0;
    public static final int OVER_LOADED = 1;
    public static final int UNDER_LOADED = 2;
    public static final int WIGGLE_LOADED = 3;
    
    public static Planning createPlanning(){
        return new Planning(DEFAULT_LOADED);
    }
    
    public static Planning createPlanning(int load){
        Planning plan = null;
        switch (load) {
        case OVER_LOADED:
            // TODO
            break;
        case UNDER_LOADED:
            // TODO
            break;
        case WIGGLE_LOADED:
            // TODO
            break;
        default:
            plan = new Planning(DEFAULT_LOADED);
        }
        return plan;
    }
}
