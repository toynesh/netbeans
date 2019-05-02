/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eric.rfid;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metro_sdp.Metro_sdp;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Eric
 */
public class Interpreter extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //http://localhost:8080/schrfidctl/Interpreter?tripchange=no&requesttype=pickup&rfid=5A F7 87 29
    String[] student1 = {"C6 9A 27 AC", "John Doe", "254728064120"};
    String[] student2 = {"5A F7 87 29", "Eric Muli", "254728064120"};

    List<String> _students = new ArrayList<>();
    String _appresponse = "Error Occured";
    Metro_sdp sdp = new Metro_sdp();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        DataStore data = new DataStore();
        try {
            DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:MM:ss");
            String datee = fmt.print(dt);

            Connection con = data.connect();
            String _tripchange = request.getParameter("tripchange");
            String _requesttype = request.getParameter("requesttype");
            String _driver = request.getParameter("driver");
            String _otherparams = request.getParameter("otherparams");
           // String _manualinput = request.getParameter("manualinput");
            Logger.getLogger(Interpreter.class.getName()).log(Level.INFO, "Request Type: " + _requesttype + " Trip change: " + _tripchange);
            Logger.getLogger(Interpreter.class.getName()).log(Level.INFO, "RFID: " + request.getParameter("rfid").trim());
            System.out.println("OP: " + _otherparams);
            if (_requesttype.equals("pickup")) {
                    if (_tripchange.equals("yes")) {
                        //_students.clear();
                        String update = "update trips set flag = ? WHERE requesttype = ? AND driver= ? AND flag= ?";
                        PreparedStatement preparedStmt = con.prepareStatement(update);
                        preparedStmt.setInt(1, 1);
                        preparedStmt.setString(2, "drophome");
                        preparedStmt.setString(3, _driver);
                        preparedStmt.setInt(4, 0);
                        preparedStmt.executeUpdate();
                        //System.out.println("Updated Mpesa");
                    }
                    String rfid = request.getParameter("rfid").trim();
                    String pdchck = data.chechPD(rfid);
                    if (pdchck.equals("none")) {
                        //_students.add(rfid);
                        String values = "insert into trips(rfid,requesttype,driver,lastlocation) values (?,?,?,?)";
                        try {
                            PreparedStatement prep = con.prepareStatement(values);
                            prep.setString(1, rfid);
                            prep.setString(2, _requesttype);
                            prep.setString(3, _driver);
                            prep.setString(4, _otherparams);
                            prep.execute();
                            prep.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(CRUDManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        // _appresponse = rfid+" Picked Up Successfully";
                        //sdp.sendSdpSms(student1[2], "The bus has picked " + rfid + " To School", "RFID", "704307");
                        String query = "SELECT `rfid`,`full_name`,`parent_name`,`parent_email`,`parent_msisdn`,`id` FROM `students` WHERE `rfid`='" + rfid + "'";
                        try {
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            if (rs.isBeforeFirst()) {
                                while (rs.next()) {
                                    _appresponse = rs.getString(2) + " Picked Successfully";
                                    sdp.sendSdpSms(rs.getString(5), "The bus "+data.getBus(_driver)+" has picked " + rs.getString(2) + " To School On " + datee + " Driver: "+_driver+" " + _otherparams + "", "STRFID", "704309", "http://197.248.161.166:8085/VendITNotification/SmsNotificationService");
                                }

                            } else {
                                _appresponse = "RFID:" + rfid + ":is Not Registered";
                            }
                        } catch (SQLException e) {

                        }
                    } else {
                        String pdchksplit[] = pdchck.split(":");
                        _appresponse = "The Student has a "+pdchksplit[1]+" By Driver: "+pdchksplit[2]+" Bus: "+data.getBus(pdchksplit[2]);
                    }

                }
                if (_requesttype.equals("drophome")) {
                    if (_tripchange.equals("yes")) {
                        //_students.clear();
                        String update = "update trips set flag = ? WHERE requesttype = ? AND driver= ? AND flag= ?";
                        PreparedStatement preparedStmt = con.prepareStatement(update);
                        preparedStmt.setInt(1, 1);
                        preparedStmt.setString(2, "pickup");
                        preparedStmt.setString(3, _driver);
                        preparedStmt.setInt(4, 0);
                        preparedStmt.executeUpdate();
                    }
                    String rfid = request.getParameter("rfid").trim();
                    String pdchck = data.chechPD(rfid);
                    if (pdchck.equals("none")) {
                        //_students.add(rfid);
                        String values = "insert into trips(rfid,requesttype,driver,lastlocation) values (?,?,?,?)";
                        try {
                            PreparedStatement prep = con.prepareStatement(values);
                            prep.setString(1, rfid);
                            prep.setString(2, _requesttype);
                            prep.setString(3, _driver);
                            prep.setString(4, _otherparams);
                            prep.execute();
                            prep.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(CRUDManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        String query = "SELECT `rfid`,`full_name`,`parent_name`,`parent_email`,`parent_msisdn`,`id` FROM `students` WHERE `rfid`='" + rfid + "'";
                        try {
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            if (rs.isBeforeFirst()) {
                                while (rs.next()) {
                                    _appresponse = rs.getString(2) + " Dropped Home Successfully";
                                    sdp.sendSdpSms(rs.getString(5), "The bus "+data.getBus(_driver)+" has dropped " + rs.getString(2) + " at Home On " + datee + " Driver: "+_driver+" " + _otherparams + "", "STRFID", "704309", "http://197.248.161.166:8085/VendITNotification/SmsNotificationService");
                                }

                            } else {
                                _appresponse = "RFID:" + rfid + ":is Not Registered";//0721802302 Vending Point
                            }
                        } catch (SQLException e) {

                        }
                    } else {
                         String pdchksplit[] = pdchck.split(":");
                        _appresponse = "The Student has a "+pdchksplit[1]+" By Driver: "+pdchksplit[2]+" Bus: "+data.getBus(pdchksplit[2]);
                    }
                }
            int spikd=data.getPstndts(_driver, _requesttype);
            Logger.getLogger(Interpreter.class.getName()).log(Level.INFO, "Students Picked/Droped: " + spikd);
            Logger.getLogger(Interpreter.class.getName()).log(Level.INFO, "Response to App: " + _appresponse);
            //out.write(_appresponse);
            con.close();
        } catch (Exception e) {

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
        PrintWriter out = response.getWriter();
        out.write(_appresponse);
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
        processRequest(request, response);
        PrintWriter out = response.getWriter();
        out.write(_appresponse);
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
