/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpesalogger;

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
            String mpesa = "create table if not exists mpesa" + tdate + "(id INT NOT NULL AUTO_INCREMENT, MessageId varchar(200), MpesaAccountnumber varchar(200), MpesaAllocated varchar(200), MpesaOriginaltext varchar(200), MpesaSendermobile varchar(50), MpesaSendername varchar(200), MpesaTerminal varchar(200), MpesaTxcode varchar(200), MpesaAmount varchar(200), MpesaDatetime varchar(200), MpesaId varchar(200), primary key(id), unique(MpesaTxcode))";
            Statement stm = conn.createStatement();
            stm.execute(mpesa);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
