/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kplc.mpesa;

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
"        <title>KPLC Transactions</title>" +
"        <meta charset='UTF-8'>" +
"        <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
"        <link rel='shortcut icon' href='img/kenyapower.png' type='image/png'>" +
"" +
"        <link href='css/datepicker.min.css' rel='stylesheet' type='text/css'>" +
"        <!-- Bootstrap -->" +
"        <link href='css/bootstrap.min.css' rel='stylesheet'>" +
"        <!-- manual CSS -->" +
"        <link href='css/kplc.css' rel='stylesheet'>" +
"        <link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet'>" +
"        <script defer src='https://use.fontawesome.com/releases/v5.0.8/js/all.js'></script>" +
"    </head>" +
"    <body>" +
"        <div class='container'>" +
"            <div class='row'>" +
"                <div class='col-lg-12 '>" +
"                    <span><a href='/kplcvending/Home'><img src='img/kenyapower.png' width='100'></a></span><span class='pull-right' style='margin-top:3%;font-size:40px;color: #ffcf01;'><i class='fas fa-hashtag'></i> Paybill 888880/888888</span>" +
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
"        <script src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>" +
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
            String myUrl = "jdbc:mysql://localhost/KenyaPower";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }    
}
