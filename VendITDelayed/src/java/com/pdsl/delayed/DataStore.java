package com.pdsl.delayed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataStore {

    public Connection conn = null;

    public DataStore() {
        //createTables();
    }

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/mobile_wallet";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");
            System.out.println("Connecting to the database...");
            this.conn = conn;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void insert(String insert) {
        this.conn = connect();
        PreparedStatement pstm = null;
        try {
            System.out.println(insert);

            pstm = this.conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
            return;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                pstm.close();
                this.conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}
