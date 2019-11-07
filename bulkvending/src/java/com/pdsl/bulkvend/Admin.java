/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.bulkvend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author julius
 */
public class Admin extends HttpServlet {

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
                if (!fullname.equals("Professional Digital Systems Ltd")) {
                    String redirectURL = "Home";
                    response.sendRedirect(redirectURL);
                }
                if (null != request.getParameter("cid")) {
                    String cid = request.getParameter("cid");
                    String query = "SELECT id, status from clients where id=" + cid + " ";
                    int status = 0;
                    try {
                        Connection con = this.data.connect();
                        //System.out.println(query);
                        Statement stm = con.createStatement();
                        ResultSet rs = stm.executeQuery(query);
                        while (rs.next()) {
                            status = rs.getInt(2);
                        }
                        con.close();
                    } catch (SQLException ex) {
                        //Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (status == 0) {
                        try {
                            Connection con = this.data.connect();
                            String update = "update clients set status = ? WHERE id = ?";
                            PreparedStatement preparedStmt = con.prepareStatement(update);
                            preparedStmt.setInt(1, 1);
                            preparedStmt.setString(2, cid);
                            preparedStmt.executeUpdate();
                            System.out.println("Updated client status to disabled");
                        } catch (SQLException ex) {
                            //Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        String redirectURL = "Admin";
                        response.sendRedirect(redirectURL);
                    } else {
                        try {
                            Connection con = this.data.connect();
                            String update = "update clients set status = ? WHERE id = ?";
                            PreparedStatement preparedStmt = con.prepareStatement(update);
                            preparedStmt.setInt(1, 0);
                            preparedStmt.setString(2, cid);
                            preparedStmt.executeUpdate();
                            System.out.println("Updated client status to enable");
                        } catch (SQLException ex) {
                            //Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        String redirectURL = "Admin";
                        response.sendRedirect(redirectURL);
                    }
                }
                if (null != request.getParameter("uPass")) {
                    String clientCode = request.getParameter("clientCode");
                    String fullName = request.getParameter("fullName");
                    String uName = request.getParameter("uName");
                    String uPass = request.getParameter("uPass");
                    String uType = "user";
                    String values = "insert into clients(clientCode,fullName,uName,uPass,uType) values (?,?,?,?,?)";
                    try {
                        Connection con = data.connect();
                        PreparedStatement prep = con.prepareStatement(values);
                        prep.setString(1, clientCode);
                        prep.setString(2, fullName);
                        prep.setString(3, uName);
                        prep.setString(4, uPass);
                        prep.setString(5, uType);
                        prep.execute();
                        prep.close();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (null != request.getParameter("amount")) {
                    String clientId = request.getParameter("client");
                    String reference = request.getParameter("reference");
                    String amount = request.getParameter("amount");

                    DateTime dt = new DateTime();
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
                    String year = fmt.print(dt);
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
                    String month = formatter.print(dt);
                    String tdate = month + year;
                    String values = "insert into transactions" + tdate + "(clientId,groupType,meter,deposit) values (?,?,?,?)";
                    try {
                        Connection con = data.connect();
                        PreparedStatement prep = con.prepareStatement(values);
                        prep.setString(1, clientId);
                        prep.setString(2, "FLOAT DEPOSIT");
                        prep.setString(3, reference);
                        prep.setString(4, amount);
                        prep.execute();
                        prep.close();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                out.println(data.pageHeader(true, fullname));
                out.println("<section class='page-section about-heading'>");
                out.println("<div class='container'>");
                out.println("<div class='about-heading-content'>");
                out.println("<div class='row'>");
                out.println("<div class='col-xl-6 col-lg-6 mx-auto'>");
                out.println("<div class='bg-faded rounded p-5 mt-3'>");
                out.println("<h2 class='section-heading mb-4'>");
                out.println("<span class='section-heading-upper'>Add Client</span>");
                out.println("</h2>");
                out.println("<form action='Admin' method='POST'>");
                out.println("<div class='form-group row'>");
                out.println("<label for='client' class='col-sm-2 col-form-label'>Code</label>");
                out.println("<div class='col-sm-9'>");
                out.println("<input type='text' class='form-control' id='clientCode' name='clientCode' required>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='form-group row'>");
                out.println("<label for='reference' class='col-sm-2 col-form-label'>Full Name</label>");
                out.println("<div class='col-sm-9'>");
                out.println("<input type='text' class='form-control' id='fullName' name='fullName' required>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='form-group row'>");
                out.println("<label for='amount' class='col-sm-2 col-form-label'>uName</label>");
                out.println("<div class='col-sm-9'>");
                out.println("<input type='text' class='form-control' id='uName' name='uName' required>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='form-group row'>");
                out.println("<label for='amount' class='col-sm-2 col-form-label'>Password</label>");
                out.println("<div class='col-sm-9'>");
                out.println("<input type='password' class='form-control' id='uPass' name='uPass' required>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='form-group row'>");
                out.println("<div class='col-sm-12'>");
                out.println("<button type='submit' value='submit' class='btn btn-primary'>Add</button>");
                out.println("</div>");
                out.println("</div>");
                out.println("</form>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='col-xl-6 col-lg-6 mx-auto'>");
                out.println("<div class='bg-faded rounded p-5 mt-3'>");
                out.println("<h2 class='section-heading mb-4'>");
                out.println("<span class='section-heading-upper'>Add Float</span>");
                out.println("</h2>");
                out.println("<form action='Admin' method='POST'>");
                out.println("<div class='form-group row'>");
                out.println("<label for='client' class='col-sm-2 col-form-label'>Client</label>");
                out.println("<div class='col-sm-10'>");
                out.println("<select class='custom-select mr-sm-2' name='client'>");
                String selquery2 = "SELECT id,fullName from clients order by id desc";
                try {
                    Connection con = data.connect();
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(selquery2);
                    int count = 1;
                    while (rs.next()) {
                        if (count == 1) {
                            out.println("<option selected value='" + rs.getString(1) + "'>" + rs.getString(2) + "</option>");
                        } else {
                            out.println("<option value='" + rs.getString(1) + "'>" + rs.getString(2) + "</option>");
                        }
                        count++;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.println("</select>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='form-group row'>");
                out.println("<label for='reference' class='col-sm-2 col-form-label'>Reference</label>");
                out.println("<div class='col-sm-10'>");
                out.println("<input type='text' class='form-control' id='reference' name='reference' required>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='form-group row'>");
                out.println("<label for='amount' class='col-sm-2 col-form-label'>Amount</label>");
                out.println("<div class='col-sm-10'>");
                out.println("<input type='text' class='form-control' id='amount' name='amount' placeholder='0.00' required>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='form-group row'>");
                out.println("<div class='col-sm-12'>");
                out.println("<button type='submit' value='submit' class='btn btn-primary'>Add</button>");
                out.println("</div>");
                out.println("</div>");
                out.println("</form>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='bg-faded rounded p-5 mt-3'>");
                out.println("<div class='row'>");
                out.println("<div class='col-xl-12 col-lg-12 mx-auto'>");
                out.println("<h2 class='section-heading mb-4'>");
                out.println("<span class='section-heading-lower text-center'>Clients</span>");
                out.println("</h2>");
                out.println("<table class='table table-striped'>");
                out.println("<thead class='thead-dark'>");
                out.println("<tr>");
                out.println("<th scope='col'>#</th>");
                out.println("<th scope='col'>Client Code</th>");
                out.println("<th scope='col'>Name</th>");
                out.println("<th scope='col'>Float Balance</th>");
                out.println("<th scope='col'>Action</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");
                String query = "SELECT id,clientCode,fullName,status from clients";
                try {
                    Connection con = data.connect();
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    int count = 1;
                    while (rs.next()) {
                        out.println("<tr>");
                        out.println("<th scope='row'>" + count + "</th>");
                        out.println("<td>" + rs.getString(2) + "</td>");
                        out.println("<td>" + rs.getString(3) + "</td>");
                        String fb = data.getFloatBal(parseInt(rs.getString(1)));
                        DecimalFormat formatter = new DecimalFormat("#,###.00");
                        double floatbal = Math.round(Double.parseDouble(fb) * 100.0) / 100.0;
                        out.println("<td>" + formatter.format(floatbal) + "</td>");
                        out.println("<td>");
                        out.println("<div class='list-group list-group-horizontal-sm'>");
                        out.println("<a href='Admin?cid=" + rs.getString(1) + "' class='list-group-item list-group-item-action list-group-item-warning'>");
                        if (rs.getInt(4) == 0) {
                            out.println("Click to Disable");
                        } else {
                            out.println("Click to enable");
                        }
                        out.println("</a>");
                        out.println("</div>");
                        out.println("</td>");
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
