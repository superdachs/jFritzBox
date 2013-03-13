/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jfritzbox;

/**
 *
 * @author stk
 */
public interface Runnable extends java.lang.Runnable {

    @Override
    public void run();

    public void terminate();
}
