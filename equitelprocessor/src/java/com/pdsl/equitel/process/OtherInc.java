package com.pdsl.equitel.process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class OtherInc {

    // HTTP GET request
    public String autoFloat(String tranid) throws Exception {
        DateTime cd = new DateTime();
        DateTimeFormatter dfm = DateTimeFormat.forPattern("yyyy-MM-dd HHmmss");
        DateTimeFormatter dfm2 = DateTimeFormat.forPattern("dd-MM-yyyy");
        String d = dfm.print(cd);
        String d2 = dfm2.print(cd);
        String url = "http://172.27.116.21:8084/equitynotification/notification?trancurrency=kes&phonenumber=254728064120&tranid="+tranid+d.replaceAll("-", "").replaceAll(" ", "")+"&refno=900620&tranparticular=AUTOFLOAT&debitaccount=900620&trantype=cash&debitcustname=EQUITEL&tranamount=100000&trandate="+d2+"%2000:00:00";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        
        return response.toString();

    }
}
