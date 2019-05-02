package com.pdsl.vendit.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class EquitelSMS
{
  String username;
  String password;
  String text;
  String binfo;
  String to;
  String from;
  String smsc;
  String dlr;
  String server;
  String type;
  
  public EquitelSMS(String server, String smsc, String username, String password, String to, String text, String from)
  {
    this.server = server;
    this.smsc = smsc;
    this.username = username;
    this.password = password;
    this.to = to;
    this.text = text;
    this.from = from;
  }
  
  public void submitMessage()
  {
    try
    {
      StringBuffer res = new StringBuffer();
      String data = "sender=" + this.from;
      data = data + "&to=" + this.to;
      data = data + "&smsc=" + this.smsc;
      data = data + "&to=" + this.to;
      data = data + "&message=" + URLEncoder.encode(this.text.toString(), "UTF-8");
      
      System.out.println(data);
      
      URL url = null;
      
      url = new URL("http://" + this.server + "/kannel/send.php?" + data);
      
      URLConnection conn = url.openConnection();
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response;
      while ((response = rd.readLine()) != null) {
        res.append(response);
      }
      rd.close();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public static void main(String[] args) {}
  
  private static StringBuffer convertToUnicode(String regText)
  {
    char[] chars = regText.toCharArray();
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < chars.length; i++)
    {
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
