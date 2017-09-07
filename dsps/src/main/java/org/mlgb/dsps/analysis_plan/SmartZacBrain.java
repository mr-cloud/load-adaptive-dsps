package org.mlgb.dsps.analysis_plan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mlgb.dsps.util.Consts;
import org.mlgb.dsps.util.LoggerUtil;
import org.mlgb.dsps.util.Utils;
import org.mlgb.dsps.util.vo.BoltVO;
import org.mlgb.dsps.util.vo.MachinesStatsVO;

import uni.akilis.helper.LoggerX;

public class SmartZacBrain extends BaseZacBrain implements Optimizer{
    private boolean STOP_CRITERION = false;
    private long SEARCHING_TIME = 1000 * 60 * 1;
    private Random random = new Random();
    private Thread optimizer;
    private double[][] doms;
    private double[] gras;
    private int batch;
    private double cHighOrigin;
    private double cLowOrigin;
    private Queue<MachinesStatsVO> machines = new LinkedList<>();
    private Queue<ArrayList<BoltVO>> capacities = new LinkedList<ArrayList<BoltVO>>();
    public static final String TAG = SmartZacBrain.class.getName();
    private Logger myLogger = LoggerUtil.getLogger();
    private double historyCost;
    private double cur_y_opt;
    
    public SmartZacBrain() {
        super();
        this.setStrategy(Consts.STRATEGY_THRESHOLD_BASED_OPT);
        // Only consider the cHigh and cLow.
        doms = new double[2][2];
        gras = new double[2];

        Properties prop = Utils.loadConfigProperties(Consts.ZAC_CONFIG);
        if (prop == null) {
            myLogger.log(Level.SEVERE, "Cant load config file!");
            System.exit(1);
        }
        doms[0][0] = Double.parseDouble(prop.getProperty(Consts.CHIGH_INF, "0.8"));
        doms[0][1] = Double.parseDouble(prop.getProperty(Consts.CHIGH_SUP, "0.9"));
        gras[0] = Double.parseDouble(prop.getProperty(Consts.CHIGH_GRANULARITY, "0.01"));
        doms[1][0] = Double.parseDouble(prop.getProperty(Consts.CLOW_INF, "0.5"));
        doms[1][1] = Double.parseDouble(prop.getProperty(Consts.CLOW_SUP, "0.6"));
        gras[1] = Double.parseDouble(prop.getProperty(Consts.CLOW_GRANULARITY, "0.01"));
        
        batch = this.profiler.BATCH_SIZE;
        cHighOrigin = this.profiler.cHigh;
        cLowOrigin = this.profiler.cLow;
    }

    
    @Override
    public void brainStorming() {
        super.brainStorming();
        this.optimizer = new Thread(new Opt());
        this.optimizer.start();
    }


    @Override
    public void exit() {
        LoggerX.println(TAG, "Stopping optimizer ...");
        this.optimizer.interrupt();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.exit();
    }


