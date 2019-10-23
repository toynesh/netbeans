/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.bulkvend;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author julius
 */
public class Contact extends HttpServlet {

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
            String notify = "";
            HttpSession session = request.getSession();
            if (null != session.getAttribute("clientCode")) {
                String fullname = (String) request.getSession().getAttribute("fullname");
                out.println(data.pageHeader(true, fullname));
            } else {
                out.println(data.pageHeader(false, notify));
                //System.out.println("User Not Loggedin");
            }
            out.println("<section class='page-section about-heading text-light'>"
                    + "      <div class='container'>"
                    + "        <div class='row'>"
                    + "          <div class='col-xl-9 mx-auto'>"
                    + "            <div class='cta-inner text-center rounded'>"
                    + "              <h2 class='section-heading mb-5'>"
                    + "                <span class='section-heading-upper'>Come On In</span>"
                    + "                <span class='section-heading-lower'>We're Open</span>"
                    + "              </h2>"
                    + "              <ul class='list-unstyled list-hours mb-5 text-left mx-auto'>"
                    + "                <li class='list-unstyled-item list-hours-item d-flex'>"
                    + "                  Sunday"
                    + "                  <span class='ml-auto'>Closed</span>"
                    + "                </li>"
                    + "                <li class='list-unstyled-item list-hours-item d-flex'>"
                    + "                  Monday"
                    + "                  <span class='ml-auto'>8:00 AM to 5:00 PM</span>"
                    + "                </li>"
                    + "                <li class='list-unstyled-item list-hours-item d-flex'>"
                    + "                  Tuesday"
                    + "                  <span class='ml-auto'>8:00 AM to 5:00 PM</span>"
                    + "                </li>"
                    + "                <li class='list-unstyled-item list-hours-item d-flex'>"
                    + "                  Wednesday"
                    + "                  <span class='ml-auto'>8:00 AM to 5:00 PM</span>"
                    + "                </li>"
                    + "                <li class='list-unstyled-item list-hours-item d-flex'>"
                    + "                  Thursday"
                    + "                  <span class='ml-auto'>8:00 AM to 5:00 PM</span>"
                    + "                </li>"
                    + "                <li class='list-unstyled-item list-hours-item d-flex'>"
                    + "                  Friday"
                    + "                  <span class='ml-auto'>8:00 AM to 5:00 PM</span>"
                    + "                </li>"
                    + "                <li class='list-unstyled-item list-hours-item d-flex'>"
                    + "                  Saturday"
                    + "                  <span class='ml-auto'>Closed</span>"
                    + "                </li>"
                    + "              </ul>"
                    + "              <p class='address mb-5'>"
                    + "                <em>"
                    + "                  <strong>Ngong road Professional Center</strong>"
                    + "                  <br>"
                    + "                  Ngong road, Nairobi"
                    + "                </em>"
                    + "              </p>"
                    + "              <p class='mb-0'>"
                    + "                <small>"
                    + "                  <em>Call Anytime</em>"
                    + "                </small>"
                    + "                <br>"
                    + "                (254)  709 711 555"
                    + "              </p>"
                    + "            </div>"
                    + "          </div>"
                    + "        </div>"
                    + "      </div>"
                    + "    </section>");
            out.println(data.pageFoorter(""));
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
