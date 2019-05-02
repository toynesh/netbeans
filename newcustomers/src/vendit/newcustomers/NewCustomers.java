/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendit.newcustomers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juliuskun
 */
public class NewCustomers {

    public static void main(String[] args) {

        DataStore data = new DataStore();
        NewCustomers customer = new NewCustomers();

        List<List<String>> msisdn = new ArrayList<>();  // List of list, one per row
        List<String> allmsisdn = new ArrayList<>();
        try {
            System.out.println("Preparing Report Table");
            Connection con = data.connect();
            Statement statement = con.createStatement();
            statement.executeUpdate("TRUNCATE reportmpesa");
            con.close();
            String insert = "INSERT IGNORE INTO `reportmpesa`(`id`, `orig`, `dest`, `tstamp`,"
                    + " `text`, `user`, `pass`, `mpesa_code`, `mpesa_acc`, `mpesa_msisdn`,"
                    + " `mpesa_trx_date`, `mpesa_trx_time`, `mpesa_amt`, `mpesa_sender`, `timestamp`,"
                    + " `status`, `msgid`, `message`, `retry`, `msg_status`, `originalmsg`,"
                    + " `delaymessage`, `originaldelaymessage`, `source`, `dlry_status`) SELECT `id`,"
                    + " `orig`, `dest`, `tstamp`, `text`, `user`, `pass`, `mpesa_code`, `mpesa_acc`,"
                    + " `mpesa_msisdn`, `mpesa_trx_date`, `mpesa_trx_time`, `mpesa_amt`, `mpesa_sender`,"
                    + " `timestamp`, `status`, `msgid`, `message`, `retry`, `msg_status`, `originalmsg`,"
                    + " `delaymessage`, `originaldelaymessage`, `source`, `dlry_status` FROM `mpesa201707`";
            String insert2 = "INSERT IGNORE INTO `reportmpesa`(`id`, `orig`, `dest`, `tstamp`, `text`,"
                    + " `user`, `pass`, `mpesa_code`, `mpesa_acc`, `mpesa_msisdn`, `mpesa_trx_date`,"
                    + " `mpesa_trx_time`, `mpesa_amt`, `mpesa_sender`, `timestamp`, `status`, `msgid`,"
                    + " `message`, `retry`, `msg_status`, `originalmsg`, `delaymessage`,"
                    + " `originaldelaymessage`, `source`, `dlry_status`) SELECT `id`, `orig`,"
                    + " `dest`, `tstamp`, `text`, `user`, `pass`, `mpesa_code`, `mpesa_acc`,"
                    + " `mpesa_msisdn`, `mpesa_trx_date`, `mpesa_trx_time`, `mpesa_amt`, `mpesa_sender`,"
                    + " `timestamp`, `status`, `msgid`, `message`, `retry`, `msg_status`, `originalmsg`,"
                    + " `delaymessage`, `originaldelaymessage`, `source`, `dlry_status` FROM `mpesa` WHERE `timestamp`< '2017-10-01 00:00:00'";
            data.insert(insert);
            data.insert(insert2);
            System.out.println("Report Table Created!!");
        } catch (SQLException ex) {
            Logger.getLogger(NewCustomers.class.getName()).log(Level.SEVERE, "ReportTable" + ex);
        }
        try {
            Connection con = data.connect();
            String query = "SELECT DISTINCT mpesa_msisdn,mpesa_sender,mpesa_acc,timestamp FROM reportmpesa WHERE timestamp >= '2017-09-16 00:00:00' ORDER BY timestamp";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numcols = metadata.getColumnCount();
            while (rs.next()) {
                System.out.println("Saving: " + rs.getString(1));
                List<String> row = new ArrayList<>(numcols); // new list per row
                int i = 1;
                while (i <= numcols) {  // don't skip the last column, use <=
                    row.add(rs.getString(i++));
                }
                msisdn.add(row); // add it to the result
            }
            /*for (int y = 0; y < msisdn.size(); y++) {
                String phone=null;
                String cname=null;
                    phone=msisdn.get(y).get(0);
                    cname=msisdn.get(y).get(1);
                System.out.println("Phone In a list :=" + phone + cname);
            }*/
            System.out.println("Search Array Size: " + msisdn.size());
            String query2 = "SELECT DISTINCT mpesa_msisdn,timestamp FROM reportmpesa WHERE timestamp < '2017-09-16 00:00:00'";
            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery(query2);
            while (rs2.next()) {
                System.out.println("Saving All: " + rs2.getString(1));
                allmsisdn.add(rs2.getString(1));
            }
            System.out.println("Search Array Size: " + allmsisdn.size());
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(NewCustomers.class.getName()).log(Level.SEVERE, "Querrydatta" + ex);
        }
        System.out.println("Array Size: " + msisdn.size());
        try {
            Connection con = data.connect();
            Statement statement = con.createStatement();
            statement.executeUpdate("TRUNCATE newcustomers");
            con.close();
            String phone = null;
            String customername = null;
            String meter = null;
            String firstdate = null;
            for (int y = 0; y < msisdn.size(); y++) {
                phone = msisdn.get(y).get(0);
                customername = msisdn.get(y).get(1);
                meter = msisdn.get(y).get(2);
                firstdate = msisdn.get(y).get(3);
                System.out.println("Checking :=" + phone);
                if (!allmsisdn.contains(phone)) {
                    System.out.println("New Customer:=" + phone + " Name:=" + customername + " Meter:=" + meter + " Time:=" + firstdate);
                    String insert = "INSERT INTO `mobile_wallet`.`newcustomers` ( `msisdn`, `customer`, `meter`, `firstdate`) VALUES ( '" + phone + "','" + customername + "','" + meter + "','" + firstdate + "')";
                    data.insert(insert);
                } else {
                    System.out.println("Old Customer");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(NewCustomers.class.getName()).log(Level.SEVERE, "Querrydatta" + ex);
        }
    }
}
