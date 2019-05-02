/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.sms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    public String head(String pg) {
        String header = "<!DOCTYPE html>"
                + "<!--"
                + "To change this license header, choose License Headers in Project Properties."
                + "To change this template file, choose Tools | Templates"
                + "and open the template in the editor."
                + "-->"
                + "<html>"
                + "<head>"
                + "<title>pdslSMS</title>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<link rel='shortcut icon' href='img/pdsl.png' type='image/png'>"
                + ""
                + "<script defer src='https://use.fontawesome.com/releases/v5.0.8/js/all.js'></script>"
                + "<link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet'> "
                + "<link href='https://fonts.googleapis.com/css?family=Poiret+One' rel='stylesheet'> "
                + "<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet'> "
                + "<!-- fawsomeanimations -->"
                + "<link href='css/font-awesome-animation.min.css' rel='stylesheet'>"
                + "<!-- Bootstrap CSS -->"
                + "<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css' integrity='sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO' crossorigin='anonymous'>"
                + "<link href='css/bootstrap-datetimepicker.min.css' rel='stylesheet'><!-- custom -->"
                + "<link href='css/pdslsms.css' rel='stylesheet'>"
                + "</head>"
                + "<body>"
                + "<nav class='navbar navbar-expand-lg navbar-dark bg-primary fixed-top navbar-custom'>"
                + "<a href='Home'><img src='img/head.png' class='rounded-circle' width='30' height='30'></a>"
                + "<button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarSupportedContent' aria-controls='navbarSupportedContent' aria-expanded='false' aria-label='Toggle navigation'>"
                + "<span class='navbar-toggler-icon'></span>"
                + "</button>"
                + "<div class='collapse navbar-collapse' id='navbarSupportedContent'>"
                + "<ul class='navbar-nav ml-auto'>"
                + "<li class='nav-item dropdown active'>"
                + "<a class='nav-link dropdown-toggle' href='#' id='navbarDropdown' role='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>"
                + "<i class='fas fa-user'></i> Logged in as: Julius"
                + "</a>"
                + "<div class='dropdown-menu' aria-labelledby='navbarDropdown'>"
                + "<a class='dropdown-item' href='#'><i class='fas fa-sign-out-alt'></i> Logout</a>"
                + "<div class='dropdown-divider'></div>"
                + "<a class='dropdown-item' href='#'><i class='fas fa-exchange-alt'></i> Change Password</a>"
                + "</div>"
                + "</li>"
                + "</ul>"
                + "</div>"
                + "</nav>"
                + "<div class='container-fluid  h-100'>"
                + "<div class='row  h-100'>"
                + "<div class='col-sm-2 col-md-2 col-lg-2' id='vnav'>"
                + "<h5 style='font-family: \"Poiret One\", cursive; margin-top:30%'>Send/Receive SMS</h5>"
                + "<a href='Home' style='outline:none'><img src='img/bs.jpg' width='170' class='img-fluid'></a>"
                + "<ul class='list-group list-group-flush'>"
                + "<li class='list-group-item  border-right'><a href='Single' class='faa-parent animated-hover'>Send Single<i class='fas fa-fighter-jet faa-passing float-right pdslcolor'></i></a></li>"
                + "<li class='list-group-item'><a href='Bulk' class='faa-parent animated-hover'>Send Bulk<i class='fas fa-fighter-jet faa-passing float-right pdslcolor'></i></a></li>"
                + "<li class='list-group-item border-right'><a href='Bulk' class='faa-parent animated-hover'>Sent Items<i class='fas fa-fighter-jet faa-passing float-right pdslcolor'></i></a></li>"
                + "<li class='list-group-item'><a href='ManageGroups' class='faa-parent animated-hover'>Manage Groups<i class='fas fa-fighter-jet faa-passing float-right pdslcolor'></i></a></li>"
                + "<li class='list-group-item border-right'><a href='#' class='faa-parent animated-hover'>Inbox<i class='fas fa-fighter-jet faa-passing float-right pdslcolor'></i></a></li>"
                + "<li class='list-group-item'><a href='#' class='faa-parent animated-hover'>Manage Users<i class='fas fa-fighter-jet faa-passing float-right pdslcolor'></i></a></li>";

        if (pg.equals("Admin")) {
            header = header + "<li class='list-group-item'><a href='Admin' class='faa-parent animated-hover'>Manage Clients<i class='fas fa-fighter-jet faa-passing float-right pdslcolor'></i></a></li><li class='list-group-item'></li>";
        }
        header = header + "</ul>"
                + "</div>";
        return header;
    }

    public String foot(String pg) {
        String footer = "</div>"
                + "</div>"
                + "<!-- Optional JavaScript -->"
                + "<!-- jQuery first, then Popper.js, then Bootstrap JS -->"
                + "<script src='https://code.jquery.com/jquery-3.3.1.slim.min.js' integrity='sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo' crossorigin='anonymous'></script>"
                + "<script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js' integrity='sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49' crossorigin='anonymous'></script>"
                + "<script src='https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js' integrity='sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy' crossorigin='anonymous'></script>"
                + "<script src='js/bootstrap-datetimepicker.js' charset='UTF-8'></script>"
                + "<script src='js/locales/bootstrap-datetimepicker.en.js' charset='UTF-8'></script>"
                + "<script type='text/javascript'>"
                + "$('#datetimepicker10').datetimepicker({"
                + "format: 'dd-mm-yyyy  hh:ii:ss',"
                + "showMeridian: true,"
                + "startDate: new Date(),"
                + "todayBtn: true,"
                + "autoclose: 1,"
                + "startView: 3,"
                + "todayHighlight: 1,"
                + "minView: 0,"
                + "pickerPosition: 'top-left'"
                + "});"
                + "</script>"
                + ""
                + "</body>"
                + "</html>";
        return footer;
    }

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/dspDelivery";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");

        } catch (Exception e) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, e.getMessage());
        }
        return conn;
    }

    public void createTables() {
        Connection conn = connect();
        try {
            String clients = "create table if not exists pdslSMSclients(id INT NOT NULL AUTO_INCREMENT,accesscode varchar(50),cname varchar(100),masking varchar(100),primary key(id),unique(accesscode))";
            String users = "create table if not exists pdslSMSusers(id INT NOT NULL AUTO_INCREMENT,accesscode varchar(50),fullname varchar(200),uname varchar(50),utype varchar(50),password text,status varchar(50), primary key(id))";
            String inbox = "create table if not exists pdslSMSinbox(id INT NOT NULL AUTO_INCREMENT,accesscode varchar(50),linkid text,msisdn varchar(100),message text,correlator text,intime timestamp default current_timestamp,type varchar(200) default 'ondemand',replystatus int default 0, primary key(id))";
            String outbox = "create table if not exists pdslSMSoutbox(id INT NOT NULL AUTO_INCREMENT,accesscode varchar(50),msisdn varchar(100),groupname varchar(200),message text,sender varchar(50),sendtype varchar(50),outtime timestamp default current_timestamp,sendstatus int default 0,delivery varchar(100),primary key(id))";
            String groups = "create table if not exists pdslSMSgroups(id INT NOT NULL AUTO_INCREMENT,clientid INT,groupname varchar(500),contacts text,primary key(id))";
            Statement stm = conn.createStatement();
            stm.execute(clients);
            stm.execute(users);
            stm.execute(inbox);
            stm.execute(outbox);
            stm.execute(groups);
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

    public List<List<String>> getClients() {
        DataStore data = new DataStore();
        List<List<String>> clients = new ArrayList<>();
        String query = "SELECT `id`,`accesscode`,`cname`,`masking` FROM `pdslSMSclients` ORDER BY `id`";
        try {
            Connection con = data.connect();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                List<String> row = new ArrayList<>(numcols);
                int i = 1;
                while (i <= numcols) {
                    row.add(rs.getString(i++));
                }
                clients.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clients;
    }

    public List<String> getClientById(String cid) {
        DataStore data = new DataStore();
        List<String> client = new ArrayList<>();
        String query = "SELECT `id`,`accesscode`,`cname`,`masking` FROM `pdslSMSclients` WHERE `id`="+cid+"  ORDER BY `id`";
        try {
            Connection con = data.connect();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                client.add(rs.getString(1));
                client.add(rs.getString(2));
                client.add(rs.getString(3));
                client.add(rs.getString(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return client;
    }
    public List<List<String>> getGroups() {
        DataStore data = new DataStore();
        List<List<String>> clients = new ArrayList<>();
        String query = "SELECT `id`,`clientid`,`groupname`,`contacts` FROM `pdslSMSgroups` ORDER BY `id`";
        try {
            Connection con = data.connect();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                List<String> row = new ArrayList<>(numcols);
                int i = 1;
                while (i <= numcols) {
                    row.add(rs.getString(i++));
                }
                clients.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clients;
    }
}
