/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eric.rfid;

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

/**
 *
 * @author coolie
 */
public class BusReport extends HttpServlet {

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
            out.println(data.header);
            out.println("<div class='container'>");
            out.println("<div class='row'>");
            out.println("    <div class='col-lg-3'>");
            out.println("        <div class='panel panel-default'>");
            out.println("            <div class='panel-heading'>");
            out.println("                QueryBy");
            out.println("            </div>");
            out.println("            <div class='panel-body text-primary'>");
            out.println("                <form  role='form' method='post' action='" + request.getContextPath() + "/BusReport'>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='sdate' >StartDate</label>");
            out.println("                        <input type='date' class='form-control' id='sdate' name='sdate'>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='edate' >EndDate</label>");
            out.println("                        <input type='date' class='form-control' id='edate' name='edate'>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='sstudent' >Student/RFID</label>");
            out.println("                        <input type='text' class='form-control' id='sstudent' name='sstudent'>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='sdriver' >Driver</label>");
            out.println("                        <input type='text' class='form-control' id='sdriver' name='sdriver'>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                        <label for='strip' >Trip</label>");
            out.println("                        <input type='text' class='form-control' id='strip' name='strip'>");
            out.println("                    </div>");
            out.println("                    <div class='form-group'>");
            out.println("                            <input id='submit' name='submit' type='submit' value='Search' class='btn btn-primary'>");
            out.println("                    </div>");
            out.println("                </form>");
            out.println("            </div>");
            out.println("        </div>");
            out.println("    </div>");
            out.println("    <div class='col-lg-9'>");
            out.println("        <div class='panel panel-default'>");
            out.println("            <div class='panel-heading text-center'>");
            out.println("                Trips");
            out.println("            </div>");
            out.println("            <div class='panel-body'>");
            out.println("<table class='table table-bordered'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Date</th>");
            out.println("<th>RFID</th>");
            out.println("<th>RequestType</th>");
            out.println("<th>Driver</th>");
            out.println("<th>lastlocation</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");

            String sdate = "none";
            String edate = "none";
            String student = "none";
            String driver = "none";
            String triptype = "none";
            if (null != request.getParameter("sdate")) {
                if (!request.getParameter("sdate").equals("")) {
                    sdate = request.getParameter("sdate");
                }
            }
            if (null != request.getParameter("edate")) {
                if (!request.getParameter("edate").equals("")) {
                    edate = request.getParameter("edate");
                }
            }
            if (null != request.getParameter("sstudent")) {
                if (!request.getParameter("sstudent").equals("")) {
                    student = request.getParameter("sstudent");
                }
            }
            if (null != request.getParameter("sdriver")) {
                if (!request.getParameter("sdriver").equals("")) {
                    driver = request.getParameter("sdriver");
                }
            }
            if (null != request.getParameter("strip")) {
                if (!request.getParameter("strip").equals("")) {
                    triptype = request.getParameter("strip");
                }
            }

            System.out.println("SDATE: " + sdate);
            System.out.println("EDATE: " + edate);
            System.out.println("STUDENT: " + student);
            System.out.println("DRIVER: " + driver);
            System.out.println("TRIP: " + triptype);
            try {
                Connection con = data.connect();
                String query = "SELECT `triptime`,`rfid`,`requesttype`,`driver`,`lastlocation` FROM `trips` ORDER BY `triptime` DESC";
                if (!sdate.equals("none")) {
                    if (!edate.equals("none")) {
                        query = "SELECT `triptime`,`rfid`,`requesttype`,`driver`,`lastlocation` FROM `trips` WHERE `triptime` BETWEEN '" + sdate + " 00:00:00' AND '" + edate + " 23:59:59' ORDER BY `triptime` DESC";
                    }
                } else if (!student.equals("none")) {
                    query = "SELECT `triptime`,`rfid`,`requesttype`,`driver`,`lastlocation` FROM `trips` WHERE `rfid`='" +student+ "' ORDER BY `triptime` DESC";
                } else if (!driver.equals("none")) {
                    query = "SELECT `triptime`,`rfid`,`requesttype`,`driver`,`lastlocation` FROM `trips` WHERE `driver`='" +driver+ "' ORDER BY `triptime` DESC";
                } else if (!triptype.equals("none")) {
                    query = "SELECT `triptime`,`rfid`,`requesttype`,`driver`,`lastlocation` FROM `trips` WHERE `requesttype`='" + triptype + "' ORDER BY `triptime` DESC";
                } else {
                    query = "SELECT `triptime`,`rfid`,`requesttype`,`driver`,`lastlocation` FROM `trips` ORDER BY `triptime` DESC";
                }
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getString(1) + "</td>");
                    out.println("<td>" + rs.getString(2) + "</td>");
                    out.println("<td>" + rs.getString(3) + "</td>");
                    out.println("<td>" + rs.getString(4) + "</td>");
                    out.println("<td>" + rs.getString(5) + "</td>");
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
