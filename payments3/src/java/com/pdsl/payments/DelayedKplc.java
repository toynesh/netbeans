package com.pdsl.payments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DelayedKplc {

    Sdp sdp = new Sdp();
    DataStore data = new DataStore();
    MessageEditor msEObj = new MessageEditor();
    List<String> safprefix = new ArrayList<>();
    PreparedStatement prep = null;

    String mesg = null;
    DateTime dt = new DateTime();
    org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
    String datee = fmt.print(dt);
    PhonePrefixes prfx = new PhonePrefixes();

    public void push(String mpesa_msisdn, String results, String msgid) {
        Connection con = null;
        con = this.data.connect();
        results = results.replaceAll("ÖrÖn", "");
        System.out.println("AFTERDELAYCLOUDMSG: " + results);
        try {
            if (!results.contains("Token generation failed due to a security module")) {
                if (results.startsWith("FAIL")) {
                    try {
                        mesg = this.msEObj.editToken(results, "delayed", mpesa_msisdn, msgid, datee);
                        String msisdn = mpesa_msisdn.replace("+", "");
                        if (prfx.checkPhonePrefix(msisdn).equals("SAFARICOM")) {
                            this.sdp.sendSMS(mpesa_msisdn, mesg, msgid, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                        } else if (prfx.checkPhonePrefix(msisdn).equals("AIRTELRX")) {
                            AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", msisdn, "PdslKeT2", "VENDIT", "0", mesg, "pdsl@999");
                            esdp.submitMessage();
                        } else {
                            EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(msisdn), "pdsl219", "pdsl219", mpesa_msisdn, mesg, "VendIT");
                            esdp.submitMessage();
                        }
                    } catch (Exception ex) {
                    }
                } else if (results.startsWith("OK")) {
                    try {
                        mesg = this.msEObj.editToken(results, "delayed", mpesa_msisdn, msgid, datee);
                        String msisdn = mpesa_msisdn.replace("+", "");
                        if (prfx.checkPhonePrefix(msisdn).equals("SAFARICOM")) {
                            this.sdp.sendSMS(mpesa_msisdn, mesg, msgid, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                        } else if (prfx.checkPhonePrefix(msisdn).equals("AIRTELRX")) {
                            AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", msisdn, "PdslKeT2", "VENDIT", "0", mesg, "pdsl@999");
                            esdp.submitMessage();
                        } else {
                            EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(msisdn), "pdsl219", "pdsl219", mpesa_msisdn, mesg, "VendIT");
                            esdp.submitMessage();
                        }
                    } catch (Exception ex) {
                    }
                } else {
                    mesg = this.msEObj.editToken(results, "delayed", mpesa_msisdn, msgid, datee);
                    String msisdn = mpesa_msisdn.replace("+", "");
                    if (prfx.checkPhonePrefix(msisdn).equals("SAFARICOM")) {
                        this.sdp.sendSMS(mpesa_msisdn, mesg, msgid, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                    } else if (prfx.checkPhonePrefix(msisdn).equals("AIRTELRX")) {
                        AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", msisdn, "PdslKeT2", "VENDIT", "0", mesg, "pdsl@999");
                        esdp.submitMessage();
                    } else {
                        EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(msisdn), "pdsl219", "pdsl219", mpesa_msisdn, mesg, "VendIT");
                        esdp.submitMessage();
                    }
                }
            }
            String insert2 = "insert into outbound (msisdn,message,mpesa_code) values (?,?,?)";
            this.prep = con.prepareStatement(insert2);
            this.prep.setString(1, mpesa_msisdn);
            this.prep.setString(2, mesg);
            this.prep.setString(3, msgid);
            this.prep.execute();
            this.prep.close();

            Logger.getLogger(DelayedKplc.class.getName()).log(Level.INFO, "Message sent out");
        } catch (SQLException ex) {
            Logger.getLogger(DelayedKplc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
                Logger.getLogger(DelayedKplc.class.getName()).log(Level.INFO, "Connection closed on delayed KPLC...");
            } catch (SQLException ex) {
            }
        }
    }

    public static void main(String[] args) {
        DelayedKplc delayed = new DelayedKplc();
        delayed.push("254728064120", "OK| POWER IS brak ON", "RINGME");
    }

}
