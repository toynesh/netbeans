/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vaudits;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
    public Home() {
        //updateVendITArray();
    }
    List<String> vendITmpesacodes = new ArrayList<>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            HttpSession session = request.getSession();
            if (null == session.getAttribute("fullname")) {
                String redirectURL = "Login";
                response.sendRedirect(redirectURL);
            }

            String fullname = (String) request.getSession().getAttribute("fullname");

            DataStore data = new DataStore();
            out.println(data.header);
            DateTime sdt = new DateTime().minusDays(2);
            DateTimeFormatter sfmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            String placeholderdate = sfmt.print(sdt);
            out.println("<div class='container-fluid' style='margin-top:2%'>");
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
            out.println("<form action='#' method='post'>");
            out.println("<div class='input-group' style='margin-bottom:4%'>");
            out.println("<span class='input-group-addon' id='basic-addon1'>Date:</span>");
            out.println("<input type='text' style='width:90%' class='datepicker-here' data-language='en' data-position='bottom right' data-date-format='yyyy-mm-dd' name='qdate' placeholder='" + placeholderdate + "' aria-describedby='basic-addon1'>");
            out.println("</div>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='msisdn' name='msisdn' placeholder='E.g 254720640926' required>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='submit'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.meternumber'>");
            out.println("<div class='meternumber collapse in'>");
            out.println("<i class='fas fa-tachometer-alt'></i> Query By Meter Number <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div> ");
            out.println("<div class='meternumber collapse'>");
            out.println("<i class='fas fa-tachometer-alt'></i> Query By Meter Number <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='meternumber collapse' style='padding-top:4%'>");
            out.println("<form action='#' method='post'>");
            out.println("<div class='input-group' style='margin-bottom:4%'>");
            out.println("<span class='input-group-addon' id='basic-addon1'>Date:</span>");
            out.println("<input type='text' style='width:90%' class='datepicker-here' data-language='en' data-position='bottom right' data-date-format='yyyy-mm-dd' name='qdate' placeholder='" + placeholderdate + "' aria-describedby='basic-addon1'>");
            out.println("</div>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='meter' name='meter' placeholder='E.g 14763391074' required>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='submit'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.accountnumber'>");
            out.println("<div class='accountnumber collapse in'>");
            out.println("<i class='fab fa-accusoft'></i> Query By Account Number <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div>");
            out.println("<div class='accountnumber collapse'>");
            out.println("<i class='fab fa-accusoft'></i> Query By Account Number <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='accountnumber collapse' style='padding-top:4%'>");
            out.println("<form action='#' method='post'>");
            out.println("<div class='input-group' style='margin-bottom:4%'>");
            out.println("<span class='input-group-addon' id='basic-addon1'>Date:</span>");
            out.println("<input type='text' style='width:90%' class='datepicker-here' data-language='en' data-position='top right' data-date-format='yyyy-mm-dd' name='qdate' placeholder='" + placeholderdate + "' aria-describedby='basic-addon1'>");
            out.println("</div>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='meter' name='meter' placeholder='E.g 2586079-02' required>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='submit'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.mpesacode'>");
            out.println("<div class='mpesacode collapse in'>");
            out.println("<i class='fas fa-money-bill-alt'></i> Query By Mpesa Code <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div>");
            out.println("<div class='mpesacode collapse'>");
            out.println("<i class='fas fa-money-bill-alt'></i> Query By Mpesa Code <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='mpesacode collapse' style='padding-top:4%'>");
            out.println("<form action='#' method='post'>");
            out.println("<div class='input-group' style='margin-bottom:4%'>");
            out.println("<span class='input-group-addon' id='basic-addon1'>Date:</span>");
            out.println("<input type='text' style='width:90%' class='datepicker-here' data-language='en' data-position='top right' data-date-format='yyyy-mm-dd' name='qdate' placeholder='" + placeholderdate + "' aria-describedby='basic-addon1'>");
            out.println("</div>");
            out.println("<div class='input-group'>");
            out.println("<input type='text' class='form-control' id='mpesacode' name='mpesacode' placeholder='E.g QUYTREWTY0I' required>");
            out.println("<span class='input-group-btn'>");
            out.println("<button class='btn btn-primary' type='submit'>Go!</button>");
            out.println("</span>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<hr>");
            out.println("<a data-toggle='collapse' href='.daterange'>");
            out.println("<div class='daterange collapse in'>");
            out.println("<i class='fas fa-calendar-alt'></i> Query By Date <i class='fas fa-angle-double-right pull-right'></i>");
            out.println("</div>");
            out.println("<div class='daterange collapse'>");
            out.println("<i class='fas fa-calendar-alt'></i> Query By Date <i class='fas fa-angle-double-down pull-right'></i>");
            out.println("</div>");
            out.println("</a>");
            out.println("<div class='daterange collapse' style='padding-top:4%'>");
            out.println("<form action='" + request.getContextPath() + "/Home' method='post'>");
            out.println("<div class='input-group' style='margin-bottom:4%'>");
            out.println("<span class='input-group-addon' id='basic-addon1'>Date:</span>");
            out.println("<input type='text' style='width:90%' class='datepicker-here' data-language='en' data-position='top right' data-date-format='yyyy-mm-dd' name='qdate' placeholder='" + placeholderdate + "' aria-describedby='basic-addon1'>");
            out.println("</div>");
            out.println("<button class='btn btn-primary' type='submit'   style='margin-left:80%'>Go!</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("<div style='margin-top:9%'>");
            out.println("<a href='" + request.getContextPath() + "/Login?logout=logout' style='color:red'><i class='fas fa-outdent'></i> Sign Out</a> --- User: " + fullname + "");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");

            //String qdate = "2018-04-26";
            String qdate = placeholderdate;
            String msisdn = "none";
            String meter = "none";
            String mpesacode = "none";
            if (null != request.getParameter("qdate")) {
                if (!request.getParameter("qdate").equals("")) {
                    qdate = request.getParameter("qdate");
                }
            }
            if (null != request.getParameter("msisdn")) {
                if (!request.getParameter("msisdn").equals("")) {
                    msisdn = request.getParameter("msisdn");
                }
            }
            if (null != request.getParameter("meter")) {
                if (!request.getParameter("meter").equals("")) {
                    meter = request.getParameter("meter");
                }
            }
            if (null != request.getParameter("mpesacode")) {
                if (!request.getParameter("mpesacode").equals("")) {
                    mpesacode = request.getParameter("mpesacode");
                }
            }
            //System.out.println("Selected Date: " + qdate);
            List<String> mpesacodes = new ArrayList<>();
            List<String> mpesacodes2 = new ArrayList<>();
            List<List<String>> ipaympesa = new ArrayList<>();
            List<List<String>> mpesa = new ArrayList<>();
            List<List<String>> mpesa2 = new ArrayList<>();
            List<List<String>> equitel = new ArrayList<>();
            List<List<String>> manualvends = new ArrayList<>();
            List<List<String>> unknown = new ArrayList<>();
            Connection con = data.connect();
            try {
                String query = "SELECT `MpesaTxcode` FROM `mpesa" + qdate.replaceAll("-", "") + "`";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    //System.out.println("Adding: " + rs.getString(1));
                    mpesacodes.add(rs.getString(1));
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                out.println("<h5 class='text-center text-danger'> >>No Records Found for The selected Date<< </h5>");
            }
            try {
                String query = "SELECT `MpesaTxcode` FROM `mpesa" + qdate.replaceAll("-", "") + "` WHERE `MpesaTerminal`='501201'";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    //System.out.println("Adding: " + rs.getString(1));
                    mpesacodes2.add(rs.getString(1));
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                out.println("<h5 class='text-center text-danger'> >>No Records Found for The selected Date<< </h5>");
            }
            out.println("<div class='col-lg-9'>");
            out.println("<div class='well  table-responsive'>");
            out.println("<span><h4>Mpesa Transactions(" + qdate + ") </span><span class='pull-right'>Export: <a href='" + request.getContextPath() + "/Home?excelexpo=mpesa&qdate=" + qdate + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "'><i class='far fa-file-excel'></i> Excel</a>&nbsp;&nbsp;<a href='" + request.getContextPath() + "/Home?pdfexpo=mpesa&qdate=" + qdate + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "'><i class='far fa-file-pdf'></i> PDF</a></span></h4>");
            out.println("<div class='achievements-wrapper'>");
            out.println("<table class='table table-bordered table-striped fixed-header'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>#</th>");
            out.println("<th>Date</th>");
            out.println("<th>Phone</th>");
            out.println("<th>Meter/Account</th>");
            out.println("<th>Amount</th>");
            out.println("<th>Code</th>");
            out.println("<th>Token</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");

            double mpesasum = 0.0;
            double mpesasumprep = 0.0;
            double mpesasumpost = 0.0;
            try {
                String query = "SELECT `req_sent`,`phone_num`,`meter_account`,`amt`,`external_ref`,`token`,`success` FROM `ipay" + qdate.replaceAll("-", "") + "`";
                if (!msisdn.equals("none")) {
                    query = "SELECT `req_sent`,`phone_num`,`meter_account`,`amt`,`external_ref`,`token`,`success` FROM `ipay" + qdate.replaceAll("-", "") + "` WHERE `phone_num`='" + msisdn + "'";
                } else if (!meter.equals("none")) {
                    query = "SELECT `req_sent`,`phone_num`,`meter_account`,`amt`,`external_ref`,`token`,`success` FROM `ipay" + qdate.replaceAll("-", "") + "` WHERE `meter_account`='" + meter + "'";
                } else if (!mpesacode.equals("none")) {
                    query = "SELECT `req_sent`,`phone_num`,`meter_account`,`amt`,`external_ref`,`token`,`success` FROM `ipay" + qdate.replaceAll("-", "") + "` WHERE `external_ref`='" + mpesacode + "'";
                }
                System.out.println("Query: " + query);
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
                    ipaympesa.add(row);
                }
            } catch (SQLException ex) {
                //ex.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {

                }
            }
            for (int y = 0; y < ipaympesa.size(); y++) {
                System.out.println("===========Next rooww======== ");
                if (mpesacodes.contains(ipaympesa.get(y).get(4))) {
                    if (ipaympesa.get(y).get(6).equals("t")) {
                        if (mpesacodes2.contains(ipaympesa.get(y).get(4))) {
                            mpesa2.add(ipaympesa.get(y));
                        }else{
                            mpesa.add(ipaympesa.get(y));
                        }
                    }
                    /*} else if (ipaympesa.get(y).get(4).contains("EQU_")) {
                    if (!ipaympesa.get(y).get(3).equals("0.00")) {
                        equitel.add(ipaympesa.get(y));
                    }*/
                } else if (mpesacodes.contains(ipaympesa.get(y).get(4))) {
                    if (ipaympesa.get(y).get(6).equals("f")) {
                        equitel.add(ipaympesa.get(y));
                    }
                } else if (ipaympesa.get(y).get(3).equals("0.00")) {
                    manualvends.add(ipaympesa.get(y));
                } else {
                    if (!mpesa.contains(ipaympesa.get(y))) {
                        unknown.add(ipaympesa.get(y));
                    }
                    /*if (vendITmpesacodes.contains(ipaympesa.get(y).get(4))) {
                        System.out.println(">>>>>Found in Manual vend<<<<<<<");
                        manualvends.add(ipaympesa.get(y));
                    } else {
                        unknown.add(ipaympesa.get(y));
                    }*/
                }
            }
            //EXPORT!!
            if (null != request.getParameter("excelexpo")) {
                if (request.getParameter("excelexpo").equals("mpesa")) {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "mpesaudits" + currdate;
                    exportExcel(mpesa, filename);
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".xls";
                    response.sendRedirect(redirectURL);
                } else if (request.getParameter("excelexpo").equals("mpesa2")) {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "501201udits" + currdate;
                    exportExcel(mpesa2, filename);
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".xls";
                    response.sendRedirect(redirectURL);
                } else if (request.getParameter("excelexpo").equals("equitel")) {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "equiteludits" + currdate;
                    exportExcel(equitel, filename);
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".xls";
                    response.sendRedirect(redirectURL);
                } else if (request.getParameter("excelexpo").equals("manual")) {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "manualvendsudits" + currdate;
                    exportExcel(manualvends, filename);
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".xls";
                    response.sendRedirect(redirectURL);
                } else {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "unknownudits" + currdate;
                    exportExcel(unknown, filename);
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".xls";
                    response.sendRedirect(redirectURL);
                }
            }
            if (null != request.getParameter("pdfexpo")) {
                if (request.getParameter("pdfexpo").equals("mpesa")) {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "mpesaudits" + currdate;
                    exportPDF(mpesa, filename);
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".pdf";
                    response.sendRedirect(redirectURL);
                } else if (request.getParameter("pdfexpo").equals("mpesa2")) {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "501201udits" + currdate;
                    exportPDF(mpesa2, filename);
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".pdf";
                    response.sendRedirect(redirectURL);
                } else if (request.getParameter("pdfexpo").equals("equitel")) {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "equiteludits" + currdate;
                    exportPDF(equitel, filename);
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".pdf";
                    response.sendRedirect(redirectURL);
                } else if (request.getParameter("pdfexpo").equals("manual")) {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "manualvendsudits" + currdate;
                    exportPDF(manualvends, filename);
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".pdf";
                    response.sendRedirect(redirectURL);
                } else {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "unknownudits" + currdate;
                    exportPDF(unknown, filename);
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".pdf";
                    response.sendRedirect(redirectURL);
                }
            }

            int index1 = 1;
            for (int y = 0; y < mpesa.size(); y++) {
                String token = "NONE";
                if (null != mpesa.get(y).get(5)) {
                    token = mpesa.get(y).get(5);
                }
                out.println("<tr>");
                out.println("<td>" + index1 + "</td>");
                out.println("<td>" + mpesa.get(y).get(0) + "</td>");
                out.println("<td>" + mpesa.get(y).get(1) + "</td>");
                out.println("<td>" + mpesa.get(y).get(2) + "</td>");
                out.println("<td>" + mpesa.get(y).get(3) + "</td>");
                out.println("<td>" + mpesa.get(y).get(4) + "</td>");
                out.println("<td>" + token + "</td>");
                out.println("</tr>");
                mpesasum = mpesasum + Double.parseDouble(mpesa.get(y).get(3));
                if (mpesa.get(y).get(2).length() < 11) {
                    mpesasumpost = mpesasumpost + Double.parseDouble(mpesa.get(y).get(3));
                } else {
                    mpesasumprep = mpesasumprep + Double.parseDouble(mpesa.get(y).get(3));
                }
                index1++;
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            out.println("<h5><span>Sum Mpesa Amount</span><span class='pull-right' style='margin-right:5%'>Prepaid: " + formatter.format(mpesasumprep) + " Postpaid: " + formatter.format(mpesasumpost) + " Total: " + formatter.format(mpesasum) + "</span></h5>");
            out.println("<div class='horizontalLine'></div>");
            out.println("<div class='horizontalLine'></div>");
            out.println("</br>");
            out.println("</br>");
            
            out.println("<h4>501201 Mpesa Transactions(" + qdate + ") </span><span class='pull-right'>Export: <a href='" + request.getContextPath() + "/Home?excelexpo=mpesa2&qdate=" + qdate + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "'><i class='far fa-file-excel'></i> Excel</a>&nbsp;&nbsp;<a href='" + request.getContextPath() + "/Home?pdfexpo=mpesa2&qdate=" + qdate + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "'><i class='far fa-file-pdf'></i> PDF</a></span></h4>");
            out.println("<div class='achievements-wrapper'>");
            out.println("<table class='table table-bordered table-striped'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>#</th>");
            out.println("<th>Date</th>");
            out.println("<th>Phone</th>");
            out.println("<th>Meter/Account</th>");
            out.println("<th>Amount</th>");
            out.println("<th>Code</th>");
            out.println("<th>Token</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            double mpesa2sum = 0.0;
            double mpesa2sumprep = 0.0;
            double mpesa2sumpost = 0.0;
            int index22 = 1;
            for (int y = 0; y < mpesa2.size(); y++) {
                String token = "NONE";
                if (null != mpesa2.get(y).get(5)) {
                    token = mpesa2.get(y).get(5);
                }
                out.println("<tr>");
                out.println("<td>" + index22 + "</td>");
                out.println("<td>" + mpesa2.get(y).get(0) + "</td>");
                out.println("<td>" + mpesa2.get(y).get(1) + "</td>");
                out.println("<td>" + mpesa2.get(y).get(2) + "</td>");
                out.println("<td>" + mpesa2.get(y).get(3) + "</td>");
                out.println("<td>" + mpesa2.get(y).get(4) + "</td>");
                out.println("<td>" + token + "</td>");
                out.println("</tr>");
                mpesa2sum = mpesa2sum + Double.parseDouble(mpesa2.get(y).get(3));
                if (mpesa2.get(y).get(2).length() < 11) {
                    mpesa2sumpost = mpesa2sumpost + Double.parseDouble(mpesa2.get(y).get(3));
                } else {
                    mpesa2sumprep = mpesa2sumprep + Double.parseDouble(mpesa2.get(y).get(3));
                }
                index22++;
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("<h5><span>Sum 501201 Mpesa Amount</span><span class='pull-right' style='margin-right:5%'>Prepaid: " + formatter.format(mpesa2sumprep) + " Postpaid: " + formatter.format(mpesa2sumpost) + " Total: " + formatter.format(mpesa2sum) + "</span></h5>");
            out.println("<div class='horizontalLine'></div>");
            out.println("<div class='horizontalLine'></div>");
            out.println("</br>");
            out.println("</br>");

            out.println("<h4>Failed Mpesa Transactions(" + qdate + ") </span><span class='pull-right'>Export: <a href='" + request.getContextPath() + "/Home?excelexpo=equitel&qdate=" + qdate + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "'><i class='far fa-file-excel'></i> Excel</a>&nbsp;&nbsp;<a href='" + request.getContextPath() + "/Home?pdfexpo=equitel&qdate=" + qdate + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "'><i class='far fa-file-pdf'></i> PDF</a></span></h4>");
            out.println("<div class='achievements-wrapper'>");
            out.println("<table class='table table-bordered table-striped'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>#</th>");
            out.println("<th>Date</th>");
            out.println("<th>Phone</th>");
            out.println("<th>Meter/Account</th>");
            out.println("<th>Amount</th>");
            out.println("<th>Code</th>");
            out.println("<th>Token</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            double euqitelsum = 0.0;
            double euqitelsumprep = 0.0;
            double euqitelsumpost = 0.0;
            int index2 = 1;
            for (int y = 0; y < equitel.size(); y++) {
                String token = "NONE";
                if (null != equitel.get(y).get(5)) {
                    token = equitel.get(y).get(5);
                }
                out.println("<tr>");
                out.println("<td>" + index2 + "</td>");
                out.println("<td>" + equitel.get(y).get(0) + "</td>");
                out.println("<td>" + equitel.get(y).get(1) + "</td>");
                out.println("<td>" + equitel.get(y).get(2) + "</td>");
                out.println("<td>" + equitel.get(y).get(3) + "</td>");
                out.println("<td>" + equitel.get(y).get(4) + "</td>");
                out.println("<td>" + token + "</td>");
                out.println("</tr>");
                euqitelsum = euqitelsum + Double.parseDouble(equitel.get(y).get(3));
                if (equitel.get(y).get(2).length() < 11) {
                    euqitelsumpost = euqitelsumpost + Double.parseDouble(equitel.get(y).get(3));
                } else {
                    euqitelsumprep = euqitelsumprep + Double.parseDouble(equitel.get(y).get(3));
                }
                index2++;
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("<h5><span>Sum Failed Mpesa Amount</span><span class='pull-right' style='margin-right:5%'>Prepaid: " + formatter.format(euqitelsumprep) + " Postpaid: " + formatter.format(euqitelsumpost) + " Total: " + formatter.format(euqitelsum) + "</span></h5>");
            out.println("<div class='horizontalLine'></div>");
            out.println("<div class='horizontalLine'></div>");
            out.println("</br>");
            out.println("</br>");

            out.println("<h4>Manual Vends(" + qdate + ") </span><span class='pull-right'>Export: <a href='" + request.getContextPath() + "/Home?excelexpo=manual&qdate=" + qdate + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "'><i class='far fa-file-excel'></i> Excel</a>&nbsp;&nbsp;<a href='" + request.getContextPath() + "/Home?pdfexpo=manual&qdate=" + qdate + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "'><i class='far fa-file-pdf'></i> PDF</a></span></h4>");
            out.println("<div class='achievements-wrapper'>");
            out.println("<table class='table table-bordered table-striped'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>#</th>");
            out.println("<th>Date</th>");
            out.println("<th>Phone</th>");
            out.println("<th>Meter/Account</th>");
            out.println("<th>Amount</th>");
            out.println("<th>Code</th>");
            out.println("<th>Token</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            double manualsum = 0.0;
            double manualsumprep = 0.0;
            double manualsumpost = 0.0;
            int index3 = 1;
            for (int w = 0; w < manualvends.size(); w++) {
                String token = "NONE";
                if (null != manualvends.get(w).get(5)) {
                    token = manualvends.get(w).get(5);
                }
                out.println("<tr>");
                out.println("<td>" + index3 + "</td>");
                out.println("<td>" + manualvends.get(w).get(0) + "</td>");
                out.println("<td>" + manualvends.get(w).get(1) + "</td>");
                out.println("<td>" + manualvends.get(w).get(2) + "</td>");
                out.println("<td>" + manualvends.get(w).get(3) + "</td>");
                out.println("<td>" + manualvends.get(w).get(4) + "</td>");
                out.println("<td>" + token + "</td>");
                out.println("</tr>");
                manualsum = manualsum + Double.parseDouble(manualvends.get(w).get(3));
                if (manualvends.get(w).get(2).length() < 11) {
                    manualsumpost = manualsumpost + Double.parseDouble(manualvends.get(w).get(3));
                } else {
                    manualsumprep = manualsumprep + Double.parseDouble(manualvends.get(w).get(3));
                }
                index3++;
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("<h5><span>Sum Manual Vends</span><span class='pull-right' style='margin-right:5%'>Prepaid: " + formatter.format(manualsumprep) + " Postpaid: " + formatter.format(manualsumpost) + " Total: " + formatter.format(manualsum) + "</span></h5>");
            out.println("<div class='horizontalLine'></div>");
            out.println("<div class='horizontalLine'></div>");
            out.println("</br>");
            out.println("</br>");

            out.println("<h4>Manual With Amount(" + qdate + ") </span><span class='pull-right'>Export: <a href='" + request.getContextPath() + "/Home?excelexpo=unknown&qdate=" + qdate + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "'><i class='far fa-file-excel'></i> Excel</a>&nbsp;&nbsp;<a href='" + request.getContextPath() + "/Home?pdfexpo=unknown&qdate=" + qdate + "&msisdn=" + msisdn + "&meter=" + meter + "&mpesacode=" + mpesacode + "'><i class='far fa-file-pdf'></i> PDF</a></span></h4>");
            out.println("<div class='achievements-wrapper'>");
            out.println("<table class='table table-bordered table-striped'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>#</th>");
            out.println("<th>Date</th>");
            out.println("<th>Phone</th>");
            out.println("<th>Meter/Account</th>");
            out.println("<th>Amount</th>");
            out.println("<th>Code</th>");
            out.println("<th>Token</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            double unknownsum = 0.0;
            double unknownsumprep = 0.0;
            double unknownsumpost = 0.0;
            int index4 = 1;
            for (int x = 0; x < unknown.size(); x++) {
                String token = "NONE";
                if (null != unknown.get(x).get(5)) {
                    token = unknown.get(x).get(5);
                }
                out.println("<tr>");
                out.println("<td>" + index4 + "</td>");
                out.println("<td>" + unknown.get(x).get(0) + "</td>");
                out.println("<td>" + unknown.get(x).get(1) + "</td>");
                out.println("<td>" + unknown.get(x).get(2) + "</td>");
                out.println("<td>" + unknown.get(x).get(3) + "</td>");
                out.println("<td>" + unknown.get(x).get(4) + "</td>");
                out.println("<td>" + token + "</td>");
                unknownsum = unknownsum + Double.parseDouble(unknown.get(x).get(3));
                if (unknown.get(x).get(2).length() < 11) {
                    unknownsumpost = unknownsumpost + Double.parseDouble(unknown.get(x).get(3));
                } else {
                    unknownsumprep = unknownsumprep + Double.parseDouble(unknown.get(x).get(3));
                }
                out.println("</tr>");
                index4++;
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("<h5><span>Sum Manual With Amount</span><span class='pull-right' style='margin-right:5%'>Prepaid: " + formatter.format(unknownsumprep) + " Postpaid: " + formatter.format(unknownsumpost) + " Total: " + formatter.format(unknownsum) + "</span></h5>");
            out.println("<div class='horizontalLine'></div>");
            out.println("<div class='horizontalLine'></div>");
            out.println("</br>");
            out.println("</br>");

            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println(data.footer);
        }
    }

    public void exportExcel(List<List<String>> export, String file) throws FileNotFoundException, IOException {
        //System.out.println("Exporting in the here size: " + export.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Excel Sheet");
        HSSFRow rowhead = sheet.createRow((short) 0);
        rowhead.createCell((short) 0).setCellValue("#");
        rowhead.createCell((short) 1).setCellValue("Date");
        rowhead.createCell((short) 2).setCellValue("Phone");
        rowhead.createCell((short) 3).setCellValue("Meter");
        rowhead.createCell((short) 4).setCellValue("Amount");
        rowhead.createCell((short) 5).setCellValue("Code");
        rowhead.createCell((short) 6).setCellValue("Token");
        int index = 1;
        for (int x = 0; x < export.size(); x++) {
            HSSFRow row = sheet.createRow((short) index);
            String token = "NONE";
            if (null != export.get(x).get(5)) {
                token = export.get(x).get(5);
            }
            row.createCell((short) 0).setCellValue(index);
            row.createCell((short) 1).setCellValue(export.get(x).get(0));
            row.createCell((short) 2).setCellValue(export.get(x).get(1));
            row.createCell((short) 3).setCellValue(export.get(x).get(2));
            row.createCell((short) 4).setCellValue(export.get(x).get(3));
            row.createCell((short) 5).setCellValue(export.get(x).get(4));
            row.createCell((short) 6).setCellValue(token);
            index++;
        }

        String filename = file + ".xls";
        FileOutputStream fileOut = new FileOutputStream("/opt/applications/downloads/" + filename);
        wb.write(fileOut);
        fileOut.close();

    }

    public void exportPDF(List<List<String>> export, String file) throws FileNotFoundException, IOException {
        try {
            //System.out.println("Exporting in the here size: " + export.size());

            Document PDFLogReport = new Document(PageSize.A4, 0f, 0f, 15f, 0f);
            PdfWriter.getInstance(PDFLogReport, new FileOutputStream("/opt/applications/downloads/" + file + ".pdf"));
            PDFLogReport.open();
            Paragraph paragraph1 = new Paragraph("VendIT Audits Report");
            paragraph1.setSpacingAfter(10f);
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            PDFLogReport.add(paragraph1);

            PdfPTable LogTable = new PdfPTable(7);
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);

            //create a cell object
            PdfPCell table_cell;
            String num = "#";
            table_cell = new PdfPCell(new Phrase(num, font));
            LogTable.addCell(table_cell);
            String Date = "Date";
            table_cell = new PdfPCell(new Phrase(Date, font));
            LogTable.addCell(table_cell);
            String Phone = "Phone";
            table_cell = new PdfPCell(new Phrase(Phone, font));
            LogTable.addCell(table_cell);
            String Meter = "Meter";
            table_cell = new PdfPCell(new Phrase(Meter, font));
            LogTable.addCell(table_cell);
            String Amount = "Amount";
            table_cell = new PdfPCell(new Phrase(Amount, font));
            LogTable.addCell(table_cell);
            String Code = "Code";
            table_cell = new PdfPCell(new Phrase(Code, font));
            LogTable.addCell(table_cell);
            String Token = "Token";
            table_cell = new PdfPCell(new Phrase(Token, font));
            LogTable.addCell(table_cell);
            int index = 1;
            for (int x = 0; x < export.size(); x++) {
                String token = "NONE";
                if (null != export.get(x).get(5)) {
                    token = export.get(x).get(5);
                }
                String rc = Integer.toString(index);
                table_cell = new PdfPCell(new Phrase(rc, font));
                LogTable.addCell(table_cell);
                String Date1 = export.get(x).get(0);
                table_cell = new PdfPCell(new Phrase(Date1, font));
                LogTable.addCell(table_cell);
                String Phone1 = export.get(x).get(1);
                table_cell = new PdfPCell(new Phrase(Phone1, font));
                LogTable.addCell(table_cell);
                String Meter1 = export.get(x).get(2);
                table_cell = new PdfPCell(new Phrase(Meter1, font));
                LogTable.addCell(table_cell);
                String Amount1 = export.get(x).get(3);
                table_cell = new PdfPCell(new Phrase(Amount1, font));
                LogTable.addCell(table_cell);
                String Code1 = export.get(x).get(4);
                table_cell = new PdfPCell(new Phrase(Code1, font));
                LogTable.addCell(table_cell);
                String Token1 = token;
                table_cell = new PdfPCell(new Phrase(Token1, font));
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

    public void updateVendITArray() {
        if (vendITmpesacodes.size() == 0) {

            try {
                com.vend.manualvends.QueryMpesaCode_Service service = new com.vend.manualvends.QueryMpesaCode_Service();
                com.vend.manualvends.QueryMpesaCode port = service.getQueryMpesaCodePort();
                // TODO initialize WS operation arguments here
                java.lang.String mcode = "getting";
                // TODO process result here
                java.util.List<java.lang.String> result = port.querymanual(mcode);
                for (int w = 0; w < result.size(); w++) {
                    System.out.println("Adding:" + result.get(w));
                    vendITmpesacodes.add(result.get(w));
                }
            } catch (Exception ex) {
                // TODO handle custom exceptions here
            }
        } else {
            //System.out.println("Codes VendITArray Already Created: " + vendITmpesacodes.size());
        }
        System.out.println("VendITArray Size: " + vendITmpesacodes.size());
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
