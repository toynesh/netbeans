/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vendit.airtel;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

/**
 *
 * @author juliuskun
 */
public class DataStore {

    String header = "<!DOCTYPE html>"
            + "<html lang='en'>"
            + ""
            + "<head>"
            + ""
            + "    <meta charset='utf-8'>"
            + "    <meta http-equiv='X-UA-Compatible' content='IE=edge'>"
            + "    <meta name='viewport' content='width=device-width, shrink-to-fit=no, initial-scale=1'>"
            + "    <meta name='description' content=''>"
            + "    <meta name='author' content=''>"
            + ""
            + "    <title>Airtel Transactions</title>"
            + ""
            + "    <!-- Bootstrap Core CSS -->"
            + "    <link href='css/bootstrap.min.css' rel='stylesheet'>"
            + ""
            + "    <!-- Custom CSS -->"
            + "    <link href='css/airtel.css' rel='stylesheet'>"
            + "    <link href='https://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet'>"
            + "    <link href='https://fonts.googleapis.com/css?family=Muli' rel='stylesheet'>"
            + "    <link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>"
            + "    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->"
            + "    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->"
            + "    <!--[if lt IE 9]>"
            + "        <script src='https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js'></script>"
            + "        <script src='https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js'></script>"
            + "    <![endif]-->"
            + "<link rel='shortcut icon' href='img/vendit.png' type='image/png'>"
            + "</head>"
            + ""
            + "<body>"
            + ""
            + "    <div id='wrapper'>"
            + ""
            + "        <!-- Sidebar -->"
            + "        <div id='sidebar-wrapper'>"
            + "            <ul class='sidebar-nav'>"
            + "                <li class='sidebar-brand'>"
            + "                    <a href='vendithome.jsp'><img src='imgs/vendit.png' width='180' style='margin-top:5%;margin-left:5%;' alt=''/></a>"
            + "                </li>"
            + "                <li>"
            + "                    <br />"
            + "                </li>"
            + "                <li>"
            + "                    <br />"
            + "                </li>"
            + "                <li>"
            + "                    <a href='home'><i class='fa fa-home' aria-hidden='true'></i> Home</a>"
            + "                </li>"
            + "                <li>"
            + "                    <a href='#' data-toggle='modal' data-target='#Qphone'><i class='fa fa-phone' aria-hidden='true'></i> Query By Phone</a>"
            + "                </li>"
            + "<!-- Modal -->"
            + "<div id='Qphone' class='modal fade' role='dialog'>"
            + "  <div class='modal-dialog'>"
            + ""
            + "    <!-- Modal content-->"
            + "    <div class='modal-content'>"
            + "      <div class='modal-header'>"
            + "        <h4 class='modal-title'><i class='fa fa-phone' aria-hidden='true'></i> Enter Phone Number</h4>"
            + "      </div>"
            + "      <div class='modal-body'>"
            + "      <form action='GetTransactionPhone' method='post'>"
            + "         <div class='form-group'>"
            + "             <input type='text' name='qphone' class='form-control' id='qphone' placeholder='2547...' required>"
            + "         </div>"
            + "         <button type='submit' class='btn btn-primary'>Submit</button>"
            + "      </form>"
            + "      </div>"
            + "     <div class='modal-footer'>"
            + "        <button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>"
            + "      </div>"
            + "    </div>"
            + ""
            + "  </div>"
            + "</div>"
            + "                <li>"
            + "                    <a  href='#' data-toggle='modal' data-target='#QMeter'><i class='fa fa-tachometer' aria-hidden='true'></i> Query By Meter</a>"
            + "                </li>"
            + "<!-- Modal -->"
            + "<div id='QMeter' class='modal fade' role='dialog'>"
            + "  <div class='modal-dialog'>"
            + ""
            + "    <!-- Modal content-->"
            + "    <div class='modal-content'>"
            + "      <div class='modal-header'>"
            + "        <h4 class='modal-title'><i class='fa fa-tachometer' aria-hidden='true'></i> Enter Meter Number</h4>"
            + "      </div>"
            + "      <div class='modal-body'>"
            + "      <form action='GetTransactionMeter' method='post'>"
            + "         <div class='form-group'>"
            + "             <input type='text' name='qmeter' class='form-control' id='qmeter' placeholder='413213...' required>"
            + "         </div>"
            + "         <button type='submit' class='btn btn-primary'>Submit</button>"
            + "      </form>"
            + "      </div>"
            + "     <div class='modal-footer'>"
            + "        <button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>"
            + "      </div>"
            + "    </div>"
            + ""
            + "  </div>"
            + "</div>"
            + "                <li>"
            + "                    <a  href='#' data-toggle='modal' data-target='#Qcode'><i class='fa fa-barcode' aria-hidden='true'></i> Query By Code</a>"
            + "                </li>"
            + "<!-- Modal -->"
            + "<div id='Qcode' class='modal fade' role='dialog'>"
            + "  <div class='modal-dialog'>"
            + ""
            + "    <!-- Modal content-->"
            + "    <div class='modal-content'>"
            + "      <div class='modal-header'>"
            + "        <h4 class='modal-title'><i class='fa fa-barcode' aria-hidden='true'></i> Enter Code</h4>"
            + "      </div>"
            + "      <div class='modal-body'>"
            + "      <form action='GetTransactionCode' method='post'>"
            + "         <div class='form-group'>"
            + "             <input type='text' name='qcode' class='form-control' id='qcode' placeholder='3213...' required>"
            + "         </div>"
            + "         <button type='submit' class='btn btn-primary'>Submit</button>"
            + "      </form>"
            + "      </div>"
            + "     <div class='modal-footer'>"
            + "        <button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>"
            + "      </div>"
            + "    </div>"
            + ""
            + "  </div>"
            + "</div>"
            + "                <li>"
            + "                    <a  href='#' data-toggle='modal' data-target='#Qdetails'><i class='fa fa-search' aria-hidden='true'></i> Meter Details</a>"
            + "                </li>"
            + "<!-- Modal -->"
            + "<div id='Qdetails' class='modal fade' role='dialog'>"
            + "  <div class='modal-dialog'>"
            + ""
            + "    <!-- Modal content-->"
            + "    <div class='modal-content'>"
            + "      <div class='modal-header'>"
            + "        <h4 class='modal-title'><i class='fa fa-search' aria-hidden='true'></i> Enter Meter</h4>"
            + "      </div>"
            + "      <div class='modal-body'>"
            + "      <form action='GetMeterDetails' method='post'>"
            + "         <div class='form-group'>"
            + "             <input type='text' name='qdetails' class='form-control' id='qdetails' required>"
            + "         </div>"
            + "         <button type='submit' class='btn btn-primary'>Submit</button>"
            + "      </form>"
            + "      </div>"
            + "     <div class='modal-footer'>"
            + "        <button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>"
            + "      </div>"
            + "    </div>"
            + ""
            + "  </div>"
            + "</div>"
            + "            </ul>"
            + "        </div>"
            + "        <!-- /#sidebar-wrapper -->";

