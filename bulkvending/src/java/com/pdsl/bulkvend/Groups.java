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

/**
 *
 * @author julius
 */
public class Groups extends HttpServlet {

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
                String clientCode = (String) request.getSession().getAttribute("clientCode");
                if (null != request.getParameter("groupName")) {
                    String groupName = request.getParameter("groupName");
                    String groupType = request.getParameter("groupType");
                    String values = "insert into groups(clientId,clientCode,groupName,groupType) values (?,?,?,?)";
                    try {
                        Connection con = data.connect();
                        PreparedStatement prep = con.prepareStatement(values);
                        prep.setString(1, clientId);
                        prep.setString(2, clientCode);
                        prep.setString(3, groupName);
                        prep.setString(4, groupType);
                        prep.execute();
                        prep.close();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (null != request.getParameter("idd")) {
                    String gid = request.getParameter("idd");
                    String query = "SELECT id, status from groups where id=" + gid + " ";
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
                            String update = "update groups set status = ? WHERE id = ?";
                            PreparedStatement preparedStmt = con.prepareStatement(update);
                            preparedStmt.setInt(1, 1);
                            preparedStmt.setString(2, gid);
                            preparedStmt.executeUpdate();
                            System.out.println("Updated group status to disabled");
                        } catch (SQLException ex) {
                            //Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        String redirectURL = "Groups";
                        response.sendRedirect(redirectURL);
                    } else {
                        try {
                            Connection con = this.data.connect();
                            String update = "update groups set status = ? WHERE id = ?";
                            PreparedStatement preparedStmt = con.prepareStatement(update);
                            preparedStmt.setInt(1, 0);
                            preparedStmt.setString(2, gid);
                            preparedStmt.executeUpdate();
                            System.out.println("Updated group status to enable");
                        } catch (SQLException ex) {
                            //Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        String redirectURL = "Groups";
                        response.sendRedirect(redirectURL);
                    }
                }

                out.println(data.pageHeader(true, fullname));
                out.println("<section class='page-section about-heading'>");
                out.println("<div class='container'>");
                out.println("<div class='about-heading-content'>");
                out.println("<div class='row'>");
                out.println("<div class='col-xl-12 col-lg-12 mx-auto'>");
                out.println("<div class='bg-faded rounded p-5 mt-3'>");
                out.println("<h2 class='section-heading mb-4'>");
                out.println("<span class='section-heading-upper'>New Group</span>");
                out.println("</h2>");
                out.println("<form action='Groups' method='POST'>");
                out.println("<div class='form-group row'>");
                out.println("<label for='groupName' class='col-sm-1 col-form-label'>Name</label>");
                out.println("<div class='col-sm-5'>");
                out.println("<input type='text' class='form-control' id='groupName' name='groupName' placeholder='Group Name' required>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='form-group row'>");
                out.println("<label for='groupName' class='col-sm-1 col-form-label'>Group Type</label>");
                out.println("<div class='col-sm-5'>");
                out.println("<select class='custom-select mr-sm-2' id='inlineFormCustomSelect' name='groupType'>");
                out.println("<option selected value='Postpaid'>Postpaid</option>");
                out.println("<option value='Prepaid'>Prepaid</option>");
                out.println("<option value='Airtime'>Airtime</option>");
                out.println("</select>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='form-group row'>");
                out.println("<div class='col-sm-10'>");
                out.println("<button type='submit' value='submit' class='btn btn-primary'>Add</button>");
                out.println("</div>");
                out.println("</div>");
                out.println("</form>");
                out.println("<h2 class='section-heading mb-4'>");
                out.println("<span class='section-heading-lower text-center'>Group List</span>");
                out.println("</h2>");
                out.println("<table class='table table-striped'>");
                out.println("<thead class='thead-dark'>");
                out.println("<tr>");
                out.println("<th scope='col'>#</th>");
                out.println("<th scope='col'>Group Name</th>");
                out.println("<th scope='col'>Type</th>");
                out.println("<th scope='col' class='text-center'>Action</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");
                String limit = " limit 10";
                String query = "SELECT id,groupName, groupType, status from groups where clientId=" + clientId + " order by id desc" + limit + "";
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
                        out.println("<td>");
                        out.println("<div class='list-group list-group-horizontal-sm'>");
                        out.println("<a href='Groups?idd=" + rs.getString(1) + "' class='list-group-item list-group-item-action list-group-item-warning'>");
                        if (rs.getInt(4) == 0) {
                            out.println("Click to Disable");
                        } else {
                            out.println("Click to enable");
                        }
                        out.println("</a>");
                        if (rs.getInt(4) == 0) {
                            out.println("<a href='ManageAccounts?gid=" + rs.getString(1) + "' class='list-group-item list-group-item-action list-group-item-info'>");
                            if (rs.getString(3).equals("Postpaid")) {
                                out.println("Manage Accounts");
                            } else if (rs.getString(3).equals("Prepaid")) {
                                out.println("Manage Meters");
                            } else {
                                out.println("Manage Phone Numbers");
                            }
                            out.println("</a>");

                        }
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
