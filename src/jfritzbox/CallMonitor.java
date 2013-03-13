/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jfritzbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jfritzbox.Call.Direction;

/**
 *
 * @author stk
 */
public class CallMonitor implements Runnable {

    private boolean terminate = false;
    private Socket socket;
    private BufferedReader in;
    private String fbAddress = "192.168.1.1";
    private int fbPort = 1012;
    
    private HashMap<Integer, Call> activeCalls;
    private LinkedList<Object> oldCalls;
    
    CallMonitor() {

        
        this.activeCalls = new HashMap<>();
        this.oldCalls = new LinkedList<>();
        
        connect();

    }

    private void connect() {
        try {
            socket = new Socket("192.168.1.1", 1012);
            socket.setKeepAlive(true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException ex) {
            Logger.getLogger(CallMonitor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CallMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (!terminate) {

            while (!socket.isConnected()) {
                connect();
            }

            String line;
            try {
                while ((line = in.readLine()) != null) {
                    parseLine(line);
                }
            } catch (IOException ex) {
                Logger.getLogger(CallMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CallMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void terminate() {
        this.terminate = true;
    }

    private void parseLine(String line) {

        String dateStr = line.split(" ")[0];
        String dayStr = dateStr.split("\\.")[0];
        String monthStr = dateStr.split("\\.")[1];
        String yearStr = dateStr.split("\\.")[2];

        Integer day = Integer.parseInt(dayStr);
        Integer month = Integer.parseInt(monthStr);
        Integer year = Integer.parseInt(yearStr);
        year = year + 100;

        String timeStr;
        timeStr = line.split(";")[0];
        timeStr = timeStr.split(" ")[1];

        String hourStr = timeStr.split(":")[0];
        String minStr = timeStr.split(":")[1];
        String secStr = timeStr.split(":")[2];

        Integer hours = Integer.parseInt(hourStr);
        Integer minutes = Integer.parseInt(minStr);
        Integer seconds = Integer.parseInt(secStr);

        Date date = new Date();
        date.setHours(hours);
        date.setMinutes(minutes);
        date.setSeconds(seconds);
        date.setDate(day);
        date.setMonth(month);
        date.setYear(year);

        Integer callId = Integer.parseInt(line.split(";")[2]);
        String reason = line.split(";")[1];

        switch (reason) {
            case "RING":
                newInCall(callId, date, line);
                break;
            case "CALL":
                newOutCall(callId, date, line);
                break;
            case "CONNECT":
                connectCall(callId, date, line);
                break;
            case "DISCONNECT":
                disconnectCall(callId, date, line);
                break;
            default:
                break;
        }
    }

    private void newInCall(Integer callId, Date date, String line) {
        String[] lineArray = line.split(";");
        System.out.println("ID " + callId + " RING - " + date + " FROM " + lineArray[3] + " ON " + lineArray[4]);
        Call c = new Call(callId, lineArray[3], lineArray[4], date, Direction.IN);
        activeCalls.put(callId, c);
    }

    private void newOutCall(Integer callId, Date date, String line) {
        String[] lineArray = line.split(";");
        System.out.println("ID " + callId + " OUT - " + date + " FROM " + lineArray[3] + " ON " + lineArray[4]);
        Call c = new Call(callId, lineArray[3], lineArray[4], date, Direction.IN);
        activeCalls.put(callId, c);
    }

    private void connectCall(Integer callId, Date date, String line) {
        System.out.println("ID " + callId + " CONNECT");
        activeCalls.get(callId).answer(date);

    }

    private void disconnectCall(Integer callId, Date date, String line) {
        System.out.println("ID " + callId + " DISCONNECT - " + date);
        activeCalls.get(callId).end(date);
        oldCalls.add(activeCalls.get(callId));
        activeCalls.remove(callId);
    }
}
