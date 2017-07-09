package org.mlgb.dsps.monitor;
/**
 * 
 * @author Leo
 *
 */
public class PlanningFactory {
    public static final int OVER_LOADED = 1;
    public static final int UNDER_LOADED = 2;
    public static final int WIGGLE_LOADED = 0;
    public static final int DEFAULT_LOADED = WIGGLE_LOADED;
    
    public static Planning createPlanning(){
        return new Planning(DEFAULT_LOADED);
    }
    
    public static Planning createPlanning(int load){
        Planning plan = null;
        switch (load) {
        case OVER_LOADED:
            plan = new OverloadPlanning();
            break;
        case UNDER_LOADED:
            plan = new UnderloadPlanning();
            break;
        case WIGGLE_LOADED:
            plan = new Planning(WIGGLE_LOADED);
            break;
        default:
            plan = new Planning(DEFAULT_LOADED);
        }
        return plan;
    }
}
