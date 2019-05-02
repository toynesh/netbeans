package za.co.ipay.retail.system.bizswitch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import org.apache.tools.bzip2.CBZip2OutputStream;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import za.co.ipay.retail.common.config.IConfiguration;
import za.co.ipay.retail.common.context.IContext;
import za.co.ipay.retail.common.context.IContextConfiguration;
import za.co.ipay.retail.common.context.ILogger;
import za.co.ipay.retail.common.exception.ICommsException;

public class BizswitchConnection
{
  public static final String IPAY_LOGON = "ipay.co.za";
  public static final String IPAY_PASSWORD = "pass";
  public static final String IPAY_TERMINAL = IContext.configuration.terminalId;
  public static final int MAX_CONNECTION_ATTEMPTS = 1;
  private static final String keyStorePassword = "531753";
  private Socket socket;
  private BufferedInputStream inputStream;
  private BufferedOutputStream outputStream;
  private String ipAddress;
  private int port;
  private String connectionId;
  private IpayMessageWrapper ipayMessageWrapper = new IpayMessageWrapper();
  private IpayXmlMessageFactory ipayXmlMessageFactory = new IpayXmlMessageFactory();
  private boolean connected = false;

  public BizswitchConnection(String ipAddress, int ipPort, String connectionId)
    throws IOException
  {
    IContextConfiguration.logger.entering(new Object[] { ipAddress, Integer.valueOf(ipPort) });
    this.ipAddress = ipAddress;
    this.port = ipPort;
    this.connectionId = connectionId;
    int i = 0; if (i < 1)
      try {
        if (this.connected) {
          IContextConfiguration.logger.warning("already connected");
        }
        else
          connect();
      }
      catch (IOException ioe) {
        IContextConfiguration.logger.severe(new Object[] { ioe });
        if (this.socket != null) {
          this.socket.close();
        }
        throw ioe;
      }
  }

  public IpayXmlMessageFactory getMessageFactory()
  {
    return this.ipayXmlMessageFactory;
  }

  public String renderXML(Message msg) {
    XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
    return xmlo.outputString(((IpayXmlMessage)msg).ipayMsgElement);
  }

  public void sendMessage(Message ipayXmlMessage) throws IOException {
    IContextConfiguration.logger.entering(new Object[] { Boolean.valueOf(this.connected) });
    IContext.messageLogger.println(null, new Object[] { renderXML(ipayXmlMessage) });
    byte[] msgBytes = ipayXmlMessage.getBytes();
    if (!this.connected) {
      throw new IOException("No connection");
    }
    if (!IContext.configuration.isCompression()) {
      this.outputStream.write(this.ipayMessageWrapper.wrap(msgBytes));
      this.outputStream.flush();
    }
    else {
      ByteArrayOutputStream compBaos = new ByteArrayOutputStream(msgBytes.length);
     // OutputStream compOs;
      OutputStream compOs;
      if (IContext.configuration.isBzip2()) {
        IContext.messageLogger.entering("***BZIP2 compression enabled***");
        compBaos.write(66);
        compBaos.write(90);
        compOs = new CBZip2OutputStream(compBaos, 1);
      } else {
        compOs = null;
      }
      compOs.write(msgBytes);
      compOs.close();
      msgBytes = compBaos.toByteArray();
      int retry = IContext.context.connectionRetry;
      try
      {
        this.outputStream.write(this.ipayMessageWrapper.wrap(msgBytes));
        this.outputStream.flush();
      } catch (IOException e) {
        while (true) {
          retry--; if (retry <= 0) break;
          connect();
        }

        throw e;
      }

      compBaos.close();
    }
  }

  public Message receiveResponseMessage(int timeout) throws IOException, ICommsException {
    Message returnMessage = null;
    if (!this.connected) {
      throw new ICommsException("No connection", new Object[0]);
    }
    this.socket.setSoTimeout(timeout);
    try {
      returnMessage = this.ipayXmlMessageFactory.buildMessage(this.ipayMessageWrapper.unWrap(this.inputStream), null, -1L);
    } catch (SocketTimeoutException ste) {
      throw ste;
    }
    IContext.messageLogger.println(null, new Object[] { "-- Received message --\n" });
    IContext.messageLogger.println(null, new Object[] { renderXML(returnMessage) });
    return returnMessage;
  }

  protected void connect() throws IOException {
    IContextConfiguration.logger.entering(new Object[] { this.ipAddress, Integer.valueOf(this.port), this.connectionId });
    if (this.socket != null) {
      try {
        this.socket.close();
      } catch (IOException ioe) {
        IContextConfiguration.logger.warning(new Object[] { ioe });
      }
    }
    this.connected = false;
    close(this.socket);
    try {
      this.socket = createSocket();
      this.outputStream = new BufferedOutputStream(this.socket.getOutputStream());
      this.inputStream = new BufferedInputStream(this.socket.getInputStream());
      this.connected = true;
    } catch (Exception e) {
      IContextConfiguration.logger.warning(new Object[] { e, this.ipAddress, Integer.valueOf(this.port) });
    }
  }

  protected void close(Socket socket) {
    try {
      if (socket != null)
        socket.close();
    }
    catch (IOException ioe) {
      IContextConfiguration.logger.warning(new Object[] { ioe });
    }
  }

  protected Socket createSocket() throws Exception {
    File localKeyStoreFile = new File(IContext.context.localKeyStoreFile);
    File remoteKeyStoreFile = new File(IContext.context.remoteKeyStoreFile);
    if ((!localKeyStoreFile.exists()) || (!remoteKeyStoreFile.exists())) {
      return new Socket(this.ipAddress, this.port);
    }
    IContextConfiguration.logger.finest(IContext.context.localKeyStoreFile);
    KeyStore localKeyStore = KeyStore.getInstance("JKS");
    localKeyStore.load(new FileInputStream(localKeyStoreFile), "531753".toCharArray());
    KeyStore remoteKeyStore = KeyStore.getInstance("JKS");
    remoteKeyStore.load(new FileInputStream(remoteKeyStoreFile), "531753".toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(remoteKeyStore);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(localKeyStore, "531753".toCharArray());
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextInt();
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), secureRandom);
    SSLSocketFactory sf = sslContext.getSocketFactory();
    return sf.createSocket(this.ipAddress, this.port);
  }

  public boolean isConnected() {
    return this.connected;
  }

  public void reconnect() throws IOException {
    connect();
  }

  public void close() {
    close(this.socket);
  }
}