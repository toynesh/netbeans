/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipaylogger;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juliuskun
 */
public class DataStore {

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/audits";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables(String tdate) {
        try {
            Connection conn = connect();
            String ipay = "create table if not exists ipay" + tdate + "(id INT NOT NULL AUTO_INCREMENT, req_sent varchar(200), time_completed varchar(200), phone_num varchar(50), meter_account varchar(200), amt varchar(200), success varchar(200), res varchar(200), ref_received varchar(200), external_ref varchar(200), token varchar(200), primary key(id))";
            Statement stm = conn.createStatement();
            stm.execute(ipay);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
}
