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
public class SavePayLoan extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String loanid = request.getParameter("lid");
            String brwid = request.getParameter("bid");
            Double loanamount = Double.parseDouble(request.getParameter("loanamount"));
            Double pamount = Double.parseDouble(request.getParameter("pamount"));
            Double totalpaid = Double.parseDouble(request.getParameter("totalpaid"));
            String pdate = request.getParameter("pdate");
            String collateral = request.getParameter("collateral");
            String notes = request.getParameter("notes");

            Double newtotalpaid = totalpaid + pamount;
            Double newbalance = loanamount - newtotalpaid;
            
            int flag=0;
            int bflag=1;
            if(newbalance<=0){
                flag=1;
                bflag=0;
            }

            DataStore data = new DataStore();
            Connection con = data.connect();
            String values = "insert into paymenttrend(loanid,borrowerid,amount,balance,collateral,notes) values (?,?,?,?,?,?)";
            try {
                PreparedStatement prep = con.prepareStatement(values);
                prep.setString(1, loanid);
                prep.setString(2, brwid);
                prep.setDouble(3, pamount);
                prep.setDouble(4, newbalance);
                prep.setString(5, collateral);
                prep.setString(6, notes);
                prep.execute();
                prep.close();

                String update = "update loans set totalpaid = ?,paydate = ?,collateral = ?,notes = ?,flag = ? WHERE id = ?";
                PreparedStatement preparedStmt = con.prepareStatement(update);
                preparedStmt.setDouble(1, newtotalpaid);
                preparedStmt.setString(2, pdate);
                preparedStmt.setString(3, collateral);
                preparedStmt.setString(4, notes);
                preparedStmt.setInt(5, flag);
                preparedStmt.setString(6, loanid);
                preparedStmt.executeUpdate();
                System.out.println("Updated loans table");
                
                String bupdate = "update borrowers set flag = ? WHERE id = ?";
                PreparedStatement bpreparedStmt = con.prepareStatement(bupdate);
                bpreparedStmt.setInt(1, bflag);
                bpreparedStmt.setString(2, brwid);
                bpreparedStmt.executeUpdate();
                System.out.println("Updated borrowers table");

            } catch (SQLException ex) {
                Logger.getLogger(SavePayLoan.class.getName()).log(Level.SEVERE, null, ex);
            }
            String redirectURL = "Home?payloan=1";
            response.sendRedirect(redirectURL);

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
