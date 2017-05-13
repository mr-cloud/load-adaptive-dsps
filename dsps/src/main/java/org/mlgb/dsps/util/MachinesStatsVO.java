package org.mlgb.dsps.util;

public class MachinesStatsVO {
    private int machinesTotal;
    private int machinesRunning;
    public int getMachinesTotal() {
        return machinesTotal;
    }
    public void setMachinesTotal(int machinesTotal) {
        this.machinesTotal = machinesTotal;
    }
    public int getMachinesRunning() {
        return machinesRunning;
    }
    public void setMachinesRunning(int machinesRunning) {
        this.machinesRunning = machinesRunning;
    }
    @Override
    public String toString() {
        return "Machines stats:\n" + "#machines: " + this.machinesTotal + "\n"
                + "#running machines: " + this.machinesRunning;
    }
    
}
