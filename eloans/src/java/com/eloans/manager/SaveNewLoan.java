/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eloans.manager;

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
public class SaveNewLoan extends HttpServlet {

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
            String borrowerid = "";
            String fullname = "";
            String phone = "";
            String amount = "";
            String interest = "";
            Double netamount = 0.0;
            String issuedate = "";
            String expecteddate = "";

            if (null != request.getParameter("fullname")) {
                if (!request.getParameter("fullname").equals("")) {
                    String alln = request.getParameter("fullname");
                    String asplit[] = alln.split(" - ");
                    fullname = asplit[1];
                    phone = asplit[0];
                }
            }
            if (null != request.getParameter("bid")) {
                if (!request.getParameter("bid").equals("")) {
                    borrowerid = request.getParameter("bid");
                }
            }
            if (null != request.getParameter("amount")) {
                if (!request.getParameter("amount").equals("")) {
                    amount = request.getParameter("amount");
                }
            }
            if (null != request.getParameter("idate")) {
                if (!request.getParameter("idate").equals("")) {
                    issuedate = request.getParameter("idate");
                }
            }
            if (null != request.getParameter("epdate")) {
                if (!request.getParameter("epdate").equals("")) {
                    expecteddate = request.getParameter("epdate");
                }
            }
            if (null != request.getParameter("interest")) {
                if (!request.getParameter("interest").equals("")) {
                    interest = request.getParameter("interest");
                }
            }
            if (null != request.getParameter("netamount")) {
                if (!request.getParameter("netamount").equals("")) {
                    netamount = Double.parseDouble(request.getParameter("netamount"));
                }
            }

            DataStore data = new DataStore();
            Connection con = data.connect();

            //check -negative balance in previous loans
            String negbal = checknegbalance(borrowerid);
            if (!negbal.equals("none")) {
                netamount = netamount + Double.parseDouble(negbal);
                try {
                    String update = "update paymenttrend set flag = ? WHERE borrowerid = ? AND flag = ?";
                    PreparedStatement preparedStmt = con.prepareStatement(update);
                    preparedStmt.setString(1, "1");
                    preparedStmt.setString(2, borrowerid);
                    preparedStmt.setString(3, "0");
                    preparedStmt.executeUpdate();
                    System.out.println("Updated paymenttrend table");
                } catch (SQLException ex) {
                    Logger.getLogger(SaveNewLoan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String values = "insert into loans(borrowerid,fullname,phone,amount,interest,netamount,issuedate,expecteddate) values (?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement prep = con.prepareStatement(values);
                prep.setString(1, borrowerid);
                prep.setString(2, fullname);
                prep.setString(3, phone);
                prep.setString(4, amount);
                prep.setString(5, interest);
                prep.setDouble(6, netamount);
                prep.setString(7, issuedate);
                prep.setString(8, expecteddate);
                prep.execute();
                prep.close();

                String update = "update borrowers set flag = ? WHERE id = ?";
                PreparedStatement preparedStmt = con.prepareStatement(update);
                preparedStmt.setString(1, "1");
                preparedStmt.setString(2, borrowerid);
                preparedStmt.executeUpdate();
                System.out.println("Updated borrowers table");

            } catch (SQLException ex) {
                Logger.getLogger(SaveNewLoan.class.getName()).log(Level.SEVERE, null, ex);
            }
            String redirectURL = "Home?newloan=1";
            response.sendRedirect(redirectURL);
        }
    }

    public String checknegbalance(String brrwid) {
        String res = "none";
        try {
            DataStore data = new DataStore();
            Connection con = data.connect();
            String query = "SELECT `balance` FROM (SELECT * FROM `paymenttrend` WHERE `borrowerid`=" + brrwid + ") AS B WHERE  balance<0 AND `flag`=0";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                res = rs.getString(1);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
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
