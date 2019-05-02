/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author julius
 */
public class DowntimeTable extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            HttpSession session = request.getSession();
            if (null == session.getAttribute("retailer")) {
                String redirectURL = "Login";
                response.sendRedirect(redirectURL);
            } else {
                String fullname = (String) request.getSession().getAttribute("fullname");
                String ret = (String) request.getSession().getAttribute("retailer");
                String rtlr = "Select...";
                if (!ret.equals("Admin")) {
                    rtlr = ret;
                }
                if (null != request.getParameter("retailer")) {
                    if (!request.getParameter("retailer").equals("")) {
                        rtlr = request.getParameter("retailer");
                    }
                }
                DateTime rdt = new DateTime().minusMonths(1);
                DateTimeFormatter rfm = DateTimeFormat.forPattern("MMM yyyy");
                String currdt = rfm.print(rdt);
                if (null != request.getParameter("mw")) {
                    currdt = request.getParameter("mw");
                }
                out.println(data.handleHeader("DowntimeTable", rtlr, "none", currdt, fullname));
                out.println("<div id='tprint' class='col-lg-12' style='margin-top:1%'>");
                DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM yyyy");
                DateTime sdate = formatter.parseDateTime(currdt);
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                String str = fmt.print(sdate);
                String gmnth = formatter.print(sdate);
                out.println("<h4 class='text-center text-info text-capitalize'><u><span class='text-danger'>" + gmnth + "</span> Outages Table</u></h4>");
                int rid = 1;
                out.println("<table class='table table-bordered fixed-header text-right' style='font-size:18px'>");
                out.println("<thead>");
                out.println("<tr>");
                out.println("<th class='text-center' style='border: solid;'>No.</th>");
                out.println("<th class='text-center' style='border: solid;width: 15%'>Date</th>");
                out.println("<th class='text-center' style='border: solid;width: 15%'>Time started</th>");
                out.println("<th class='text-center' style='border: solid;width: 15%'>Time ended</th>");
                out.println("<th class='text-center' style='border: solid;width: 15%'>Duration (HH:MM:SS)</th>");
                out.println("<th class='text-center' style='border: solid;'>Service</th>");
                out.println("<th class='text-center' style='border: solid;'>Severity</th>");
                out.println("<th class='text-center' style='border: solid;'>RCA</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");
                try {
                    String query = "SELECT `otime`,`status`,`duration`,`severity`,`hduration`,`mduration`,`sduration`,`notes` FROM `bill" + str.replaceAll("-", "") + "` WHERE `status`='PROBLEM' AND `mduration`>= 30 AND `notes` IS NOT NULL AND `notes`<>'' ORDER BY `id` DESC";
                    Connection con = data.connect();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        DateTimeFormatter otfmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                        DateTime sot = otfmt.parseDateTime(rs.getString(1));
                        DateTimeFormatter ofmt = DateTimeFormat.forPattern("dd-MMM-yy");
                        String ot = ofmt.print(sot);
                        out.println("<tr>");
                        out.println("<th class='text-center' style='border: solid;'>" + rid + "</th>");
                        out.println("<td class='text-center' style='border: solid;'>" + ot + "</td>");
                        out.println("<td class='text-center' style='border: solid;'>" + rs.getString(1) + "</td>");
                        DateTime et = sot.plusHours(rs.getInt(5));
                        et = et.plusMinutes(rs.getInt(6));
                        et = et.plusSeconds(rs.getInt(7));
                        out.println("<td class='text-center' style='border: solid;'>" + otfmt.print(et) + "</td>");
                        out.println("<td class='text-center' style='border: solid;'>" + rs.getString(3) + "</td>");
                        out.println("<td class='text-center' style='border: solid;'>Postpaid</td>");
                        out.println("<td class='text-center' style='border: solid;'>" + rs.getString(4) + "</td>");
                        out.println("<td class='text-center' style='border: solid;'>" + rs.getString(8) + "</td>");
                        out.println("</tr>");
                        rid++;
                    }
                    con.close();
                } catch (SQLException ex) {
                    //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    String query = "SELECT `otime`,`status`,`duration`,`severity`,`hduration`,`mduration`,`sduration`,`notes` FROM `evg" + str.replaceAll("-", "") + "` WHERE `status`='PROBLEM' AND `mduration`>= 30 AND `notes` IS NOT NULL AND `notes`<>'' ORDER BY `id` DESC";
                    Connection con = data.connect();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        DateTimeFormatter otfmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                        DateTime sot = otfmt.parseDateTime(rs.getString(1));
                        DateTimeFormatter ofmt = DateTimeFormat.forPattern("dd-MMM-yy");
                        String ot = ofmt.print(sot);
                        out.println("<tr>");
                        out.println("<th class='text-center' style='border: solid;'>" + rid + "</th>");
                        out.println("<td class='text-center' style='border: solid;'>" + ot + "</td>");
                        out.println("<td class='text-center' style='border: solid;'>" + rs.getString(1) + "</td>");
                        DateTime et = sot.plusHours(rs.getInt(5));
                        et = et.plusMinutes(rs.getInt(6));
                        et = et.plusSeconds(rs.getInt(7));
                        out.println("<td class='text-center' style='border: solid;'>" + otfmt.print(et) + "</td>");
                        out.println("<td class='text-center' style='border: solid;'>" + rs.getString(3) + "</td>");
                        out.println("<td class='text-center' style='border: solid;'>Prepaid</td>");
                        out.println("<td class='text-center' style='border: solid;'>" + rs.getString(4) + "</td>");
                        out.println("<td class='text-center' style='border: solid;'>" + rs.getString(8) + "</td>");
                        out.println("</tr>");
                        rid++;
                    }
                    con.close();
                } catch (SQLException ex) {
                    //Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.println("</tbody>");
                out.println("</table>");
                if (rid == 1) {
                    out.println("<p class='text-danger text-center'><strong>No Records found for the selected date " + currdt + ". This table only captures downtimes more than 30 minutes</strong></p>");
                }
                out.println("</div>");
                out.println(data.footer);
            }
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
