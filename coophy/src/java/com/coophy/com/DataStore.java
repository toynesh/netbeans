/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coophy.com;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juliuskun
 */
public class DataStore {

    public DataStore() {
        createTables();
    }

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/coophy";
            Class.forName(myDriver);
            //conn = DriverManager.getConnection(myUrl, "root", "1root2");
            conn = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables() {
        try {
            Connection con = connect();
            String chlist = "CREATE TABLE IF NOT EXISTS chlist ("
                    + "id int(11) NOT NULL AUTO_INCREMENT,"
                    + "tvg_name varchar(200) NOT NULL,"
                    + "tvg_logo text NOT NULL,"
                    + "group_title varchar(200) NOT NULL,"
                    + "source_url text NOT NULL,"
                    + "PRIMARY KEY (id))";
            Statement stm = con.createStatement();
            stm.execute(chlist);
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] mijimiji() {
        String chlist = "";
        try {
            Connection con = connect();
            String query = "SELECT tvg_name,tvg_logo,group_title,source_url FROM chlist";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                chlist = chlist + rs.getString(1) + " manamanayama " + rs.getString(2) + " manamanayama " + rs.getString(3) + " manamanayama " + rs.getString(4) + ";";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("Channel List Size: " + chlist.size());
        //System.out.println("====================================================================");
        byte[] c = chlist.getBytes();
        return c;
    }

    public byte[] kaangeta() {
        String groups = "";
        try {
            Connection con = connect();
            String query = "SELECT DISTINCT group_title FROM chlist";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                groups = groups + rs.getString(1) + ";";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
       //System.out.println("Channel Group Size: " + groups.size());
        //System.out.println("====================================================================");
        byte[] g = groups.getBytes();
        return g;
    }
}
