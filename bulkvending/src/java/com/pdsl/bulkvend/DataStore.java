/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.bulkvend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
            String myUrl = "jdbc:mysql://localhost/bulkvending";
            Class.forName(myDriver);
            //conn = DriverManager.getConnection(myUrl, "root", "juliusroot2");
            conn = DriverManager.getConnection(myUrl, "root", "pdslvendit@!thika");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables() {
        DateTime dt = new DateTime();
        /*if (dt.dayOfMonth().get() == 1) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
            String year = fmt.print(dt);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
            String month = formatter.print(dt);
            DateTimeFormatter tformatter = DateTimeFormat.forPattern("hh");
            String time = tformatter.print(dt);
            Logger.getLogger(DataStore.class.getName()).log(Level.WARNING, "Time:" + time);
            if (time.equals("12")) {*/
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
        String year = fmt.print(dt);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
        String month = formatter.print(dt);
        DateTimeFormatter tformatter = DateTimeFormat.forPattern("hh");
        String time = tformatter.print(dt);
        Logger.getLogger(DataStore.class.getName()).log(Level.WARNING, "Time:" + time);
        //System.out.println("Creating Tables...");
        try {
            Connection con = connect();
            String clients = "create table if not exists clients(id INT NOT NULL AUTO_INCREMENT, clientCode int, fullName varchar(200), uName varchar(200), uPass varchar(1000), uType varchar(100), creationDate timestamp default current_timestamp, status int default 0, primary key(id), unique(clientCode))";
            String groups = "create table if not exists groups(id INT NOT NULL AUTO_INCREMENT, clientId int, clientCode int, groupName varchar(200), groupType varchar(50), status int default 0, creationDate timestamp default current_timestamp, primary key(id), unique(groupName), foreign key(clientId) references clients(id) ON DELETE CASCADE)";
            String meters = "create table if not exists meters(id INT NOT NULL AUTO_INCREMENT,  clientCode int, groupId int, groupName varchar(200), groupType varchar(50), fullName varchar(200), phone varchar(50), meter varchar(50), amount  varchar(5000), status int default 0, creationDate timestamp default current_timestamp, primary key(id), foreign key(groupId) references groups(id) ON DELETE CASCADE)";
            Statement stm = con.createStatement();
            stm.execute(clients);
            stm.execute(groups);
            stm.execute(meters);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Create Transaction table
        String tdate = month + year;
        try {
            Connection conn = connect();
            String transactions = "create table transactions" + tdate + "(id INT NOT NULL AUTO_INCREMENT, clientId int, clientCode int, groupType varchar(200), meter varchar(200), amount double default 0, deposit double default 0, vendResponse text, creationDate timestamp default current_timestamp, status int default '0', primary key(id))";
            Statement stm = conn.createStatement();
            stm.execute(transactions);

            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            SimpleDateFormat formatm = new SimpleDateFormat("MMM");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            String lastmonth = formatm.format(cal.getTime()) + format.format(cal.getTime());
            Logger.getLogger(DataStore.class.getName()).log(Level.WARNING, "Previous Month:" + lastmonth);

            String query1 = "SELECT `id`,`clientCode` FROM `clients`";
            Statement stm1 = conn.createStatement();
            ResultSet rs1 = stm1.executeQuery(query1);
            while (rs1.next()) {
                Logger.getLogger(DataStore.class.getName()).log(Level.WARNING, "Inseting for client:" + rs1.getString(1));
                String query2 = "SELECT SUM(`amount`),SUM(`deposit`) FROM `transactions" + lastmonth + "` WHERE  `clientId`=" + rs1.getString(1) + "";
                Logger.getLogger(DataStore.class.getName()).log(Level.WARNING, query2);
                Statement stm2 = conn.createStatement();
                ResultSet rs2 = stm2.executeQuery(query2);
                if (rs2.isBeforeFirst()) {
                    while (rs2.next()) {
                        if (rs2.getString(2) == null) {
                            String values = "insert into transactions" + month + year + "(clientId,clientCode,groupType,amount,deposit) values (?,?,?,?,?)";
                            PreparedStatement prep = conn.prepareStatement(values);
                            prep.setString(1, rs1.getString(1));
                            prep.setString(2, rs1.getString(2));
                            prep.setString(3, "BALANCE BROUGHT FORWARD");
                            prep.setString(4, "0.0");
                            prep.setString(5, "0.0");
                            prep.execute();
                            prep.close();
                        } else {
                            String values = "insert into transactions" + month + year + "(clientId,clientCode,groupType,amount,deposit) values (?,?,?,?,?)";
                            PreparedStatement prep = conn.prepareStatement(values);
                            prep.setString(1, rs1.getString(1));
                            prep.setString(2, rs1.getString(2));
                            prep.setString(3, "BALANCE BROUGHT FORWARD");
                            prep.setString(4, rs2.getString(1));
                            prep.setString(5, rs2.getString(2));
                            prep.execute();
                            prep.close();
                        }
                    }
                } else {
                    String values = "insert into transactions" + month + year + "(clientId,clientCode,groupType,amount,deposit) values (?,?,?,?,?)";
                    PreparedStatement prep = conn.prepareStatement(values);
                    prep.setString(1, rs1.getString(1));
                    prep.setString(2, rs1.getString(2));
                    prep.setString(3, "BALANCE BROUGHT FORWARD");
                    prep.setString(4, "0.0");
                    prep.setString(5, "0.0");
                    prep.execute();
                    prep.close();
                }
            }
            conn.close();
        } catch (SQLException ex) {
            //Aready created
            Logger.getLogger(DataStore.class.getName()).log(Level.WARNING, "Transaction Table already created");
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*} else {
                Logger.getLogger(DataStore.class.getName()).log(Level.WARNING, "Its Past midnight. Tables already created");
            }

        } else {
            Logger.getLogger(DataStore.class.getName()).log(Level.WARNING, dt.dayOfMonth().get() + " Day of the month");
        }*/
    }

    public String getFloatBal(int clientId) {
        String res = "0.00";
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
        String year = fmt.print(dt);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
        String month = formatter.print(dt);
        try {
            Connection con = connect();
            String query = "SELECT SUM(`deposit`-`amount`) FROM `transactions" + month + year + "` WHERE  `clientId`=" + clientId + "";
            //System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    res = rs.getString(1);
                }
            }
            con.close();
        } catch (SQLException ex) {
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (res == null) {
            res = "0.00";
        }
        return res;
    }

    public String checkUser(String uName, String uPass) {
        String res = "none";
        try {
            Connection con = connect();
            String query = "SELECT `id`, `clientCode`, `fullName`, `uName` FROM `clients` WHERE  `uName`='" + uName + "' AND `uPass`='" + uPass + "' AND status=0";
            //System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    res = "found:" + rs.getString(1) + ":" + rs.getString(2) + ":" + rs.getString(3) + ":" + rs.getString(4);
                }
            }
            con.close();
        } catch (SQLException ex) {
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public String getGroupById(String id) {
        String res = "none";
        try {
            Connection con = connect();
            String query = "SELECT `clientId`, `clientCode`, `groupName`, `groupType`,status FROM `groups` WHERE  `id`=" + id + "";
            //System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    res = rs.getString(1) + ":" + rs.getString(2) + ":" + rs.getString(3) + ":" + rs.getString(4) + ":" + rs.getString(5);
                }
            }
            con.close();
        } catch (SQLException ex) {
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public Double getGroupSum(String groupId) {
        Double sum = 0.0;
        try {
            Connection con = connect();
            String query = "SELECT amount FROM `meters` WHERE  `groupId`=" + groupId + "";
            //System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if (rs.getString(1).contains("_")) {
                        String amtsplit[] = rs.getString(1).split("_");
                        String amt = amtsplit[1];
                        sum = sum + Double.parseDouble(amt);
                    } else {
                        sum = sum + rs.getDouble(1);
                    }
                }
            }
            con.close();
        } catch (SQLException ex) {
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sum;
    }

    public String pageHeader(boolean status, String fullname) {
        String header = "<!DOCTYPE html>"
                + "<html lang='en'>"
                + "    <head>"
                + "        <meta charset='utf-8'>"
                + "        <meta name='viewport' content='width=device-width, initial-scale=1, shrink-to-fit=no'>"
                + "        <link rel='shortcut icon' href='img/pdsl.png' type='image/png'>"
                + "        <meta name='description' content=''>"
                + "        <meta name='author' content=''>"
                + "        <title>PDSL - Bulk Vending</title>"
                + "        <!-- Bootstrap core CSS -->"
                + "        <link href='vendor/bootstrap/css/bootstrap.min.css' rel='stylesheet'>"
                + "        <!-- Custom fonts for this template -->"
                + "        <link href='https://fonts.googleapis.com/css?family=Raleway:100,100i,200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i' rel='stylesheet'>"
                + "        <link href='https://fonts.googleapis.com/css?family=Lora:400,400i,700,700i' rel='stylesheet'>"
                + "        <!-- Custom styles for this template -->"
                + "        <link href='css/bulkvend.css' rel='stylesheet'>"
                + "    </head>"
                + "    <body>"
                + "        <h1 class='site-heading text-center text-white d-none d-lg-block'>"
                + "            <span class='site-heading-upper text-primary mb-3'><img src='img/logo.png'></span>"
                + "            <span class='site-heading-lower'>Bulk Vending</span>"
                + "        </h1>"
                + "        <!-- Navigation -->"
                + "        <nav class='navbar navbar-expand-lg navbar-dark py-lg-4' style='background:#000' id='mainNav'>"
                + "            <div class='container-fluid'>"
                + "                <a class='navbar-brand text-uppercase text-expanded font-weight-bold d-lg-none' href='#'>Bulk Vending</a>"
                + "                <button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarResponsive' aria-controls='navbarResponsive' aria-expanded='false' aria-label='Toggle navigation'>"
                + "                    <span class='navbar-toggler-icon'></span>"
                + "                </button>"
                + "                <div class='collapse navbar-collapse' id='navbarResponsive'>"
                + "                    <ul class='navbar-nav mx-auto'>"
                + "                        <li class='nav-item active px-lg-4'>"
                + "                            <a class='nav-link text-uppercase text-expanded' href='/bulkvending/'>Dashboard"
                + "                                <span class='sr-only'>(current)</span>"
                + "                            </a>"
                + "                        </li>"
                + "                        <li class='nav-item px-lg-4'>"
                + "                            <a class='nav-link text-uppercase text-expanded' href='Groups'>Manage Groups</a>"
                + "                        </li>"
                + "                        <li class='nav-item px-lg-4'>"
                + "                            <a class='nav-link text-uppercase text-expanded' href='VendRequest'>Bulk Vending</a>"
                + "                        </li>"
                + "                        <li class='nav-item  px-lg-4 dropdown'>"
                + "                            <a class='nav-link dropdown-toggle text-uppercase text-expanded' href='#' id='navbarDropdown' role='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>"
                + "                                Reports"
                + "                            </a>"
                + "                            <div class='dropdown-menu' aria-labelledby='navbarDropdown'>"
                + "                                <p class='font-weight-bold text-center'>PREPAID HISTORY</p>"
                + "                                <div class='dropdown-divider'></div>"
                + "                                <a class='dropdown-item' href='Reports?rt=Prepaid'>   Token Purchase History</a>"
                + "                                <a class='dropdown-item' href='Reports?rt=Airtime'>   Airtime Purchase History</a>"
                + "                                <div class='dropdown-divider'></div>"
                + "                                <p class='font-weight-bold text-center'>POSTPAID HISTORY</p>"
                + "                                <div class='dropdown-divider'></div>"
                + "                                <a class='dropdown-item' href='Reports?rt=Postpaid'>  Bill Payments History</a>"
                + "                                <div class='dropdown-divider'></div>"
                + "                                <p class='font-weight-bold text-center'>FLOAT HISTORY</p>"
                + "                                <div class='dropdown-divider'></div>"
                + "                                <a class='dropdown-item' href='Reports?rt=FLOAT DEPOSIT'>  Float Account History</a>"
                + "                            </div>"
                + "                        </li>"
                + "                        <li class='nav-item px-lg-4'>"
                + "                            <a class='nav-link text-uppercase text-expanded' href='Contact'>Contact Us</a>"
                + "                        </li>";
        if (status) {
            header = header + "<li class='nav-item px-lg-4'>"
                    + "                            <a class='nav-link text-info'>" + fullname + "</a>"
                    + "                        </li>"
                    + "<li class='nav-item px-lg-4'>"
                    + "  <a class='nav-link text-uppercase text-expanded' href='/bulkvending/Home?logout=\"logout\"'> Logout</a>"
                    + "                        </li>";
        } else {
            header = header + "<li class='nav-item px-lg-4'>"
                    + "                            <a class='nav-link text-danger'>" + fullname + "</a>"
                    + "                        </li>"
                    + "<li class='nav-item px-lg-4'>"
                    + "<a class='nav-link text-uppercase text-expanded' data-toggle='modal' data-target='#login' id='loginbtn' href='#'> Login</a>"
                    + "                        </li>";

        }
        header = header + "                    </ul>"
                + "                </div>"
                + "            </div>"
                + "        </nav>";
        return header;
    }

    DateTime dt = new DateTime();
    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
    String year = fmt.print(dt);

    public String pageFoorter(String login) {
        String footer = "<!--Login Modal -->"
                + "        <div class='modal fade' id='login' tabindex='-1' role='dialog' aria-labelledby='login' aria-hidden='true'>"
                + "            <div class='modal-dialog modal-dialog-centered' role='document'>"
                + "                <div class='modal-content'>"
                + "                    <form name='loginform' action='/bulkvending/Home' method='POST'>"
                + "                        <div class='modal-header'>"
                + "                            <h5 class='modal-title' id='hlogin'>User Login</h5>"
                + "                            <button type='button' class='close' data-dismiss='modal' aria-label='Close'>"
                + "                                <span aria-hidden='true'>&times;</span>"
                + "                            </button>"
                + "                        </div>"
                + "                        <div class='modal-body'>"
                + "                            <div class='form-group'>"
                + "                                <div class='input-group mb-2'>"
                + "                                    <div class='input-group-prepend'>"
                + "                                        <div class='input-group-text'>Username:</div>"
                + "                                    </div>"
                + "                                    <input type='text' class='form-control' id='uName' name='uName'>"
                + "                                </div>"
                + "                            </div>"
                + "                            <div class='form-group'>"
                + "                                <div class='input-group mb-2'>"
                + "                                    <div class='input-group-prepend'>"
                + "                                        <div class='input-group-text'>Password:&nbsp;</div>"
                + "                                    </div>"
                + "                                    <input type='password' class='form-control' id='uPass' name='uPass'>"
                + "                                </div>"
                + "                            </div>"
                + "                        </div>"
                + "                        <div class='modal-footer'>"
                + "                            <button type='button' class='btn btn-secondary' data-dismiss='modal'>Exit</button>"
                + "                            <button type='submit' class='btn btn-primary' value='submit'>Go</button>"
                + "                        </div>"
                + "                    </form>"
                + "                </div>"
                + "            </div>"
                + "        </div>"
                + "<footer class='footer text-faded text-center py-5'>"
                + "            <div class='container'>"
                + "                <p class='m-0 small'>Copyright &copy; pdsl " + year + "</p>"
                + "            </div>"
                + "        </footer>"
                + ""
                + "        <!-- Bootstrap core JavaScript -->"
                + "        <script src='vendor/jquery/jquery.min.js'></script>"
                + "        <script src='vendor/bootstrap/js/bootstrap.bundle.min.js'></script>";
        if (login.equals("Please Login!")) {
            footer = footer + "<script>"
                    + "window.onload=function(){"
                    + "  document.getElementById('loginbtn').click();"
                    + "}"
                    + "</script>";
        }
        footer = footer + "    </body>"
                + ""
                + "</html>";
        return footer;

    }

}
