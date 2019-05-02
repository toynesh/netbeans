/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtelhttpmsg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author coolie
 */
public class AirtelHTTPMSG {

    /**
     * @param args the command line arguments
     */
    String REQUESTTYPE;
    String MOBILENO;
    String USERNAME;
    String ORIGIN_ADDR;
    String TYPE;
    String text;
    String PASSWORD;
    String server;

    public AirtelHTTPMSG(String MOBILENO, String text) {
        this.REQUESTTYPE = "SMSSubmitReq";
        this.MOBILENO = MOBILENO;
        this.USERNAME = "PdslKeT2";
        this.ORIGIN_ADDR = "VENDIT";
        this.TYPE = "0";
        this.text = text;
        this.PASSWORD = "pdsl@Air22";
        this.server = "41.223.59.92:9002";
    }

    public void submitMessage() {
        try {
            StringBuffer res = new StringBuffer();
            String data = "REQUESTTYPE=" + this.REQUESTTYPE;
            data = data + "&MOBILENO=" + this.MOBILENO;
            data = data + "&USERNAME=" + this.USERNAME;
            data = data + "&ORIGIN_ADDR=" + this.ORIGIN_ADDR;
            data = data + "&TYPE=" + this.TYPE;
            data = data + "&PASSWORD=" + this.PASSWORD;
            data = data + "&MESSAGE=" + URLEncoder.encode(this.text.toString(), "UTF-8");

            System.out.println(data);

            URL url = null;

            url = new URL("http://" + this.server + "/smshttp/qs/?" + data);

            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response;
            while ((response = rd.readLine()) != null) {
                res.append(response);
            }
            rd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        //ian 254734034614
        AirtelHTTPMSG esdp = new AirtelHTTPMSG("254734034614", "Working now");
        esdp.submitMessage();
        

    }

}
