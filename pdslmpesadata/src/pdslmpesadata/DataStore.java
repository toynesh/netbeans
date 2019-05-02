/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdslmpesadata;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juliuskun
 */
public class DataStore {

    public DataStore() {
        createTables();
    }

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/mpesaipaydata";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables() {
        Connection conn = connect();
        try {
            String transactions = "create table if not exists transactions(id INT NOT NULL AUTO_INCREMENT, elec_trans_id text, "
                    + "client text, terminal text, ref_received varchar(1000), amt text, "
                    + "token text, free_token text, total_debt text, total_fixed text, "
                    + "meter_num text, phone_num text, vend_req_sent text, vend_res_received text, "
                    + "web_user_id text, registration_reference_id text, vend_res_code text, external_ref text, "
                    + "time_completed text, successful_ text, orig_amt text, date_reversed text, "
                    + "our_ref text, vend_req_received text, vend_res_text text, fin_trans_id text, "
                    + "rev_req_sent text, rev_res_received text, rev_res_code text, rep_count text, "
                    + "orig_time text, low_balance_topup_id text, payment_mode text, token_units text, "
                    + "free_units text, token_receipt_num text, free_receipt_num text, refund_fin_trans_id text, "
                    + "user_notified text, UNIQUE(ref_received), primary key(id))";
            
            Statement stm = conn.createStatement();
            stm.execute(transactions);
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

    public void insert(String insert) {
        Connection conn = connect();
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
