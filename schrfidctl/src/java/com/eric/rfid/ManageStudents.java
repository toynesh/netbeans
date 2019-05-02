/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eric.rfid;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
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
public class ManageStudents extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        DataStore data = new DataStore();
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String posts = "";
            posts = request.getParameter("posts");
            if (posts == null) {
                posts = "Manage Students Portal";
            }
            String studentName = "";
            String studentRFID = "";
            String parentName = "";
            String parentEmail = "";
            String parentPhone = "";
            String crudtype = "newstudent";
            String sid = "";

            if (null != request.getParameter("crudtype")) {
                crudtype = request.getParameter("crudtype");
                sid = request.getParameter("ID");
                try {
                    Connection con = data.connect();
                    String query = "SELECT `rfid`,`full_name`,`parent_name`,`parent_email`,`parent_msisdn`,`id` FROM `students` WHERE id=" + sid + "";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        studentName = rs.getString(2);
                        studentRFID = rs.getString(1);
                        parentName = rs.getString(3);
                        parentEmail = rs.getString(4);
                        parentPhone = rs.getString(5);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ManageStudents.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            out.println(data.header);
            out.println("<div class='container'>");
            out.println("<div class='row'>");
            out.println("    <div class='col-lg-3'>");
            out.println("        <div class='panel panel-default'>");
            out.println("            <div class='panel-heading'>");
            out.println("                Add/Remove Student");
            out.println("            </div>");
            out.println("            <div class='panel-body text-primary'>");
            out.println("                <form  role='form' method='post' action='/schrfidctl/CRUDManager'>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='sname' >Student Name</label>");
            out.println("                        <input type='text' class='form-control' id='sname' name='sname' placeholder='First & Last Name' value='" + studentName + "'>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='RFID' >RFID</label>");
            out.println("                        <input type='text' class='form-control' id='srfid' name='srfid' placeholder='AE TR OV MQ' value='" + studentRFID + "'>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='spname' >Parent Name</label>");
            out.println("                        <input type='text' class='form-control' id='spname' name='spname' placeholder='First & Last Name' value='" + parentName + "'>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='pemail'>Parent Email</label>");
            out.println("                            <input type='email' class='form-control' id='pemail' name='pemail' placeholder='Email: example@domain.com' value='" + parentEmail + "'>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='sphone' >Parent Phone</label>");
            out.println("                        <input type='text' class='form-control' id='sphone' name='sphone' placeholder='254' value='" + parentPhone + "'>");
            out.println("                        <input type='text' style='display:none'  id='crudtype' name='crudtype' value='"+crudtype+"'>");
            out.println("                        <input type='text' style='display:none'  id='sid' name='sid' value='"+sid+"'>");
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
            out.println("                All Students");
            out.println("            </div>");
            out.println("            <div class='panel-body'>");
            out.println("<table class='table table-bordered'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Name</th>");
            out.println("<th>RFID</th>");
            out.println("<th>ParentName</th>");
            out.println("<th>Email</th>");
            out.println("<th>Phone</th>");
            out.println("<th>Action</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            try {
                Connection con = data.connect();
                String query = "SELECT `rfid`,`full_name`,`parent_name`,`parent_email`,`parent_msisdn`,`id` FROM `students`";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getString(2) + "</td>");
                    out.println("<td>" + rs.getString(1) + "</td>");
                    out.println("<td>" + rs.getString(3) + "</td>");
                    out.println("<td>" + rs.getString(4) + "</td>");
                    out.println("<td>" + rs.getString(5) + "</td>");
                    out.println("<td class='danger'><a href='/schrfidctl/ManageStudents?crudtype=sedit&ID=" + rs.getString(6) + "'>Edit</a> || <a href='/schrfidctl/CRUDManager?crudtype=sdelete&ID=" + rs.getString(6) + "'>Delete</a></td>");
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
