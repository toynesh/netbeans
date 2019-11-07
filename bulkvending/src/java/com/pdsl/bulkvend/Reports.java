/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.bulkvend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

/**
 *
 * @author julius
 */
public class Reports extends HttpServlet {

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
            if (null == session.getAttribute("clientCode")) {
                String redirectURL = "Home?login";
                response.sendRedirect(redirectURL);
            } else {

                String fullname = (String) request.getSession().getAttribute("fullname");
                String clientId = (String) request.getSession().getAttribute("clientId");

                if (fullname.equals("Professional Digital Systems Ltd")) {

                    String groupType = "";
                    if (null != request.getParameter("rt")) {
                        groupType = request.getParameter("rt");
                    }
                    if (null != request.getParameter("client")) {
                        clientId = request.getParameter("client");
                    }

                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
                    String year = fmt.print(dt);
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
                    String month = formatter.print(dt);
                    DateTimeFormatter tformatter = DateTimeFormat.forPattern("hh");
                    String time = tformatter.print(dt);
                    out.println(data.pageHeader(true, fullname));
                    out.println("<section class='page-section about-heading'>");
                    out.println("<div class='container'>");
                    out.println("<div class='about-heading-content'>");
                    out.println("<div class='row'>");
                    out.println("<div class='col-xl-12 col-lg-12 mx-auto'>");
                    out.println("<div class='bg-faded rounded p-5 mt-3'>");
                    out.println("<h2 class='section-heading mb-4'>");
                    out.println("<span class='section-heading-upper text-center'>Reports</span>");
                    out.println("</h2>");

                    out.println("<h2 class='section-heading mb-4'>");
                    out.println("<span class='section-heading-lower text-center'>" + groupType + " Report</span>");
                    out.println("</h2>");
                    out.println("<div class='form-group row'>");
                    out.println("<label for='client' class='col-sm-3 col-form-label'> </label>");
                    out.println("<label for='client' class='col-sm-1 col-form-label'>Client</label>");
                    out.println("<div class='col-sm-4'>");
                    out.println("<select  class='custom-select mr-sm-2' onChange=\"window.document.location.href='Reports?rt=" + groupType + "&client='+this.value;\">");
                    String selquery2 = "SELECT id,fullName from clients order by id desc";
                    try {
                        Connection con = data.connect();
                        Statement stm = con.createStatement();
                        ResultSet rs = stm.executeQuery(selquery2);
                        while (rs.next()) {
                            if (clientId.equals(rs.getString(1))) {
                                out.println("<option selected value='" + rs.getString(1) + "'>" + rs.getString(2) + "</option>");
                            } else {
                                out.println("<option value='" + rs.getString(1) + "'>" + rs.getString(2) + "</option>");
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    out.println("</select>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("<table class='table table-striped'>");
                    out.println("<thead class='thead-dark'>");
                    out.println("<tr>");
                    out.println("<th scope='col'>#</th>");
                    out.println("<th scope='col'>Type</th>");
                    out.println("<th scope='col'>Particulars</th>");
                    out.println("<th scope='col'>Amount</th>");
                    out.println("<th scope='col'>Response</th>");
                    out.println("<th scope='col'>Date</th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody>");
                    String query = "SELECT id,clientId,clientCode,groupType,meter,amount,vendResponse,creationDate from transactions" + month + year + " where clientId=" + clientId + " AND groupType='" + groupType + "'";
                    if (groupType.equals("FLOAT DEPOSIT")) {
                        //System.out.println("Floatdepo");
                        query = "SELECT id,clientId,clientCode,groupType,meter,deposit,vendResponse,creationDate from transactions" + month + year + " where clientId=" + clientId + " AND (groupType='" + groupType + "' OR groupType='BALANCE BROUGHT FORWARD') ";
                    }
                    try {
                        Connection con = data.connect();
                        Statement stm = con.createStatement();
                        ResultSet rs = stm.executeQuery(query);
                        int count = 1;
                        while (rs.next()) {
                            out.println("<tr>");
                            out.println("<th scope='row'>" + count + "</th>");
                            out.println("<td>" + rs.getString(4) + "</td>");
                            String pat = " ";
                            if (null != rs.getString(5)) {
                                pat = rs.getString(5);
                            }
                            out.println("<td>" + pat + "</td>");
                            out.println("<td>" + rs.getString(6) + "</td>");
                            String vres = " ";
                            if (null != rs.getString(7)) {
                                vres = rs.getString(7);
                            }
                            out.println("<td>" + vres + "</td>");
                            out.println("<td>" + rs.getString(8) + "</td>");
                            out.println("</tr>");
                            count++;
                        }
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    out.println("</tbody>");
                    out.println("</table>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("</section>");
                    out.println(data.pageFoorter(""));

                } else {
                    String groupType = "";
                    if (null != request.getParameter("rt")) {
                        groupType = request.getParameter("rt");
                    }

                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
                    String year = fmt.print(dt);
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
                    String month = formatter.print(dt);
                    DateTimeFormatter tformatter = DateTimeFormat.forPattern("hh");
                    String time = tformatter.print(dt);
                    out.println(data.pageHeader(true, fullname));
                    out.println("<section class='page-section about-heading'>");
                    out.println("<div class='container'>");
                    out.println("<div class='about-heading-content'>");
                    out.println("<div class='row'>");
                    out.println("<div class='col-xl-12 col-lg-12 mx-auto'>");
                    out.println("<div class='bg-faded rounded p-5 mt-3'>");
                    out.println("<h2 class='section-heading mb-4'>");
                    out.println("<span class='section-heading-upper text-center'>Reports</span>");
                    out.println("</h2>");

                    out.println("<h2 class='section-heading mb-4'>");
                    out.println("<span class='section-heading-lower text-center'>" + groupType + " Report</span>");
                    out.println("</h2>");
                    out.println("<table class='table table-striped'>");
                    out.println("<thead class='thead-dark'>");
                    out.println("<tr>");
                    out.println("<th scope='col'>#</th>");
                    out.println("<th scope='col'>Type</th>");
                    out.println("<th scope='col'>Particulars</th>");
                    out.println("<th scope='col'>Amount</th>");
                    out.println("<th scope='col'>Response</th>");
                    out.println("<th scope='col'>Date</th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody>");
                    String query = "SELECT id,clientId,clientCode,groupType,meter,amount,vendResponse,creationDate from transactions" + month + year + " where clientId=" + clientId + " AND groupType='" + groupType + "'";
                    if (groupType.equals("FLOAT DEPOSIT")) {
                        query = "SELECT id,clientId,clientCode,groupType,meter,deposit,vendResponse,creationDate from transactions" + month + year + " where clientId=" + clientId + " AND (groupType='" + groupType + "' OR groupType='BALANCE BROUGHT FORWARD') ";
                    }
                    try {
                        Connection con = data.connect();
                        Statement stm = con.createStatement();
                        ResultSet rs = stm.executeQuery(query);
                        int count = 1;
                        while (rs.next()) {
                            out.println("<tr>");
                            out.println("<th scope='row'>" + count + "</th>");
                            out.println("<td>" + rs.getString(4) + "</td>");
                           String pat = " ";
                            if (null != rs.getString(5)) {
                                pat = rs.getString(5);
                            }
                            out.println("<td>" + pat + "</td>");
                            out.println("<td>" + rs.getString(6) + "</td>");
                            String vres = " ";
                            if (null != rs.getString(7)) {
                                vres = rs.getString(7);
                            }
                            out.println("<td>" + vres + "</td>");
                            out.println("<td>" + rs.getString(8) + "</td>");
                            out.println("</tr>");
                            count++;
                        }
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    out.println("</tbody>");
                    out.println("</table>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("</section>");
                    out.println(data.pageFoorter(""));
                }
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
