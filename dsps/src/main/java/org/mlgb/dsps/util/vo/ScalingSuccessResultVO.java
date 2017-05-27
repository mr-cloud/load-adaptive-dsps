package org.mlgb.dsps.util.vo;

public class ScalingSuccessResultVO {
    /**
     * should be 'success'.
     */
    private String rst;
    /**
     * machine name started up or turned down.
     */
    private String msg;
    public String getRst() {
        return rst;
    }
    public void setRst(String rst) {
        this.rst = rst;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    @Override
    public String toString() {
        return "Scaling: " + this.rst + "\n"
                + "Machine: " + this.msg;
    }
    
    
}
