/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.knec;

import java.sql.*;

/**
 *
 * @author juliuskun
 */
public class DataStore {

    String header = "<!DOCTYPE html>" +
"<!--" +
"To change this license header, choose License Headers in Project Properties." +
"To change this template file, choose Tools | Templates" +
"and open the template in the editor." +
"-->" +
"<html>" +
"    <head>" +
"        <title>KNEC SMS</title>" +
"        <meta charset='UTF-8'>" +
"        <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
"        <link rel='shortcut icon' href='img/knec.png' type='image/png'>" +
"" +
"        <link href='css/datepicker.min.css' rel='stylesheet' type='text/css'>" +
"        <!-- Bootstrap -->" +
"        <link href='css/bootstrap.min.css' rel='stylesheet'>" +
"        <!-- fawsomeanimations -->" +
"        <link href='css/font-awesome-animation.min.css' rel='stylesheet'>" +
"        <!-- manual CSS -->" +
"        <link href='css/kplc.css' rel='stylesheet'>" +
"        <link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet'>" +
"        <script defer src='https://use.fontawesome.com/releases/v5.0.8/js/all.js'></script>" +
"    </head>" +
"    <body>" +
"        <div class='container'>" +
"            <div class='row'>" +
"                <div class='col-lg-12 '>" +
"                    <span><a href='/knecportal/Home'><img src='img/knec.png' width='100'></a></span><span class='pull-right' style='margin-top:2%;font-size:30px;color: #E8FF22;'><i class='fas fa-hashtag'></i> KNECSMS</span>" +
"                </div>" +
"            </div>            " +
"        </div>" +
"        <div class='container-fluid'>" +
"            <div class='row'>" +
"                <div class='col-lg-12 text-center'>" +
"                    <div class='horizontalLine'></div>" +
"                </div>" +
"            </div>" +
"        </div>";

    String footer = "<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->" +
"        <script src='js/jquery.js'></script>" +
"        <!-- Include all compiled plugins (below), or include individual files as needed -->" +
"        <script src='js/bootstrap.min.js'></script>" +
"" +
"        <script src='js/datepicker.min.js'></script>" +
"        <!-- Include English language -->" +
"        <script src='js/datepicker.en.js'></script>" +
"    </body>" +
"</html>";

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/knecsms";
            Class.forName(myDriver);
            //conn = DriverManager.getConnection(myUrl, "root", "1root2");
            conn = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }
    public String checkUser(String uname, String upass) {
        String res = "none";
        if (uname.equals("pdsl") && upass.equals("Qpdslknec")) {
            res = "found:Pdsl Admin";
        }
        if (uname.equals("knec") && upass.equals("knec!@2018")) {
            res = "found:Kenya National Examinations Council";
        }
        if (uname.equals("dkedera") && upass.equals("44kedera@11")) {
            res = "found:David Kedera";
        }
        if (uname.equals("accounts") && upass.equals("@ccounts@knec")) {
            res = "found:KNEC Accounts";
        }
        return res;
    }
}
