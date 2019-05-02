package com.pdsl.testsms;

import cn.com.huawei.www.schema.common.v2_1.RequestSOAPHeader;
import cn.com.huawei.www.schema.common.v2_1.RequestSOAPHeaderE;
import com.huawei.sdp.sms.soap.client.NonceGenerator;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub.ChargingInformation;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub.SendSms;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub.SendSmsE;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub.SendSmsResponseE;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub.SimpleReference;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException;

public class SendNormal
{
  String correlator = null;
  //DataStore data = new DataStore();
  
public static void main(String[] args) {
//KPLC
 /*   String spid = "601470";
        String serviceid = "6014702000101281";
        String accesscode = "706805";

//fepkisima
  /*String spid = "601470";
        String serviceid = "6014702000019667";
        String accesscode = "704304"; */
		
	

//Vendit

	String spid = "601470";
   	String serviceid = "6014702000142672";
      	String accesscode = "706811";

//22225  0729379544 0702000344 0829 wendyshop 254713288730
/*String spid = "601470";
   	String serviceid = "6014702000022206";
      	String accesscode = "704308";
*/

String message="token: 23363903764072120702 for 350Ksh";
String address = "254725406783";

       sendSms(createHeader(address, spid, serviceid), createBody(address, message, accesscode));  

        System.out.println("Sms sent out!!");  
    }

	//0933 0705609681 50   route add default gw 197.248.31.222
public static  RequestSOAPHeaderE createHeader(String address, String spid, String serviceid) 
  {
    RequestSOAPHeaderE requestHeaderE = new RequestSOAPHeaderE();
    RequestSOAPHeader requestHeader = new RequestSOAPHeader();

    String spPassword = "Pdsl#6045";

    String spId = spid;

    String serviceId = serviceid;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
    String created = sdf.format(Calendar.getInstance().getTime());
    String password = NonceGenerator.getInstance().MD5Gen(spId + spPassword + created);// 0717112981 

    requestHeader.setSpId(spId);

    requestHeader.setSpPassword(password);
    requestHeader.setServiceId(serviceId);

   

    requestHeader.setTimeStamp(created);

    requestHeaderE.setRequestSOAPHeader(requestHeader);
    return requestHeaderE;
  }

  public static SendSmsServiceStub.SendSmsE createBody(String addressme, String message, String code)
  {
    try
    {
      System.out.println("message: " + message);
      System.out.println("address: " + addressme);

      URI address = new URI("tel:" + addressme);

      URI endpoint = new URI("http://197.248.31.217:8084/Notification/SmsNotificationService");
      SendSmsServiceStub.ChargingInformation charging = new SendSmsServiceStub.ChargingInformation();
      charging.setAmount(new BigDecimal(1));
      charging.setCode("111");
      charging.setCurrency("KES");
      charging.setDescription("description");
      SendSmsServiceStub.SimpleReference sim = new SendSmsServiceStub.SimpleReference();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
      String created = sdf.format(Calendar.getInstance().getTime());
      sim.setCorrelator(created);
     // this.correlator = created;

      sim.setEndpoint(endpoint);
      sim.setInterfaceName("SmsNotification");
      SendSmsServiceStub.SendSms param = new SendSmsServiceStub.SendSms();

      param.addAddresses(address);

      param.setMessage(message);
      param.setReceiptRequest(sim);
      param.setSenderName(code);

      SendSmsServiceStub.SendSmsE sendSms = new SendSmsServiceStub.SendSmsE();
      sendSms.setSendSms(param);
      return sendSms; } catch (URI.MalformedURIException e) {
    }
    return null;
  }

  public static void sendSms(RequestSOAPHeaderE header, SendSmsServiceStub.SendSmsE body)
  {
    try
    {

    SendSmsServiceStub stub = new SendSmsServiceStub("http://196.201.216.13:8310/SendSmsService/services/SendSms");
   //SendSmsServiceStub stub = new SendSmsServiceStub("http://127.0.0.1:8030/SendSmsService/services/SendSms");
	 
	       stub._getServiceClient().addHeader(header.getOMElement(RequestSOAPHeaderE.MY_QNAME, OMAbstractFactory.getSOAP11Factory()));
	 
	        SendSmsResponseE response = stub.sendSms(body);
    }
    catch (AxisFault e)
    {
      SendSmsServiceStub.SendSmsResponseE response;
      e.printStackTrace();
    }
    catch (RemoteException e) {
      e.printStackTrace();
    }
    catch (PolicyException e) {
      e.printStackTrace();
    }
    catch (ServiceException e) {
      e.printStackTrace();
    }
    catch (ADBException e) {
      e.printStackTrace();
    }
  }
}
