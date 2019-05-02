/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendsecurityerror;

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
public class Sendsecurityerror {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here7
        DataStore data = new DataStore();
        try {
            for (;;) {
                Connection con = data.connect();
                List<List<String>> varray = new ArrayList<>();  // List of list, one per row
                List<String> outboundmsisdn = new ArrayList<>();
                
                String query = "SELECT `msisdn` FROM (SELECT * FROM `outbound` WHERE `time_out` >'2018-04-12 00:00:00' AND `retry`=0) AS D WHERE `message` LIKE '%Token generation failed due to a security module%' OR `message` IS NULL  ORDER BY `id` DESC;";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    System.out.println("Saving: " + rs.getString(1));
                    outboundmsisdn.add(rs.getString(1));
                }
                
                String query2 = "SELECT `mpesa_msisdn`,`mpesa_code`,`mpesa_acc`,`mpesa_sender`,`timestamp` FROM `mpesa` WHERE `timestamp`>'2018-04-14 00:00:00' ORDER BY  `timestamp` ASC;";
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                ResultSetMetaData metadata = rs2.getMetaData();
                int numcols = metadata.getColumnCount();
                while (rs2.next()) {
                    System.out.println("Saving Varr: " + rs2.getString(1) + " || " + rs2.getString(2));
                    List<String> row = new ArrayList<>(numcols); // new list per row
                    int i = 1;
                    while (i <= numcols) {  // don't skip the last column, use <=
                        row.add(rs2.getString(i++));
                    }
                    varray.add(row); // add it to the result
                }
                String msisdn = null;
                String mpesa_code = null;
                String account = null;
                String mpesa_sender = null;
                String tmsp = null;
                
                for (int u = 0; u < varray.size(); u++) {
                    msisdn = varray.get(u).get(0);
                    mpesa_code = varray.get(u).get(1);
                    account = varray.get(u).get(2);
                    mpesa_sender = varray.get(u).get(3);
                    tmsp = varray.get(u).get(4);
                    if (outboundmsisdn.contains(msisdn)) {
                        System.out.println("Processing :=" + mpesa_code + " " + account + " " + mpesa_sender + " " + tmsp);
                        String res = data.sendPayment(tmsp, mpesa_code + "jk", account, msisdn, "0", mpesa_sender);
                        System.out.println("Payments Response With Zero Amount:" + res);
                        
                        PreparedStatement prep = null;
                        String update = "update outbound set retry = ? where msisdn = ? ";
                        prep = con.prepareStatement(update);
                        prep.setString(1, "1");
                        prep.setString(2, msisdn);
                        prep.executeUpdate();
                        prep.close();
                        System.out.println("Updated outbound table");
                    }
                }
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Sendsecurityerror.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
