/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.listen;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
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
 * @author juliuskun
 */
public class Back extends HttpServlet {

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

        DataStore data = new DataStore();
        Connection conn = data.connect();
        try {
            conn = data.connect();
            PreparedStatement prep = null;
            DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
            String datee = fmt.print(dt);

            String query = "SELECT cust_mobile_no,cust_meter_no,cust_mpesa_code,cust_amount_added,cust_name,res_Remarks,Vend_by,vend_timestamp,status,server,agent FROM manual_vend WHERE status=0  ORDER BY vend_timestamp DESC";
            Statement st = null;
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String cust_mobile_no = rs.getString(1);
                String cust_meter_no = rs.getString(2);
                String cust_mpesa_code = rs.getString(3);
                String cust_amount_added = rs.getString(4);
                String cust_name = rs.getString(5);
                String res_Remarks = rs.getString(6);
                String Vend_by = rs.getString(7);
                String vend_timestamp = rs.getString(8);
                String server = rs.getString(10);
                String agent = rs.getString(11);
                int status = rs.getInt(9);
                Double amt = Double.valueOf(Double.parseDouble(cust_amount_added));

                String query2 = "SELECT * FROM failedtokens WHERE cust_mpesa_code LIKE '" + cust_mpesa_code + "'";
                Statement st2 = conn.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                if (!rs2.isBeforeFirst()) {
                    String res = data.sendPayment(datee, cust_mpesa_code, cust_meter_no, cust_mobile_no, "0", cust_name);

                    String msg = "Source:" + server + " " + agent + " Done by:" + Vend_by + " At:" + datee + "\nTransaction msisdn:" + cust_mobile_no + " Meter:" + cust_meter_no + " Mpesa Code:" + cust_mpesa_code + " Amount:" + cust_amount_added + " Customer:" + cust_name + " Comments:" + res_Remarks + "\nMessage:" + res;

                    Logger.getLogger(Back.class.getName()).log(Level.INFO, "::=::" + msg);
                    if (amt.doubleValue() > 0.0D) {
                        //SendEmail.sendSimpleMail("munene@pdslkenya.com", msg);
                    }
                    String insert = "insert into failedtokens (cust_mobile_no,cust_meter_no,cust_mpesa_code,cust_amount_added,cust_name,res_Remarks,Vend_by,vend_timestamp,ipayres,server,agent) values (?,?,?,?,?,?,?,?,?,?,?)";
                    prep = conn.prepareStatement(insert);
                    prep.setString(1, cust_mobile_no);
                    prep.setString(2, cust_meter_no);
                    prep.setString(3, cust_mpesa_code);
                    prep.setString(4, cust_amount_added);
                    prep.setString(5, cust_name);
                    prep.setString(6, res_Remarks);
                    prep.setString(7, Vend_by);
                    prep.setString(8, vend_timestamp);
                    prep.setString(9, res);
                    prep.setString(10, server);
                    prep.setString(11, agent);
                    prep.execute();
                    prep.close();

                    String updatempesa = "UPDATE manual_vend SET status= ?, ipayres= ? where cust_mpesa_code= ?";
                    PreparedStatement prepu = conn.prepareStatement(updatempesa);
                    prepu.setInt(1, 1);
                    prepu.setString(2, res);
                    prepu.setString(3, cust_mpesa_code);
                    prepu.executeUpdate();
                    prepu.close();
                } else {
                    System.out.println("Code exists");
                    Logger.getLogger(Back.class.getName()).log(Level.INFO, "Code exists");
                    String updatempesa = "UPDATE manual_vend SET status= ? where cust_mpesa_code= ?";
                    PreparedStatement prepu = conn.prepareStatement(updatempesa);
                    prepu.setInt(1, 1);
                    prepu.setString(2, cust_mpesa_code);
                    prepu.executeUpdate();
                    prepu.close();
                }
            }
            conn.close();
        } catch (Exception ex) {
            try {
                conn.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Back.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        /*try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. 
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Back</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Back at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }*/
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
