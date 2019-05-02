package com.edservice.SE;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OtherInc
{
  public String sendReq(String param)
    throws IOException
  {
    String request = "http://172.27.116.36:8084/equitelprocessor/Sell?";
    URL url = new URL(request);
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.setInstanceFollowRedirects(false);
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    connection.setRequestProperty("charset", "utf-8");
    connection.setRequestProperty("Content-Length", Integer.toString(param.getBytes().length));
    connection.setUseCaches(false);
    
    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
    out.writeBytes(param);
    out.flush();
    
    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    
    StringBuffer response = new StringBuffer();
    String inputLine;
    while ((inputLine = in.readLine()) != null)
    {
      response.append(inputLine);
    }
    in.close();
    
    System.out.println(response.toString());
    out.close();
    connection.disconnect();
    return response.toString();
  }
  
  public String sendPayment(String meter, String amt, String phn)
  {
    String url = "meter=" + meter + "&amt=" + amt + "&phn=" + phn + "";
    String res;
    try
    {
      res = sendReq(url);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      res = "FAILED";
    }
    return res;
  }
}
