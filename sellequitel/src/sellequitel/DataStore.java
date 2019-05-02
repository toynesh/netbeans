/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sellequitel;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

/**
 *
 * @author juliuskun
 */
public class DataStore {

    public Connection conn = null;

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/equitel";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");

            this.conn = conn;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public String sendReq(String param) throws IOException {
        String request = " http://197.248.9.105:8084/VendIT/Render?";
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(param.getBytes().length));
        connection.setUseCaches(false);

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(param);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        out.close();
        connection.disconnect();
        return response.toString();
    }

    public String sendPayment(String transtime, String transID, String refNO, String msisdn, String amount, String name) {
        String url = "id=3630&orig=MPESA&tstamp=" + transtime + "&mpesa_code=" + transID + "&mpesa_acc=" + refNO + "&mpesa_msisdn=" + msisdn + "&mpesa_amt=" + amount + "&mpesa_sender=" + name + "&mpesa_verify=onicha";
        String res;
        try {
            res = sendReq(url);
        } catch (IOException e) {
            e.printStackTrace();
            res = "FAILED";
        }
        return res;
    }
}
