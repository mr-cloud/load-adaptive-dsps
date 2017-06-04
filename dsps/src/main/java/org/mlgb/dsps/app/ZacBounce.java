package org.mlgb.dsps.app;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.mlgb.dsps.analysis_plan.BaseZacBrain;
import org.mlgb.dsps.monitor.Planning;
import org.mlgb.dsps.monitor.PlanningFactory;
import org.mlgb.dsps.util.Consts;

import uni.akilis.helper.LoggerX;

/**
 * Launch Zac.  
 * @author Leo
 *
 */
public class ZacBounce extends TimerTask{
    public static final String TAG = ZacBounce.class.getName();
    private  BaseZacBrain zac;
    private Timer timer;
    
    public ZacBounce(BaseZacBrain zac, Timer timer) {
        this.zac = zac;
        this.timer = timer;
    }

    public static void main(String[] args){
        BaseZacBrain zac = null;
        // Parse strategy.
        if (args.length > 0 && "tuning".equalsIgnoreCase(args[0])) {
            zac = new BaseZacBrain();
            zac.setTuning(true);
        }
        else if (args.length > 0) {
            switch (args[0].toUpperCase()) {
            case Consts.STRATEGY_FIXED_THRESHOLD:
                zac = new BaseZacBrain();
                zac.setStrategy(Consts.STRATEGY_FIXED_THRESHOLD);
                break;
            case Consts.STRATEGY_THRESHOLD_BASED_OPT:
                // TODO
                break;
            case Consts.STRATEGY_REINFORCEMENT_LEARNING:
                // TODO
                break;
            }
            if (zac == null) {
                LoggerX.error(TAG, "Strategy " + args[0] + " is not supported now!");
                System.exit(1);
            }
        }
        else{
            // No parameter Error.
            LoggerX.error(TAG, "No parameter warning: \n"
                    + "tuning|<strategy> [<plan>] [<fined scaling toggle>]");
            System.exit(1);
        }
        
        // Parse planning
        Planning plan = null;
        if (args.length > 1) {
            plan = PlanningFactory.createPlanning(Integer.parseInt(args[1]));
            if (plan == null) {
                LoggerX.error(TAG, "Planning " + args[1] + " is not supported now!");
                System.exit(1);
            }
        }
        else{
            plan = PlanningFactory.createPlanning();
        }
        zac.setPlan(plan);
        
        // Parse scaling granularity: boolean, true for fined level 
        // (i.e., in thread or process level).
        if (args.length > 2) {
            if ("true".equalsIgnoreCase(args[2])) {
                zac.setFINED_SCALING_TOGGLE(true);
            }
            else if ("false".equalsIgnoreCase(args[2])) {
                zac.setFINED_SCALING_TOGGLE(false);
            }
            else{
                LoggerX.error(TAG, "Fined scaling toggle: " + args[2] + " must be TRUE or False (case insensitive)!");
                System.exit(1);
            }
        }
        
        // Start Zac
        zac.brainStorming();

        // Set timer to schedule experiments.
        // Daemon thread.
        Timer timer = new Timer(true);
        ZacBounce dancer = new ZacBounce(zac, timer);
        timer.schedule(dancer, Consts.TEST_RUNNING_TIME, Consts.TEST_RUNNING_TIME);        
   }

    @Override
    public void run() {
        LoggerX.println(TAG, "Timer task started at:" + new Date());
        // Stop Zac.
        zac.exit();        
        // Cancel experiment or continue to next experiment.
        timer.cancel();
        LoggerX.println(TAG, "Timer task finished at:" + new Date());
    }
}
