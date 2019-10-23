/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eloans.manager;

import java.net.URLEncoder;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
            String myUrl = "jdbc:mysql://localhost/eloans";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "juliusroot2");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables() {
        Connection conn = connect();
        try {
            String borrowers = "create table if not exists borrowers(id INT NOT NULL AUTO_INCREMENT,fname varchar(50),lname varchar(50), city varchar(50), idnumber varchar(50), phone varchar(50), address1 varchar(500), address2 varchar(500), flag int default 0, primary key(id), unique(phone))";
            String loans = "create table if not exists loans(id INT NOT NULL AUTO_INCREMENT,borrowerid int,fullname varchar(200),phone varchar(50),amount double,interest double,netamount double,totalpaid double default 0,issuedate  varchar(50),expecteddate  varchar(50),paydate  varchar(50),ledger varchar(200),collateral varchar(500),notes text, flag int default 0, primary key(id))";
            String paymenttrend = "create table if not exists paymenttrend(id INT NOT NULL AUTO_INCREMENT,loanid int,borrowerid int,amount double,balance double,ledger varchar(200),collateral varchar(500),notes text,paydate timestamp default current_timestamp, flag int default 0, primary key(id))";
            Statement stm = conn.createStatement();
            stm.execute(borrowers);
            stm.execute(loans);
            stm.execute(paymenttrend);
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

    public String header = "<!DOCTYPE html>"
            + "<!--"
            + "To change this license header, choose License Headers in Project Properties."
            + "To change this template file, choose Tools | Templates"
            + "and open the template in the editor."
            + "-->"
            + "<html>"
            + "    <head>"
            + "        <title>eloans</title>"
            + "        <meta charset='UTF-8'>"
            + "        <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "        <link href='https://fonts.googleapis.com/css?family=Barlow+Semi+Condensed' rel='stylesheet'>"
            + "        <script defer src='https://use.fontawesome.com/releases/v5.0.8/js/all.js'></script>"
            + "        <!-- Bootstrap -->"
            + "        <link href='css/bootstrap.min.css' rel='stylesheet'>"
            + "        <!-- custom -->"
            + "        <link href='css/eloans.css' rel='stylesheet'>"
            + "    </head>"
            + "    <body>"
            + "        <div class='container' style='margin-top:1%'>"
            + "            <div class='row'>"
            + "                <div class='col-lg-12'>"
            + "                    <h1><i class='fab fa-leanpub'></i> Eloans</h1>"
            + "                </div>                    "
            + "            </div>"
            + "        </div>";
    public String lnheader = "<!DOCTYPE html>"
            + "<!--"
            + "To change this license header, choose License Headers in Project Properties."
            + "To change this template file, choose Tools | Templates"
            + "and open the template in the editor."
            + "-->"
            + "<html>"
            + "    <head>"
            + "        <title>eloans</title>"
            + "        <meta charset='UTF-8'>"
            + "        <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "        <link href='https://fonts.googleapis.com/css?family=Barlow+Semi+Condensed' rel='stylesheet'>"
            + "        <script defer src='https://use.fontawesome.com/releases/v5.0.8/js/all.js'></script>"
            + "        <!-- Bootstrap -->"
            + "        <link href='css/bootstrap.min.css' rel='stylesheet'>"
            + "        <!-- custom -->"
            + "        <link href='css/eloans.css' rel='stylesheet'>"
            + "    </head>"
            + "    <body>"
            + "        <div class='container' style='margin-top:1%'>"
            + "            <div class='row'>"
            + "                <div class='col-lg-12'>"
            + "                    "
            + "                </div>                    "
            + "            </div>"
            + "        </div>";
    public String footer = "<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->"
            + "        <script src='js/jquery.js'></script>"
            + "        <!-- Include all compiled plugins (below), or include individual files as needed -->"
            + "        <script src='js/bootstrap.min.js'></script>"
            + "    </body>"
            + "</html>";

    public String getBorrowername(String id) {
        String res = "";
        try {
            DataStore data = new DataStore();
            Connection con = data.connect();
            String query = "SELECT  `phone`,`fname`,`lname` FROM `borrowers` WHERE `id`=" + id + " LIMIT 1";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                res = rs.getString(1) + " - " + rs.getString(2) + " " + rs.getString(3);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public String getLoanByID(String id) {
        String res = "";
        try {
            DataStore data = new DataStore();
            Connection con = data.connect();
            String query = "SELECT  `phone`,`fullname`,`netamount`,`totalpaid`,`interest`,`borrowerid` FROM `loans` WHERE `id`=" + id + " LIMIT 1";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                res = rs.getString(1) + "-" + rs.getString(2) + "-" + rs.getString(3) + "-" + rs.getString(4) + "-" + rs.getString(5) + "-" + rs.getString(6);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public String checkUser(String uname, String upass) {
        String res = "none";
        if (uname.equals("admin") && upass.equals("eloans2018")) {
            res = "found:Eloans Admin";
        }
        /*try {
            Connection con = connect();
            String query = "SELECT `full_names`,`vendor_code`,`id` FROM `users` WHERE  `uname`='" + uname + "' AND `upass`='" + upass + "'";
            //System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    res = "found:" + rs.getString(1)+":"+rs.getString(2)+":"+rs.getString(3);
                }
            }
            con.close();
        } catch (SQLException ex) {
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return res;
    }

}
