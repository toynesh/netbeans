/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipaypdslmpesa;


import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author juliuskun
 */
public class Ipaypdslmpesa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            DataStore data = new DataStore();
            String elec_trans_id = null;
            String client = null;
            String terminal = null;
            String ref_received = null;
            String amt = null;
            String token = null;
            String free_token = null;
            String total_debt = null;
            String total_fixed = null;
            String meter_num = null;
            String phone_num = null;
            String vend_req_sent = null;
            String vend_res_received = null;
            String web_user_id = null;
            String registration_reference_id = null;
            String vend_res_code = null;
            String external_ref = null;
            String time_completed = null;
            String successful_ = null;
            String orig_amt = null;
            String date_reversed = null;
            String our_ref = null;
            String vend_req_received = null;
            String vend_res_text = null;
            String fin_trans_id = null;
            String rev_req_sent = null;
            String rep_count = null;
            String orig_time = null;
            String low_balance_topup_id = null;
            String payment_mode = null;
            String token_units = null;
            String free_units = null;
            String token_receipt_num = null;
            String free_receipt_num = null;
            String refund_fin_trans_id = null;
            String user_notified = null;

            Connection con = data.connect();
            Statement statement = con.createStatement();
            statement.executeUpdate("TRUNCATE transactions");
            con.close();

            String csvFilename = "/home/juliuskun/IAN/pdsl_mpesa_transactions/data1.csv";
            CSVReader csvReader = new CSVReader(new FileReader(csvFilename), '|');
            String[] row = null;
            int j=1;
            while ((row = csvReader.readNext()) != null) {
                elec_trans_id = row[0];
                client = row[1];
                terminal = row[2];
                ref_received = row[3];
                amt = row[4];
                token = row[5];
                free_token = row[6];
                total_debt = row[7];
                total_fixed = row[8];
                meter_num = row[9];
                meter_num = meter_num.replaceAll("\'", "");
                phone_num = row[10];
                vend_req_sent = row[11];
                vend_res_received = row[12];
                web_user_id = row[13];
                registration_reference_id = row[14];
                vend_res_code = row[15];
                external_ref = row[16];
                external_ref = external_ref.replace("\\", "");
                time_completed = row[17];
                successful_ = row[18];
                orig_amt = row[19];
                date_reversed = row[20];
                our_ref = row[21];
                vend_req_received = row[22];
                vend_res_text = row[23];
                vend_res_text = vend_res_text.replaceAll("\'", "");
                fin_trans_id = row[24];
                rev_req_sent = row[25];
                rep_count = row[26];
                orig_time = row[27];
                low_balance_topup_id = row[28];
                payment_mode = row[29];
                token_units = row[30];
                free_units = row[31];
                token_receipt_num = row[32];
                free_receipt_num = row[33];
                refund_fin_trans_id = row[34];
                user_notified = row[35];

                System.out.println("Inserting row:=" + j + " " + elec_trans_id + "|" + client + "|" + terminal + "|" + ref_received + "|" + amt + "|" + token + "|" + free_token + "|" + total_debt + "|" + total_fixed + "|" + meter_num + "|" + phone_num + "|" + vend_req_sent + "|" + vend_res_received + "|" + web_user_id + "|" + registration_reference_id + "|" + vend_res_code + "|" + external_ref + "|" + time_completed + "|" + successful_ + "|" + orig_amt + "|" + date_reversed + "|" + our_ref + "|" + vend_req_received + "|" + vend_res_text + "|" + fin_trans_id + "|" + rev_req_sent + "|" + rep_count + "|" + orig_time + "|" + low_balance_topup_id + "|" + payment_mode + "|" + token_units + "|" + free_units + "|" + token_receipt_num + "|" + free_receipt_num + "|" + refund_fin_trans_id + "|" + user_notified);

                String insert = "INSERT INTO  transactions (`elec_trans_id`, `client`, `terminal`, `ref_received`, `amt`, `token`, `free_token`, `total_debt`, `total_fixed`, `meter_num`, `phone_num`, `vend_req_sent`, `vend_res_received`, `web_user_id`, `registration_reference_id`, `vend_res_code`, `external_ref`, `time_completed`, `successful_`, `orig_amt`, `date_reversed`, `our_ref`, `vend_req_received`, `vend_res_text`, `fin_trans_id`, `rev_req_sent`, `rep_count`, `orig_time`, `low_balance_topup_id`, `payment_mode`, `token_units`, `free_units`, `token_receipt_num`, `free_receipt_num`, `refund_fin_trans_id`, `user_notified`) VALUES ('" + elec_trans_id + "',"
                        + "                '" + client + "',"
                        + "                '" + terminal + "',"
                        + "                '" + ref_received + "',"
                        + "                '" + amt + "',"
                        + "                '" + token + "',"
                        + "                '" + free_token + "',"
                        + "                '" + total_debt + "',"
                        + "                '" + total_fixed + "',"
                        + "                '" + meter_num + "',"
                        + "                '" + phone_num + "',"
                        + "                '" + vend_req_sent + "',"
                        + "                '" + vend_res_received + "',"
                        + "                '" + web_user_id + "',"
                        + "                '" + registration_reference_id + "',"
                        + "                '" + vend_res_code + "',"
                        + "                '" + external_ref + "',"
                        + "                '" + time_completed + "',"
                        + "                '" + successful_ + "',"
                        + "                '" + orig_amt + "',"
                        + "                '" + date_reversed + "',"
                        + "                '" + our_ref + "',"
                        + "                '" + vend_req_received + "',"
                        + "                '" + vend_res_text + "',"
                        + "                '" + fin_trans_id + "',"
                        + "                '" + rev_req_sent + "',"
                        + "                '" + rep_count + "',"
                        + "                '" + orig_time + "',"
                        + "                '" + low_balance_topup_id + "',"
                        + "                '" + payment_mode + "',"
                        + "                '" + token_units + "',"
                        + "                '" + free_units + "',"
                        + "                '" + token_receipt_num + "',"
                        + "                '" + free_receipt_num + "',"
                        + "                '" + refund_fin_trans_id + "',"
                        + "                '" + user_notified + "')";
                data.insert(insert);
                /*Connection con = data.connect();
            Statement statement = con.createStatement();
            statement.executeUpdate("DELETE FROM transactions WHERE elec_trans_id="+elec_trans_id+"");
            con.close();*/
                j++;
            }
            csvReader.close();
        } catch (IOException ex) {
            Logger.getLogger(Ipaypdslmpesa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Ipaypdslmpesa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
