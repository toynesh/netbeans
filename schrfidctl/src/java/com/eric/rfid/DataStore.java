/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eric.rfid;

import java.sql.*;
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
            String myUrl = "jdbc:mysql://localhost/rfids";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables() {
        Connection conn = connect();
        try {
            String users = "create table if not exists users(id INT NOT NULL AUTO_INCREMENT, full_name varchar(200), bus_assigned varchar(200), user_name varchar(200), user_password text, primary key(id))";
            String students = "create table if not exists students(id INT NOT NULL AUTO_INCREMENT, rfid varchar(50), full_name varchar(200), parent_name varchar(200), parent_email varchar(200), parent_msisdn varchar(50), primary key(id), unique(rfid))";
            String trips = "create table if not exists trips(id INT NOT NULL AUTO_INCREMENT, rfid varchar(50), requesttype varchar(200), driver varchar(200), lastlocation text,triptime timestamp not null default current_timestamp, flag int default 0, primary key(id))";
            Statement stm = conn.createStatement();
            stm.execute(users);
            stm.execute(students);
            stm.execute(trips);
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

    public String chechPD(String rfid) {
        String res = "none";
        try {
            Connection con = connect();
            String query = "SELECT `rfid`,`requesttype`,`driver` FROM `trips` WHERE `flag`=0 AND `rfid`='" + rfid + "'";
            //System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    res = "found:" + rs.getString(2)+":"+ rs.getString(3);
                }
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("check res: "+res);
        return res;
    }

    public String getBus(String driver) {
        String res = "";
        try {
            Connection con = connect();
            String query = "SELECT `bus_assigned`,`user_name` FROM `users` WHERE `user_name`='" + driver + "'";
            //System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                res = rs.getString(1);
            }
            con.close();
        } catch (SQLException ex) {
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public int getPstndts(String _driver, String requesttype) {
        int res = 0;
        try {
            Connection con = connect();
            String query = "SELECT COUNT(*) FROM `trips` WHERE `flag`=0 AND `driver`='" + _driver + "' AND `requesttype`='" + requesttype + "'";
            //System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    res = rs.getInt(1);
                }
            }
            con.close();
        } catch (SQLException ex) {
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public String header = "<!DOCTYPE html>"
            + "<html lang='en'>"
            + "    <head>"
            + "        <meta charset='utf-8'>"
            + "        <meta http-equiv='X-UA-Compatible' content='IE=edge'>"
            + "        <meta name='viewport' content='width=device-width, initial-scale=1'>"
            + "        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->"
            + "        <title>SCHRIFD</title>"
            + ""
            + "        <!-- Bootstrap -->"
            + "        <link href='css/bootstrap.min.css' rel='stylesheet'>"
            + "    </head>"
            + "    <body>"
            + "        <nav class='navbar navbar-default navbar-inverse'>"
            + "            <div class='container-fluid'>"
            + "                <!-- Brand and toggle get grouped for better mobile display -->"
            + "                <div class='navbar-header'>"
            + "                    <button type='button' class='navbar-toggle collapsed' data-toggle='collapse' data-target='#bs-example-navbar-collapse-1' aria-expanded='false'>"
            + "                        <span class='sr-only'>Toggle navigation</span>"
            + "                        <span class='icon-bar'></span>"
            + "                        <span class='icon-bar'></span>"
            + "                        <span class='icon-bar'></span>"
            + "                    </button>"
            + "                    <a class='navbar-brand' href='ManageStudents'>SMART SCHOOL BUS SYSTEM</a>"
            + "                </div>"
            + ""
            + "                <!-- Collect the nav links, forms, and other content for toggling -->"
            + "                <div class='collapse navbar-collapse' id='bs-example-navbar-collapse-1'>"
            + "                    <ul class='nav navbar-nav'>"
            + "                        <li class='active'><a href='ManageStudents'>Manage Students<span class='sr-only'>(current)</span></a></li>"
            + "                        <li><a href='ManageUsers'>Manage Drivers</a></li>"
            + "                        <li><a href='BusReport'>Bus Reports</a></li>"
            + "                        <!--li class='dropdown'>"
            + "                            <a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>Dropdown <span class='caret'></span></a>"
            + "                            <ul class='dropdown-menu'>"
            + "                                <li><a href='#'>Action</a></li>"
            + "                                <li><a href='#'>Another action</a></li>"
            + "                                <li><a href='#'>Something else here</a></li>"
            + "                                <li role='separator' class='divider'></li>"
            + "                                <li><a href='#'>Separated link</a></li>"
            + "                                <li role='separator' class='divider'></li>"
            + "                                <li><a href='#'>One more separated link</a></li>"
            + "                            </ul>"
            + "                        </li-->"
            + "                    </ul>"
            + "                    <!--form class='navbar-form navbar-right'>"
            + "                        <div class='form-group'>"
            + "                            <input type='text' class='form-control' placeholder='Search'>"
            + "                        </div>"
            + "                        <button type='submit' class='btn btn-default'>Submit</button>"
            + "                    </form>"
            + "                    <ul class='nav navbar-nav navbar-right'>"
            + "                        <!--li><a href='#'>Link</a></li>"
            + "                        <li class='dropdown'>"
            + "                            <a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'>Report<span class='caret'></span></a>"
            + "                            <ul class='dropdown-menu'>"
            + "                                <li><a href='#'>Action</a></li>"
            + "                                <li><a href='#'>Another action</a></li>"
            + "                                <li><a href='#'>Something else here</a></li>"
            + "                                <li role='separator' class='divider'></li>"
            + "                                <li><a href='#'>Separated link</a></li>"
            + "                            </ul>"
            + "                        </li>"
            + "                    </ul-->"
            + "                </div><!-- /.navbar-collapse -->"
            + "            </div><!-- /.container-fluid -->"
            + "        </nav>";
    public String footer = "<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->"
            + "        <script src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>"
            + "        <!-- Include all compiled plugins (below), or include individual files as needed -->"
            + "        <script src='js/bootstrap.min.js'></script>"
            + "    </body>"
            + "</html>";
}
