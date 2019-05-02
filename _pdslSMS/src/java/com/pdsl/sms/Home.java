/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.sms;

import java.io.IOException;
import java.io.PrintWriter;
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
public class Home extends HttpServlet {

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

            out.println(data.head("Home"));
            out.println("<div class='col-sm-10'>");
            out.println("<div class='row' style='overflow:hidden;height:92%'>");
            out.println("<div class='col-sm-12'>");
            out.println("<video id='vid' video autobuffer autoplay loop>");
            out.println("<source id='mp4' src='img/homebg.mp4' type='video/mp4'>");
            out.println("</video>");
            out.println("<div class='card card-image mt-5'  style='background:none;border-style:none'>");
            out.println("<div class='text-white text-center py-3 px-4 my-5'>");
            out.println("<div>");
            out.println("<img src='img/pdsl.png' width='130' class='img-fluid rounded-circle bg-white'>");
            out.println("<h1 class='card-title pt-3 mb-5 font-bold'><strong>SMS Portal</strong></h1>");
            out.println("<p class='mx-5 mb-5 pt-12'>You can send SMS blasts to Databases of Phone Numbers in Groups on Demand, or Scheduled.</p>");
            out.println("<a href='Single' class='btn btn-primary btn-lg btn-rounded'><i class='fa fa-clone left faa-pulse animated'></i> <strong>Send SMS</strong></a>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class='row bg-secondary' style=';height:8%'>");
            out.println("<div class='col-sm-12'>");
            DateTime cyr = new DateTime();
            DateTimeFormatter yfm = DateTimeFormat.forPattern("yyyy");
            String yr = yfm.print(cyr);
            out.println("<p class='mt-3 text-center text-white'>&copy; "+yr+" by Professional Digital Systems Ltd.</p>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println(data.foot("Home"));
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
