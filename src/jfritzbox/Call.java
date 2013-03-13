/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jfritzbox;

import java.util.Date;

/**
 *
 * @author stk
 */
public class Call {
    
    public enum Direction {IN, OUT};
    
    private Integer id;
    private boolean isActive;
    private boolean answered;
    private String remoteNumber;
    private String localNumber;
    private Date start;
    private Date connect;
    private Date end;
    private Direction direction;
    
    Call(Integer id, String remoteNumber, String localNumber, Date start, Direction direction) {
        this.remoteNumber = remoteNumber;
        this.localNumber = localNumber;
        this.start = start;
        this.isActive = true;
        this.id = id;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void answer(Date connect) {
        this.connect = connect;
        this.answered = true;
    }
    
    public void end(Date end) {
        this.end = end;
        this.isActive = false;
    }
    
    
}
