package org.mlgb.dsps.app.sale_recommend;

import org.mlgb.dsps.monitor.Jones;
import org.mlgb.dsps.monitor.PlanningFactory;
import org.mlgb.dsps.util.Consts;

import uni.akilis.helper.LoggerX;

/**
 * Start a thread for producing messages to Kafka when tuning an app. 
 * @author Leo
 *
 */
public class AppTuner {
    public static void main (String[] args) {
        Jones jones = new Jones();
        jones.uprisingProducer(Consts.TOPIC, PlanningFactory.createPlanning());
        LoggerX.println("Started producer.");
    }
}
