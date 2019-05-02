/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getvenditcustomerinfo;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author coolie
 */
public class GetvenditcustomerInfo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        DataStore data = new DataStore();
        String db = "excelcustomerinfo";

        List<String> msisdn = new ArrayList<>();
        Statement dstmt;
        Connection con = null;
        try {
            /*System.out.println("Preparing DB & Info Table");
            con = data.connect(db);
            dstmt = con.createStatement();
            String dropdbsql = "DROP DATABASE  IF EXISTS " + db + "";
            dstmt.executeUpdate(dropdbsql);
            String createdbsql = "CREATE DATABASE  IF NOT EXISTS " + db + "";
            dstmt.executeUpdate(createdbsql);
            con.close();

            data.createTables(db);
            con = data.connect(db);
            Statement statement = con.createStatement();
            statement.executeUpdate("TRUNCATE mpesainfo");
            con.close();
            String insert = "INSERT IGNORE INTO `mpesainfo`(`id`, `orig`, `dest`, `tstamp`,"
                    + " `text`, `user`, `pass`, `mpesa_code`, `mpesa_acc`, `mpesa_msisdn`,"
                    + " `mpesa_trx_date`, `mpesa_trx_time`, `mpesa_amt`, `mpesa_sender`, `timestamp`,"
                    + " `status`, `msgid`, `message`, `retry`, `msg_status`, `originalmsg`,"
                    + " `delaymessage`, `originaldelaymessage`, `source`, `dlry_status`) SELECT `id`,"
                    + " `orig`, `dest`, `tstamp`, `text`, `user`, `pass`, `mpesa_code`, `mpesa_acc`,"
                    + " `mpesa_msisdn`, `mpesa_trx_date`, `mpesa_trx_time`, `mpesa_amt`, `mpesa_sender`,"
                    + " `timestamp`, `status`, `msgid`, `message`, `retry`, `msg_status`, `originalmsg`,"
                    + " `delaymessage`, `originaldelaymessage`, `source`, `dlry_status` FROM `mobile_wallet`.`mpesa201707`";
            String insert2 = "INSERT IGNORE INTO `mpesainfo`(`id`, `orig`, `dest`, `tstamp`, `text`,"
                    + " `user`, `pass`, `mpesa_code`, `mpesa_acc`, `mpesa_msisdn`, `mpesa_trx_date`,"
                    + " `mpesa_trx_time`, `mpesa_amt`, `mpesa_sender`, `timestamp`, `status`, `msgid`,"
                    + " `message`, `retry`, `msg_status`, `originalmsg`, `delaymessage`,"
                    + " `originaldelaymessage`, `source`, `dlry_status`) SELECT `id`, `orig`,"
                    + " `dest`, `tstamp`, `text`, `user`, `pass`, `mpesa_code`, `mpesa_acc`,"
                    + " `mpesa_msisdn`, `mpesa_trx_date`, `mpesa_trx_time`, `mpesa_amt`, `mpesa_sender`,"
                    + " `timestamp`, `status`, `msgid`, `message`, `retry`, `msg_status`, `originalmsg`,"
                    + " `delaymessage`, `originaldelaymessage`, `source`, `dlry_status` FROM `mobile_wallet`.`mpesa`";
            data.insert(insert, db);
            data.insert(insert2, db);
            System.out.println("Info Table Created!!");*/
            con = data.connect(db);
            String query2 = "SELECT DISTINCT mpesa_msisdn FROM mpesainfo";
            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery(query2);
            int i=1;
            while (rs2.next()) {
                System.out.println("Saving to Array= " +i+": "+ rs2.getString(1));
                msisdn.add(rs2.getString(1));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetvenditcustomerInfo.class.getName()).log(Level.SEVERE, "Creating info table" + ex);
        }
        System.out.println("Inf0 Array Size: " + msisdn.size());
        try {
            File f = new File("/home/coolie/IAN/prepodata.xls");
            Workbook wb = Workbook.getWorkbook(f);
            int numberOfSheets = wb.getNumberOfSheets();
            for (int x = 0; x < numberOfSheets; x++) {
                data.createSheetTable(x, db);
                System.out.println("Creating Table sheet" + x + " completed!!");
                Sheet s = wb.getSheet(x);
                int erow = s.getRows();
                int ecol = s.getColumns();
                for (int j = 1; j < erow; j++) {
                    Cell phonenumber = s.getCell(0, j);
                    String qphone = phonenumber.getContents();
                    System.out.println("Checking:"+j+"=" + qphone);
                    if (msisdn.contains(qphone)) {
                        String insert = "INSERT INTO `" + db + "`.`sheet" + x + "` ( `msisdn`, `customer`, `meter`, `firstdate`) SELECT mpesa_msisdn,mpesa_sender,mpesa_acc,timestamp FROM mpesainfo WHERE mpesa_msisdn=" + qphone + " LIMIT 1";
                        data.insert(insert, db);
                    } else {
                        System.out.println("Saving Record:=" + qphone + " Name:=none Meter:=none Time:=none");
                        String insert = "INSERT INTO `" + db + "`.`sheet" + x + "` ( `msisdn`, `customer`, `meter`, `firstdate`) VALUES ( '"+qphone+"','none','none','none')";
                        data.insert(insert, db);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GetvenditcustomerInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(GetvenditcustomerInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
