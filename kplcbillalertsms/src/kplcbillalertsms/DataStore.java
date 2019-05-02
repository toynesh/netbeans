/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kplcbillalertsms;

import java.sql.*;

/**
 *
 * @author juliuskun
 */
public class DataStore {

    public Connection conn = null;

    public Connection kplcconnect() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@//localhost:1521/kplcdb";
            conn = DriverManager.getConnection(url, "root", "1root2");

            this.conn = conn;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }    
    
}
