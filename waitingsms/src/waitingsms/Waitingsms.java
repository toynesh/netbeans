/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package waitingsms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author coolie
 */
public class Waitingsms {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DataStore data = new DataStore();
        Sdp sdp = new Sdp();
        List<List<String>> sdptimeout = new ArrayList<>();
        List<List<String>> mpesa = new ArrayList<>();
        List<String> mcodes = new ArrayList<>();
        Connection con = data.connect();
        try {
            String query = "SELECT id,mpesa_code,flag FROM sdptimeout WHERE flag=0";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                System.out.println("Saving: " + rs.getString(2));
                List<String> row = new ArrayList<>(numcols); // new list per row
                int i = 1;
                while (i <= numcols) {  // don't skip the last column, use <=
                    row.add(rs.getString(i++));
                }
                sdptimeout.add(row); // add it to the result
            }
        } catch (SQLException ex) {
            Logger.getLogger(Waitingsms.class.getName()).log(Level.SEVERE, null, ex);
        }
        String query = "SELECT `mpesa_msisdn`,`mpesa_code`,`message`,`dlry_status` FROM `mpesa` WHERE `dlry_status`='SmsDeliveryWaiting' OR dlry_status='SDPTIMEOUT'";
        try {
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                mcodes.add(rs.getString(2));
                List<String> row = new ArrayList<>(numcols); // new list per row
                int i = 1;
                while (i <= numcols) {  // don't skip the last column, use <=
                    row.add(rs.getString(i++));
                }
                mpesa.add(row); // add it to the result                
            }
            for (int ta = 0; ta < sdptimeout.size(); ta++) {
                if (mcodes.contains(sdptimeout.get(ta).get(1))) {
                    for (int ma = 0; ma < mpesa.size(); ma++) {
                        if (sdptimeout.get(ta).get(1).equals(mpesa.get(ma).get(1))) {
                            try {
                                System.out.println("Sending to: " + mpesa.get(ma).get(0) + " Message: " + mpesa.get(ma).get(2));
                                String sdpres=sdp.sendSMS(mpesa.get(ma).get(0), mpesa.get(ma).get(2), mpesa.get(ma).get(1), "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
                                try {
                                    String values = "insert into sentmsgid(mpesacode,msgid) values (?,?)";
                                    PreparedStatement prep = con.prepareStatement(values);
                                    prep.setString(1, mpesa.get(ma).get(1));
                                    prep.setString(2, sdpres);
                                    prep.execute();
                                    prep.close();
                                    System.out.println("SAVED MSGID");
                                } catch (SQLException ex) {
                                    //Logger.getLogger(SendSafMSG.class.getName()).log(Level.SEVERE, "Error Saving sdptimeout" + ex);
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                String update = "update sdptimeout set flag = ? WHERE id = ?";
                PreparedStatement preparedStmt = con.prepareStatement(update);
                preparedStmt.setInt(1, 1);
                preparedStmt.setString(2, sdptimeout.get(ta).get(0));
                preparedStmt.executeUpdate();
                System.out.println("Updated sdptimeout");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Waitingsms.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
            }
        }
    }

}
