/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vaudits;

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

    public String checkUser(String uname, String upass) {
        String res = "none";
        if (uname.equals("pdsl") && upass.equals("pdsl0987")) {
            res = "found:Pdsl Admin";
        }
        if (uname.equals("accounts") && upass.equals("accounts@2018")) {
            res = "found:Pdsl Accounts";
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

    
    String header = "<!DOCTYPE html><html>"
            + "    <head>"
            + "        <title>VenditAudits</title>"
            + "        <meta charset='UTF-8'>"
            + "        <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "        <link rel='shortcut icon' href='img/head.png' type='image/png'>"
            + ""
            + "        <link href='css/datepicker.min.css' rel='stylesheet' type='text/css'>"
            + "        <link href='https://fonts.googleapis.com/css?family=Barlow+Semi+Condensed' rel='stylesheet'>"
            + "        <script defer src='https://use.fontawesome.com/releases/v5.0.8/js/all.js'></script>"
            + "        <script src='js/jquery.js'></script>"
            + "        <!-- Bootstrap -->"
            + "        <link href='css/bootstrap.min.css' rel='stylesheet'>"
            + "        <!-- custom -->"
            + "        <link href='css/vaudits.css' rel='stylesheet'>"
            + "    </head>"
            + "    <body>"
            + "        <div class='container-fluid'>"
            + "            <div class='row'>"
            + "                <div class='col-lg-12 text-center'>"
            + "                    <h3><i class='fas fa-american-sign-language-interpreting'></i> VendIT Audits <i class='fas fa-anchor'></i></h3>"
            + "                </div>"
            + "            </div>"
            + "            <div class='row'>"
            + "                <div class='col-lg-12'>"
            + "                    <div class='horizontalLine'></div>"
            + "                </div>"
            + "            </div>"
            + "        </div>";

    String footer = "<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->"
            + "        <!-- Include all compiled plugins (below), or include individual files as needed -->"
            + "        <script src='js/bootstrap.min.js'></script>"
            + "        <script src='js/datepicker.min.js'></script>"
            + "        <!-- Include English language -->"
            + "        <script src='js/datepicker.en.js'></script>"
            + "    </body>"
            + "</html>";
}
