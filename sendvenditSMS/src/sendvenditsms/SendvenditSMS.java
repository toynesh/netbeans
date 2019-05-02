/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendvenditsms;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import metro_sdp.Metro_sdp;

/**
 *
 * @author coolie
 */
public class SendvenditSMS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            DataStore data = new DataStore();
            Metro_sdp sdp = new Metro_sdp();
            Connection con = data.connect();
            String query = "SELECT mpesa_msisdn,mpesa_code,message FROM (SELECT * FROM mpesa WHERE timestamp > '2017-09-27 00:00:00' AND dlry_status IS NULL) AS T WHERE message not like '%OK|MTRFE010: KPLC Specified transaction already processed. Queries ph:0709711000.%'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String mpesa_msisdn= rs.getString(1);
                String mpesa_code= rs.getString(2);
                String mesg= rs.getString(3);
                System.out.println("Sending to:"+mpesa_msisdn +"=="+mesg);
                sdp.sendSdpSms(mpesa_msisdn, mesg, mpesa_code, "704307", "http://197.248.61.166:8086/VendITNotification/SmsNotificationService");
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(SendvenditSMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
