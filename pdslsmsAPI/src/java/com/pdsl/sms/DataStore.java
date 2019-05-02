/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.sms;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juliuskun
 */
public class DataStore {

    public Connection conn = null;

    DataStore() {
        createTables();
    }

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/pdslsmsAPI";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");

            this.conn = conn;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }
    public Connection prefixconnect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/phoneprefixes";
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
            //serviceid = "6014702000147264";
            String transactions = "create table if not exists transactions(id INT NOT NULL AUTO_INCREMENT, msisdn varchar(20), customer varchar(500), message text, serviceid varchar(200), correlator varchar(200), prsp varchar(50), primary key(id))";
            String dlr_status= "CREATE TABLE IF NOT EXISTS `dlr_status` (`dlr_id` int(11) NOT NULL AUTO_INCREMENT, `msisdn` varchar(50) DEFAULT NULL, `correlator` varchar(50) DEFAULT NULL, `status` varchar(200) DEFAULT NULL, `dlr_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`dlr_id`))";
            Statement stm = conn.createStatement();
            stm.execute(transactions);
            stm.execute(dlr_status);
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
                while(rs.next()){
                res = rs.getString(2);
                }
            }else{
                res = "notfound";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public void bbinsert(String insert) {
        conn = connect();
        PreparedStatement pstm = null;
        try {

            //System.out.println(insert);
            pstm = conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
            conn.close();
            Logger.getLogger(DataStore.class.getName()).log(Level.INFO, "...DRL SAVED!!...");
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, "Error Updating PDSLSMSAPI Delivery Status" + ex);
        } finally {
        }
    }
}
