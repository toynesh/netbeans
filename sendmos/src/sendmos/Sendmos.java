/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendmos;

import java.io.IOException;
import java.io.InputStream;
import static java.lang.Integer.parseInt;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author coolie
 */
public class Sendmos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner in = new Scanner(System.in);
        System.out.print("Type name of number of MOS to be sent ");
        int ntms = parseInt(in.next());
        for (int i = 1; i <= ntms; i++) {
            System.out.print("MO N0;-" + i + " To Client: ");
            new Thread(new Runnable() {
                public void run() {
                    try {
                        String urlString = "http://localhost:88/inject_mo?short_message=35601211&source_addr=254728064120&destination_addr=20076&submit=Submit+Message&service_type=&source_addr_ton=1&source_addr_npi=1&dest_addr_ton=1&dest_addr_npi=1&esm_class=0&protocol_ID=&priority_flag=&registered_delivery_flag=0&data_coding=0&user_message_reference=&source_port=&destination_port=&sar_msg_ref_num=&sar_total_segments=&sar_segment_seqnum=&user_response_code=&privacy_indicator=&payload_type=&message_payload=&callback_num=&source_subaddress=&dest_subaddress=&language_indicator=&tlv1_tag=&tlv1_len=&tlv1_val=&tlv2_tag=&tlv2_len=&tlv2_val=&tlv3_tag=&tlv3_len=&tlv3_val=&tlv4_tag=&tlv4_len=&tlv4_val=&tlv5_tag=&tlv5_len=&tlv5_val=&tlv6_tag=&tlv6_len=&tlv6_val=&tlv7_tag=&tlv7_len=&tlv7_val=";
                        URL url = new URL(urlString);
                        URLConnection conn = url.openConnection();
                        InputStream is = conn.getInputStream();
// Do what you want with that stream
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(Sendmos.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Sendmos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        }
    }

}
