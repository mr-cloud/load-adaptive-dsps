package org.mlgb.dsps.monitor;

import org.mlgb.dsps.utils.ClusterSummaryVO;
import org.mlgb.dsps.utils.Consts;
import org.mlgb.dsps.utils.MachinesStatsVO;
import org.mlgb.dsps.utils.MessagesStatsVO;
import org.mlgb.dsps.utils.TopologiesSummaryVO;
import org.mlgb.dsps.utils.TopologyProfileVO;

import junit.framework.TestCase;
import uni.akilis.helper.LoggerX;

public class JonesTest extends TestCase {

    private Jones jones;
    
    public JonesTest(String name) {
        super(name);
        this.jones = new Jones();
    }

    public void testGetClusterSummary() {
        ClusterSummaryVO vo = this.jones.getClusterSummary();
        assertNotNull(vo);
        LoggerX.println(vo.toString());
        
    }

    public void testGetTopologiesSummary() {
       TopologiesSummaryVO vo = this.jones.getTopologiesSummary();
       assertNotNull(vo);
       LoggerX.println(vo.toString());
    }

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

    public void testGetMachinesStats() {
        MachinesStatsVO vo = this.jones.getMachinesStats();
        assertNotNull(vo);
        LoggerX.println(vo.toString());
    }

    public void testGetMessagesStats() {
        this.jones.uprising(Consts.TOPIC, new Planning());
        try {
            Thread.sleep(100*1000);
            MessagesStatsVO vo = this.jones.getMessagesStats();
            LoggerX.println(vo.toString());
            assertTrue(vo.getMessagesTotal() > 0 && vo.getMessagesConsumed() > 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