    class Opt implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(Consts.BRAIN_WAKE_UP_TIME * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!Thread.interrupted()){
                try {
                    optimizing();
                    Thread.sleep(profiler.freezeT * 1000);
                } catch (InterruptedException e) {
                    break;
                }       
            }
            LoggerX.println(TAG, "\n\nOriginal thresholds:\n"
                    + "cHigh: " + cHighOrigin + "\n"
                    + "cLow: " + cLowOrigin + "\n\n");
            LoggerX.println(TAG, "\n\nOptimized thresholds:\n"
                    + "cHigh: " + profiler.cHigh + "\n"
                    + "cLow: " + profiler.cLow + "\n\n");
            LoggerX.println(TAG, "Exit Optimizer.");
        }
    }
    @Override
    public void optimizing() {
        if (this.profiler.capacities.size() < batch ) {
            LoggerX.debug(TAG, "Not enough samples.");
            return;
        }
        myLogger.log(Level.INFO, "***Optimized START***");
        this.machines.clear();
        this.capacities.clear();
        this.historyCost = 0;
        synchronized(this.profiler){
            for (int i = 0; i < batch; i++) {
                this.historyCost += this.profiler.machines.peek().getMachinesRunning();
                this.machines.offer(this.profiler.machines.poll());
                this.capacities.offer(this.profiler.capacities.poll());
            }
        }
        double[] opted = rrs(doms, gras);
        LoggerX.debug("\n\nOptimized thresholds:");
        StringBuilder sb = new StringBuilder();
        for (double param: opted) {
            LoggerX.debug(param);
            sb.append(String.valueOf(param))
                .append("\t");
            
        }
        myLogger.log(Level.INFO, sb.toString());            
        LoggerX.debug("\n\n");
        myLogger.log(Level.INFO, "history cost: " + this.historyCost + ", "
                + "optimized cost: " + this.cur_y_opt);
        if (this.cur_y_opt < this.historyCost) {
            synchronized(this.profiler){
                myLogger.log(Level.INFO, "Optimization success!");
                this.profiler.cHigh = opted[0];
                this.profiler.cLow = opted[1];            
            }   
        }
        myLogger.log(Level.INFO, "***Optimized END***");
    }
    
    /**
     * Cost function with given thresholds.
     * @param params
     * @return
     */
    public double cost(double[] params){
        int[] hp = new int[batch];
        int priceVM = 0;
        double cHigh = params[0];
        double cLow = params[1];
        Iterator<MachinesStatsVO> macIter = this.machines.iterator();
        Iterator<ArrayList<BoltVO>> capIter = this.capacities.iterator();
        for (int i = 0; i < batch; i++) {
            int h = macIter.next().getMachinesRunning();
            List<BoltVO> caps = capIter.next();
            double cap = Double.parseDouble(caps.get(0).getCapacity());
            // Impact factor.
            double alpha = 0.0;
            for (BoltVO bolt: caps) {
                alpha += Double.parseDouble(bolt.getCapacity());
            }
            alpha = alpha < Consts.ERROR_EPSILON? 1.0: cap / alpha;
            if (i == 0) {
                hp[0] = h;
            }
            else {
                // The more machines, the less capacity. 
                if (alpha * cap * h / hp[i-1] > cHigh) {
                    hp[i] = hp[i-1] + 1;
                }
                else if (alpha * cap * h / hp[i-1] < cLow) {
                    hp[i] = hp[i-1] - 1;
                }
                else{
                    hp[i] = hp[i-1];
                }
            }
            priceVM += hp[i];
        }        
        return priceVM;
    }
    

    /**
     * Recursive Random Search. 
     * Ref. A Recursive Random Search Algorithm For Large-Scale
Network Parameter Configuration
     * @param doms parameters space.
     * @param gras granularity for each space.  
     * @return best parameters.
     */
    public double[] rrs(double[][] doms, double gras[]){
        double p = 0.99, r = 0.1, q = 0.99, v = 0.8, c = 0.5, st = 0.001;
        return rrs(doms, gras, p, r, q, v, c, st);
    }

    /**
     * Recursive Random Search. 
     * Ref. A Recursive Random Search Algorithm For Large-Scale
Network Parameter Configuration
     * @param doms parameters space.
     * @param gras granularity for each space.  
     * @param p confidence for convergence in exploration.
     * @param r percentile in exploration.
     * @param q confidence for convergence in exploitation.
     * @param v percentile in  exploitation.
     * @param c percentile decay rate in exploitation.
     * @param st resolution of the optimization.
     * @return best parameters.
     */
    public double[] rrs(double[][] doms, double[] gras, double p, double r, double q, double v, double c, double st) {
        /*
         * Initialization
         */
        int n = (int) Math.round(Math.log(1 - p)/Math.log(1 - r));
        int l = (int) Math.round(Math.log(1 - q)/Math.log(1 - v));
        double[][] samples = randomSample(doms, gras, n);
        int minIdx = -1;
        double minCost = Double.MAX_VALUE;
        for (int i = 0; i < samples.length; i++) {
            double curCost = cost(samples[i]);
            if (curCost < minCost) {
                minIdx = i;
                minCost = curCost;
            }
        }
        double[] x0 = samples[minIdx];
        double yr = minCost;
        long numF = 1;
        int i = 0;
        int exploit_flag = 1;
        double[] x_opt = x0;
        double y_opt = yr;
        
        /*
         * Exploration
         */
        double yr_next = Double.MAX_VALUE;
        // Set timer for searching.
        long startTime = System.currentTimeMillis();
        while (STOP_CRITERION) {
            if (exploit_flag == 1) {
                /*
                 * Exploitation
                 */
                int j = 0;
                double fc = cost(x0);
                double[] xl = x0;
                double rou = r;
                double[][] doms_neighbor = findSampleSpace(doms, gras, rou, xl);
                while (rou > st) {
                    double[] xp = randomSample(doms_neighbor, gras);
                    double y = cost(xp);
                    // Realign
                    if (y < fc) {
                        xl = xp;
                        fc = y;
                    }
                    else{
                        j++;
                    }
                    // Shrink
                    if (j == l) {
                        rou *= c;
                        j = 0;
                    }
                    // Time out
                    if (System.currentTimeMillis() - startTime > SEARCHING_TIME) {
                        break;
                    }                }
                exploit_flag = 0;
                if (fc < y_opt) {
                    x_opt = xl;
                    y_opt = fc;
                }
            }
            x0 = randomSample(doms, gras);
            double y = cost(x0);
            yr_next = Math.min(y, yr_next);
            if (y < yr) {
                exploit_flag = 1;
            }
            if (i == n) {
                yr = (yr * numF + yr_next)/(numF+1);
                numF++;
                i = 0;
                yr_next = Double.MAX_VALUE;
            }
            i++;
            if (System.currentTimeMillis() - startTime > SEARCHING_TIME) {
                STOP_CRITERION = false;
                myLogger.log(Level.INFO, "***Optimized***"
                        + "\nTIME OUT\n"
                        + "***Optimized***");
            }
        }
        STOP_CRITERION = true;
        cur_y_opt = y_opt;
        return x_opt;
    }

    /**
     * Compute neighbor sample space for current parameters given  ρ.
     * @param doms
     * @param gras
     * @param rou
     * @param xl
     * @return 
     */
    private double[][] findSampleSpace(double[][] doms, double[] gras, double rou, double[] xl) {
        double[][] neighbor_space = new double[doms.length][2];
        double factor = Math.pow(rou, 1.0/doms.length);
        for (int i = 0; i < doms.length; i++) {
            double bound = factor * (doms[i][1] - doms[i][0]);
            neighbor_space[i][0] = xl[i] - bound;
            neighbor_space[i][1] = xl[i] + bound;
        }
        return neighbor_space;
    }

    /**
     * Sample one point uniformly randomly.
     * @param doms
     * @param gras
     * @return
     */
    private double[] randomSample(double[][] doms, double[] gras) {
        double[] sample = new double[doms.length];
        for (int i = 0; i < sample.length; i++) {
            int span = (int)((doms[i][1] - doms[i][0])/gras[i]);
            span = span < 0? 0: span;
            random.setSeed(i);
            int offset = random.nextInt(span + 1);
            sample[i] = doms[i][0] + gras[i] * offset; 
        }
        return sample;
    }

    /**
     * Sample n points uniformly randomly.
     * @param doms
     * @param gras
     * @param n
     * @return
     */
    private double[][] randomSample(double[][] doms, double[] gras, int n) {
        double[][] samples = new double[n][doms.length];
        for (int i = 0; i < n; i++) {
            samples[i] = randomSample(doms, gras);
        }
        return samples;
    }
}
