package com.pdsl.vendIT;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DelayNotifications {

    DataStore data = new DataStore();
    Sdp sdp = new Sdp();
    PhonePrefixes prfx = new PhonePrefixes();

    public void updateNotifaction(String message, String mpesa_code, String meter) {
        Connection con = this.data.connectvendit();
        try {
            Logger.getLogger(DelayNotifications.class.getName()).log(Level.INFO, "Delay Notify msg: " + message + " Code :" + mpesa_code);
            if (message.equals("on")) {
                String query = "select phone, login, notification from user where notification like 'yes' ";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    try {
                        if (meter.length() < 11) {
                            if (prfx.checkPhonePrefix(rs.getString(1)).equals("SAFARICOM")) {
                                this.sdp.sendSMS(rs.getString(1), "There is a postpaid system delay from KPLC", mpesa_code, "704307", "http://197.248.9.1005:8084/VendITNotification/SmsNotificationService");
                            } else if (prfx.checkPhonePrefix(rs.getString(1)).equals("AIRTELRX")) {
                                AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", rs.getString(1), "PdslKeT2", "VENDIT", "0", "There is a system delay from KPLC", "pdsl@999");
                                esdp.submitMessage();
                            } else {
                                EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(rs.getString(1)), "pdsl219", "pdsl219", rs.getString(1), "There is a system delay from KPLC", "VendIT");
                                esdp.submitMessage();
                            }
                        } else {
                            if (prfx.checkPhonePrefix(rs.getString(1)).equals("SAFARICOM")) {
                                sdp.sendSMS(rs.getString(1), "There is a prepaid system delay from KPLC", mpesa_code, "704307", "http://197.248.9.1005:8084/VendITNotification/SmsNotificationService");
                            } else if (prfx.checkPhonePrefix(rs.getString(1)).equals("AIRTELRX")) {
                                AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", rs.getString(1), "PdslKeT2", "VENDIT", "0", "There is a system delay from KPLC", "pdsl@999");
                                esdp.submitMessage();
                            } else {
                                EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(rs.getString(1)), "pdsl219", "pdsl219", rs.getString(1), "There is a system delay from KPLC", "VendIT");
                                esdp.submitMessage();
                            }
                        }
                    } catch (Exception ex) {

                    }
                    Logger.getLogger(DelayNotifications.class.getName()).log(Level.INFO, "Delay msg to :" + rs.getString(1) + " :=:" + rs.getString(2));
                }
            } else {
                String query = "select phone, login, notification from user where notification='yes'";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    try {
                        if (meter.length() < 11) {
                            if (prfx.checkPhonePrefix(rs.getString(1)).equals("SAFARICOM")) {
                                this.sdp.sendSMS(rs.getString(1), "The postpaid delay/timeout is over", mpesa_code, "704307", "http://197.248.9.1005:8084/VendITNotification/SmsNotificationService");
                            } else if (prfx.checkPhonePrefix(rs.getString(1)).equals("AIRTELRX")) {
                                AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", rs.getString(1), "PdslKeT2", "VENDIT", "0", "The delay/timeout is over", "pdsl@999");
                                esdp.submitMessage();
                            } else {
                                EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(rs.getString(1)), "pdsl219", "pdsl219", rs.getString(1), "The delay/timeout is over", "VendIT");
                                esdp.submitMessage();
                            }
                        } else {
                            if (prfx.checkPhonePrefix(rs.getString(1)).equals("SAFARICOM")) {
                                this.sdp.sendSMS(rs.getString(1), "The prepaid delay/timeout is over", mpesa_code, "704307", "http://197.248.9.1005:8084/VendITNotification/SmsNotificationService");
                            } else if (prfx.checkPhonePrefix(rs.getString(1)).equals("AIRTELRX")) {
                                AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", rs.getString(1), "PdslKeT2", "VENDIT", "0", "The delay/timeout is over", "pdsl@999");
                                esdp.submitMessage();
                            } else {
                                EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(rs.getString(1)), "pdsl219", "pdsl219", rs.getString(1), "The delay/timeout is over", "VendIT");
                                esdp.submitMessage();
                            }
                        }
                    } catch (Exception ex) {

                    }
                    Logger.getLogger(DelayNotifications.class.getName()).log(Level.INFO, "delay over msg to :" + rs.getString(1) + " :=:" + rs.getString(2));
                }
                con.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(DelayNotifications.class.getName()).log(Level.SEVERE, "Delay notify err: " + ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DelayNotifications.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getStatus() {

        String stt = "";
        try {
            FileReader reader = new FileReader("/home/julius/delaystatus.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            stt = bufferedReader.readLine();
            reader.close();

        } catch (IOException e) {
            //setStatus("off");
            e.printStackTrace();
        }
        return stt;
    }

    public void setStatus(String stt) {
        try {
            FileWriter writer = new FileWriter("/home/julius/delaystatus.txt");
            writer.write(stt);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] arg) {
        DelayNotifications dn = new DelayNotifications();
        //dn.updateNotifaction("off", "ETYEY22234", "56-565");
        dn.setStatus("poff");
        System.out.println(dn.getStatus());
    }
}
