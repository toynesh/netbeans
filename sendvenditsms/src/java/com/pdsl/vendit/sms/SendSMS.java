/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vendit.sms;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author coolie
 */
public class SendSMS extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    PhonePrefixes prfx = new PhonePrefixes();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        //http://vendit.pdslkenya.com:8084/sendvenditsms/SendSMS?to=254728064120&message=venditsms
        DataStore data = new DataStore();
        Sdp sdp = new Sdp();
        String to = "";
        String message = "";
        if (null != request.getParameter("to")) {
            if (!request.getParameter("to").equals("")) {
                to = request.getParameter("to");
            }
        }
        if (null != request.getParameter("message")) {
            if (!request.getParameter("message").equals("")) {
                message = request.getParameter("message");
            }
        }
        to = to.replace("+", "");
        if (prfx.checkPhonePrefix(to).equals("SAFARICOM")) {
            sdp.sendSMS(to, message, "VendIT", "704307");
        } else if (prfx.checkPhonePrefix(to).equals("AIRTELRX")) {
            AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", to, "PdslKeT2", "VENDIT", "0", message, "pdsl@999");
            esdp.submitMessage();
        } else {
            EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(to), "pdsl219", "pdsl219", to, message, "VendIT");
            esdp.submitMessage();
        }

        String redirectURL = "https://vendit.pdslkenya.com/vendit/member-index.php";
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
