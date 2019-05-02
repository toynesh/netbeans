package com.pdsl.vendIT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender
{
  boolean test = true;
  Logger log = Logger.getLogger(Sender.class.getName());
  Connection con = null;
  
  public static void main(String[] args)
  {
    String message = "The Baby experts invites you to the health and nutrition segment at the Baby Banda on the 24th Sept (Saturday 11am _ 1pm) Panel comprised of Pinky Ghelani and Gertude?s Hospital paediatrician and nutritionist.  Invite a friend and get free samples at our stand on any of the 3 days";
    new Sender().sendMes("254723156864", message);
  }
  
  public void pick()
  {
    try
    {
      String query = "select msisdn from where ";
      PreparedStatement prep = this.con.prepareStatement(query);
      ResultSet rst = prep.executeQuery();
      String message = "You can now post goat milk for sale on iCow Soko. Send Soko#sell. What other features you would like to see on iCow.";
      while (rst.next())
      {
        long msisdn = rst.getLong(1);
        sendMes("" + msisdn, message);
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public void sendMesDisable(String number, String message)
  {
    System.out.println(message);
    String outmessage = message;
    try
    {
      message = URLEncoder.encode(message.toString(), "UTF-8");
    }
    catch (UnsupportedEncodingException uee)
    {
      System.err.println(uee);
    }
    try
    {
      String username = "tester";
      String password = "tester";
      
      String host = "127.0.0.1";
      String port = "14020";
      
      String smic = "";
      String destination = number;
      StringBuffer res = new StringBuffer();
      if ((destination.startsWith("25472")) || (destination.startsWith("25471")) || (destination.startsWith("25470"))) {
        smic = "SAFTX";
      } else if ((destination.startsWith("25477")) || (destination.startsWith("077"))) {
        smic = "ORTRX";
      } else if ((destination.startsWith("25473")) || (destination.startsWith("25478"))) {
        smic = "CELTX";
      }
      String directives = "username=" + username;
      directives = directives + "&password=" + password;
      directives = directives + "&dlr-mask=31";
      directives = directives + "&smsc=" + smic;
      directives = directives + "&to=" + destination;
      
      directives = directives + "&text=" + message;
      
      URL url = null;
      
      url = new URL("http://" + host + ":" + port + "/cgi-bin/sendsms?" + directives);
      
      URLConnection conn = url.openConnection();
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response;
      while ((response = rd.readLine()) != null) {
        res.append(response);
      }
      rd.close();
      
      String resultCode = res.substring(0, res.indexOf(":"));
      String str1;
      if (!resultCode.equals("0"))
      {
        System.out.println(res.toString() + "Exception Occured while Sending Message " + resultCode);
        str1 = "insert into outmessages (msisdn,message,status) values (" + number + ",'" + outmessage + "'," + resultCode + ")";
      }
      else
      {
        str1 = "insert into outmessages (msisdn,message,status) values (" + number + ",'" + outmessage + "',0)";
      }
    }
    catch (Exception localException) {}
  }
  
  public void sendChargeMes(String number, String message)
  {
    System.out.println(message);
    String outmessage = message;
    try
    {
      message = URLEncoder.encode(message.toString(), "UTF-8");
    }
    catch (UnsupportedEncodingException uee)
    {
      System.err.println(uee);
    }
    try
    {
      String username = "tester";
      String password = "tester";
      String host = "192.168.100.202";
      
      String port = "14020";
      String from = "4226";
      
      String smic = "";
      String destination = number;
      StringBuffer res = new StringBuffer();
      if ((destination.startsWith("25472")) || (destination.startsWith("25471")) || (destination.startsWith("25470"))) {
        smic = "SAFTX";
      } else if ((destination.startsWith("25477")) || (destination.startsWith("077"))) {
        smic = "ORTRX";
      } else if ((destination.startsWith("25473")) || (destination.startsWith("25478"))) {
        smic = "CELTX";
      }
      String directives = "username=" + username;
      directives = directives + "&password=" + password;
      directives = directives + "&dlr-mask=31";
      directives = directives + "&smsc=" + smic;
      directives = directives + "&to=" + destination;
      directives = directives + "&from=" + from;
      directives = directives + "&binfo=6463";
      directives = directives + "&text=" + message;
      
      URL url = null;
      
      url = new URL("http://" + host + ":" + port + "/cgi-bin/sendsms?" + directives);
      
      URLConnection conn = url.openConnection();
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response;
      while ((response = rd.readLine()) != null) {
        res.append(response);
      }
      rd.close();
      
      String resultCode = res.substring(0, res.indexOf(":"));
      String str1;
      if (!resultCode.equals("0"))
      {
        System.out.println(res.toString() + "Exception Occured while Sending Message " + resultCode);
        str1 = "insert into outmessages (msisdn,message,status) values (" + number + ",'" + outmessage + "'," + resultCode + ")";
      }
      else
      {
        str1 = "insert into outmessages (msisdn,message,status) values (" + number + ",'" + outmessage + "',0)";
      }
    }
    catch (Exception localException) {}
  }
  
  public void sendMes(String number, String message)
  {
    if ((number.startsWith("25473")) || (number.startsWith("25478"))) {
      sendMesDisable(number, message);
    } else {
      sendNormalMes(number, message);
    }
  }
  
  public void airtel(String number, String message)
  {
    StringBuffer res = new StringBuffer();
    try
    {
      String outmessage = message;
      try
      {
        this.log.log(Level.INFO, message);
        
        message = URLEncoder.encode(message.toString(), "UTF-8");
      }
      catch (UnsupportedEncodingException uee)
      {
        this.log.log(Level.WARNING, uee.getMessage());
      }
      URL url = null;
      
      URLConnection conn = url.openConnection();
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response;
      while ((response = rd.readLine()) != null) {
        res.append(response);
      }
      rd.close();
      
      String resultCode = res.toString();
      if (!resultCode.contains("RESP:0")) {}
      return;
    }
    catch (IOException ex)
    {
      Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public void sendNormalMes(String number, String message)
  {
    System.out.println(message);
    String outmessage = message;
    try
    {
      this.log.log(Level.INFO, message);
      
      message = URLEncoder.encode(message.toString(), "UTF-8");
    }
    catch (UnsupportedEncodingException uee)
    {
      System.err.println(uee);
    }
    try
    {
      String username = "tester";
      String password = "tester";
      
      String host = "127.0.0.1";
      String port = "14020";
      
      String from = "22226";
      
      String smic = "";
      String destination = number;
      StringBuffer res = new StringBuffer();
      if ((destination.startsWith("25472")) || (destination.startsWith("25471")) || (destination.startsWith("25470")))
      {
        smic = "SAFTXP";
      }
      else if ((destination.startsWith("25477")) || (destination.startsWith("077")) || (destination.startsWith("77")))
      {
        smic = "ORTRX";
        if (!destination.startsWith("25477")) {
          if (!destination.startsWith("77")) {}
        }
      }
      else if ((destination.startsWith("25473")) || (destination.startsWith("25478")))
      {
        smic = "CELTX";
      }
      String directives = "username=" + username;
      directives = directives + "&password=" + password;
      directives = directives + "&dlr-mask=31";
      directives = directives + "&smsc=" + smic;
      directives = directives + "&to=" + destination;
      directives = directives + "&from=" + from;
      
      directives = directives + "&text=" + message;
      
      URL url = null;
      
      url = new URL("http://" + host + ":" + port + "/cgi-bin/sendsms?" + directives);
      
      URLConnection conn = url.openConnection();
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response;
      while ((response = rd.readLine()) != null) {
        res.append(response);
      }
      rd.close();
      this.log.log(Level.INFO, res.toString());
      String resultCode = res.substring(0, res.indexOf(":"));
      if (!resultCode.equals("0")) {
        System.out.println(res.toString() + "Exception Occured while Sending Message " + resultCode);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
