/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslvending.portal;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author coolie
 */
public class ChangePassword extends HttpServlet {

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
            if (null == session.getAttribute("vendorcode")) {
                String redirectURL = "Login";
                response.sendRedirect(redirectURL);
            }
            String notify = "";
            String oupass = "";
            String nupass = "";
            String cupass = "";
            String fullname = (String) request.getSession().getAttribute("fullname");
            String uid = (String) request.getSession().getAttribute("uid");
            if (null != request.getParameter("oupass")) {
                oupass = request.getParameter("oupass");
                nupass = request.getParameter("nupass");
                cupass = request.getParameter("cupass");

                if (!nupass.equals(cupass)) {
                    notify = "New Password Missmatch!!";
                } else {
                    try {
                        Connection con = data.connect();
                        String query = "SELECT `full_names`,`vendor_code`,`upass` FROM `users` WHERE  `upass`='" + oupass + "' AND `id`=" + uid + "";
                        //System.out.println(query);
                        Statement stm = con.createStatement();
                        ResultSet rs = stm.executeQuery(query);
                        if (rs.isBeforeFirst()) {
                            String update = "update `users` set `upass` = ? WHERE `id` = ?";
                            PreparedStatement preparedStmt = con.prepareStatement(update);
                            preparedStmt.setString(1, nupass);
                            preparedStmt.setString(2, uid);
                            preparedStmt.executeUpdate();
                            notify = "User Password Updated!!";                            
                            System.out.println("User Password Updated");
                            String redirectURL = "Login?logout=logout";
                            response.sendRedirect(redirectURL);
                        } else {
                            notify = "Wrong Old Password!!";
                        }
                        con.close();
                    } catch (SQLException ex) {
                        //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            out.println(data.loginheader);
            out.println("<div class='container' style='margin-top:2%'>");
            out.println("<div class='row'>");
            out.println("<div class='col-lg-4 col-lg-offset-4' >");
            out.println("<div id='addvendor'>");
            out.println("<h5><i class='fa fa-sign-in' aria-hidden='true'></i>&nbsp;&nbsp;<u>Login</u></h5>");
            out.println("<br />");
            out.println("<form action='" + request.getContextPath() + "/ChangePassword' method='post'>");
            out.println("<div class='form-group'>");
            out.println("<p class='text-danger'>" + notify + "</p>");
            out.println("<label for='uname'>User Name: " + fullname + "</label>");
            out.println("<input type='text' style='display:none'  id='uid' name='uid' value='" + uid + "'>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label for='oupass'>Old Password:</label>");
            out.println("<input type='password' class='form-control no-border' id='oupass' name='oupass' placeholder='*******' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label for='nupass'>New Password:</label>");
            out.println("<input type='password' class='form-control no-border' id='nupass' name='nupass' placeholder='*******' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label for='cupass'>Confirm Password:</label>");
            out.println("<input type='password' class='form-control no-border' id='cupass' name='cupass' placeholder='*******' required>");
            out.println("</div>");
            out.println("<button type='submit' class='btn btn-warning'>Change</button>");
            out.println("<a href='" + request.getContextPath() + "/NormalUser' class='btn btn-info pull-right'>Exit</a>");
            out.println("</form>");
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
