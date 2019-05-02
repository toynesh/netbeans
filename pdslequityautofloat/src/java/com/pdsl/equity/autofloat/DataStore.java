package com.pdsl.equity.autofloat;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julius
 */
public class DataStore {

    public DataStore() {
        createTables();

    }

    public Connection connect() {
        Connection conn = null;
        try {

            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/equityautofloat";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());

        }
        return conn;

    }

    public void createTables() {
        try {
            Connection con = connect();
            String transactions = "CREATE TABLE IF NOT EXISTS transactions ("
                    + "id int(11) NOT NULL AUTO_INCREMENT,"
                    + "time_recieved timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "billNumber varchar(100) NOT NULL,"
                    + "billAmount varchar(200) NOT NULL,"
                    + "customerRefNumber varchar(1000) NOT NULL,"
                    + "bankreference varchar(1000) NOT NULL,"
                    + "paymentMode varchar(100) NOT NULL,"
                    + "transactionDate varchar(200) NOT NULL,"
                    + "phonenumber varchar(100) NOT NULL,"
                    + "debitaccount varchar(200) NOT NULL,"
                    + "debitcustname varchar(300) NOT NULL,"
                    + "username varchar(200) NOT NULL,"
                    + "password varchar(500) NOT NULL,"
                    + "PRIMARY KEY (id))";
            String aggregators = "CREATE TABLE IF NOT EXISTS aggregators ("
                    + " id int(11) NOT NULL AUTO_INCREMENT,"
                    + " ag_code varchar(100) DEFAULT NULL,"
                    + " ag_name varchar(200) DEFAULT NULL,"
                    + " description text,"
                    + " retailer_type varchar(200) DEFAULT NULL,"
                    + " eazzybiz text,"
                    + " cheque text,"
                    + " rtgs text,"
                    + " PRIMARY KEY (id))";
            String vendors = "CREATE TABLE IF NOT EXISTS vendors ("
                    + " id int(11) NOT NULL AUTO_INCREMENT,"
                    + " vendor_code varchar(100) NOT NULL,"
                    + " vendor_name varchar(200) NOT NULL,"
                    + " description text NOT NULL,"
                    + " retailer_type varchar(200) NOT NULL,"
                    + " eazzybiz text,"
                    + " cheque text,"
                    + " rtgs text,"
                    + " PRIMARY KEY (id))";
            Statement stm = con.createStatement();
            stm.execute(transactions);
            stm.execute(aggregators);
            stm.execute(vendors);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insert(String insert) {
        Connection conn = connect();
        PreparedStatement pstm = null;
        try {
            System.out.println(insert);
            pstm = conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
        } catch (SQLException ex) {
        } finally {
            try {
                pstm.close();
                conn.close();

            } catch (SQLException ex) {
                //ex.printStackTrace();
            }
        }

    }

}
