package com.pdsl.checkacc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.co.ipay.retail.system.bizswitch.IpayMessageWrapper;
import za.co.ipay.retail.system.bizswitch.IpayMessageWrapper2;

public class ConnectionManager
{
  private static ConnectionManager connectionManagerInstance = null;
  static Socket socket = null;
  InputStream inputStream;
  public static SSLClientTest sslconnect = new SSLClientTest();
  
  public static ConnectionManager getConnectionManagerInstance()
  {
    if (connectionManagerInstance == null) {
      connectionManagerInstance = new ConnectionManager();
    }
    return connectionManagerInstance;
  }
  
  public String readServerResponse(Socket socket)
  {
    String results = null;
    try
    {
      BufferedInputStream serverReader = new BufferedInputStream(socket.getInputStream());
      IpayMessageWrapper2 wrap = new IpayMessageWrapper2();
      StringBuilder build = new StringBuilder();
      byte[] all = wrap.unWrap(serverReader);
      for (int n = 0; n < all.length; n++)
      {
        char c = (char)all[n];
        build.append(c);
      }
      return build.toString();
    }
    catch (IOException localIOException) {}
    return "ERROR";
  }
  
  public String connection(String build)
  {
    BufferedOutputStream buff = null;
    String results = "ERROR";
    try
    {
      if (this.inputStream == null)
      {
        //this.inputStream = getClass().getClassLoader().getResourceAsStream("resource/keystore.jks");
        //socket = sslconnect.main2("41.204.194.188", 9417, "changeit", "6000", this.inputStream);//airtel live
      }
      socket = new Socket("41.204.194.188", 3520);// Test== Cellulant(3520)
      IpayMessageWrapper wrap = new IpayMessageWrapper();
      
      buff = new BufferedOutputStream(socket.getOutputStream());
      buff.write(wrap.wrap(build.getBytes()));
      buff.flush();
      
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO, "Msg to Ipay: " + build);
      
      results = getConnectionManagerInstance().readServerResponse(socket);
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO, "Msg from Ipay: " + results);
      
      return results;
    }
    catch (IOException ex)
    {
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO, "Lost Connection to Ipay");
      this.inputStream = null;
      Thread.currentThread().interrupt();
    }
    return results;
  }
}
