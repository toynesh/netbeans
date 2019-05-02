/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vendit.sms;

/**
 *
 * @author root
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;



public class AirtelSMS {

    String REQUESTTYPE;
    String MOBILENO;
    String USERNAME;
    String ORIGIN_ADDR;
    String TYPE;
    String MESSAGE;
    String PASSWORD;
    //http://41.223.59.92:9002/smshttp/qs/?REQUESTTYPE=SMSSubmitReq&MOBILENO=254730214600&USERNAME=PdslKeT2&ORIGIN_ADDR=VENDIT&TYPE=0&MESSAGE=testsms&PASSWORD=pdsl@999

    public AirtelSMS(String REQUESTTYPE, String MOBILENO, String USERNAME, String ORIGIN_ADDR, String TYPE, String MESSAGE, String PASSWORD) {
        this.REQUESTTYPE = REQUESTTYPE;
        this.MOBILENO = MOBILENO;
        this.USERNAME = USERNAME;
        this.ORIGIN_ADDR = ORIGIN_ADDR;
        this.TYPE = TYPE;
        this.MESSAGE = MESSAGE;
        this.PASSWORD = PASSWORD;
    }

    public void submitMessage() {
        try {
            StringBuffer res = new StringBuffer();
            String data = "REQUESTTYPE=" + this.REQUESTTYPE;
            data = data + "&MOBILENO=" + this.MOBILENO;
            data = data + "&USERNAME=" + this.USERNAME;
            data = data + "&ORIGIN_ADDR=" + this.ORIGIN_ADDR;
            data = data + "&TYPE=" + this.TYPE;
            data = data + "&MESSAGE=" + URLEncoder.encode(this.MESSAGE.toString(), "UTF-8");
            data = data + "&PASSWORD=" + this.PASSWORD;

            System.out.println(data);

            URL url = null;

            url = new URL("http://41.223.59.92:9002/smshttp/qs/?" + data);

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
        AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", "254780922453", "PdslKeT2", "VENDIT", "0", "KPLC Mtr No:37169012285 Token:73373733877139129595 Units:4.9 Amount:Ksh:100 (Elec:62.07 Other Charges:37.93) Date:2017-09-27 04:19:28 Ref:PDSLOVS79387753", "pdsl@999");
        esdp.submitMessage();
    }

    private static StringBuffer convertToUnicode(String regText) {
        char[] chars = regText.toCharArray();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            String iniHexString = Integer.toHexString(chars[i]);
            if (iniHexString.length() == 1) {
                iniHexString = "000" + iniHexString;
            } else if (iniHexString.length() == 2) {
                iniHexString = "00" + iniHexString;
            } else if (iniHexString.length() == 3) {
                iniHexString = "0" + iniHexString;
            }
            hexString.append(iniHexString);
        }
        return hexString;
    }
}
