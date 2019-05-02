/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipaypdslmpesa;

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
            String myUrl = "jdbc:mysql://localhost/ipaypdslmpesa";
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
            String transactions = "create table if not exists transactions(elec_trans_id varchar(100), client varchar(20), terminal varchar(50), ref_received varchar(500), amt varchar(50), token varchar(500), free_token varchar(500), total_debt varchar(50), total_fixed varchar(50), meter_num varchar(50), phone_num varchar(50), vend_req_sent varchar(100), vend_res_received varchar(100), web_user_id varchar(100), registration_reference_id varchar(100), vend_res_code varchar(100), external_ref varchar(500), time_completed varchar(100), successful_ varchar(500), orig_amt varchar(500), date_reversed varchar(100), our_ref text null, vend_req_received  text, vend_res_text text, fin_trans_id text, rev_req_sent varchar(500), rep_count varchar(100), orig_time  varchar(100), low_balance_topup_id varchar(100), payment_mode varchar(50), token_units varchar(500), free_units varchar(500), token_receipt_num varchar(500), free_receipt_num varchar(500), refund_fin_trans_id varchar(100), user_notified varchar(500))";

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
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
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
