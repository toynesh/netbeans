/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tscpslip;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jsoup.Jsoup;
import org.json.*;

/**
 *
 * @author coolie
 */
public class Tscpslip {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JSONException {
        // TODO code application logic here
        Tscpslip tsc = new Tscpslip();
        //https://payslip.tsc.go.ke/pslip/apiPayslip.php?tscno=400000&key=sg5MD8j8fqXBF6oqFnoeaIs52ZOmzjem5dayxa&ym=2018-05
        System.setProperty("jsse.enableSNIExtension", "false");
        String response = html2text(tsc.sendTSC(400000, "sg5MD8j8fqXBF6oqFnoeaIs52ZOmzjem5dayxa", "2018-05"));
        System.out.println(response);
        System.out.println("=============================================================");
        // String str = tsc.sendTSC(800798, "2018-02");
        String ressplit[] = response.split("\\}");
        System.out.println("Number of Json Strings: " + ressplit.length);
        for (int i = 0; i <= ressplit.length; i++) {
            //System.out.println("Row " + i + ": " + ressplit[i] + "}");
            JSONObject obj = new JSONObject(ressplit[i] + "}");
            if (ressplit[i].contains("pin")) {
                System.out.println("First Row");
                String tscno = obj.getString("tscno");
                String sname = obj.getString("sname");
                String fname = obj.getString("fname");
                String idnum = obj.getString("idnum");
                String pin = obj.getString("pin");
                String age = obj.getString("age");
                String incrm = obj.getString("incrm");
                String DesigName = obj.getString("DesigName");
                String terms = obj.getString("terms");
                String cStatonCode = obj.getString("cStatonCode");
                String station = obj.getString("station");
                String bankname = obj.getString("bankname");
                System.out.println("tscno: " + tscno + " " + " sname: " + sname + " fname: " + fname+ " idnum: " + idnum+ " pin: " + pin+ " age: " + age+ " incrm: " + incrm+ " DesigName: " + DesigName+ " terms: " + terms+ " cStatonCode: " + cStatonCode+ " station: " + station+ " bankname: " + bankname);
            } else if (ressplit[i].contains("Balance")) {
                String edtype = obj.getString("edtype");
                String Amount = obj.getString("Amount");
                String Balance = obj.getString("Balance");
                System.out.println("Edtype: " + edtype + " " + " Amount: " + Amount + " Balance: " + Balance);
            } else {
                String edtype = obj.getString("edtype");
                String Amount = obj.getString("Amount");
                System.out.println("Edtype: " + edtype + " " + " Amount: " + Amount);
            }
        }

    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public String sendReq(String param) throws IOException {
        String request = "https://payslip.tsc.go.ke/pslip/apiPayslip.php?";
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

        //System.out.println(response.toString());
        out.close();
        connection.disconnect();
        return response.toString();
    }

    public String sendTSC(int tscno, String key, String ym) {
        String url = "tscno=" + tscno + "&key="+ key +"&ym=" + ym + "";
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
