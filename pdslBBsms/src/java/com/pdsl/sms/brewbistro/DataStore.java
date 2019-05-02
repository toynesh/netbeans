package com.pdsl.sms.brewbistro;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataStore {

    public Connection conn = null;

    DataStore() {
        createTables();
    }

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/brewbistrosms";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");

            this.conn = conn;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables() {
        Connection conn = connect();
        try {
            String transactions = "create table if not exists transactions(id INT NOT NULL AUTO_INCREMENT, msisdn varchar(20), customer varchar(500), message text, serviceid varchar(200), correlator varchar(200), prsp varchar(50), primary key(id))";
            String dlr_status = "CREATE TABLE IF NOT EXISTS `dlr_status` (`dlr_id` int(11) NOT NULL AUTO_INCREMENT, `msisdn` varchar(50) DEFAULT NULL, `correlator` varchar(50) DEFAULT NULL, `status` varchar(200) DEFAULT NULL, `dlr_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`dlr_id`))";
            Statement stm = conn.createStatement();
            stm.execute(transactions);
            stm.execute(dlr_status);
            return;
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String checkCorr(String correlator) {
        String res = "none";
        try {
            Connection con = connect();
            String query = "select correlator from dlr_status where correlator='" + correlator + "' limit 1";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.isBeforeFirst()) {
                res = "used";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public String getDlrStatus(String correlator) {
        String res = "none";
        try {
            Connection con = connect();
            String query = "select correlator, status from dlr_status where correlator='" + correlator + "' limit 1";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    res = rs.getString(2);
                }
            }
            res = "notfound";

            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public void bbinsert(String insert) {
        this.conn = connect();
        PreparedStatement pstm = null;
        try {
            pstm = this.conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
            this.conn.close();
            Logger.getLogger(DataStore.class.getName()).log(Level.INFO, "...DRL SAVED!!...");
        } catch (SQLException ex) {
            ex
                    = ex;
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, "Error Updating BREWBISTRO Delivery Status" + ex);
        } finally {
        }
    }
}
