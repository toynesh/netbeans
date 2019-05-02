/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package waitingsms;

import java.sql.*;

/**
 *
 * @author juliuskun
 */
public class DataStore {

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/mobile_wallet";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }
    
}
