/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getvenditcustomerinfo;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juliuskun
 */
public class DataStore {

    public Connection connect(String db) {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/"+db+"";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables(String db) {
        Connection conn = connect(db);
        try {
            String mpesainfo = "create table if not exists mpesainfo AS SELECT * FROM `mobile_wallet`.`mpesa` WHERE `idmpesa`<0";
            //String stat = "INSERT IGNORE INTO `mpesainfo`(`id`, `orig`, `dest`, `tstamp`,`text`, `user`, `pass`, `mpesa_code`, `mpesa_acc`, `mpesa_msisdn`, `mpesa_trx_date`, `mpesa_trx_time`, `mpesa_amt`, `mpesa_sender`, `timestamp`, `status`,`msgid`, `message`, `retry`, `msg_status`, `originalmsg`,`delaymessage`, `originaldelaymessage`, `source`, `dlry_status`) SELECT `id`,`orig`, `dest`, `tstamp`, `text`, `user`, `pass`, `mpesa_code`, `mpesa_acc`,`mpesa_msisdn`, `mpesa_trx_date`, `mpesa_trx_time`, `mpesa_amt`, `mpesa_sender`,`timestamp`, `status`, `msgid`, `message`, `retry`, `msg_status`, `originalmsg`,`delaymessage`, `originaldelaymessage`, `source`, `dlry_status` FROM `mpesa201707`;";
            Statement stm = conn.createStatement();
            stm.execute(mpesainfo);
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void createSheetTable(int sheetno,String db) {
        Connection conn = connect(db);
        try {
            String sheet = "create table if not exists sheet"+sheetno+"(id INT NOT NULL AUTO_INCREMENT, msisdn varchar(20), customer varchar(100), meter varchar(100), firstdate varchar(100), primary key(id))";
            
            Statement stm = conn.createStatement();
            stm.execute(sheet);
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void insert(String insert,String db) {
        Connection conn = connect(db);
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
            return;
        } catch (SQLException ex) {
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
