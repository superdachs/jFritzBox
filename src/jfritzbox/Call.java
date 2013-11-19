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

    /**
     * get the remote number
     *
     * @return the remote number as String
     */
    String getRemoteNumber() {
        return remoteNumber;
    }

    String getLocalNumber() {
        return localNumber;
    }

    Date getStartDate() {
        return start;
    }

    Date getConnectDate() {
        return connect;
    }

    Date getEndDate() {
        return end;
    }

    Direction getDirection() {
        return this.direction;
    }

    void setConnectDate(Date endDate) {
        this.connect = endDate;
    }

    public enum Direction {

        IN, OUT
    };

    private Integer id;             //  Database fields:
    private boolean isActive;       //
    private boolean answered;       //
    private String remoteNumber;    //  remoteNumber    char[]
    private String localNumber;     //  localNumber     char[]
    private Date start;             //  start           date
    private Date connect;           //  connect         date
    private Date end;               //  end             date
    private Direction direction;    //  direction       char (i/o)

    Call(Integer id, String remoteNumber, String localNumber, Date start, Direction direction) {
        this.remoteNumber = remoteNumber;
        this.localNumber = localNumber;
        this.start = start;
        this.isActive = true;
        this.id = id;
        this.direction = direction;
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
