/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updatevdlry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 3627907 3608385
 *
 * @author coolie
 */
public class Updatevdlry {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        for (;;) {
            DataStore data = new DataStore();
            List<List<String>> dlrcor = new ArrayList<>();
            List<List<String>> msgids = new ArrayList<>();
            List<String> vcor = new ArrayList<>();
            List<String> msgsent = new ArrayList<>();
            List<String> msidcor = new ArrayList<>();
            List<List<String>> vcorid = new ArrayList<>();
            try {
                Connection con = data.connect();
                String query = "SELECT dlr_id,correlator,status FROM dlr_status WHERE vstatus=0";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                ResultSetMetaData metadata = rs.getMetaData();
                int numcols = metadata.getColumnCount();
                while (rs.next()) {
                    //System.out.println("Saving: " + rs.getString(1));
                    List<String> row = new ArrayList<>(numcols); // new list per row
                    int i = 1;
                    while (i <= numcols) {  // don't skip the last column, use <=
                        row.add(rs.getString(i++));
                    }
                    dlrcor.add(row); // add it to the result
                }
                con.close();
                System.out.println("Dlr Array Size: " + dlrcor.size());

                con = data.connect();
                String query2 = "SELECT idmpesa,mpesa_code,dlry_status FROM mpesa WHERE dlry_status='SmsDeliveryWaiting' OR dlry_status='MessageSent'";
                //String query2 = "SELECT idmpesa,mpesa_code FROM mpesa WHERE timestamp> '2018-10-01 23:59:59'";
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                ResultSetMetaData metadata2 = rs2.getMetaData();
                int numcols2 = metadata2.getColumnCount();
                while (rs2.next()) {                    
                    if(rs2.getString(3).equals("MessageSent")){
                        msgsent.add(rs2.getString(2));
                    }
                    List<String> row2 = new ArrayList<>(numcols2); // new list per row
                    int k = 1;
                    while (k <= numcols2) {  // don't skip the last column, use <=
                        row2.add(rs2.getString(k++));
                    }
                    vcorid.add(row2); // add it to the result

                    //System.out.println("Saving: " + rs2.getString(2));
                    vcor.add(rs2.getString(2));
                }
                con.close();
                System.out.println("Vendit Array Size: " + vcor.size());
                System.out.println("MessageSent Array Size: " + msgsent.size());

                con = data.connect();
                String mquery = "SELECT id,mpesacode,msgid,flag FROM sentmsgid WHERE flag=0";
                Statement mst = con.createStatement();
                ResultSet mrs = mst.executeQuery(mquery);
                ResultSetMetaData mmetadata = mrs.getMetaData();
                int mnumcols = mmetadata.getColumnCount();
                while (mrs.next()) {
                    //System.out.println("Saving: " + mrs.getString(3));
                    List<String> row = new ArrayList<>(mnumcols); // new list per row
                    int i = 1;
                    while (i <= mnumcols) {  // don't skip the last column, use <=
                        row.add(mrs.getString(i++));
                    }
                    msgids.add(row); // add it to the result
                    msidcor.add(mrs.getString(2));
                }
                con.close();
                System.out.println("MsgID Array Size: " + msgids.size());
                for (int m = 0; m < msgids.size(); m++) {
                    if (vcor.contains(msgids.get(m).get(1))) {
                        System.out.println("Found msid");
                        String mid = "empty";
                        for (int i = 0; i < vcorid.size(); i++) {
                            String correlator2 = vcorid.get(i).get(1);
                            if (msgids.get(m).get(1).equals(correlator2)) {
                                mid = vcorid.get(i).get(0);
                                break;
                            }
                        }
                        con = data.connect();
                        String update = "update mpesa set dlry_status = ?,msgid = ? WHERE idmpesa = ?";
                        PreparedStatement preparedStmt = con.prepareStatement(update);
                        preparedStmt.setString(1, "MessageSent");
                        preparedStmt.setString(2, msgids.get(m).get(2));
                        preparedStmt.setString(3, mid);
                        preparedStmt.executeUpdate();
                        System.out.println("Updated Mpesa");

                        String update2 = "update sentmsgid set flag = ? WHERE id = ?";
                        PreparedStatement preparedStmt2 = con.prepareStatement(update2);
                        preparedStmt2.setString(1, "1");
                        preparedStmt2.setString(2, msgids.get(m).get(0));
                        preparedStmt2.executeUpdate();
                        con.close();
                        System.out.println("Updated MsgID Table");
                    }/* else {
                        try {
                            con = data.connect();
                            String update = "update mpesa set dlry_status = ?,msgid = ? WHERE mpesa_code = ?";
                            PreparedStatement preparedStmt = con.prepareStatement(update);
                            preparedStmt.setString(1, "MessageSent");
                            preparedStmt.setString(2, msgids.get(m).get(2));
                            preparedStmt.setString(3, msgids.get(m).get(1));
                            preparedStmt.executeUpdate();
                            System.out.println("Updated Mpesa");

                            String update2 = "update sentmsgid set flag = ? WHERE id = ?";
                            PreparedStatement preparedStmt2 = con.prepareStatement(update2);
                            preparedStmt2.setString(1, "1");
                            preparedStmt2.setString(2, msgids.get(m).get(0));
                            preparedStmt2.executeUpdate();
                            con.close();
                            System.out.println("Updated MsgID Table");
                        } catch (SQLException sqerr) {
                            System.out.println("Failed Updating");
                        }
                    }*/
                }
                for (int y = 0; y < dlrcor.size(); y++) {
                    String dlrid = dlrcor.get(y).get(0);
                    String correlator = dlrcor.get(y).get(1);
                    String dlrystatus = dlrcor.get(y).get(2);
                    System.out.println("Checking :=" + correlator);
                    if (vcor.contains(correlator)) {
                        System.out.println("Found in mpesa");
                        String idmpesa = "empty";
                        for (int z = 0; z < vcorid.size(); z++) {
                            String correlator2 = vcorid.get(z).get(1);
                            if (correlator.equals(correlator2)) {
                                idmpesa = vcorid.get(z).get(0);
                                break;
                            }
                        }
                        //System.out.println("Mpesa Id:"+idmpesa);
                        con = data.connect();
                        String update = "update mpesa set dlry_status = ? WHERE idmpesa = ?";
                        PreparedStatement preparedStmt = con.prepareStatement(update);
                        preparedStmt.setString(1, dlrystatus);
                        preparedStmt.setString(2, idmpesa);
                        preparedStmt.executeUpdate();
                        System.out.println("Updated Mpesa");

                        String update2 = "update dlr_status set vstatus = ? WHERE dlr_id = ?";
                        PreparedStatement preparedStmt2 = con.prepareStatement(update2);
                        preparedStmt2.setString(1, "1");
                        preparedStmt2.setString(2, dlrid);
                        preparedStmt2.executeUpdate();
                        con.close();
                        System.out.println("Updated Delivery Table");
                    } else {
                        System.out.println("Not found in mpesa");
                        con = data.connect();
                        String update2 = "update dlr_status set vstatus = ? WHERE dlr_id = ?";
                        PreparedStatement preparedStmt2 = con.prepareStatement(update2);
                        preparedStmt2.setString(1, "1");
                        preparedStmt2.setString(2, dlrid);
                        preparedStmt2.executeUpdate();
                        con.close();
                        System.out.println("Updated Delivery Table");

                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(Updatevdlry.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
