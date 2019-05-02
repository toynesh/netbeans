package com.pdsl.vendIT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import metro_sdp.Metro_sdp;

public class DelayNotifications
{
  DataStore data = new DataStore();
  Metro_sdp sdp = new Metro_sdp();
  
  public void updateNotifaction(String message, String mpesa_code, String meter)
  {
    try
    {
      Logger.getLogger(DelayNotifications.class.getName()).log(Level.INFO, "Delay Notify msg: " + message + " Code :" + mpesa_code);
      if (message.equals("on"))
      {
        Connection con = this.data.connectvendit();
        String query = "select phone, login, notification from user where notification like 'yes' ";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next())
        {
          if (meter.contains("-"))
          {
            this.sdp.sendSdpSms(rs.getString(1), "There is a postpaid system delay from KPLC", mpesa_code, "704307");
            EquitelSMS esdp = new EquitelSMS("197.248.31.219", "ORANGERX", "pdsl219", "pdsl219", rs.getString(1), "There is a system delay from KPLC", "VendIT");
            esdp.submitMessage();
          }
          else
          {
            this.sdp.sendSdpSms(rs.getString(1), "There is a prepaid system delay from KPLC", mpesa_code, "704307");
            EquitelSMS esdp = new EquitelSMS("197.248.31.219", "ORANGERX", "pdsl219", "pdsl219", rs.getString(1), "There is a system delay from KPLC", "VendIT");
            esdp.submitMessage();
          }
          Logger.getLogger(DelayNotifications.class.getName()).log(Level.INFO, "Delay msg to :" + rs.getString(1) + " :=:" + rs.getString(2));
        }
        con.close();
      }
      else
      {
        Connection con = this.data.connectvendit();
        String query = "select phone, login, notification from user where notification='yes'";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next())
        {
          if (meter.contains("-"))
          {
            this.sdp.sendSdpSms(rs.getString(1), "The postpaid delay/timeout is over", mpesa_code, "704307");
            EquitelSMS esdp = new EquitelSMS("197.248.31.219", "ORANGERX", "pdsl219", "pdsl219", rs.getString(1), "The delay/timeout is over", "VendIT");
            esdp.submitMessage();
          }
          else
          {
            this.sdp.sendSdpSms(rs.getString(1), "The prepaid delay/timeout is over", mpesa_code, "704307");
            EquitelSMS esdp = new EquitelSMS("197.248.31.219", "ORANGERX", "pdsl219", "pdsl219", rs.getString(1), "The delay/timeout is over", "VendIT");
            esdp.submitMessage();
          }
          Logger.getLogger(DelayNotifications.class.getName()).log(Level.INFO, "delay over msg to :" + rs.getString(1) + " :=:" + rs.getString(2));
        }
        con.close();
      }
    }
    catch (Exception ex)
    {
      Logger.getLogger(DelayNotifications.class.getName()).log(Level.SEVERE, "Delay notify err: " + ex);
    }
  }
  
  public String getStatus()
    throws SQLException
  {
    String stt = "";
    Connection vcon = this.data.connectvendit();
    String vquery = "select status from delaystatus limit 1 ";
    Statement vst = vcon.createStatement();
    ResultSet vrs = vst.executeQuery(vquery);
    if (!vrs.isBeforeFirst())
    {
      stt = "on";
      PreparedStatement prep = null;
      String values = "insert into delaystatus(status)values (?)";
      prep = vcon.prepareStatement(values);
      prep.setString(1, "on");
      prep.execute();
      prep.close();
    }
    else
    {
      while (vrs.next()) {
        stt = vrs.getString(1);
      }
    }
    vcon.close();
    return stt;
  }
  
  public void setStatus(String stt)
    throws SQLException
  {
    Connection vcon = this.data.connectvendit();
    String vquery = "select status from delaystatus limit 1 ";
    Statement vst = vcon.createStatement();
    ResultSet vrs = vst.executeQuery(vquery);
    if (!vrs.isBeforeFirst())
    {
      PreparedStatement prep = null;
      String values = "insert into delaystatus(status)values (?)";
      prep = vcon.prepareStatement(values);
      prep.setString(1, stt);
      prep.execute();
      prep.close();
    }
    else
    {
      String update = "update delaystatus set status = ?";
      PreparedStatement prep = null;
      prep = vcon.prepareStatement(update);
      prep.setString(1, stt);
      prep.executeUpdate();
      prep.close();
    }
    vcon.close();
  }
  
  public static void main(String[] arg)
  {
    DelayNotifications dn = new DelayNotifications();
    dn.updateNotifaction("off", "ETYEY22234", "56-565");
  }
}
