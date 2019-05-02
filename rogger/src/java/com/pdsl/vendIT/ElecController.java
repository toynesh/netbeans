package com.pdsl.vendIT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ElecController
{
  Logger log = Logger.getLogger(ElecController.class.getName());
  DataStore data = new DataStore();
  
  public String controller(String msisdn, String amount, String account)
  {
    String output = http(msisdn, amount, account);
    return output;
  }
  
  public String http(String msisdn, String amount, String account)
  {
    try
    {
      String directives = "mpesa_acc=" + account;
      directives = directives + "&mpesa_amt=" + amount;
      directives = directives + "&mpesa_msisdn=" + msisdn;
      
      URL url = null;
      StringBuilder res = new StringBuilder();
      url = new URL("http://pdsl.bizswitch.net:9124/mpesa?" + directives);
      
      URLConnection conn = url.openConnection();
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response;
      while ((response = rd.readLine()) != null) {
        res.append(response);
      }
      rd.close();
      
      Logger.getLogger(ElecController.class.getName()).log(Level.INFO, res.toString());
      return res.toString();
    }
    catch (MalformedURLException ex)
    {
      Logger.getLogger(ElecController.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (IOException ex)
    {
      Logger.getLogger(ElecController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return "Failed";
  }
  
  public String post(List params)
  {
    HttpClient httpClient = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost("http://pdsl.bizswitch.net:9124/mpesa");
    try
    {
      httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    try
    {
      HttpResponse response = httpClient.execute(httpPost);
      
      System.out.println(httpPost.getEntity().toString() + "Response Reason " + response.getStatusLine().getReasonPhrase());
      HttpEntity respEntity = response.getEntity();
      if (respEntity != null) {
        return EntityUtils.toString(respEntity);
      }
    }
    catch (ClientProtocolException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return "TIMED OUT";
  }
  
  public void smpp(String url) {}
  
  public String limitAmounts(String msisdn, String amount, String acc)
  {
    double amounts = Double.parseDouble(amount);
    if (amounts < 200.0D)
    {
      amounts += check(msisdn).intValue();
      if (amounts < 200.0D)
      {
        if (check(msisdn).intValue() > 0)
        {
          String update = "update mpesa_holder set amount=" + amounts + " where msisdn=" + msisdn;
          this.data.insert(update);
        }
        else
        {
          String insert = "insert into mpesa_holder (msisdn,amount) values(" + msisdn + "," + amounts + ")";
          this.data.insert(insert);
        }
        String message = "FAIL|Tendered amount For ACC: " + acc + " is below 200 ADD Ksh " + (200.0D - amounts) + ". For purchase completion. Else Refund will be Done AFTER 24 hrs";
        return message;
      }
      String update = "update mpesa_holder set status=1 where msisdn=" + msisdn;
      this.data.insert(update);
      return "OK|" + amounts;
    }
    return "OK|" + amounts;
  }
  
  public Integer check(String msisdn)
  {
    try
    {
      String query = "select accountid,amount from mpesa_holder where msisdn=" + msisdn + " and status=0";
      PreparedStatement prep = this.data.connect().prepareStatement(query);
      ResultSet rst = prep.executeQuery();
      
      int total = 0;
      while (rst.next()) {
        total += rst.getInt(2);
      }
      return Integer.valueOf(total);
    }
    catch (SQLException ex)
    {
      Logger.getLogger(ElecController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return Integer.valueOf(0);
  }
  
  public static void main(String[] args)
  {
    ElecController elec = new ElecController();
    System.out.println(elec.limitAmounts("254723156864", "150", "ajja"));
  }
}
