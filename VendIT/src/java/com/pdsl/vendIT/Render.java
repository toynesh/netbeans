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
import java.util.ArrayList;
import java.util.List;
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
public class Render extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     * http://localhost:8080/VendIT/Render?id=3630&orig=MPESA&tstamp=20170801150817&mpesa_code=MA10Z61PCG&mpesa_acc=14140057598&mpesa_msisdn=254728064120&mpesa_amt=1&mpesa_sender=JULIUS
     * KARIUKI&mpesa_verify=onicha
     */
    public Render() {
        callMpesaArray();
        callBlacklistArray();
    }
    DataStore data = new DataStore();
    SendEmail mailing = new SendEmail();
    MessageEditor msEObj = new MessageEditor();
    DelayNotifications statusobj = new DelayNotifications();
    List<String> mpesacodes = new ArrayList<>();
    List<String> blacklist = new ArrayList<>();
    String dnotfy = "INACTIVE";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection con = null;
        con = this.data.connect();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
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
            Logger.getLogger(Render.class.getName()).log(Level.INFO, remote);
            Logger.getLogger(Render.class.getName()).log(Level.INFO, "Source:" + source);
            String mpesa_msisdn = request.getParameter("mpesa_msisdn");
            String mpesa_sender = request.getParameter("mpesa_sender");
            if ((!mpesa_msisdn.equals("NONE")) && (!mpesa_msisdn.equals("522843")) && (!mpesa_sender.contains("KCB MYKAS")) && (!mpesa_sender.contains("Business by"))) {
                if ((remote.equals("172.27.116.21")) || (remote.equals("172.27.116.25")) || (remote.equals("197.248.31.218")) || (remote.equals("172.27.103.12"))) {
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
                            String mpesa_amt = request.getParameter("mpesa_amt");
                            String deliverystatus = "SmsDeliveryWaiting";
                            if (!blacklist.contains(mpesa_acc)) {
                                //if (!check(mpesa_msisdn, mpesa_code)) {
                                if (!mpesacodes.contains(mpesa_code)) {
                                    try {
                                        //send mail
                                        /*if(Double.parseDouble(mpesa_amt)>=20000.00){
                                            mailing.sendSimpleMail("accounts@pdslkenya.com","Phone:"+mpesa_msisdn+" Meter: "+mpesa_acc+" Amount: "+mpesa_amt);
                                            mailing.sendSimpleMail("magondu@pdslkenya.com","Phone:"+mpesa_msisdn+" Meter: "+mpesa_acc+" Amount: "+mpesa_amt);
                                        }*/
                                        //Sending to Ipay
                                        long startTime = System.nanoTime();
                                        results = new Ipay().sendRequest(tstamp, mpesa_msisdn, mpesa_code, mpesa_sender, mpesa_amt, tstamp, mpesa_acc);
                                        ///////do the thing////////////
                                        long endTime = System.nanoTime();
                                        long duration = (endTime - startTime);
                                        System.out.println("Ipay Response Took " + duration / 1000000 / 1000 + " seconds");

                                        Logger.getLogger(Render.class.getName()).log(Level.INFO, "Results:-" + results);
                                        results = results.replace("$", "KES");
                                        results = results.replaceAll("FAIL", "OK");
                                        String[] reslt = results.split("|");
                                        DateTime dt = new DateTime();
                                        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
                                        String datee = fmt.print(dt);
                                        mesg = this.msEObj.editToken(results, mpesa_acc, mpesa_msisdn, mpesa_code, datee);
                                        if (results.startsWith("OK|An error occurred.")) {
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
                                            Logger.getLogger(Render.class.getName()).log(Level.INFO, "An Error occurred from IPAY");
                                        } else {
                                            System.out.println("Adding to Array code>> : " + mpesa_code);
                                            mpesacodes.add(mpesa_code);
                                            System.out.println("Codes Array Size after Current Add: " + mpesacodes.size());
                                            if (results.toLowerCase().contains("timed out")) {
                                                mesg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction";
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
                                                /*if (this.dnotfy.equals("INACTIVE")) {
                                                    try {
                                                        String prevStatus = this.statusobj.getStatus();
                                                        if ((mpesa_acc.length() < 11) && (prevStatus.equals("on"))) {
                                                            this.statusobj.setStatus("poff");
                                                            this.statusobj.updateNotifaction("on", mpesa_code, mpesa_acc);
                                                        }
                                                        if ((mpesa_acc.length() >= 11) && (prevStatus.equals("on"))) {
                                                            this.statusobj.setStatus("off");
                                                            this.statusobj.updateNotifaction("on", mpesa_code, mpesa_acc);
                                                        }
                                                        dnotfy = "ACTIVE";
                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }*/
                                            }
                                            if (results.toLowerCase().contains("system is busy")) {
                                                mesg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction";
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
                                            }
                                            if (results.toLowerCase().contains("unhandled program error has occured")) {
                                                mesg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction";
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
                                            }
                                            if (results.toLowerCase().contains("stored procedure TRN_ROOT_INSERT:")) {
                                                mesg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction";
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
                                            }
                                            if (results.toLowerCase().contains("TRN_ROOT")) {
                                                mesg = "Dear Customer,We are experiencing KPLC delays. Once the system is up we will resend your transaction";
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
                                            }
                                            if (results.startsWith("FAIL")) {
                                                try {
                                                    mesg = results.substring(5);
                                                    this.msEObj.setStatus("error");
                                                } catch (Exception ex) {
                                                    //this.send.sendMes(mpesa_msisdn, results.substring(5));
                                                    mesg = results.substring(5);
                                                    this.msEObj.setStatus("error");
                                                }
                                            }
                                            /*if (results.toLowerCase().contains("system delayed")) {
                                                if (this.dnotfy.equals("INACTIVE")) {
                                                    try {
                                                        String prevStatus = this.statusobj.getStatus();
                                                        if ((mpesa_acc.length() < 11) && (prevStatus.equals("on"))) {
                                                            this.statusobj.setStatus("poff");
                                                            this.statusobj.updateNotifaction("on", mpesa_code, mpesa_acc);
                                                        }
                                                        if ((mpesa_acc.length() >= 11) && (prevStatus.equals("on"))) {
                                                            this.statusobj.setStatus("off");
                                                            this.statusobj.updateNotifaction("on", mpesa_code, mpesa_acc);
                                                        }
                                                        dnotfy = "ACTIVE";
                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }
                                            }*/
                                            if (mesg.contains("PAID") || mesg.contains("KPLC Mtr No")) {
                                                //end of delay
                                                if (this.dnotfy.equals("ACTIVE")) {
                                                    try {
                                                        String prevStatus = this.statusobj.getStatus();
                                                        if ((mpesa_acc.length() < 11) && (mesg.contains("PAID"))) {
                                                            if (prevStatus.equals("poff")) {
                                                                this.statusobj.setStatus("on");
                                                                this.statusobj.updateNotifaction("off", mpesa_code, mpesa_acc);
                                                            }
                                                        }
                                                        if ((mpesa_acc.length() >= 11) && mesg.contains("KPLC Mtr No")) {
                                                            if (prevStatus.equals("off")) {
                                                                this.statusobj.setStatus("on");
                                                                this.statusobj.updateNotifaction("off", mpesa_code, mpesa_acc);
                                                            }
                                                        }
                                                        this.dnotfy = "INACTIVE";
                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }
                                            }
                                            Logger.getLogger(Render.class.getName()).log(Level.INFO, "DELAYS: <<" + dnotfy + ">>");

                                            //Send sms  
                                            long startTimeSMS = System.nanoTime();
                                            Logger.getLogger(Render.class.getName()).log(Level.INFO, "Phn:" + mpesa_msisdn + " NEW MESSAGE:" + mesg);
                                            try {
                                                //mesg = this.msEObj.editToken(results, mpesa_acc, mpesa_msisdn, mpesa_code, datee);
                                                if (mesg.equals("OK|MTRFE010: KPLC Specified transaction already processed. Queries ph:0709711000.")) {
                                                    deliverystatus = "SmsNotSent";
                                                    Logger.getLogger(Render.class.getName()).log(Level.INFO, "Message not sent out to customer on no FBE");
                                                } else if (mesg.equals("OK|Duplicate mpesa_code. Queries ph:0707-373794.")) {
                                                    Logger.getLogger(Render.class.getName()).log(Level.INFO, "Duplicate mpesa_code");
                                                } else {
                                                    //SendSafMSG sendsaf = new SendSafMSG();
                                                    //sendsaf.sendSafmsg(mpesa_msisdn, mesg, mpesa_code);
                                                    final String omsg = mesg;
                                                    new Thread(new Runnable() {
                                                        public void run() {
                                                            SendSafMSG sendsaf = new SendSafMSG();
                                                            sendsaf.sendSafmsg(mpesa_msisdn, omsg, mpesa_code);
                                                        }
                                                    }).start();
                                                }
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            long endTimeSMS = System.nanoTime();
                                            long durationSMS = (endTimeSMS - startTimeSMS);
                                            System.out.println("SEND SMS Took " + durationSMS / 1000000 / 1000 + " seconds");

                                            //save to db
                                            out.println(mesg);
                                            if (mesg.equals("OK|Duplicate mpesa_code. Queries ph:0707-373794.")) {
                                                Logger.getLogger(Render.class.getName()).log(Level.INFO, "Duplicate mpesa_code......");
                                                deliverystatus = "SmsNotSent";
                                            } else if (results.toLowerCase().contains("timed out")) {
                                                Logger.getLogger(Render.class.getName()).log(Level.INFO, "Already saved in timeout");
                                            } else {
                                                long startTimeDBS = System.nanoTime();
                                                String values = "insert into mpesa(id,orig,tstamp,mpesa_code,mpesa_acc,mpesa_msisdn,mpesa_amt,mpesa_sender,msgid,message,originalmsg,msg_status,source,dlry_status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                                                prep.setString(14, deliverystatus);
                                                prep.execute();
                                                prep.close();
                                                long endTimeDBS = System.nanoTime();
                                                long durationDBS = (endTimeDBS - startTimeDBS);
                                                System.out.println("SAVE TO MPESA Took " + durationDBS / 1000000 / 1000 + " seconds");

                                                Logger.getLogger(Render.class.getName()).log(Level.INFO, "Message Status:--" + this.msEObj.getStatus());
                                            }

                                            Logger.getLogger(Render.class.getName()).log(Level.INFO, "Message sent out");
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Render.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    System.out.println("Array code Exists>> : " + mpesa_code);
                                    Logger.getLogger(Render.class.getName()).log(Level.INFO, "The VendIT Transaction exists");
                                }
                            } else {
                                Logger.getLogger(Render.class.getName()).log(Level.INFO, "Exitss in BL");
                            }
                        } else {
                            Logger.getLogger(Render.class.getName()).log(Level.INFO, "Unauthorised request. Mwizi!!!!");
                        }
                    } else {
                        Logger.getLogger(Render.class.getName()).log(Level.INFO, "Unauthorised null request on verify. Mwizi!!!!");
                    }
                } else {
                    Logger.getLogger(Render.class.getName()).log(Level.INFO, "Unauthorised null request on ip. Mwizi!!!!");
                }
            } else {
                Logger.getLogger(Render.class.getName()).log(Level.INFO, ">>>>>>>>>>>>>>>>>>>>>>>>.KCB MYKAS MSISDN IS NONE jaja");
            }
        } catch (Exception ex) {
            Logger.getLogger(Render.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
                Logger.getLogger(Render.class.getName()).log(Level.INFO, "Connection closed...");
            } catch (SQLException ex) {
                Logger.getLogger(Render.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.close();
        }

    }

    public boolean check(String trx_code) {
        try {
            Connection con = this.data.connect();
            String query = "SELECT distinct 1 mpesa_code FROM mpesa WHERE mpesa_code = '" + trx_code + "'";
            //System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (rs.isBeforeFirst()) {
                System.out.println("<<MPESA CODE FOUND: " + trx_code + ">>");
                return true;
            } else {
                System.out.println("<<NEW MPESA CODE>>");
            }
            con.close();
        } catch (SQLException ex) {
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void callMpesaArray() {
        if (mpesacodes.size() == 0) {
            try {
                Connection con = data.connect();
                String query = "SELECT mpesa_code FROM mpesa";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    System.out.println("Saving: " + rs.getString(1));
                    mpesacodes.add(rs.getString(1));
                }
                System.out.println("Codes Array Size: " + mpesacodes.size());
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ArrayCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Codes Array Already Created: " + mpesacodes.size());
        }
    }

    public void callBlacklistArray() {
        if (blacklist.size() == 0) {
            try {
                Connection con = data.connect();
                String query = "SELECT meter FROM mpesa2017";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    System.out.println("SavingBl: " + rs.getString(1));
                    blacklist.add(rs.getString(1));
                }
                System.out.println("BL Size: " + blacklist.size());
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ArrayCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Codes Array Already Created: " + mpesacodes.size());
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
