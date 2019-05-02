/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vending;

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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class Autofloat extends HttpServlet {

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

    protected String processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        //http://172.27.116.36:8084/pdslvending/Autofloat?vcode=101&refe=testautofloat&amount=1
        Connection con = data.connect();
        String resp = "fail";
        try {
            String vcode = request.getParameter("vcode");
            String refe = request.getParameter("refe");
            String amount = request.getParameter("amount");
            String remote = request.getRemoteAddr();
            //Logger.getLogger(Autofloat.class.getName()).log(Level.INFO, "Source:" + remote +" vcode:"+vcode+" refe:"+refe+" amount:"+amount);
            if (remote.equals("172.27.116.21") || remote.equals("197.248.61.164") || remote.equals("172.27.103.9")) {
                DateTime dt = new DateTime();
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
                String year = fmt.print(dt);
                DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
                String month = formatter.print(dt);
                if (data.chechVendor(vcode).equals("found")) {
                    if (!data.chechFloatRef(refe, month, year).equals("found")) {
                        String values = "insert into transactions" + month + year + "(vendor_code,tran_type,refnumber,tran_depo_amt,status) values (?,?,?,?,?)";
                        try {
                            PreparedStatement prep = con.prepareStatement(values);
                            prep.setString(1, vcode);
                            prep.setString(2, "FLOAT DEPOSIT");
                            prep.setString(3, refe);
                            prep.setString(4, amount);
                            prep.setString(5, "1");
                            prep.execute();
                            prep.close();
                            resp = "success";
                            Logger.getLogger(DataStore.class.getName()).log(Level.INFO, "SubAG " + vcode + " Floating Successful");
                        } catch (SQLException ex) {
                            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                    Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, "Duplicate " + refe + " Ref trying to float for vendor  " + vcode + "");
                }
                }
            } else {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, "Wrong IP trying to float for vendor  " + vcode + "");
            }
        } catch (Exception ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, "Error Autofloating" + ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resp;
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
        String r = processRequest(request, response);
        PrintWriter out = response.getWriter();
        out.write(r);
        out.close();
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
        String r = processRequest(request, response);
        //response.getWriter().append(resretun);
        PrintWriter out = response.getWriter();
        out.write(r);
        out.close();
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