    String footer = "<!-- jQuery -->"
            + "    <script src='js/jquery.js'></script>"
            + ""
            + "    <!-- Bootstrap Core JavaScript -->"
            + "    <script src='js/bootstrap.min.js'></script>"
            + ""
            + "    <!-- Menu Toggle Script -->"
            + "    <script>"
            + "    $('#menu-toggle').click(function(e) {"
            + "        e.preventDefault();"
            + "        $('#wrapper').toggleClass('toggled');"
            + "    });"
            + "    </script>"
            + ""
            + "</body>"
            + ""
            + "</html>";

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/AirtelMoney";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }
    public String sendReq(String param) throws IOException {
        String request = "http://localhost:8086/PdslMerchantQuery/AirtelServlet?";
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(param.getBytes().length));
        connection.setUseCaches(false);

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(param);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        out.close();
        connection.disconnect();
        return response.toString();
    }

    public String sendPayment(String code, String newmeter, String meter, String mobile, String amount, String names) {
        String url = "transid="+code+"&newmeter="+newmeter+"&meter="+meter+"&mobile="+mobile+"&amount=0&client=PdslAirtel&term=00001&names="+names+"";
        String res;
        System.out.println("Revend URL: "+url);
        try {
            res = sendReq(url);
        } catch (IOException e) {
            e.printStackTrace();
            res = "FAILED";
        }
        return res;
    }
}
