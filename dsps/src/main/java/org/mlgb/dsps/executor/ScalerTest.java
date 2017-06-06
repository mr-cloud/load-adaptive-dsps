package org.mlgb.dsps.executor;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.mlgb.dsps.monitor.Jones;
import org.mlgb.dsps.util.Consts;
import org.mlgb.dsps.util.vo.BoltVO;
import org.mlgb.dsps.util.vo.TopologiesSummaryVO;
import org.mlgb.dsps.util.vo.TopologyProfileVO;

import uni.akilis.helper.LoggerX;

/**
 * Testing Env: 
 *  Storm cluster 
 *  Storm UI
 *  MongoDB
 *  Kafka
 *  Topology: ToyApp
 *  vbmanager
 *  
 *  @author Leo
 *
 */
public class ScalerTest {

    public static void main(String[] args) {
        JUnitCore.main("org.mlgb.dsps.executor.ScalerTest");
    }
    private Jones jones;
    private Scaler scaler;
    
    /**
     * Assertion metric.
     */
    private int beforeA;
    private int afterA;
    private int beforeB;
    private int afterB;
    
    @Before
    public void setUp() throws Exception {
        this.jones = new Jones();
        Thread.sleep(3000);
        this.scaler = ScalerFactory.createScaler();
        this.beforeA = 0;
        this.afterA = 0;
        this.beforeB = 0;
        this.afterB = 0;
    }

    @After
    public void tearDown() throws Exception {
        this.jones.hibernate();
        Thread.sleep(10000);
    }


    @Test
    public void testScaleOut() {
        LoggerX.println("\nTest method: " + "testScaleOut" + "\n");
        beforeA = this.jones.getMachinesStats().getMachinesRunning();
        assertTrue(this.scaler.scaleOut());
        afterA = this.jones.getMachinesStats().getMachinesRunning();
        assertTrue(afterA == beforeA + 1);
    }

    @Test
    public void testScaleIn() {
        LoggerX.println("\nTest method: " + "testScaleIn" + "\n");

        // Scale out first.
        this.testScaleOut();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        beforeA = this.jones.getMachinesStats().getMachinesRunning();
        assertTrue(this.scaler.scaleIn());
        afterA = this.jones.getMachinesStats().getMachinesRunning();
        assertTrue(afterA == beforeA - 1);
    }

    @Test
    public void testRebalanceCluster() {
        LoggerX.println("\nTest method: " + "testRebalanceCluster" + "\n");
        // Scale up testing.
        
        Properties prop = new Properties();
        TopologiesSummaryVO vo = this.jones.getTopologiesSummary();
        assertNotNull(vo);
        if (vo.getTopologies().size() == 0) {
            fail("There is no running topogy.");
        }
        // Rebalance in Process level. 
        TopologyProfileVO topo = this.jones.getTopologyProfile(vo.getTopologies().get(0).getId());
        prop.put(Consts.REBALANCE_PARAMETER_id, vo.getTopologies().get(0).getId());
        prop.put(Consts.REBALANCE_PARAMETER_wait_time, Consts.REBALANCE_DEFAUT_WAIT_TIME_SECONDS);
        prop.put(Consts.REBALANCE_PARAMETER_numWorkers, topo.getWorkersTotal() + 1);
        beforeA = topo.getWorkersTotal();
        assertTrue(this.scaler.rebalanceCluster(prop));
        afterA = this.jones.getTopologyProfile(vo.getTopologies().get(0).getId()).getWorkersTotal();
        assertTrue(afterA == beforeA + 1);
        
        // Rebalance in Thread level.
        prop = new Properties();
        prop.put(Consts.REBALANCE_PARAMETER_id, vo.getTopologies().get(0).getId());
        prop.put(Consts.REBALANCE_PARAMETER_wait_time, Consts.REBALANCE_DEFAUT_WAIT_TIME_SECONDS);
        List<BoltVO> bolts= topo.getBolts();
        Collections.sort(bolts);
        prop.put(Consts.REBALANCE_PARAMETER_bolt_id, bolts.get(0).getBoltId());
        prop.put(Consts.REBALANCE_PARAMETER_numExecutors, bolts.get(0).getExecutors() + 1);
        beforeA = bolts.get(0).getExecutors();
        assertTrue(this.scaler.rebalanceCluster(prop));
        afterA = this.jones.getTopologyProfile(vo.getTopologies().get(0).getId()).getBolts().get(0).getExecutors();
        assertTrue(afterA == beforeA + 1);
    }

    @Test
    public void testScaleOutProperties() {
        LoggerX.println("\nTest method: " + "testScaleOutProperties" + "\n");

        Properties prop = new Properties();
        TopologiesSummaryVO vo = this.jones.getTopologiesSummary();
        assertNotNull(vo);
        if (vo.getTopologies().size() == 0) {
            fail("There is no running topogy.");
        }
        TopologyProfileVO topo = this.jones.getTopologyProfile(vo.getTopologies().get(0).getId());
        prop.put(Consts.REBALANCE_PARAMETER_id, vo.getTopologies().get(0).getId());
        prop.put(Consts.REBALANCE_PARAMETER_wait_time, Consts.REBALANCE_DEFAUT_WAIT_TIME_SECONDS);
        prop.put(Consts.REBALANCE_PARAMETER_numWorkers, topo.getWorkersTotal() + 1);
        this.beforeA = this.jones.getMachinesStats().getMachinesRunning();
        this.beforeB = topo.getWorkersTotal();
        assertTrue(this.scaler.scaleOut(prop));
        this.afterA = this.jones.getMachinesStats().getMachinesRunning();
        this.afterB = this.jones.getTopologyProfile(vo.getTopologies().get(0).getId()).getWorkersTotal();
        assertTrue(afterA == beforeA + 1);
        assertTrue(afterB == beforeB + 1);
    }

    @Test
    public void testScaleInProperties() {
        LoggerX.println("\nTest method: " + "testScaleInProperties" + "\n");

        // Scale out first.
        this.testScaleOutProperties();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Properties prop = new Properties();
        TopologiesSummaryVO vo = this.jones.getTopologiesSummary();
        assertNotNull(vo);
        if (vo.getTopologies().size() == 0) {
            fail("There is no running topogy.");
        }
        TopologyProfileVO topo = this.jones.getTopologyProfile(vo.getTopologies().get(0).getId());
        prop.put(Consts.REBALANCE_PARAMETER_id, vo.getTopologies().get(0).getId());
        prop.put(Consts.REBALANCE_PARAMETER_wait_time, Consts.REBALANCE_DEFAUT_WAIT_TIME_SECONDS);
        prop.put(Consts.REBALANCE_PARAMETER_numWorkers, topo.getWorkersTotal() - 1);
        this.beforeA = this.jones.getMachinesStats().getMachinesRunning();
        this.beforeB = topo.getWorkersTotal();
        assertTrue(this.scaler.scaleIn(prop));
        this.afterA = this.jones.getMachinesStats().getMachinesRunning();
        this.afterB = this.jones.getTopologyProfile(vo.getTopologies().get(0).getId()).getWorkersTotal();
        assertTrue(afterA == beforeA - 1);
        assertTrue(afterB == beforeB - 1);
    }

}
