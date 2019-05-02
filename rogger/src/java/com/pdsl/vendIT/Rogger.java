/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vendIT;

import com.pdslkenya.reconnectus.Ipay;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * @author juliuskun
 */
public class Rogger extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *      
     * http://172.27.16.21:8084/VendIT/Rogger?id=3630&orig=MPESA&tstamp=20170801150817&mpesa_code=LH15EEDDVVTrecD&mpesa_acc=14140057598&mpesa_msisdn=254728064120&mpesa_amt=1&mpesa_sender=JULIUS KARIUKI&mpesa_verify=onicha
     */
    DataStore data = new DataStore();
    Metro_sdp sdp = new Metro_sdp();
    Sender send = new Sender();
    MessageEditor msEObj = new MessageEditor();
    DelayNotifications statusobj = new DelayNotifications();
    //private static String delaystt = "INACTIVE";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection con = null;
        con = this.data.connect();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String encode = request.getCharacterEncoding();
        PreparedStatement prep = null;

        String results = null;
        String mesg = null;
        try {
            String verify = null;
            String source = "safaricom";
            if (request.getParameter("mpesa_verify") != null) {
                verify = request.getParameter("mpesa_verify");
            }
            if (request.getParameter("source") != null) {
                source = request.getParameter("source");
            }
            String remote = request.getRemoteAddr();
            Logger.getLogger(Rogger.class.getName()).log(Level.INFO, remote);
            Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Source:" + source);
            if ((remote.equals("172.27.16.21")) || (remote.equals("192.168.0.248")) || (remote.equals("172.27.16.23")) || (remote.equals("192.168.0.46"))) {
                if (verify != null) {
                    if (verify.equals("onicha")) {
                        String id = request.getParameter("id");
                        String orig = request.getParameter("orig");

                        String tstamp = request.getParameter("tstamp");

                        String mpesa_code = request.getParameter("mpesa_code");
                        String mpesa_acc = request.getParameter("mpesa_acc");
                        String orig_mpesa_acc = request.getParameter("mpesa_acc");
                        if (mpesa_acc.contains("SEEL")) {
                            mpesa_acc = mpesa_acc.replaceAll("SEEL", "");
                        }
                        mpesa_acc = mpesa_acc.replaceAll(" ", "");
                        String mpesa_msisdn = request.getParameter("mpesa_msisdn");

                        String mpesa_amt = request.getParameter("mpesa_amt");
                        String mpesa_sender = request.getParameter("mpesa_sender");

                        String querye = "SELECT * FROM mpesa2017 WHERE meter LIKE '" + mpesa_acc + "'";
                        Statement ste = con.createStatement();
                        ResultSet rse = ste.executeQuery(querye);
                        if (!rse.isBeforeFirst()) {
                            if (!check(mpesa_msisdn, mpesa_code)) {
                                try {
                                    results = new Ipay().sendRequest(tstamp, mpesa_msisdn, mpesa_code, mpesa_sender, mpesa_amt, tstamp, mpesa_acc);

                                    Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Results:-" + results);
                                    results = results.replace("$", "KES");

                                    results = results.replaceAll("FAIL", "OK");
                                    out.println(results);
                                    String[] reslt = results.split("|");
                                    if (results.toLowerCase().contains("timed out")) {
                                        mesg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction";
                                        if (source.equals("equitel")) {
                                            EquitelSMS esdp = new EquitelSMS("172.27.16.22", "EQUITELRX", "pdsl219", "pdsl219", mpesa_msisdn, mesg, "VendIT");
                                            esdp.submitMessage();
                                            this.sdp.sendSdpSms(mpesa_msisdn, mesg, mpesa_code.substring(5), "704307", "http://197.248.61.166:8084/VendITNotification/SmsNotificationService");
                                        } else {
                                            this.sdp.sendSdpSms(mpesa_msisdn, mesg, mpesa_code, "704307", "http://197.248.61.166:8084/VendITNotification/SmsNotificationService");
                                        }
                                        this.msEObj.setStatus("delay");
                                        String timeout = "insert into timeout (msisdn,mpesa_code,mpesa_sender,amount,tx_date,account) values (?,?,?,?,?,?)";
                                        prep = con.prepareStatement(timeout);
                                        prep.setString(1, mpesa_msisdn);
                                        prep.setString(2, mpesa_code);
                                        prep.setString(3, mpesa_sender);
                                        prep.setString(4, mpesa_amt);
                                        prep.setString(5, tstamp);
                                        prep.setString(6, mpesa_acc);
                                        prep.execute();
                                        prep.close();
                                    } else if (results.toLowerCase().contains("system is busy")) {
                                        mesg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction";
                                        if (source.equals("equitel")) {
                                            EquitelSMS esdp = new EquitelSMS("172.27.16.22", "EQUITELRX", "pdsl219", "pdsl219", mpesa_msisdn, mesg, "VendIT");
                                            esdp.submitMessage();
                                            this.sdp.sendSdpSms(mpesa_msisdn, mesg, mpesa_code.substring(5), "704307", "http://197.248.61.166:8084/VendITNotification/SmsNotificationService");
                                        } else {
                                            this.sdp.sendSdpSms(mpesa_msisdn, mesg, mpesa_code, "704307", "http://197.248.61.166:8084/VendITNotification/SmsNotificationService");
                                        }
                                        this.msEObj.setStatus("delay");
                                        String timeout = "insert into timeout (msisdn,mpesa_code,mpesa_sender,amount,tx_date,account) values (?,?,?,?,?,?)";
                                        prep = con.prepareStatement(timeout);
                                        prep.setString(1, mpesa_msisdn);
                                        prep.setString(2, mpesa_code);
                                        prep.setString(3, mpesa_sender);
                                        prep.setString(4, mpesa_amt);
                                        prep.setString(5, tstamp);
                                        prep.setString(6, mpesa_acc);
                                        prep.execute();
                                        prep.close();
                                    } else if (results.startsWith("FAIL")) {
                                        try {
                                            if (source.equals("equitel")) {
                                                EquitelSMS esdp = new EquitelSMS("172.27.16.22", "EQUITELRX", "pdsl219", "pdsl219", mpesa_msisdn, mesg, "VendIT");
                                                esdp.submitMessage();
                                                this.sdp.sendSdpSms(mpesa_msisdn, mesg, mpesa_code.substring(5), "704307", "http://197.248.61.166:8084/VendITNotification/SmsNotificationService");
                                            } else {
                                                this.sdp.sendSdpSms(mpesa_msisdn, results.substring(5), mpesa_code, "704307", "http://197.248.61.166:8084/VendITNotification/SmsNotificationService");
                                            }
                                            mesg = results.substring(5);
                                            this.msEObj.setStatus("error");
                                        } catch (Exception ex) {
                                            this.send.sendMes(mpesa_msisdn, results.substring(5));
                                            mesg = results.substring(5);
                                            this.msEObj.setStatus("error");
                                        }
                                    } else if (results.toLowerCase().contains("system delayed")) {
                                        DateTime dt = new DateTime();
                                        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
                                        String datee = fmt.print(dt);
                                        mesg = this.msEObj.editToken(results, mpesa_acc, mpesa_msisdn, mpesa_code, datee);
                                        if (source.equals("equitel")) {
                                            EquitelSMS esdp = new EquitelSMS("172.27.16.22", "EQUITELRX", "pdsl219", "pdsl219", mpesa_msisdn, mesg, "VendIT");
                                            esdp.submitMessage();
                                            this.sdp.sendSdpSms(mpesa_msisdn, mesg, mpesa_code.substring(5), "704307", "http://197.248.61.166:8084/VendITNotification/SmsNotificationService");
                                        } else {
                                            this.sdp.sendSdpSms(mpesa_msisdn, mesg, mpesa_code, "704307", "http://197.248.61.166:8084/VendITNotification/SmsNotificationService");
                                        }
                                        /*String prevStatus = this.statusobj.getStatus();
                                        if ((mpesa_acc.contains("-"))
                                                && (prevStatus.equals("on"))) {
                                            this.statusobj.updateNotifaction("on", mpesa_code, mpesa_acc);
                                            this.statusobj.setStatus("poff");
                                            delaystt = "ACTIVE";
                                        }
                                        if ((!mpesa_acc.contains("-")) && ((prevStatus.equals("on")) || (prevStatus.equals("poff")))) {
                                            this.statusobj.updateNotifaction("on", mpesa_code, mpesa_acc);
                                            this.statusobj.setStatus("off");
                                            delaystt = "ACTIVE";
                                        }*/
                                    } else if (results.startsWith("OK")) {
                                        try {
                                            DateTime dt = new DateTime();
                                            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
                                            String datee = fmt.print(dt);
                                            mesg = this.msEObj.editToken(results, mpesa_acc, mpesa_msisdn, mpesa_code, datee);
                                            if (mesg.equals("OK|MTRFE010: KPLC Specified transaction already processed. Queries ph:0709711000.")) {
                                                Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Message not sent out to customer on no FBE");
                                            } else if (mesg.equals("OK|Duplicate mpesa_code. Queries ph:0707-373794.")) {
                                                Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Duplicate mpesa_code");
                                            } else if (source.equals("equitel")) {
                                                EquitelSMS esdp = new EquitelSMS("172.27.16.22", "EQUITELRX", "pdsl219", "pdsl219", mpesa_msisdn, mesg, "VendIT");
                                                esdp.submitMessage();
                                                this.sdp.sendSdpSms(mpesa_msisdn, mesg, mpesa_code.substring(5), "704307", "http://197.248.61.166:8084/VendITNotification/SmsNotificationService");
                                            } else {
                                                this.sdp.sendSdpSms(mpesa_msisdn, mesg, mpesa_code, "704307", "http://197.248.61.166:8084/VendITNotification/SmsNotificationService");
                                            }
                                            Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Phn:" + mpesa_msisdn + " NEW MESSAGE:" + mesg);

                                            //Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "DelayStatus: " + delaystt);

                                            /*String prevStatus = "";
                                            if (delaystt.equals("ACTIVE")) {
                                                prevStatus = this.statusobj.getStatus();
                                            }
                                            if ((mpesa_acc.contains("-"))
                                                    && (mesg.contains("PAID"))
                                                    && (prevStatus.equals("poff"))) {
                                                this.statusobj.updateNotifaction("off", mpesa_code, mpesa_acc);
                                                this.statusobj.setStatus("on");
                                                delaystt = "INACTIVE";
                                            }
                                            if ((mesg.contains("KPLC Mtr No"))
                                                    && (prevStatus.equals("off"))) {
                                                this.statusobj.updateNotifaction("off", mpesa_code, mpesa_acc);
                                                this.statusobj.setStatus("on");
                                                delaystt = "INACTIVE";
                                            }*/
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                    if (mesg.equals("OK|Duplicate mpesa_code. Queries ph:0707-373794.")) {
                                        Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Duplicate mpesa_code......");
                                    } else if (results.toLowerCase().contains("timed out")) {
                                        Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Already saved in timeout");
                                    } else {
                                        String values = "insert into mpesa(id,orig,tstamp,mpesa_code,mpesa_acc,mpesa_msisdn,mpesa_amt,mpesa_sender,msgid,message,originalmsg,msg_status,source) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                        prep = con.prepareStatement(values);
                                        prep.setString(1, id);
                                        prep.setString(2, orig);
                                        prep.setString(3, tstamp);
                                        prep.setString(4, mpesa_code);
                                        prep.setString(5, orig_mpesa_acc);
                                        prep.setString(6, mpesa_msisdn);
                                        prep.setString(7, mpesa_amt);
                                        prep.setString(8, mpesa_sender);
                                        prep.setString(9, mpesa_code);
                                        prep.setString(10, mesg);
                                        prep.setString(11, results);
                                        prep.setString(12, this.msEObj.getStatus());
                                        prep.setString(13, source);
                                        prep.execute();
                                        prep.close();

                                        Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Message Status:--" + this.msEObj.getStatus());
                                    }
                                    Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Message sent out");
                                } catch (SQLException ex) {
                                    Logger.getLogger(Rogger.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "The VendIT Transaction exists");
                            }
                        } else {
                            Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Exitss in BL");
                        }
                    } else {
                        Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Unauthorised request. Mwizi!!!!");
                    }
                } else {
                    Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Unauthorised null request on verify. Mwizi!!!!");
                }
            } else {
                Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Unauthorised null request on ip. Mwizi!!!!");
            }
        } catch (Exception ex) {
            Logger.getLogger(Rogger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
                Logger.getLogger(Rogger.class.getName()).log(Level.INFO, "Connection closed...");
            } catch (SQLException ex) {
                Logger.getLogger(Rogger.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.close();
        }
        /*response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. 
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Rogger</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Rogger at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }*/
    }

    public boolean check(String msisdn, String trx_code) {
        Connection con2 = this.data.connect();
        try {
            System.out.println(new StringBuilder().append("msisdn").append(msisdn).toString());
            System.out.println(new StringBuilder().append("trx code").append(trx_code).toString());

            String query = new StringBuilder().append("select * from mpesa where  mpesa_code='").append(trx_code).append("'").toString();
            PreparedStatement prep = con2.prepareStatement(query);
            ResultSet rst = prep.executeQuery();

            if (rst.next()) {
                prep.close();
                con2.close();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Rogger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con2.close();
            } catch (SQLException ex) {
            }
        }
        return false;
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
