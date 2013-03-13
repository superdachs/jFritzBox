package jfritzbox;

/**
 *
 * @author stk
 */
public class JFritzBox {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ProcessMonitor processMonitor = new ProcessMonitor();
        Thread processMonitorThread = new Thread(processMonitor);
        processMonitorThread.start();
        
        processMonitor.addProcessAndStart(new CallMonitor(), Boolean.TRUE, "monitor", Boolean.TRUE);
        
    }
    
    
}


