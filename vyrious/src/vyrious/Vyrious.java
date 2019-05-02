/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vyrious;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author coolie
 */
public class Vyrious {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {

        // TODO code application logic here
        Sdp sdp = new Sdp();

        //send open report messages
        /*try {
            File f = new File("/opt/applications/upload/senddata.xls");
            Workbook wb = Workbook.getWorkbook(f);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            int col = s.getColumns();
            for (int j = 1; j < row; j++) {
            Cell dt = s.getCell(1, j);
            Cell phone = s.getCell(2, j);
            Cell account = s.getCell(3, j);
            Cell amount = s.getCell(4, j);
            Cell code = s.getCell(8, j);
            Cell token = s.getCell(9, j);
            
            String dtm = dt.getContents();
            String msisdn = phone.getContents();
            String meter = account.getContents();
            String amt = amount.getContents();
            String ref = code.getContents();
            String tkn = token.getContents();
            String mesg = "KPLC Mtr No:" + meter + " Token:" + tkn + " Amount:Ksh:" + amt + " Date:" + dtm;
            
            if (!tkn.isEmpty()) {
            System.out.println(msisdn+"="+mesg);
            String res = sdp.sendSMS(msisdn, mesg, ref, "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
            System.out.println("SDP Resp:" + res);
            }
            }
            } catch (IOException ex) {
            Logger.getLogger(Vyrious.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BiffException ex) {
            Logger.getLogger(Vyrious.class.getName()).log(Level.SEVERE, null, ex);
            }
        try {
            DataStore data = new DataStore();
            Connection con = data.connect();
            String query = "SELECT timestamp,mpesa_code,mpesa_acc,mpesa_msisdn,mpesa_amt,mpesa_sender FROM mpesa12 WHERE mpesa_amt>0 AND mpesa_code NOT LIKE 'EQU_%' AND mpesa_code NOT LIKE '%COMB%'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String query2 = "SELECT mpesa_code FROM mpesa WHERE mpesa_code LIKE '%"+rs.getString(2)+"%'";
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                if (!rs.isBeforeFirst()) {
                     System.out.println("PENDING>> "+rs.getString(2) +" <<PHONE "+rs.getString(4));
                }

                //String res = data.sendPayment(datee, mpesa_code, account, msisdn, amount, mpesa_sender);
                //EquitelSMS esdp = new EquitelSMS("197.248.61.165", "EQUITELRX", "pdsl219", "pdsl219", rs.getString(2), rs.getString(3), "VendIT");
                //esdp.submitMessage();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Vyrious.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        //Brewbistrolllllllll
        //sdp.sendSdpSms("254728064120", "Hi Julius", "testbrew", "704309", "http://197.248.161.1466:8085/VendITNotification/SmsNotificationService");
       // String res = sdp.sendSMS("254728064120", "Kendrick", "vendIT", "704307", "http://197.248.9.105:8084/VendITNotification/SmsNotificationService");
        //System.out.println("Resp:" + res);
//APEXSTEEL
        //sdp.sendSdpSms("254728064120", "Hi Julius", "testAPEX", "706815", "http://197.248.161.1466:8085/VendITNotification/SmsNotificationService");
//KNEC
        sdp.sendSMS("254728064120", "Hi Julius", "testKNE4C", "20078", "http://197.248.9.101:8084/knecsdpdlry/SmsNotificationService");

    }
}
