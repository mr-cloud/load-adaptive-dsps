package org.mlgb.dsps.monitor;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.mlgb.dsps.utils.ClusterSummaryVO;
import org.mlgb.dsps.utils.Consts;
import org.mlgb.dsps.utils.MachinesStatsVO;
import org.mlgb.dsps.utils.MessagesStatsVO;
import org.mlgb.dsps.utils.TopologiesSummaryVO;
import org.mlgb.dsps.utils.TopologyProfileVO;

import uni.akilis.helper.LoggerX;

public class JonesTest {
    public static void main(String[] args) {
        JUnitCore.main("org.mlgb.dsps.monitor.JonesTest");
    }
    private Jones jones;

    @Before
    public void setUp() throws Exception {
        this.jones = new Jones();
        Thread.sleep(3000);
    }

    @After
    public void tearDown() throws Exception {
        this.jones.hibernate();
    }

    @Test
    public void testGetClusterSummary() {
        ClusterSummaryVO vo = this.jones.getClusterSummary();
        assertNotNull(vo);
        LoggerX.println(vo.toString());    
    }

    @Test
    public void testGetTopologiesSummary() {
        TopologiesSummaryVO vo = this.jones.getTopologiesSummary();
        assertNotNull(vo);
        LoggerX.println(vo.toString());
    }

    @Test
    public void testGetTopologyProfile() {
        TopologiesSummaryVO vo = this.jones.getTopologiesSummary();
        assertNotNull(vo);
        if (vo.getTopologies().size() == 0) {
            LoggerX.println("There is no running topogy.");
        }
        else {
            TopologyProfileVO topo = this.jones.getTopologyProfile(vo.getTopologies().get(0).getId());
            assertNotNull(topo);
            LoggerX.println(topo.toString());
        }
    }

    @Test
    public void testGetMachinesStats() {
        MachinesStatsVO vo = this.jones.getMachinesStats();
        assertNotNull(vo);
        LoggerX.println(vo.toString());
    }

    @Test
    public void testGetMessagesStats() {
        this.jones.uprising(Consts.TOPIC, new Planning());
        try {
            Thread.sleep(100*1000);
            MessagesStatsVO vo = this.jones.getMessagesStats();
            LoggerX.println(vo.toString());
            assertTrue(vo.getMessagesTotal() > 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
