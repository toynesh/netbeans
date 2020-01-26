/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.knec;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileReader;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import au.com.bytecode.opencsv.CSVReader;

/**
 *
 * @author coolie
 */
public class Home extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    DataStore data = new DataStore();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        if (null == session.getAttribute("fullname")) {
            String redirectURL = "Login";
            response.sendRedirect(redirectURL);
        }
        String fullname = (String) request.getSession().getAttribute("fullname");
        Connection con = data.connect();
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println(data.header);
            String stable = "seveneightportaldata";
            String year = "2019";

            if (null != request.getParameter("year")) {
                year = request.getParameter("year");
            }
            if (year.equals("2018")) {
                stable = "portaldata";
            }
            if (null != request.getParameter("optradio")) {
                stable = request.getParameter("optradio");
            }
            System.out.println("TABLE TO QUERY: " + stable);
            DateTime sdt = new DateTime();
            DateTimeFormatter sfmt = DateTimeFormat.forPattern("dd-mm-yyyy");
            String placeholderdate = sfmt.print(sdt);
            out.println("<div class='container-fluid' style='margin-top:2%'>");
            out.println("<div class='row'>");
            out.println("<div class='col-lg-6'>");
            out.println("<div class='form-group row'>");
            out.println("<label for='year' class='col-sm-1 col-form-label' style='margin-top:20px'>Year: </label>");
            out.println("<div class='col-sm-3' style='height:60px'>");
            out.println("<select style='font-size:30px;height:50px;'  class='form-control' onChange=\"window.document.location.href='Home?year='+this.value;\">");
            if (year.equals("2019")) {
                out.println("<option value='2019' selected>2019</option>");
                out.println("<option value='2018'>2018</option>");
            } else {
                out.println("<option value='2019'>2019</option>");
                out.println("<option value='2018' selected>2018</option>");
            }
            out.println("</select>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='col-lg-6'>");
            out.println("<form id='oria'  action='" + request.getContextPath() + "/Home' method='post'>");
            out.println("<input type='text' value='" + year + "' name='year' style='display:none'>");
            out.println("<label class='radio-inline'>");
            if (year.equals("2019")) {
                if (stable.equals("kcpe2019portaldata")) {
                    out.println("<input value='kcpe2019portaldata' onclick=\"document.getElementById('oria').submit();\" type='radio' name='optradio' checked>KCPE");
                } else {
                    out.println("<input value='kcpe2019portaldata' onclick=\"document.getElementById('oria').submit();\" type='radio' name='optradio'>KCPE");
                }
                out.println("</label>");
                out.println("<label class='radio-inline'>");
                if (stable.equals("kcse2019portaldata")) {
                    out.println("<input value='kcse2019portaldata' onclick=\"document.getElementById('oria').submit();\"  type='radio' name='optradio' checked>KCSE");
                } else {
                    out.println("<input value='kcse2019portaldata' onclick=\"document.getElementById('oria').submit();\"  type='radio' name='optradio'>KCSE");
                }
                out.println("</label>");
                out.println("<label class='radio-inline'>");
                if (stable.equals("seveneightportaldata")) {
                    out.println("<input value='seveneightportaldata' onclick=\"document.getElementById('oria').submit();\"  type='radio' name='optradio' checked>20078");
                } else {
                    out.println("<input value='seveneightportaldata' onclick=\"document.getElementById('oria').submit();\"  type='radio' name='optradio'>20078");
                }
                out.println("</label>");
            } else {
                if (stable.equals("portaldata")) {
                    out.println("<input value='portaldata' onclick=\"document.getElementById('oria').submit();\" type='radio' name='optradio' checked>KCPE");
                } else {
                    out.println("<input value='portaldata' onclick=\"document.getElementById('oria').submit();\" type='radio' name='optradio'>KCPE");
                }
                out.println("</label>");
                out.println("<label class='radio-inline'>");
                if (stable.equals("kcseportaldata")) {
                    out.println("<input value='kcseportaldata' onclick=\"document.getElementById('oria').submit();\"  type='radio' name='optradio' checked>KCSE");
                } else {
                    out.println("<input value='kcseportaldata' onclick=\"document.getElementById('oria').submit();\"  type='radio' name='optradio'>KCSE");
                }
                out.println("</label>");
                out.println("<label class='radio-inline'>");
                if (stable.equals("regportaldata")) {
                    out.println("<input value='regportaldata' onclick=\"document.getElementById('oria').submit();\"  type='radio' name='optradio' checked>REGCHECK");
                } else {
                    out.println("<input value='regportaldata' onclick=\"document.getElementById('oria').submit();\"  type='radio' name='optradio'>REGCHECK");
                }
                out.println("</label>");
                out.println("<label class='radio-inline'>");
                if (stable.equals("seveneight2019portaldata")) {
                    out.println("<input value='seveneight2019portaldata' onclick=\"document.getElementById('oria').submit();\"  type='radio' name='optradio' checked>20078");
                } else {
                    out.println("<input value='seveneight2019portaldata' onclick=\"document.getElementById('oria').submit();\"  type='radio' name='optradio'>20078");
                }
                out.println("</label>");
            }
            out.println("</form>");

            out.println("</br>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row'>");
            out.println("<div class='col-lg-3'>");
            out.println("<div id='navcontainer'>");
            out.println("<a data-toggle='collapse' href='.mobilenumber'>");
            out.println("<div class='mobilenumber collapse in'>");
            out.println("<i class='fas fa-phone'></i> Query By Mobile Number <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div> ");
            out.println("<div class='mobilenumber collapse'>");
            out.println("<i class='fas fa-phone'></i> Query By Mobile Number <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='mobilenumber collapse' style='padding-top:4%'>");
            out.println("<form action='" + request.getContextPath() + "/Home' method='post'>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='msisdn' name='msi' placeholder='E.g 254720640926'>");
            out.println("<input type='hidden' name='optradio' value='" + stable + "'>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='submit'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.indexnumber'>");
            out.println("<div class='indexnumber collapse in'>");
            out.println("<i class='fas fa-indent'></i> Query By Index Number <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div> ");
            out.println("<div class='indexnumber collapse'>");
            out.println("<i class='fas fa-indent'></i> Query By Index Number <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='indexnumber collapse' style='padding-top:4%'>");
            out.println("<form action='" + request.getContextPath() + "/Home' method='post'>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='inde' name='indexnumber' placeholder='E.g 18335117005'>");
            out.println("<input type='hidden' name='optradio' value='" + stable + "'>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='submit'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.smsid'>");
            out.println("<div class='smsid collapse in'>");
            out.println("<i class='fas fa-quidditch'></i> Query By SMS ID <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div>");
            out.println("<div class='smsid collapse'>");
            out.println("<i class='fas fa-quidditch'></i> Query By SMS ID <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='smsid collapse' style='padding-top:4%'>");
            out.println("<form action='" + request.getContextPath() + "/Home' method='post'>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='smsid' name='smsid' placeholder='E.g 674967'>");
            out.println("<input type='hidden' name='optradio' value='" + stable + "'>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='submit'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.smsc'>");
            out.println("<div class='smsc collapse in'>");
            out.println("<i class='fas fa-comments'></i> Query By SMSC <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div>");
            out.println("<div class='smsc collapse'>");
            out.println("<i class='fas fa-comments'></i> Query By SMSC <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='smsc collapse' style='padding-top:4%'>");
            out.println("<form action='" + request.getContextPath() + "/Home' method='post'>");
            out.println("<input type='hidden' name='optradio' value='" + stable + "'>");
            out.println("<div class='input-group'>");
            out.println("<div class='radio'>");
            out.println("<label><input type='radio' name='smsc' value='SAFARICOM' checked>SAFARICOM</label>");
            out.println("</div>");
            out.println("<div class='radio'>");
            out.println("<label><input type='radio' name='smsc' value='AIRTEL'>AIRTEL</label>");
            out.println("</div>");
            out.println("<div class='radio'>");
            out.println("<label><input type='radio' name='smsc' value='TELKOM'>TELKOM</label>");
            out.println("</div>");
            out.println("<div class='radio'>");
            out.println("<label><input type='radio' name='smsc' value='EQUITEL'>EQUITEL</label>");
            out.println("</div>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='submit'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.daterange'>");
            out.println("<div class='daterange collapse in'>");
            out.println("<i class='fas fa-clock'></i> Query By Send Date Rage <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div>");
            out.println("<div class='daterange collapse'>");
            out.println("<i class='fas fa-clock'></i> Query By Send Date Rage <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='daterange collapse' style='padding-top:4%'>");
            out.println("<form action='" + request.getContextPath() + "/Home' method='post'>");
            out.println("<div class='input-group' style='margin-bottom:4%'>");
            out.println("<span class='input-group-addon' id='basic-addon1'>StartDate:</span>");
            out.println("<input type='text' class='datepicker-here' data-language='en' data-position='top left' data-date-format='yyyy-mm-dd' name='sdate' placeholder='" + placeholderdate + "' aria-describedby='basic-addon1' required>");
            out.println("<input type='hidden' name='optradio' value='" + stable + "'>");
            out.println("</div>");
            out.println("<div class='input-group' style='margin-bottom:2%'>");
            out.println("<span class='input-group-addon' id='basic-addon1'>End  Date:</span>");
            out.println("<input type='text' class='datepicker-here' data-language='en' data-position='top left' data-date-format='yyyy-mm-dd' name='edate' placeholder='" + placeholderdate + "' aria-describedby='basic-addon1' required>");
            out.println("</div>");
            out.println("<button class='btn btn-primary' type='submit'  style='margin-left:80%'>Go!</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("<br><hr>");
            if (year.equals("2019")) {
                out.println("<a href='" + request.getContextPath() + "/Home?failed=kcpe'>Export Failed KCPE<i class='fas fa-angle-double-right pull-right'></i></a><br><br>");
                out.println("<a href='" + request.getContextPath() + "/Home?failed=kcse'>Export Failed KCSE<i class='fas fa-angle-double-right pull-right'></i></a><br><br>");
            } else {
                out.println("<a href='" + request.getContextPath() + "/Home?failed=kcpe'>Export Failed KCPE<i class='fas fa-angle-double-right pull-right'></i></a><br><br>");
                out.println("<a href='" + request.getContextPath() + "/Home?failed=kcse'>Export Failed KCSE<i class='fas fa-angle-double-right pull-right'></i></a><br><br>");
                out.println("<a href='" + request.getContextPath() + "/Home?failed=reg'>Export Failed REG<i class='fas fa-angle-double-right pull-right'></i></a><br>");
            }
            out.println("</div>");
            out.println("</div>");

            //safcount
            int safcount = 0;
            if (!year.equals("2019")) {
                try {
                    Statement statement = con.createStatement();
                    //ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM `"+stable+"` WHERE `smsc`='SAFARICOM'");
                    ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM safdlry WHERE `smsc`='SAFARICOM'");
                    if (stable.equals("portaldata")) {
                        resultSet = statement.executeQuery("SELECT COUNT(*) FROM  safdlrykcpe WHERE `smsc`='SAFARICOM'");
                    } else if (stable.equals("kcseportaldata")) {
                        resultSet = statement.executeQuery("SELECT COUNT(*) FROM  safdlrykcse WHERE `smsc`='SAFARICOM'");
                    } else if (stable.equals("seveneightportaldata")) {
                        resultSet = statement.executeQuery("SELECT COUNT(*) FROM  `" + stable + "` WHERE `smsc`='SAFARICOM'");
                    }
                    while (resultSet.next()) {
                        safcount = resultSet.getInt(1);
                    }
                } catch (SQLException myex) {
                }
                if (!stable.equals("seveneightportaldata")||!stable.equals("seveneight2019portaldata")) {
                    try {
                        Statement statement = con.createStatement();
                        //ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM `"+stable+"` WHERE `smsc`='SAFARICOM'");
                        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM dlr_status WHERE `correlator`='20076' AND `status`='DeliveredToTerminal'");
                        while (resultSet.next()) {
                            safcount = safcount + resultSet.getInt(1);
                        }

                    } catch (SQLException myex) {
                    }
                }
            } else {
                try {
                    Statement statement = con.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM `" + stable + "` WHERE `smsc`='SAFARICOM'");
                    while (resultSet.next()) {
                        safcount = resultSet.getInt(1);
                    }
                } catch (SQLException myex) {
                }
            }
            //airtelcount
            int airtelcount = 0;
            try {
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM `" + stable + "` WHERE `smsc`='AIRTEL'");
                while (resultSet.next()) {
                    airtelcount = resultSet.getInt(1);
                }
            } catch (SQLException myex) {
            }

            //telkomcount
            int telkomcount = 0;
            try {
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM `" + stable + "` WHERE `smsc`='TELKOM'");
                while (resultSet.next()) {
                    telkomcount = resultSet.getInt(1);
                }
            } catch (SQLException myex) {
            }
            //equitelcount
            int equitelcount = 0;
            try {
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM `" + stable + "` WHERE `smsc`='EQUITEL'");
                while (resultSet.next()) {
                    equitelcount = resultSet.getInt(1);
                }
            } catch (SQLException myex) {
            }

            List<List<String>> eexportarray = new ArrayList<>();
            List<List<String>> pexportarray = new ArrayList<>();
            String addedurl = "&year=" + year + "&optradio=" + stable + "";
            String limit = " limit 20";
            int pageno = 1;
            int pages = 1;
            if (null != request.getParameter("page")) {
                pageno = parseInt(request.getParameter("page"));
                int sindex = (pageno * 20) - 19;
                limit = " limit " + sindex + ",20";
            }
            String query = "";
            query = "SELECT sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc from " + stable + " order by time_recieved desc" + limit + "";

            if (null != request.getParameter("msi")) {
                String msisdn = request.getParameter("msi");
                query = "SELECT sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc from " + stable + " where sender='" + msisdn + "' order by time_recieved desc" + limit + "";
                addedurl = "&year=" + year + "&msi=" + msisdn + "&optradio=" + stable + "";
            }
            if (null != request.getParameter("indexnumber")) {
                String indexnumber = request.getParameter("indexnumber");
                query = "SELECT sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc from " + stable + " where inmessage='" + indexnumber + "' order by time_recieved desc" + limit + "";
                addedurl = "&year=" + year + "&indexnumber=" + indexnumber + "&optradio=" + stable + "";
            }
            if (null != request.getParameter("smsid")) {
                String smsid = request.getParameter("smsid");
                query = "SELECT sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc from " + stable + " where msgid='" + smsid + "' order by time_recieved desc" + limit + "";
                addedurl = "&year=" + year + "&smsid=" + smsid + "&optradio=" + stable + "";
            }
            if (null != request.getParameter("smsc")) {
                String smsc = request.getParameter("smsc");
                query = "SELECT sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc from " + stable + " where smsc='" + smsc + "' order by time_recieved desc" + limit + "";
                addedurl = "&year=" + year + "&smsc=" + smsc + "&optradio=" + stable + "";
            }
            if (null != request.getParameter("sdate")) {
                String sdate = request.getParameter("sdate");
                String edate = request.getParameter("edate");
                query = "SELECT sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc from " + stable + " where timesent between '" + sdate + "' and '" + edate + "' order by time_recieved desc" + limit + "";
                addedurl = "&year=" + year + "&sdate=" + sdate + "&edate=" + edate + "&optradio=" + stable + "";
            }

            System.out.println(query);

            try {
                String cquery = query.replaceAll(limit, " ").replaceAll("sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc", "COUNT(*)");
                System.out.println("Count query: " + cquery);
                Statement rowstm = con.createStatement();
                ResultSet rowrs = rowstm.executeQuery(cquery);
                int numrows = 0;
                while (rowrs.next()) {
                    numrows = rowrs.getInt(1);
                }
                System.out.println("Rows:" + numrows);
                pages = numrows / 20;
                if ((numrows % 20) != 0) {
                    pages = pages + 1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Pages:" + pages);

            //TO EXPORT            
            if (null != request.getParameter("expo")) {
                if (request.getParameter("expo").equals("excel")) {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "knecsms" + currdate;
                    PrintWriter writer = new PrintWriter("/opt/applications/downloads/" + filename + ".csv", "UTF-8");
                    writer.println("Mobile,Shortcode,Inbox,Time sent,Message,MSG ID,Delivery Status,SMSC");
                    try {
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query.replaceAll(limit, " "));
                        ResultSetMetaData metadata = rs.getMetaData();
                        int numcols = metadata.getColumnCount();
                        while (rs.next()) {
                            List<String> row = new ArrayList<>(numcols);
                            int i = 1;
                            while (i <= numcols) {
                                row.add(rs.getString(i++));
                            }
                            //eexportarray.add(row);
                            writer.println(row);
                        }
                    } catch (SQLException ex) {
                    }
                    //exportExcel(eexportarray, filename);
                    //String redirectURL = "DownloadFile?ftdown=" + filename + ".xls";
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".csv";
                    response.sendRedirect(redirectURL);
                } else {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "knecsms" + currdate;
                    //PrintWriter writer = new PrintWriter("/opt/applications/downloads/" + filename + ".csv", "UTF-8");
                    //writer.println("Mobile,Shortcode,Inbox,Time sent,Message,MSG ID,Delivery Status,SMSC");
                    try {
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query.replaceAll(limit, " limit 14400"));
                        ResultSetMetaData metadata = rs.getMetaData();
                        int numcols = metadata.getColumnCount();
                        while (rs.next()) {
                            List<String> row = new ArrayList<>(numcols);
                            int i = 1;
                            while (i <= numcols) {
                                row.add(rs.getString(i++));
                            }
                            pexportarray.add(row);
                            //writer.println(row);
                        }
                    } catch (SQLException ex) {
                    }
                    exportPDF(pexportarray, filename);
                    //csvTOpdf(filename);
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".pdf";
                    response.sendRedirect(redirectURL);
                }
            }
            if (null != request.getParameter("failed")) {
                if (request.getParameter("failed").equals("kcpe")) {
                    String fquery = "SELECT sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc from failedkcpe order by time_recieved desc";
                    if (year.equals("2019")) {
                        fquery = "SELECT sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc from kcpe2019failed order by time_recieved desc";
                    }
                    System.out.println("Failed QUERY: " + fquery);
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "knecsmsfailedkcpe" + currdate;
                    PrintWriter writer = new PrintWriter("/opt/applications/downloads/" + filename + ".csv", "UTF-8");
                    writer.println("Mobile,Shortcode,Inbox,Time sent,Message,MSG ID,Delivery Status,SMSC");
                    try {
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(fquery);
                        ResultSetMetaData metadata = rs.getMetaData();
                        int numcols = metadata.getColumnCount();
                        while (rs.next()) {
                            List<String> row = new ArrayList<>(numcols);
                            int i = 1;
                            while (i <= numcols) {
                                row.add(rs.getString(i++));
                            }
                            //eexportarray.add(row);
                            writer.println(row);
                        }
                    } catch (SQLException ex) {
                    }
                    writer.close();
                    //exportExcel(eexportarray, filename);
                    //String redirectURL = "DownloadFile?ftdown=" + filename + ".xls";
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".csv";
                    response.sendRedirect(redirectURL);
                } else if (request.getParameter("failed").equals("kcse")) {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "knecsmsfailedkcse" + currdate;
                    PrintWriter writer = new PrintWriter("/opt/applications/downloads/" + filename + ".csv", "UTF-8");
                    writer.println("Mobile,Shortcode,Inbox,Time sent,Message,MSG ID,Delivery Status,SMSC");
                    try {
                        String fquery = "SELECT sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc from failedkcse order by time_recieved desc";
                        if (year.equals("2019")) {
                            fquery = "SELECT sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc from kcse2019failed order by time_recieved desc";
                        }
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(fquery);
                        ResultSetMetaData metadata = rs.getMetaData();
                        int numcols = metadata.getColumnCount();
                        while (rs.next()) {
                            List<String> row = new ArrayList<>(numcols);
                            int i = 1;
                            while (i <= numcols) {
                                row.add(rs.getString(i++));
                            }
                            //eexportarray.add(row);
                            writer.println(row);
                        }
                    } catch (SQLException ex) {
                    }
                    writer.close();
                    //exportExcel(eexportarray, filename);
                    //String redirectURL = "DownloadFile?ftdown=" + filename + ".xls";
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".csv";
                    response.sendRedirect(redirectURL);
                } else {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "knecsms" + currdate;
                    PrintWriter writer = new PrintWriter("/opt/applications/downloads/" + filename + ".csv", "UTF-8");
                    writer.println("Mobile,Shortcode,Inbox,Time sent,Message,MSG ID,Delivery Status,SMSC");
                    try {
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("SELECT sender,shortcode,inmessage,timesent,outmessage,msgid,deliverystatus,smsc from failedreg order by time_recieved desc");
                        ResultSetMetaData metadata = rs.getMetaData();
                        int numcols = metadata.getColumnCount();
                        while (rs.next()) {
                            List<String> row = new ArrayList<>(numcols);
                            int i = 1;
                            while (i <= numcols) {
                                row.add(rs.getString(i++));
                            }
                            //eexportarray.add(row);
                            writer.println(row);
                        }
                    } catch (SQLException ex) {
                    }
                    writer.close();
                    //exportExcel(eexportarray, filename);
                    //String redirectURL = "DownloadFile?ftdown=" + filename + ".xls";
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".csv";
                    response.sendRedirect(redirectURL);
                }
            }
            out.println("<div class='col-lg-9'>");
            out.println("<div class='panel panel-primary  table-responsive'>");
            out.println("<div class='panel-heading'><span><i class='far fa-circle faa-burst animated'></i> " + fullname + "</span><span style='margin-left:3%;font-size:14px'>Total SAFtx: " + String.format("%,d", safcount) + " <i class='text-danger fas fa-align-left fa-rotate-270'></i> AIRtx: " + String.format("%,d", airtelcount) + " <i class='text-danger fas fa-align-left fa-rotate-270'></i> TELtx: " + String.format("%,d", telkomcount) + " <i class='text-danger fas fa-align-left fa-rotate-270'></i> EQUItx: " + String.format("%,d", equitelcount) + "</span><span style='margin-left:2%;color:white;font-size:14px'>Export: <a href='" + request.getContextPath() + "/Home?expo=excel" + addedurl + "'><i class='far fa-file-excel'></i> Excel</a>&nbsp;&nbsp;&nbsp;<a href='" + request.getContextPath() + "/Home?expo=pdf" + addedurl + "'><i class='far fa-file-pdf'></i> PDF</a></span><span class='pull-right'><i class='fas fa-ellipsis-h'></i> <a href='" + request.getContextPath() + "/Login?logout=logout' style='color:white;'> Signout</a></span></div>");
            out.println("<div class='panel-body'>");

            out.println("<table class='table table-bordered' style='font-size:11px'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Mobile</th>");
            out.println("<th>Shortcode</th>");
            out.println("<th>SMSC</th>");
            out.println("<th>Inbox</th>");
            out.println("<th>Time sent</th>");
            out.println("<th>Message</th>");
            out.println("<th>MSG ID</th>");
            out.println("<th>Delivery Status</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody style='color:black'>");

            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString(1) + "</td>");
                out.println("<td>" + rs.getString(2) + "</td>");
                out.println("<td>" + rs.getString(8) + "</td>");
                out.println("<td>" + rs.getString(3) + "</td>");
                out.println("<td>" + rs.getString(4) + "</td>");
                out.println("<td>" + rs.getString(5) + "</td>");
                out.println("<td>" + rs.getString(6) + "</td>");
                out.println("<td>" + rs.getString(7) + "</td>");
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");

            //PURE PAGENATION
            out.println("<nav aria-label='...' class='text-center' id='pngn'>");
            out.println("<ul class='pagination'>");
            if (pageno > 1) {
                out.println("<li class='page-item'>");
                out.println("<a class='page-link' href='" + request.getContextPath() + "/Home?page=1" + addedurl + "#pngn' aria-label='Previous'>");
                out.println("<span aria-hidden='true'><i class='fas fa-fast-backward'></i></span>");
                out.println("<span class='sr-only'>Previous</span>");
                out.println("</a>");
                out.println("</li>");
            } else {
                out.println("<li class='page-item disabled'>");
                out.println("<a class='page-link' href='' aria-label='Previous'>");
                out.println("<span aria-hidden='true'><i class='fas fa-fast-backward'></i></span>");
                out.println("<span class='sr-only'>Previous</span>");
                out.println("</a>");
                out.println("</li>");
            }
            if (pages <= 1) {
                for (int i = 1; i <= 10; i++) {
                    out.println("<li class='page-item'><a class='page-link' href='" + request.getContextPath() + "/Home?" + i + addedurl + "#pngn'>" + i + "</a></li>");
                }
            } else {
                if ((pageno - 4) < 1) {
                    for (int i = 1; i <= 10; i++) {
                        if (i == pageno) {
                            out.println("<li class='page-item active'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                        } else {
                            out.println("<li class='page-item'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                        }
                    }
                } else if ((pageno + 5) > pages) {
                    if ((pages - 9) > 1) {
                        for (int i = pages - 9; i <= pages; i++) {
                            if (i == pageno) {
                                out.println("<li class='page-item active'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                            } else {
                                out.println("<li class='page-item'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                            }
                        }
                    } else {
                        for (int i = 1; i <= pages; i++) {
                            if (i == pageno) {
                                out.println("<li class='page-item active'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                            } else {
                                out.println("<li class='page-item'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                            }
                        }
                    }
                } else {
                    for (int i = pageno - 4; i <= pageno + 5; i++) {
                        if (i == pageno) {
                            out.println("<li class='page-item active'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                        } else {
                            out.println("<li class='page-item'><a class='page-link' href='" + request.getContextPath() + "/Home?page=" + i + addedurl + "#pngn'>" + i + "</a></li>");
                        }
                    }
                }
            }
            if ((pageno + 1) > pages) {
                out.println("<li class='page-item disabled'>");
                out.println("<a class='page-link' href='' aria-label='Next'>");
                out.println("<span aria-hidden='true'><i class='fas fa-fast-forward'></i></span>");
                out.println("<span class='sr-only'>Next</span>");
                out.println("</a>");
                out.println("</li>");
            } else {
                out.println("<li class='page-item'>");
                out.println("<a class='page-link' href='" + request.getContextPath() + "/Home?page=" + pages + addedurl + "#pngn' aria-label='Next'>");
                out.println("<span aria-hidden='true'><i class='fas fa-fast-forward'></i></span>");
                out.println("<span class='sr-only'>Next</span>");
                out.println("</a>");
                out.println("</li>");
            }
            out.println("</ul>");
            out.println("</nav> ");
            //END PAGENATION

            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println(data.footer);
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void exportExcel(List<List<String>> export, String file) throws FileNotFoundException, IOException {
        //System.out.println("Exporting in the here size: " + export.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Excel Sheet");
        HSSFRow rowhead = sheet.createRow((short) 0);
        rowhead.createCell((short) 0).setCellValue("#");
        rowhead.createCell((short) 1).setCellValue("Mobile");
        rowhead.createCell((short) 2).setCellValue("Shortcode");
        rowhead.createCell((short) 3).setCellValue("SMSC");
        rowhead.createCell((short) 4).setCellValue("Inbox");
        rowhead.createCell((short) 5).setCellValue("Time sent");
        rowhead.createCell((short) 6).setCellValue("Message");
        rowhead.createCell((short) 7).setCellValue("MSG ID");
        rowhead.createCell((short) 8).setCellValue("Delivery Status");
        int index = 1;
        for (int x = 0; x < export.size(); x++) {
            HSSFRow row = sheet.createRow((short) index);
            row.createCell((short) 0).setCellValue(index);
            row.createCell((short) 1).setCellValue(export.get(x).get(0));
            row.createCell((short) 2).setCellValue(export.get(x).get(1));
            row.createCell((short) 3).setCellValue(export.get(x).get(7));
            row.createCell((short) 4).setCellValue(export.get(x).get(2));
            row.createCell((short) 5).setCellValue(export.get(x).get(3));
            row.createCell((short) 6).setCellValue(export.get(x).get(4));
            row.createCell((short) 7).setCellValue(export.get(x).get(5));
            row.createCell((short) 8).setCellValue(export.get(x).get(6));
            index++;
        }

        String filename = file + ".xls";
        FileOutputStream fileOut = new FileOutputStream("/opt/applications/downloads/" + filename);
        wb.write(fileOut);
        fileOut.close();

    }

    public void exportPDF(List<List<String>> export, String file) throws FileNotFoundException, IOException {
        try {
            System.out.println("Exporting to pdf size: " + export.size());

            //Document PDFLogReport = new Document(PageSize.A4, 0f, 0f, 15f, 0f);
            Document PDFLogReport = new Document(new Rectangle(792, 612));
            PdfWriter.getInstance(PDFLogReport, new FileOutputStream("/opt/applications/downloads/" + file + ".pdf"));
            PDFLogReport.open();
            Paragraph paragraph1 = new Paragraph("#KNECSMS Report");
            paragraph1.setSpacingAfter(10f);
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            PDFLogReport.add(paragraph1);

            PdfPTable LogTable = new PdfPTable(9);
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);

            //create a cell object
            PdfPCell table_cell;
            String num = "#";
            table_cell = new PdfPCell(new Phrase(num, font));
            LogTable.addCell(table_cell);
            String Mobile = "Mobile";
            table_cell = new PdfPCell(new Phrase(Mobile, font));
            LogTable.addCell(table_cell);
            String Shortcode = "Shortcode";
            table_cell = new PdfPCell(new Phrase(Shortcode, font));
            LogTable.addCell(table_cell);
            String SMSC = "SMSC";
            table_cell = new PdfPCell(new Phrase(SMSC, font));
            LogTable.addCell(table_cell);
            String Inbox = "Inbox";
            table_cell = new PdfPCell(new Phrase(Inbox, font));
            LogTable.addCell(table_cell);
            String Timein = "Time sent";
            table_cell = new PdfPCell(new Phrase(Timein, font));
            LogTable.addCell(table_cell);
            String Message = "Message";
            table_cell = new PdfPCell(new Phrase(Message, font));
            LogTable.addCell(table_cell);
            String Msgid = "MSG ID";
            table_cell = new PdfPCell(new Phrase(Msgid, font));
            LogTable.addCell(table_cell);
            String dlry = "Delivery Status";
            table_cell = new PdfPCell(new Phrase(dlry, font));
            LogTable.addCell(table_cell);
            int index = 1;
            for (int x = 0; x < export.size(); x++) {
                String rc = Integer.toString(index);
                table_cell = new PdfPCell(new Phrase(rc, font));
                LogTable.addCell(table_cell);
                String Mobile1 = export.get(x).get(0);
                table_cell = new PdfPCell(new Phrase(Mobile1, font));
                LogTable.addCell(table_cell);
                String Shortcode1 = export.get(x).get(1);
                table_cell = new PdfPCell(new Phrase(Shortcode1, font));
                LogTable.addCell(table_cell);
                String SMSC1 = export.get(x).get(7);
                table_cell = new PdfPCell(new Phrase(SMSC1, font));
                LogTable.addCell(table_cell);
                String Inbox1 = export.get(x).get(2);
                table_cell = new PdfPCell(new Phrase(Inbox1, font));
                LogTable.addCell(table_cell);
                String Timein1 = export.get(x).get(3);
                table_cell = new PdfPCell(new Phrase(Timein1, font));
                LogTable.addCell(table_cell);
                String Message1 = export.get(x).get(4);
                table_cell = new PdfPCell(new Phrase(Message1, font));
                LogTable.addCell(table_cell);
                String Msgid1 = export.get(x).get(5);
                table_cell = new PdfPCell(new Phrase(Msgid1, font));
                LogTable.addCell(table_cell);
                String dlry1 = export.get(x).get(6);
                table_cell = new PdfPCell(new Phrase(dlry1, font));
                LogTable.addCell(table_cell);
                index++;
            }
            /* Attach report table to PDF */
            PDFLogReport.add(LogTable);
            PDFLogReport.close();
        } catch (DocumentException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void csvTOpdf(String inputCSVFile) {
        try {
            /* Step -1 : Read input CSV file in Java */
            CSVReader reader = new CSVReader(new FileReader("/opt/applications/downloads/" + inputCSVFile + ".csv"));
            /* Variables to loop through the CSV File */
            String[] nextLine;
            /* for every line in the file */
            int lnNum = 0;
            /* line number */
 /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_data = new Document();
            PdfWriter.getInstance(my_pdf_data, new FileOutputStream("/opt/applications/downloads/" + inputCSVFile + ".pdf"));
            my_pdf_data.open();
            PdfPTable my_first_table = new PdfPTable(2);
            PdfPCell table_cell;
            /* Step -3: Loop through CSV file and populate data to PDF table */
            while ((nextLine = reader.readNext()) != null) {
                lnNum++;
                table_cell = new PdfPCell(new Phrase(nextLine[0]));
                my_first_table.addCell(table_cell);
                table_cell = new PdfPCell(new Phrase(nextLine[1]));
                my_first_table.addCell(table_cell);
                //System.out.println("Line1:"+nextLine[0]+" Line2:"+nextLine[1]+" Line3:"+nextLine[2]+" Line4:"+nextLine[3]+" Line5:"+nextLine[4]+" Line6:"+nextLine[5]+" Line7:"+nextLine[6]+" Line8:"+nextLine[7]);
            }
            /* Step -4: Attach table to PDF and close the document */
            my_pdf_data.add(my_first_table);
            my_pdf_data.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
