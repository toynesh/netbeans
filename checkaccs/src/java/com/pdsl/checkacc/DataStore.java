package com.pdsl.checkacc;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataStore {

    public DataStore() {
        createTables();
    }

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/AirtelMoney";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");
            System.out.println("Connecting to the database...");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void insert(String insert) {
        Connection conn = connect();
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
            return;
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void createTables() {
        Connection conn = connect();
        try {
            String transaction = "create table if not exists checkaccounts(vendid INT NOT NULL AUTO_INCREMENT, reqid INT, clientid varchar(50), seqnumber varchar(50), account_number varchar(50), amount varchar(50), status varchar(50), refnumber varchar(50) NOT NULL, time varchar(50), msisdn varchar(50), terminal varchar(50), primary key(vendid))";

            Statement stm = conn.createStatement();
            stm.execute(transaction);
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String query = "SELECT * from checkaccounts";

            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (!rs.isBeforeFirst()) {
                String insert = "INSERT INTO `AirtelMoney`.`checkaccounts` ( `refnumber`) VALUES ( '00000001')";
                insert(insert);
            }
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
