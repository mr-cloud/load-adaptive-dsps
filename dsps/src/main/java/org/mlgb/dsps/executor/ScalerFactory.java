package org.mlgb.dsps.executor;
/**
 * Scaler factory with singleton pattern.
 * @author MrCloud
 *
 */
public class ScalerFactory {
    private static Scaler scaler;
    
    public static Scaler createScaler(){
        if (scaler == null) {
            scaler = new Scaler();
        }
        return scaler;
    }
}
