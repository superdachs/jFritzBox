/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jfritzbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stk
 */
public class ProcessMonitor implements Runnable {

    private HashMap<Thread, Boolean> threads;
    private HashMap<Long, Boolean> persistant;
    private HashMap<Long, Runnable> runnables;
    
    public ProcessMonitor() {
        threads = new HashMap<>();
        persistant = new HashMap<>();
        runnables = new HashMap<>();
    }

    @Override
    public void run() {

        while (true) {

//            System.out.println("ProcessMonitor alive!");

            for (Entry e : threads.entrySet()) {

                Thread t = (Thread) e.getKey();
                Boolean s = (Boolean) e.getValue();
                if (!t.isAlive() && s) {
                    t.start();
                }
                if (!t.isAlive() && !persistant.get(t.getId())) {
                    threads.remove(t);
                    persistant.remove(t.getId());
                    runnables.remove(t.getId());
                }
//                System.out.println("Thread: " + t.getId() + " - " + t.getName() 
//                        + " running: " + t.isAlive() + " restart: " + e.getValue() 
//                        + " persistant: " + persistant.get(t.getId()));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProcessMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }

    private Thread getThreadByName(String name) {
        for (Entry e : threads.entrySet()) {
            if (((Thread) e.getKey()).getName().equals(name)) {
                return (Thread) e.getKey();
            }
        }
        return null;
    }

    public List<String> getThreadNames() {
        List<String> result = new ArrayList<>();
        for (Entry e : threads.entrySet()) {
            result.add(((Thread) e.getKey()).getName());
        }
        return result;
    }

    public void addProcessAndStart(Runnable processObject, Boolean restartIfDied, String name, Boolean isPersistant) {
        Thread t = new Thread(processObject);
        t.setName(name);

        t.start();

        threads.put(t, restartIfDied);
        persistant.put(t.getId(), isPersistant);
        runnables.put(t.getId(), processObject);
    }

    public void restartThread(String name) {
        Thread t = getThreadByName(name);
        if (t != null && !t.isAlive()) {
            t.start();
        }
    }

    public void stopThread(String name) {
        Thread t = getThreadByName(name);
        if (t.isAlive()) {
            ((Runnable) runnables.get(t.getId())).terminate();
        }
    }

    @Override
    public void terminate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
