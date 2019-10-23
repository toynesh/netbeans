/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vendit.airtel;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author juliuskun
 */
public class GetTransactionMeter extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println(data.header);
            //Page Content
            String qmeter = request.getParameter("qmeter");
            //TO EXPORT            
            if (null != request.getParameter("expo")) {
                if (request.getParameter("expo").equals("excel")) {
                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddhhmmss");
                    String currdate = fmt.print(dt);
                    String filename = "meterairtelvendit" + currdate;
                    PrintWriter writer = new PrintWriter("/opt/applications/downloads/" + filename + ".csv", "UTF-8");
                    writer.println("Name,Mobile No,Amount,MeterNo,Code,Message,Time");
                    try {
                        String query = "SELECT names,mobile,amount,meter,trans_id,message,time FROM airtelTransactions WHERE meter='" + qmeter + "' ORDER BY time DESC";
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
                            //eexportarray.add(row);
                            writer.println(row);
                        }
                        con.close();
                    } catch (SQLException ex) {
                    }
                    writer.close();
                    String redirectURL = "DownloadFile?ftdown=" + filename + ".csv";
                    response.sendRedirect(redirectURL);
                }
            }
            out.println("<div id='page-content-wrapper'>");
            out.println("<div class='container-fluid'");
            out.println("<div class='row'>");
            out.println("<div class='col-lg-12'>");
            out.println("<div class='panel panel-primary'>");
            out.println("<div class='panel-heading'>");
            out.println("<span style='margin-left:70%'>Export to:&nbsp;<a href='" + request.getContextPath() + "/GetTransactionMeter?qmeter="+qmeter+"&expo=excel'>EXCEL</a> &nbsp;&nbsp;Loggedin as: " + fullname + " &nbsp;&nbsp;<a href='" + request.getContextPath() + "/Login?logout=logout'> Signout</a></span>");
            out.println("<a href='#menu-toggle' class='pull-left' id='menu-toggle'>Hide/Unhide Menu</a>");
            out.println("<h3 class=' text-center'><img src='img/airtel.png' width='100' style='' alt=''/><span style='font-size:15px'>&nbsp;&nbsp;&nbsp;&nbsp;<i class='fa fa-tachometer' aria-hidden='true'></i> " + qmeter + "</span></h3>");
            out.println("</div>");
            out.println("<div class='panel-body table-responsive'>");
            out.println("<table class='table' style='font-size:13px'>");
            out.println("  <thead>");
            out.println("    <tr>");
            out.println("      <th>Name</th>");
            out.println("      <th>Mobile No</th>");
            out.println("      <th>Amount</th>");
            out.println("     <th>MeterNo</th>");
            out.println("     <th>Code</th>");
            out.println("     <th>Message</th>");
            out.println("     <th>Time</th>");
            out.println("    </tr>");
            out.println("  </thead>");
            out.println("  <tbody>");
            try {
                Connection con = data.connect();
                String query = "SELECT id,names,mobile,amount,meter,trans_id,message,time,balance FROM airtelTransactions WHERE meter='" + qmeter + "' ORDER BY id DESC";
                Statement st;
                st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (!rs.isBeforeFirst()) {
                    out.println("    <tr>");
                    out.println("<span class='text-danger'>Empty Rows</span>");
                    out.println("    </tr>");
                } else {
                    while (rs.next()) {
                        String action = "<a href='#' data-toggle='modal' data-target='#Qrevend" + rs.getString(6).replaceAll(" ", "") + "' style='color:red'>Revend</a>";
                        String msgstyle = "style='color:red'";
                        if (rs.getString(7).contains("KPLC Mtr No:") || rs.getString(7).contains("OK: Payment recieved for KPLC account:") || rs.getString(7).contains("Balance combined")) {
                            msgstyle = "style='color:green'";
                            action = "none";
                            if (rs.getDouble(9) > 0) {
                                action = "<a href='#' data-toggle='modal' data-target='#Qrevend" + rs.getString(6).replaceAll(" ", "") + "' style='color:red'>Revend</a>";
                            }
                        }
                        double amt = Double.parseDouble(rs.getString(4));
                        //double bal = Double.parseDouble(rs.getString(9));
                        double conver = 0.01;
                        double amount = amt * conver;
                        //double balance = bal * conver;
                        out.println("    <tr>");
                        out.println("<td>" + StringUtils.abbreviate(rs.getString(2), 20) + "</td>");
                        out.println("<td>" + rs.getString(3) + "</td>");
                        out.println("<td>" + amount + "</td>");
                        out.println("<td style='color:#04337F'>" + rs.getString(5) + "</td>");
                        out.println("<td>" + rs.getString(6) + "</td>");
                        out.println("<td " + msgstyle + ">" + rs.getString(7) + "</td>");
                        out.println("<td>" + rs.getDate(8) + " " + rs.getTime(8) + "</td>");

                        //Manual vend
                        out.println("<div id='Qrevend" + rs.getString(6).replaceAll(" ", "") + "' class='modal fade' role='dialog'>");
                        out.println("  <div class='modal-dialog'>");
                        out.println("    <div class='modal-content'>");
                        out.println("      <div class='modal-header'>");
                        out.println("        <h4 class='modal-title'><i class='fa fa-search' aria-hidden='true'></i> Enter Meter</h4>");
                        out.println("      </div>");
                        out.println("      <div class='modal-body'>");
                        out.println("      <form action='Vend' method='post'>");
                        out.println("         <div class='form-group'>");
                        out.println("            <input type='text' name='newmeter' class='form-control' id='newmeter' required value='" + rs.getString(5) + "'>");
                        out.println("            <input type='text' style='display:none' name='meter' id='meter' value='" + rs.getString(5) + "'>");
                        out.println("            <input type='text' style='display:none' name='mobile' id='mobile' value='" + rs.getString(3) + "'>");
                        out.println("            <input type='text' style='display:none' name='amount' id='amount'  value='" + rs.getString(4) + "'>");
                        out.println("            <input type='text' style='display:none' name='names' id='names' value='" + rs.getString(2) + "'>");
                        out.println("            <input type='text' style='display:none' name='code' id='code' value='" + rs.getString(6).replaceAll(" ", "") + "'>");
                        out.println("         </div>");
                        out.println("         <button type='submit' class='btn btn-primary'>Submit</button>");
                        out.println("      </form>");
                        out.println("      </div>");
                        out.println("     <div class='modal-footer'>");
                        out.println("        <button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>");
                        out.println("      </div>");
                        out.println("    </div>");
                        out.println("  </div>");
                        out.println("</div>");
                        out.println("    </tr>");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
                out.println("sqlInvalid Request");
            }
            out.println("  </tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println(data.footer);
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
