/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eloans.manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
public class SaveNewBorrower extends HttpServlet {

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
        String fname = "";
        String lname = "";
        String city = "";
        String idnumber = "";
        String phone = "";
        String address1 = "";
        String address2 = "";
        if (null != request.getParameter("fname")) {
            if (!request.getParameter("fname").equals("")) {
                fname = request.getParameter("fname");
            }
        }
        if (null != request.getParameter("lname")) {
            if (!request.getParameter("lname").equals("")) {
                lname = request.getParameter("lname");
            }
        }
        if (null != request.getParameter("city")) {
            if (!request.getParameter("city").equals("")) {
                city = request.getParameter("city");
            }
        }
        if (null != request.getParameter("idnumber")) {
            if (!request.getParameter("idnumber").equals("")) {
                idnumber = request.getParameter("idnumber");
            }
        }
        if (null != request.getParameter("phone")) {
            if (!request.getParameter("phone").equals("")) {
                phone = request.getParameter("phone");
            }
        }
        if (null != request.getParameter("address1")) {
            if (!request.getParameter("address1").equals("")) {
                address1 = request.getParameter("address1");
            }
        }
        if (null != request.getParameter("address2")) {
            if (!request.getParameter("address2").equals("")) {
                address2 = request.getParameter("address2");
            }
        }

        Connection con = data.connect();
        String values = "insert into  borrowers(fname,lname,city,idnumber,phone,address1,address2) values (?,?,?,?,?,?,?)";
        try {
            PreparedStatement prep = con.prepareStatement(values);
            prep.setString(1, fname);
            prep.setString(2, lname);
            prep.setString(3, city);
            prep.setString(4, idnumber);
            prep.setString(5, phone);
            prep.setString(6, address1);
            prep.setString(7, address2);
            prep.execute();
            prep.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        String redirectURL = "Home?newborrower=1";
        response.sendRedirect(redirectURL);
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
