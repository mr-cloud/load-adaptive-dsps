package org.mlgb.dsps.monitor;
/**
 * Factory for Jones with singleton pattern.
 * @author Leo
 *
 */
public class JonesFactory {
    private static Jones jones;
    
    public static Jones createJones(){
        if (jones == null) {
            JonesFactory.jones = new Jones();
        }
        return JonesFactory.jones; 
    }
}
