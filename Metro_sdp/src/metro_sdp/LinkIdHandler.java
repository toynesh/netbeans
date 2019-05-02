package metro_sdp;

import java.io.PrintStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class LinkIdHandler
  implements SOAPHandler<SOAPMessageContext>
{
  DataStore data = new DataStore();
  Connection con = null;

  public LinkIdHandler() {
    this.con = this.data.connect2();
  }

  public boolean handleMessage(SOAPMessageContext context)
  {
    System.out.println("Client : handleMessage()......");

    Boolean isRequest = (Boolean)context.get("javax.xml.ws.handler.message.outbound");

    if (isRequest.booleanValue()) {
      try
      {
        SOAPMessage soapMsg = context.getMessage();
        SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
        SOAPBody soapBody = soapEnv.getBody();

        NodeList addresses = soapBody.getElementsByTagName("ns2:addresses");
        NodeList sendername = soapBody.getElementsByTagName("ns2:senderName");
        NodeList correlator = soapBody.getElementsByTagName("correlator");
        String shortcode = sendername.item(0).getTextContent();
        String productcode = correlator.item(0).getTextContent();
        String msisdn = addresses.item(0).getTextContent();
        String[] msidn = msisdn.split(":");
        msisdn = msidn[1];

        String linkid = "0";
        ArrayList array = details(shortcode);

        System.out.println("Link Id :" + linkid);

        String spId = "" + array.get(0);
        String serviceId = "" + array.get(1);
        String spPassword = "" + array.get(2);
        System.out.println("Password :" + spPassword);
        System.out.println(serviceId);

        String update = "update ondemand set process_status=1 where linkid=" + linkid;

        SOAPHeader soapHeader = soapEnv.getHeader();

        if (soapHeader == null) {
          soapHeader = soapEnv.addHeader();
        }

        String mac = "29299230394";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String created = sdf.format(Calendar.getInstance().getTime());
        String password = NonceGenerator.getInstance().MD5Gen(spId + spPassword + created);

        QName qname = new QName("http://www.csapi.org/schema/parlayx/sms/send/v2_2/local/", "ns1:RequestSOAPHeader");
        SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(qname);
        soapHeaderElement.addChildElement("spId", "ns1", "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local/").addTextNode(spId);
        soapHeaderElement.addChildElement("serviceId", "ns1", "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local/").addTextNode(serviceId);
        soapHeaderElement.addChildElement("spPassword", "ns1", "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local/").addTextNode(password);
        soapHeaderElement.addChildElement("timeStamp", "ns1", "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local/").addTextNode(created);
        if (linkid.length() > 2) {
          soapHeaderElement.addChildElement("linkid", "ns1", "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local/").addTextNode(linkid);
          soapHeaderElement.addChildElement("OA", "ns1", "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local/").addTextNode(msisdn);

          soapHeaderElement.addChildElement("FA", "ns1", "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local/").addTextNode(msisdn);
        }

        soapMsg.saveChanges();
      }
      catch (SOAPException e)
      {
        System.err.println(e);
      }

    }

    return true;
  }

  public boolean handleFault(SOAPMessageContext context)
  {
    System.out.println("Server : handleFault()......");

    return true;
  }

  public void close(MessageContext context)
  {
    System.out.println("Server : close()......");
  }

  public Set<QName> getHeaders()
  {
    System.out.println("Server : getHeaders()......");
    return null;
  }

  private void generateSOAPErrMessage(SOAPMessage msg, String reason) {
    try {
      SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
      SOAPFault soapFault = soapBody.addFault();
      soapFault.setFaultString(reason);
      throw new SOAPFaultException(soapFault);
    }
    catch (SOAPException e) {
    }
  }

  public void Nodeprocess(String strMsg) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try
    {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(new InputSource(new StringReader(strMsg)));

      NodeList el = document.getElementsByTagName("ns1:linkid");
      NodeList el2 = document.getElementsByTagName("smsServiceActivationNumber");
      NodeList el3 = document.getElementsByTagName("senderAddress");
      NodeList el4 = document.getElementsByTagName("ns2:correlator");
      NodeList el5 = document.getElementsByTagName("message");
      int len = el.getLength();
      System.out.println(" Lenght of the node : " + len);
      if (len >= 1) {
        for (int n = 0; n < len; n++) {
          System.out.println(" Code is  : " + n);
          String shortcode = el2.item(0).getTextContent();
          String msisdn = el3.item(0).getTextContent();
          String linkid = el.item(0).getTextContent();
          String correlator = el4.item(0).getTextContent();
          String message = el5.item(0).getTextContent();
          String[] msidn = msisdn.split(":");
          msisdn = "254" + msidn[1];
          msidn = shortcode.split(":");
          shortcode = msidn[1];
          System.err.println("message  :  " + message);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public String getLinkid(String shortcode, String msisdn)
  {
    String linkid = "0";
    try
    {
      String query = "select linkid from ondemand where scode='" + shortcode + "' and msisdn='" + msisdn + "' and process_status=0";
      PreparedStatement prep = this.con.prepareStatement(query);
      ResultSet rst = prep.executeQuery();

      while (rst.next()) {
        linkid = rst.getString(1);
      }

      System.out.println(query);
      System.out.println(linkid);

      return linkid;
    } catch (SQLException ex) {
      Logger.getLogger(LinkIdHandler.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      try {
        this.con.close();
      } catch (SQLException ex) {
        Logger.getLogger(LinkIdHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return "0";
  }

  public ArrayList details(String shortcode) {
    ArrayList array = new ArrayList();
    try
    {
      String query = "select spid,serviceid,password from scodeManager where accesscode='" + shortcode + "'";
      PreparedStatement prep = this.con.prepareStatement(query);
      ResultSet rst = prep.executeQuery();
      System.out.println(query);
      while (rst.next()) {
        array.add(rst.getString(1));
        array.add(rst.getString(2));
        array.add(rst.getString(3));
      }
    }
    catch (SQLException ex) {
      Logger.getLogger(LinkIdHandler.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      try {
        this.con.close();
      } catch (SQLException ex) {
        Logger.getLogger(LinkIdHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return array;
  }

  public void sendsms(String address, String message, String linkid, String code) {
    System.out.println("The scode to pick is " + code);
    ArrayList array = details(code);

    String spid = "" + array.get(0);
    String serviceid = "" + array.get(1);
    String accesscode = "" + array.get(2);
    if (accesscode.length() > 2) {
      code = accesscode;
    }

    String update = "update linkids set process_status=1 where linkid=" + linkid;
    this.data.insert(update);
  }
}