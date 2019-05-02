/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tmt;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author juliuskun
 */
public class Tmt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DataStore data = new DataStore();
        try {
            for (;;) {
                Connection con = data.connect();
                DateTime dt = new DateTime();
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                String datee = fmt.print(dt);
                String query = "SELECT id_timeout, msisdn,amount,account,mpesa_code,mpesa_sender FROM timeout where  id_timeout >=2433 and status=0 order by id_timeout desc";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    String rid = rs.getString(1);
                    String msisdn = rs.getString(2);
                    String amount = rs.getString(3);
                    String account = rs.getString(4);
                    String mpesa_code = rs.getString(5);
                    String mpesa_sender = rs.getString(6);
                    System.out.println("Checking if unprocessed:-" + msisdn + "|" + amount + "|" + account + "|" + mpesa_code + "|" + mpesa_sender);
                    String res = data.sendPayment(datee, mpesa_code, account, msisdn, amount, mpesa_sender);
                    System.out.println("Payments Response With Amount:" + res);
                    String res2 = data.sendPayment(datee, mpesa_code + "tmt", account, msisdn, "0", mpesa_sender);
                    System.out.println("Payments Response With Zero Amount:" + res2);

                    PreparedStatement prep = null;
                    String update = "update timeout set status = ? where id_timeout = ? ";
                    prep = con.prepareStatement(update);
                    prep.setString(1, "1");
                    prep.setString(2, rid);
                    prep.executeUpdate();
                    prep.close();
                }

                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tmt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
