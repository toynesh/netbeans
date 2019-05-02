/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vendIT;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author coolie
 */
public class SendSafMSG {

    public void sendSafmsg(String mpesa_msisdn, String mesg, String mpesa_code) {
        //Thread.sleep(100);
        DataStore data = new DataStore();
        long startTimeSMS = System.nanoTime();
        Connection con = data.connect();
        try {
            Sdp sdp = new Sdp();
            String sdpres = sdp.sendSMS(mpesa_msisdn, mesg, mpesa_code, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
            Logger.getLogger(SendSafMSG.class.getName()).log(Level.INFO, ">>SDP RESPONSE!!<<" + sdpres);
            if (null != sdpres) {
                try {
                    String values = "insert into sentmsgid(mpesacode,msgid) values (?,?)";
                    PreparedStatement prep = con.prepareStatement(values);
                    prep.setString(1, mpesa_code);
                    prep.setString(2, sdpres);
                    prep.execute();
                    prep.close();
                    System.out.println("SAVED MSGID");
                } catch (SQLException ex) {
                    Logger.getLogger(SendSafMSG.class.getName()).log(Level.SEVERE, "Error Saving sdptimeout" + ex);
                }
            } else {
                try {
                    String values = "insert into sdptimeout(mpesa_code) values (?)";
                    PreparedStatement prep = con.prepareStatement(values);
                    prep.setString(1, mpesa_code);
                    prep.execute();
                    prep.close();
                    con.close();
                    System.out.println("SAVED INTO SDP TIMEOUT");
                } catch (SQLException ex) {
                    Logger.getLogger(SendSafMSG.class.getName()).log(Level.SEVERE, "Error Saving sdptimeout" + ex);
                }
            }
        } catch (Exception xx) {
            Logger.getLogger(SendSafMSG.class.getName()).log(Level.SEVERE, ">>SDP TIMEOUT!!<<" + xx);
            try {
                String values = "insert into sdptimeout(mpesa_code) values (?)";
                PreparedStatement prep = con.prepareStatement(values);
                prep.setString(1, mpesa_code);
                prep.execute();
                prep.close();
                System.out.println("SAVED INTO SDP TIMEOUT");
            } catch (SQLException ex) {
                Logger.getLogger(SendSafMSG.class.getName()).log(Level.SEVERE, "Error Saving sdptimeout" + ex);
            }
        } finally {
            try {
                con.close();
            } catch (SQLException cex) {
            }
        }
        long endTimeSMS = System.nanoTime();
        long durationSMS = (endTimeSMS - startTimeSMS);
        System.out.println(mpesa_msisdn + "SAF SMS TOOK:" + durationSMS / 1000000 / 1000 + " seconds");
    }

    public static void main(String[] args) {
        SendSafMSG ms = new SendSafMSG();
        ms.sendSafmsg("254728064120", "implementing", "testcorr");
        //Metro_sdp sdp = new Metro_sdp();
        //String sdpres = sdp.sendSdpSms("254728064120", "TESTING SDP RES2", "WWRERTEREWEW2", "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
        //Logger.getLogger(SendSafMSG.class.getName()).log(Level.INFO, ">>SDP RESPONSE!!<<" + sdpres);
    }

}
/*
Exception: No suitable driver found for jdbc:mysql://localhost/mobile_wallet
Exception in thread "Thread-151253" java.lang.NullPointerException
	at com.pdsl.vendIT.SendSafMSG.sendSafmsg(SendSafMSG.java:33)
	at com.pdsl.vendIT.Render$2.run(Render.java:276)
	at java.lang.Thread.run(Thread.java:748)


 */
