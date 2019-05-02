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
public class CRUDManager extends HttpServlet {

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
        Connection con = this.data.connect();
        PrintWriter out = response.getWriter();

        String crudtype = request.getParameter("crudtype");
        System.out.println("CRUDType: " + crudtype);

        if (crudtype.equals("newstudent")) {
            String studentName = request.getParameter("sname");
            String studentRFID = request.getParameter("srfid");
            String parentName = request.getParameter("spname");
            String parentEmail = request.getParameter("pemail");
            String parentPhone = request.getParameter("sphone");
            System.out.println("studentName: " + studentName);
            System.out.println("studentRFID: " + studentRFID);
            System.out.println("parentName: " + parentName);
            System.out.println("parentEmail: " + parentEmail);
            System.out.println("parentPhone: " + parentPhone);

            String values = "insert into students(rfid,full_name,parent_name,parent_email,parent_msisdn) values (?,?,?,?,?)";
            try {
                PreparedStatement prep = con.prepareStatement(values);
                prep.setString(1, studentRFID);
                prep.setString(2, studentName);
                prep.setString(3, parentName);
                prep.setString(4, parentEmail);
                prep.setString(5, parentPhone);
                prep.execute();
                prep.close();
            } catch (SQLException ex) {
                Logger.getLogger(CRUDManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            String redirectURL = "ManageStudents?posts=Admin just added a new Student." + studentName;
            response.sendRedirect(redirectURL);
        } else if (crudtype.equals("sedit")) {
            String studentName = request.getParameter("sname");
            String studentRFID = request.getParameter("srfid");
            String parentName = request.getParameter("spname");
            String parentEmail = request.getParameter("pemail");
            String parentPhone = request.getParameter("sphone");
            String sid = request.getParameter("sid");
            String update = "update students set full_name = ?,rfid  = ?,parent_name  = ?,parent_email  = ?,parent_msisdn  = ? WHERE id = ?";
            try {
                PreparedStatement preparedStmt = con.prepareStatement(update);
                preparedStmt.setString(1, studentName);
                preparedStmt.setString(2, studentRFID);
                preparedStmt.setString(3, parentName);
                preparedStmt.setString(4, parentEmail);
                preparedStmt.setString(5, parentPhone);
                preparedStmt.setString(6, sid);
                preparedStmt.executeUpdate();
                System.out.println("Updated Student");
            } catch (SQLException ex) {
                Logger.getLogger(CRUDManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            String redirectURL = "ManageStudents?posts=Admin just Updated a Student >>" + studentName;
            response.sendRedirect(redirectURL);
        } else if (crudtype.equals("newuser")) {
            String full_name = request.getParameter("ufname");
            String user_name = request.getParameter("uname");
            String bus_assigned = request.getParameter("bassigned");
            String user_password = request.getParameter("pword");
            String cuser_password = request.getParameter("cpword");
            if (cuser_password.equals(user_password)) {
                String values = "insert into users(full_name,user_name,bus_assigned,user_password) values (?,?,?,?)";
                try {
                    PreparedStatement prep = con.prepareStatement(values);
                    prep.setString(1, full_name);
                    prep.setString(2, user_name);
                    prep.setString(3, bus_assigned);
                    prep.setString(4, user_password);
                    prep.execute();
                    prep.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CRUDManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                String redirectURL = "ManageUsers?posts=Admin just added a new User.";
                response.sendRedirect(redirectURL);
            } else {
                String redirectURL = "ManageUsers?posts=Password Missmatch!!.";
                response.sendRedirect(redirectURL);
            }
        } else if (crudtype.equals("sdelete")) {
            try {
                String studentID = request.getParameter("ID");
                int id = Integer.parseInt(studentID);
                String query = "delete from students where id = ?";
                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.setInt(1, id);
                // execute the preparedstatement
                preparedStmt.execute();
            } catch (SQLException ex) {
                Logger.getLogger(CRUDManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            String redirectURL = "ManageStudents?posts=Admin just deleted a Student.";
            response.sendRedirect(redirectURL);
        } else if (crudtype.equals("udelete")) {
            try {
                String userID = request.getParameter("ID");
                int id = Integer.parseInt(userID);
                String query = "delete from users where id = ?";
                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.setInt(1, id);
                // execute the preparedstatement
                preparedStmt.execute();
            } catch (SQLException ex) {
                Logger.getLogger(CRUDManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            String redirectURL = "ManageUsers?posts=Admin just deleted a User.";
            response.sendRedirect(redirectURL);
        }
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(CRUDManager.class.getName()).log(Level.SEVERE, null, ex);
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
