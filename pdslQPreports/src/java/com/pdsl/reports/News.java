/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.reports;

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
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author julius
 */
public class News extends HttpServlet {

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
            if (null == session.getAttribute("retailer")) {
                String redirectURL = "Login";
                response.sendRedirect(redirectURL);
            } else {
                String fullname = (String) request.getSession().getAttribute("fullname");
                String ret = (String) request.getSession().getAttribute("retailer");
                String rtlr = "Select...";
                if (!ret.equals("Admin")) {
                    rtlr = ret;
                }
                if (null != request.getParameter("retailer")) {
                    if (!request.getParameter("retailer").equals("")) {
                        rtlr = request.getParameter("retailer");
                    }
                }
                DateTime rdt = new DateTime().minusMonths(1);
                DateTimeFormatter rfm = DateTimeFormat.forPattern("MMM yyyy");
                String currdt = rfm.print(rdt);
                if (null != request.getParameter("mw")) {
                    currdt = request.getParameter("mw");
                }
                out.println(data.handleHeader("News", rtlr, "none", currdt, fullname));
                out.println("<div id='tprint' class='col-lg-8 col-lg-offset-2' style='margin-top:1%'>");
                out.println("<div class='horizontalLine'></div>");
                out.println("<h3 class='text-info text-center text-capitalize'>News & Updates</h3>");
                out.println("<div class='horizontalLine'></div>");
                try {
                    String query = "SELECT `id`,`notes` FROM `news` WHERE `id`=1";
                    Connection con = data.connect();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        out.println(rs.getString(2));
                    }
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(News.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.println("</div>");
                out.println(data.footer);
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
