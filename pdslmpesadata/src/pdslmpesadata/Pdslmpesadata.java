/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdslmpesadata;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

/**
 *
 * @author coolie
 */
public class Pdslmpesadata {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        DataStore data = new DataStore();
        PreparedStatement prep = null;
        int elec_trans_id;
        String client;
        String terminal;
        String ref_received;
        String amt;
        String token;
        String free_token;
        String total_debt;
        String total_fixed;
        String meter_num;
        String phone_num;
        String vend_req_sent;
        String vend_res_received;
        String web_user_id;
        String registration_reference_id;
        String vend_res_code;
        String external_ref;
        String time_completed;
        String successful_;
        String orig_amt;
        String date_reversed;
        String our_ref;
        String vend_req_received;
        String vend_res_text;
        String fin_trans_id;
        String rev_req_sent;
        String rev_res_received;
        String rev_res_code;
        String rep_count;
        String orig_time;
        String low_balance_topup_id;
        String payment_mode;
        String token_units;
        String free_units;
        String token_receipt_num;
        String free_receipt_num;
        String refund_fin_trans_id;
        String user_notified;

        String csvFile = "/home/julius/ipaydata.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = "\\|";

        try {
            Connection con = data.connect();
            br = new BufferedReader(new FileReader(csvFile));
            int i = 1;
            String fline = "";
            int status = 0;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                line = line + "|none";
                String[] customer = line.split(cvsSplitBy);
                System.out.println("Length:" + customer.length);
                if (i > 1) {
                    if (customer.length == 11) {
                        fline = line.replaceAll("\\|none", "");
                        status = 1;
                    } else if (customer.length == 2) {
                        System.out.println("Empty ROW!!");
                    } else {
                        if (status == 1) {
                            String newline = fline + line;
                            newline = newline.replaceAll("\"", "");
                            customer = newline.split(cvsSplitBy);
                            status = 0;
                        }
                        System.out.println("Row= " + (i - 1) + " ;=" + Arrays.toString(customer) + "]");
                        try {
                            String values = "insert into transactions(elec_trans_id,"
                                    + "client,"
                                    + "terminal,"
                                    + "ref_received,"
                                    + "amt,"
                                    + "token,"
                                    + "free_token,"
                                    + "total_debt,"
                                    + "total_fixed,"
                                    + "meter_num,"
                                    + "phone_num,"
                                    + "vend_req_sent,"
                                    + "vend_res_received,"
                                    + "web_user_id,"
                                    + "registration_reference_id,"
                                    + "vend_res_code,"
                                    + "external_ref,"
                                    + "time_completed,"
                                    + "successful_,"
                                    + "orig_amt,"
                                    + "date_reversed,"
                                    + "our_ref,"
                                    + "vend_req_received,"
                                    + "vend_res_text,"
                                    + "fin_trans_id,"
                                    + "rev_req_sent,"
                                    + "rev_res_received,"
                                    + "rev_res_code,"
                                    + "rep_count,"
                                    + "orig_time,"
                                    + "low_balance_topup_id,"
                                    + "payment_mode,"
                                    + "token_units,"
                                    + "free_units,"
                                    + "token_receipt_num,"
                                    + "free_receipt_num,"
                                    + "refund_fin_trans_id,"
                                    + "user_notified)"
                                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            prep = con.prepareStatement(values);
                            prep.setString(1, customer[0]);
                            prep.setString(2, customer[1]);
                            prep.setString(3, customer[2]);
                            prep.setString(4, customer[3]);
                            prep.setString(5, customer[4]);
                            prep.setString(6, customer[5]);
                            prep.setString(7, customer[6]);
                            prep.setString(8, customer[7]);
                            prep.setString(9, customer[8]);
                            prep.setString(10, customer[9]);
                            prep.setString(11, customer[10]);
                            prep.setString(12, customer[11]);
                            prep.setString(13, customer[12]);
                            prep.setString(14, customer[13]);
                            prep.setString(15, customer[14]);
                            prep.setString(16, customer[15]);
                            prep.setString(17, customer[16]);
                            prep.setString(18, customer[17]);
                            prep.setString(19, customer[18]);
                            prep.setString(20, customer[19]);
                            prep.setString(21, customer[20]);
                            prep.setString(22, customer[21]);
                            prep.setString(23, customer[22]);
                            prep.setString(24, customer[23]);
                            prep.setString(25, customer[24]);
                            prep.setString(26, customer[25]);
                            prep.setString(27, customer[26]);
                            prep.setString(28, customer[27]);
                            prep.setString(29, customer[28]);
                            prep.setString(30, customer[29]);
                            prep.setString(31, customer[30]);
                            prep.setString(32, customer[31]);
                            prep.setString(33, customer[32]);
                            prep.setString(34, customer[33]);
                            prep.setString(35, customer[34]);
                            prep.setString(36, customer[35]);
                            prep.setString(37, customer[36]);
                            prep.setString(38, customer[37]);
                            prep.execute();
                            prep.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    }
                }

                i++;

            }
            con.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
