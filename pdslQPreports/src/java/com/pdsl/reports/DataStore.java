/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.reports;

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
        try {
            Connection con = connect();
            String users = "create table if not exists users(id INT NOT NULL AUTO_INCREMENT, full_names varchar(200), rtlname varchar(200), uname varchar(200), upass varchar(1000), primary key(id), unique(uname))";
            String news = "create table if not exists news(id INT NOT NULL AUTO_INCREMENT, notes text, primary key(id))";
            Statement stm = con.createStatement();
            stm.execute(users);
            stm.execute(news);
            String values = "insert into users(full_names,rtlname,uname,upass) values (?,?,?,?)";
            PreparedStatement prep = con.prepareStatement(values);
            prep.setString(1, "Administrator");
            prep.setString(2, "Admin");
            prep.setString(3, "admin");
            prep.setString(4, "pdsl1234");
            prep.execute();
            prep.close();
            con.close();
        } catch (SQLException ex) {
//Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/pdslQPreports";
            Class.forName(myDriver);
            //conn = DriverManager.getConnection(myUrl, "root", "team12340.");
            conn = DriverManager.getConnection(myUrl, "root", "1root2");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables(String tdate) {
        String month = tdate;
        String retlr = "none";
        if (tdate.contains("<<")) {
            String[] dsplit = tdate.split("<<");
            retlr = dsplit[0];
            month = dsplit[1];
        }
        try {
            Connection conn = connect();
            String commercialTx = "create table if not exists commercialTx" + retlr + month + "(id INT NOT NULL AUTO_INCREMENT, datesold varchar(100), retailer varchar(200), txtype varchar(50),reference varchar(1000), retailvalue double default 0, mancovalue double default 0, commissionvalue double default 0, primary key(id), unique(reference))";
            String retailers = "create table if not exists retailers(id INT NOT NULL AUTO_INCREMENT, retailer varchar(200), primary key(id), unique(retailer))";
            String rtlrsP = "create table if not exists rtlrsP(id INT NOT NULL AUTO_INCREMENT, rtime varchar(200), totalsales double default 0, primary key(id), unique(rtime))";
            String salesnotes = "create table if not exists salesnotes" + month + "(id INT NOT NULL AUTO_INCREMENT, retailer varchar(200), fullname varchar(200), notes text, primary key(id), unique(retailer))";
            String ttrsalesnotes = "create table if not exists ttrsalesnotes" + month + "(id INT NOT NULL AUTO_INCREMENT, retailer varchar(200), fullname varchar(200), notes text, primary key(id), unique(retailer))";
            Statement stm = conn.createStatement();
            if (!retlr.equals("none")) {
                stm.execute(commercialTx);
            }
            stm.execute(retailers);
            stm.execute(rtlrsP);
            stm.execute(salesnotes);
            stm.execute(ttrsalesnotes);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String agOptList(String rtl) {
        String res = "";
        try {
            DataStore data = new DataStore();
            Connection con = data.connect();
            String query = "SELECT `retailer` FROM `retailers`";
            if (!rtl.equals("Admin")) {
                query = "SELECT `retailer` FROM `retailers` WHERE `retailer` LIKE '" + rtl.substring(0, 6) + "%'";
            }
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                res = res + "<option value='" + rs.getString(1) + "'>" + rs.getString(1) + " </option>";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public String checkUser(String uname, String upass) {
        String res = "none";
        try {
            Connection con = connect();
            String query = "SELECT `full_names`,`rtlname` FROM `users` WHERE`uname`='" + uname + "' AND `upass`='" + upass + "'";
//System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    res = "found:" + rs.getString(1) + ":" + rs.getString(2);
                }
            }
            con.close();
        } catch (SQLException ex) {
//Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    String footer = "<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->"
            + "<script src='js/jquery-3.3.1.min.js'></script>"
            + "<!-- Include all compiled plugins (below), or include individual files as needed -->"
            + "<script src='js/bootstrap.min.js'></script>"
            + "<script src='js/bootstrap-datetimepicker.js' charset='UTF-8'></script>"
            + "<script src='js/locales/bootstrap-datetimepicker.en.js' charset='UTF-8'></script>"
            + "<script type='text/javascript'>"
            + "$('#datetimepicker10').datetimepicker({"
            + "startView: 3,"
            + "minView: 3,"
            + "maxView: 3,"
            + "format: 'MM yyyy'"
            + "});"
            + "</script>"
            + "<script>"
            + "function printContent(el){"
            + "var restorepage = $('body').html();"
            + "var printcontent = $('#' + el).clone();"
            + "$('body').empty().html(printcontent);"
            + "window.print();"
            + "$('body').html(restorepage);"
            + "}"
            + "</script>"
            + "</body>"
            + "</html>";

    public String handleHeader(String pg, String rtlr, String rtype, String cdt, String usr) {
        DateTime odt = new DateTime().minusMonths(1);
        DateTimeFormatter fm = DateTimeFormat.forPattern("MMM yyyy");
        String currdate = fm.print(odt);
        if (!cdt.equals("none")) {
            currdate = cdt;
        }
        if (rtlr.equals("Select...")) {
            rtlr = "Admin";
        }
//System.out.println(currdate);
        String header = "<!DOCTYPE html>"
                + "<!--"
                + "To change this license header, choose License Headers in Project Properties."
                + "To change this template file, choose Tools | Templates"
                + "and open the template in the editor."
                + "-->"
                + "<html>"
                + "<head>"
                + "<title>pdslQPreports</title>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<link rel='shortcut icon' href='img/head.png' type='image/png'>"
                + "<link href='https://fonts.googleapis.com/css?family=Poiret+One' rel='stylesheet'>"
                + "<script defer src='https://use.fontawesome.com/releases/v5.0.8/js/all.js'></script>"
                + "<script type = 'text/javascript' src = 'https://www.gstatic.com/charts/loader.js'>"
                + "</script>"
                + "<!-- Bootstrap -->"
                + "<link href='css/bootstrap.min.css' rel='stylesheet'>"
                + "<link href='css/bootstrap-datetimepicker.min.css' rel='stylesheet'>"
                + "<!-- custom -->"
                + "<link href='css/qpreports.css' rel='stylesheet'>"
                + "</head>"
                + "<body>"
                + "<div class='container-fluid' style='margin-top:1%;font-size:15px'>"
                + "<div class='row'>"
                + "<div class='col-lg-12'>"
                + "<div class='row'>"
                + "<div class='col-lg-2'>"
                + "<a href='/pdslQPreports/Home'><img src='img/pdsl.png' width='100px'></a>"
                + "</div>"
                + "<div class='col-lg-3 col-lg-offset-3'>"
                + "<a href='/pdslQPreports/Home'><img src='img/vendit.png' width='170px'></a>"
                + "</div>"
                + "<div class='col-lg-3 col-lg-offset-1'>"
                + "<h3 class='pull-right text-primary'><strong><i class='fas fa-flag-checkered'></i> " + usr + "</strong> Report</h3>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "<div class='row'>"
                + "<div class='col-lg-12'>"
                + "<span class='pull-right'><i class='fas fa-user'></i> " + rtlr + " || <a href='/pdslQPreports/Login?logout=logout' style='color:red'><i class='fas fa-sign-out-alt'></i> LogOut</a></span>"
                + "</div>"
                + "</div>"
                + "<div class='row'>"
                + "<div class='col-lg-12'>"
                + "<div class='horizontalLine'></div>"
                + "</div>"
                + "</div>"
                + "<div class='row'>"
                + "<div class='col-lg-2'>"
                + "<p class='text-danger' style='font-size:23px'><strong>Technical <i class='fas fa-long-arrow-alt-right'></i></strong></p>"
                + "</div>"
                + "<form action='/pdslQPreports/CommercialReport' method='post'>"
                + "<div class='col-lg-3'>"
                + "<div class='form-group'>"
                + "<div class='input-group date' id='datetimepicker10'>"
                + "<div class='input-group-addon'>Month</div>"
                + "<input type='text' name='mnth' id='mwz' value='" + currdate + "' class='form-control' id='inputSuccess'>"
                + "<span class='input-group-addon'>"
                + "<span class='glyphicon glyphicon-calendar'>"
                + "</span>"
                + "</span>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "<div class='col-lg-6'>"
                + "<div class='row'>"
                + "<div class='col-lg-3'>"
                + "<button name='posdud' class='btn btn-default'><i class='far fa-chart-bar'></i>&nbsp;Post Paid Graph</button>"
                + "</div>"
                + "<div class='col-lg-3'>"
                + "<button name='evgud' class='btn btn-default'><i class='far fa-chart-bar'></i>&nbsp;Pre Paid Graph</button>"
                + "</div>"
                + "<div class='col-lg-3'>"
                + "<button name='adowntime' class='btn btn-default'><i class='fab fa-nintendo-switch'></i>&nbsp;Outages Table</button>"
                + "</div>"
                + "<div class='col-lg-3'>"
                + "<button name='news' class='btn btn-default'><i class='fas fa-star'></i>&nbsp;News & Updates</button>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "<div class='row'>"
                + "<div class='col-lg-12'>"
                + "<div class='horizontalLine'></div>"
                + "</div>"
                + "</div>"
                + "<div class='row'>"
                + "<div class='col-lg-2'>"
                + "<p class='text-danger' style='font-size:23px'><strong>Commercials <i class='fas fa-long-arrow-alt-right'></i></strong></p>"
                + "</div>"
                + "<div class='col-lg-2'>"
                + "<div class='form-group'>"
                + "<select style='font-size:18px' class='form-control' id='sel1'onchange=\"document.location.href='/pdslQPreports/" + pg + "?mw='+document.getElementById('mwz').value+'&retailer='+this.value\">"
                + "<option selected>" + rtlr + "</option>"
                + agOptList(rtlr)
                + "</select>"
                + "</div>"
                + "</div>"
                + "<div class='col-lg-6' style='font-size:18px'>"
                + "<input type='text' style='display:none' name='retailer' value='" + rtlr + "'>";
        if (rtype.equals("postpaid")) {
            header = header + "<label class='radio-inline'><input type='radio' name='rtype' value='postpaid' checked>PostPaid</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='prepaid'>Prepaid</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='sales'>Retailer Sales</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='salesp'>Total Sales Performance</label>";
        } else if (rtype.equals("prepaid")) {
            header = header + "<label class='radio-inline'><input type='radio' name='rtype' value='postpaid'>PostPaid</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='prepaid' checked>Prepaid</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='sales'>Retailer Sales</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='salesp'>Total Sales Performance</label>";

        } else if (rtype.equals("sales")) {
            header = header + "<label class='radio-inline'><input type='radio' name='rtype' value='postpaid'>PostPaid</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='prepaid'>Prepaid</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='sales' checked>Retailer Sales</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='salesp'>Total Sales Performance</label>";
        } else if (rtype.equals("salesp")) {
            header = header + "<label class='radio-inline'><input type='radio' name='rtype' value='postpaid'>PostPaid</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='prepaid'>Prepaid</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='sales'>Retailer Sales</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='salesp' checked>Total Sales Performance</label>";
        } else {
            header = header + "<label class='radio-inline'><input type='radio' name='rtype' value='postpaid' checked>PostPaid</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='prepaid'>Prepaid</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='sales'>Retailer Sales</label>"
                    + "<label class='radio-inline'><input type='radio' name='rtype' value='salesp'>Total Sales Performance</label>";
        }
        header = header + "</div>"
                + "<div class='col-lg-1'>"
                + "<button type='submit' class='btn btn-primary'>Go!</button>"
                + "</div>"
                + "<div class='col-lg-1'>"
                + "<button class='btn btn-primary' id=\"print\" onclick=\"printContent('tprint');\" ><i class='fas fa-print'></i> Print</button>"
                + "</div>"
                + "</form>"
                + "</div>"
                + "<div class='row'>"
                + "<div class='col-lg-12'>"
                + "<div class='horizontalLine'></div>"
                + "</div>"
                + "</div>"
                + "</div>";

        return header;
    }

    public String adminhandleHeader(String uptype) {

        String header = "<!DOCTYPE html>"
                + "<!--"
                + "To change this license header, choose License Headers in Project Properties."
                + "To change this template file, choose Tools | Templates"
                + "and open the template in the editor."
                + "-->"
                + "<html>"
                + "<head>"
                + "<title>pdslQPreports</title>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<link rel='shortcut icon' href='img/head.png' type='image/png'>"
                + ""
                + "<link href='css/datepicker.min.css' rel='stylesheet' type='text/css'>"
                + "<link href='https://fonts.googleapis.com/css?family=Poiret+One' rel='stylesheet'>"
                + "<script defer src='https://use.fontawesome.com/releases/v5.0.8/js/all.js'></script>"
                + "<script type = 'text/javascript' src = 'https://www.gstatic.com/charts/loader.js'>"
                + "</script>"
                + "<script type='text/javascript' src='js/widgEditor.js'></script>"
                + "<!-- Widgeteditor -->"
                + "<link href='css/info.css' rel='stylesheet'>"
                + "<link href='css/main.css' rel='stylesheet'>"
                + "<link href='css/widgEditor.css' rel='stylesheet'>"
                + "<!-- Bootstrap -->"
                + "<link href='css/bootstrap.min.css' rel='stylesheet'>"
                + "<link href='css/bootstrap-datetimepicker.min.css' rel='stylesheet'>"
                + "<!-- custom -->"
                + "<link href='css/qpreports.css' rel='stylesheet'>"
                + "</head>"
                + "<body>"
                + "<nav class='navbar navbar-default navbar-fixed-top'>"
                + "<div class='container-fluid'>"
                + "<!-- Brand and toggle get grouped for better mobile display -->"
                + "<div class='navbar-header'>"
                + "<button type='button' class='navbar-toggle collapsed' data-toggle='collapse' data-target='#bs-example-navbar-collapse-1' aria-expanded='false'>"
                + "<span class='sr-only'>Toggle navigation</span>"
                + "<span class='icon-bar'></span>"
                + "<span class='icon-bar'></span>"
                + "<span class='icon-bar'></span>"
                + "</button>"
                + "<a class='navbar-brand' href='/pdslQPreports/Home' style='margin-top:-10%;'><img src='img/pdsl.png' class='img-circle' style='background-color:#fff' width='100px'></a>"
                + "</div>"
                + ""
                + "<!-- Collect the nav links, forms, and other content for toggling -->"
                + "<div class='collapse navbar-collapse' id='bs-example-navbar-collapse-1'>"
                + "<ul class='nav navbar-nav navbar-right'>"
                + "<li><a href='#'><i class='fas fa-home'></i> Home</a></li>"
                + "<li class='dropdown'>"
                + "<a href='#' class='dropdown-toggle' data-toggle='dropdown' role='button' aria-haspopup='true' aria-expanded='false'><i class='fas fa-user'></i> Admin <span class='caret'></span></a>"
                + "<ul class='dropdown-menu'>"
                + "<li><a href='/pdslQPreports/Login?logout=logout'><i class='fas fa-sign-out-alt'></i> LogOut</a></li>"
                + "</ul>"
                + "</li>"
                + "</ul>"
                + "</div><!-- /.navbar-collapse -->"
                + "</div><!-- /.container-fluid -->"
                + "</nav>"
                + "<div class='container-fluid' style='margin-top:2%'>"
                + "<div class='row'>"
                + "<div class='col-lg-3'>"
                + "<div class='panel panel-info' style='border-right: 5px solid #e7e7e7;margin-top:13%'>"
                + "<div class='panel-heading'>"
                + "<div style='color:#2797D9;'>"
                + "<strong>&nbsp;&nbsp</strong>"
                + "</div>"
                + "</div>"
                + "<div class='panel-body'>"
                + "<div style='color:#2797D9;'>"
                + "<strong><i class='fas fa-tachometer-alt'></i>&nbsp;&nbsp;&nbsp;Dashboard</strong>"
                + "</div>"
                + "<form action='/pdslQPreports/Admin' method='post'>";
        if (uptype.equals("billuptime")) {
            header = header + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='billuptime' checked>Bill/PostPaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='evguptime'>EVG/Prepaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='bizswtime'>Bizswitch Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='balcheck'>KPLC AccountBalance Check</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='backend'>KPLC Backend Status</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='notes'>Uptime Notes</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='sales'>AGGs Sales Upload</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='news'>Notes & News</label>"
                    + "</div>";
        } else if (uptype.equals("evguptime")) {
            header = header + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='billuptime'>Bill/PostPaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='evguptime' checked>EVG/Prepaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='bizswtime'>Bizswitch Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='balcheck'>KPLC AccountBalance Check</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='backend'>KPLC Backend Status</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='notes'>Uptime Notes</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='sales'>AGGs Sales Upload</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='news'>Notes & News</label>"
                    + "</div>";

        } else if (uptype.equals("bizswtime")) {
            header = header + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='billuptime'>Bill/PostPaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='evguptime'>EVG/Prepaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='bizswtime' checked>Bizswitch Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='balcheck'>KPLC AccountBalance Check</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='backend'>KPLC Backend Status</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='notes'>Uptime Notes</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='sales'>AGGs Sales Upload</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='news'>Notes & News</label>"
                    + "</div>";

        } else if (uptype.equals("balcheck")) {
            header = header + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='billuptime'>Bill/PostPaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='evguptime'>EVG/Prepaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='bizswtime'>Bizswitch Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='balcheck' checked>KPLC AccountBalance Check</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='backend'>KPLC Backend Status</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='notes'>Uptime Notes</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='sales'>AGGs Sales Upload</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='news'>Notes & News</label>"
                    + "</div>";

        } else if (uptype.equals("backend")) {
            header = header + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='billuptime'>Bill/PostPaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='evguptime'>EVG/Prepaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='bizswtime'>Bizswitch Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='balcheck'>KPLC AccountBalance Check</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='backend' checked>KPLC Backend Status</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='notes'>Uptime Notes</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='sales'>AGGs Sales Upload</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='news'>Notes & News</label>"
                    + "</div>";

        } else if (uptype.equals("notes")) {
            header = header + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='billuptime'>Bill/PostPaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='evguptime'>EVG/Prepaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='bizswtime'>Bizswitch Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='balcheck'>KPLC AccountBalance Check</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='backend'>KPLC Backend Status</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='notes' checked>Uptime Notes</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='sales'>AGGs Sales Upload</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='news'>Notes & News</label>"
                    + "</div>";
        } else if (uptype.equals("sales")) {
            header = header + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='billuptime'>Bill/PostPaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='evguptime'>EVG/Prepaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='bizswtime'>Bizswitch Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='balcheck'>KPLC AccountBalance Check</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='backend'>KPLC Backend Status</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='notes'>Uptime Notes</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='sales' checked>AGGs Sales Upload</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='news'>Notes & News</label>"
                    + "</div>";
        } else if (uptype.equals("news")) {
            header = header + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='billuptime'>Bill/PostPaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='evguptime'>EVG/Prepaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='bizswtime'>Bizswitch Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='balcheck'>KPLC AccountBalance Check</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='backend'>KPLC Backend Status</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='notes'>Uptime Notes</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='sales'>AGGs Sales Upload</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='news' checked>Notes & News</label>"
                    + "</div>";
        } else {
            header = header + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='billuptime' checked>Bill/PostPaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='evguptime'>EVG/Prepaid Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='bizswtime'>Bizswitch Uptime</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='balcheck'>KPLC AccountBalance Check</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='backend'>KPLC Backend Status</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='notes'>Uptime Notes</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='sales'>AGGs Sales Upload</label>"
                    + "</div>"
                    + "<div class='radio'>"
                    + "<label><input type='radio' name='uptype' value='news'>Notes & News</label>"
                    + "</div>";
        }
        header = header + "<br /><button type='submit' class='btn btn-primary'>Go!</button>"
                + "</form>"
                + "</div>"
                + "</div>"
                + "</div>";

        return header;
    }
    String loginheader = "<!DOCTYPE html>"
            + "<!--"
            + "To change this license header, choose License Headers in Project Properties."
            + "To change this template file, choose Tools | Templates"
            + "and open the template in the editor."
            + "-->"
            + "<html>"
            + "<head>"
            + "<title>pdslQPreports</title>"
            + "<meta charset='UTF-8'>"
            + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "<link rel='shortcut icon' href='img/head.png' type='image/png'>"
            + "<!-- Bootstrap -->"
            + "<link href='css/bootstrap.min.css' rel='stylesheet'>"
            + "<!-- manual CSS -->"
            + "<link href='css/qpreports.css' rel='stylesheet'>"
            + ""
            + "<link href='https://fonts.googleapis.com/css?family=Muli' rel='stylesheet'>"
            + "<script defer src='https://use.fontawesome.com/releases/v5.0.8/js/all.js'></script>"
            + "</head>"
            + "<body style='background-image: url(img/watermark.png);background-repeat: no-repeat;background-attachment: fixed;background-position: center;'>"
            + "<div class='container'>"
            + "<div class='row' style='margin-top:1%'>"
            + "<div class='col-lg-12'>"
            + "<h5 class='pull-right'><i class='fas fa-flag-checkered' aria-hidden='true'></i>&nbsp;&nbsp;PDSL Reports</h5>"
            + "</div>"
            + "</div>"
            + "<div class='row' style='margin-top:2%'>"
            + "<div class='col-lg-12'>"
            + "<div class='row'>"
            + "<div class='col-lg-12'>"
            + "<div class='horizontalLine'></div>"
            + "</div>"
            + "</div>"
            + "</div>"
            + "</div>"
            + "</div>";
}
