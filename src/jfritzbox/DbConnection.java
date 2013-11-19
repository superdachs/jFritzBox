/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jfritzbox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stk
 */
public class DbConnection implements Runnable {
    
    private String host;
    private String db;
    private String user;
    private String password;
    private Connection c;
    
    public DbConnection(String host, String db, String user, String password) {
        this.host = host;
        this.db = db;
        this.user = user;
        this.password = password;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void terminate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void connectToDb() {
        try {
            c = DriverManager.getConnection("jdbc:postgresql://" + host + "/" + db, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
