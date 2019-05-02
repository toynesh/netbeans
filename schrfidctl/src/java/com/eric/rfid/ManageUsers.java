/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eric.rfid;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author coolie
 */
public class ManageUsers extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */String posts = "";
            posts = request.getParameter("posts");
            if (posts == null) {
                posts = "Manage Drivers Portal";
            }
            out.println(data.header);
            out.println("<div class='container'>");
            out.println("<div class='row'>");
            out.println("    <div class='col-lg-3'>");
            out.println("        <div class='panel panel-default'>");
            out.println("            <div class='panel-heading'>");
            out.println("                Add/Remove Driver");
            out.println("            </div>");
            out.println("            <div class='panel-body text-primary'>");
            out.println("                <form  role='form' method='post' action='/schrfidctl/CRUDManager'>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='ufname' >Full Name</label>");
            out.println("                        <input type='text' class='form-control' id='ufname' name='ufname' placeholder='First & Last Name' value=''>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='bassigned' >Bus Assigned</label>");
            out.println("                        <input type='text' class='form-control' id='bassigned' name='bassigned' placeholder='Bus Assigned' value=''>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='uname' >User Name</label>");
            out.println("                        <input type='text' class='form-control' id='uname' name='uname' placeholder='User Name' value=''>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='pword' >Password</label>");
            out.println("                        <input type='password' class='form-control' id='pword' name='pword' placeholder='********' value=''>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='cpword' >Confirm Password</label>");
            out.println("                        <input type='password' class='form-control' id='cpword' name='cpword' placeholder='********' value=''>");
            out.println("                        <input type='text' style='display:none'  id='crudtype' name='crudtype' value='newuser'>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                            <input id='submit' name='submit' type='submit' value='Send' class='btn btn-primary'>");
            out.println("                    </div>");
            out.println("                </form>");
            out.println("            </div>");
            out.println("        </div>");
            out.println("    </div>");
            out.println("    <div class='col-lg-7'>");
            out.println("        <div class='panel panel-default'>");
            out.println("            <div class='panel-heading text-center'>");
            out.println("                All Drivers");
            out.println("            </div>");
            out.println("            <div class='panel-body'>");
            out.println("<table class='table table-bordered'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Full Name</th>");
             out.println("<th>Bus Assigned</th>");
            out.println("<th>User Name</th>");
            out.println("<th>Action</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            try {
                Connection con = data.connect();
                String query = "SELECT `id`,`full_name`,`bus_assigned`,`user_name` FROM `users`";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>"+rs.getString(2)+"</td>");
                    out.println("<td>"+rs.getString(3)+"</td>");
                    out.println("<td>"+rs.getString(4)+"</td>");
                    out.println("<td class='danger'><a href='/schrfidctl/CRUDManager?crudtype=udelete&ID="+rs.getString(1)+"'>Delete</a></td>");
                    out.println("</tr>");
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ManageStudents.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("            </div>");
            out.println("        </div>");
            out.println("    </div>");
            out.println("    <div class='col-lg-2'>");
            out.println("        <div class='panel panel-default'>");
            out.println("            <div class='panel-heading text-center'>");
            out.println("               <i>Posts</i>");
            out.println("            </div>");
            out.println("            <div class='panel-body text-success'><i>");
            out.println(posts);
            out.println("            ...</i></div>");
            out.println("        </div>");
            out.println("    </div>");
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
