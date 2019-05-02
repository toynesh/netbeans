/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslvending.portal;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class SaveVendor extends HttpServlet {

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
        PrintWriter out = response.getWriter();

        String edit = request.getParameter("edit");
        String vendorname = request.getParameter("vendorname");
        String vendorcode = request.getParameter("vendorcode");
        String newfloat = request.getParameter("newfloat");
        String floatref = request.getParameter("floatref");
        String prepaid = request.getParameter("prepaid");
        String postpaid = request.getParameter("postpaid");
        String airtime = request.getParameter("airtime");
        String mode = request.getParameter("mode");
        if ((mode == null) || (mode.isEmpty())) {
            mode = "test";
        }
        if ((prepaid == null) || (prepaid.isEmpty())) {
            prepaid = "no";
        }
        if ((postpaid == null) || (postpaid.isEmpty())) {
            postpaid = "no";
        }
        if ((airtime == null) || (airtime.isEmpty())) {
            airtime = "no";
        }

        if (!edit.equals("yes")) {
            Connection con = data.connect();
            String values = "insert into vendors(vendor_name,vendor_code,prepaid,postpaid,airtime,status) values (?,?,?,?,?,?)";
            try {
                PreparedStatement prep = con.prepareStatement(values);
                prep.setString(1, vendorname);
                prep.setInt(2, parseInt(vendorcode));
                prep.setString(3, prepaid);
                prep.setString(4, postpaid);
                prep.setString(5, airtime);
                prep.setString(6, mode);
                prep.execute();
                prep.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            try {
                Connection con = data.connect();
                String update = "update vendors set vendor_name = ?,vendor_code = ?,prepaid = ?,postpaid = ?,airtime = ?,status = ? WHERE vendor_code = ?";
                PreparedStatement preparedStmt;
                preparedStmt = con.prepareStatement(update);
                preparedStmt.setString(1, vendorname);
                preparedStmt.setInt(2, parseInt(vendorcode));
                preparedStmt.setString(3, prepaid);
                preparedStmt.setString(4, postpaid);
                preparedStmt.setString(5, airtime);
                preparedStmt.setString(6, mode);
                preparedStmt.setString(7, vendorcode);
                preparedStmt.executeUpdate();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(SaveVendor.class.getName()).log(Level.SEVERE, null, ex);
            }
            //add float
            if (!(newfloat == null) && !(newfloat.isEmpty())) {
                DateTime dt = new DateTime();
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
                String year = fmt.print(dt);
                DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
                String month = formatter.print(dt);
                Connection con = data.connect();
                String values = "insert into transactions" + month + year + "(vendor_code,tran_type,refnumber,tran_depo_amt,status) values (?,?,?,?,?)";
                try {
                    PreparedStatement prep = con.prepareStatement(values);
                    prep.setString(1, vendorcode);
                    prep.setString(2, "FLOAT DEPOSIT");
                    prep.setString(3, floatref);
                    prep.setString(4, newfloat);
                    prep.setString(5, "1");
                    prep.execute();
                    prep.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        String redirectURL = "Admin";
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
