package com.pdsl.testsms;

import cn.com.huawei.www.schema.common.v2_1.RequestSOAPHeader;
import cn.com.huawei.www.schema.common.v2_1.RequestSOAPHeaderE;
import com.huawei.sdp.sms.soap.client.NonceGenerator;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.databinding.types.URI;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub.SendSmsResponseE;
//import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsResponse;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException;
//import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsResponse;

public class SendNormal
{
 static String  correlator = "MT22260";
  //DataStore data = new DataStore();0720814702

  

  
 public static void main(String[] args) {
        /*String spid = "601469";
	String serviceid = "6014692000106159";
	String accesscode ="22260";*/
      /* 6014692000132612 22260*/
        String spid = "601469";
        String serviceid ="6014692000132612";
        String accesscode ="22260";
        String message = "Testing on demand short code 22260" ;
        String address = "254727236699";
        String linkid="504021504471708301344295627003";



  //sendSms(createHeader(), createBody(address,destination,message));
       sendSms(createHeader(spid,serviceid,linkid), createBody(linkid,address, message, accesscode));  

        System.out.println("Sms done !!");
    }



    

public static  RequestSOAPHeaderE createHeader(String spid, String serviceid, String lnkid)
  {
    RequestSOAPHeaderE requestHeaderE = new RequestSOAPHeaderE();
    RequestSOAPHeader requestHeader = new RequestSOAPHeader();

    String spPassword = "Sdp$2018";

    String spId = spid;

    String serviceId = serviceid;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
    String created = sdf.format(Calendar.getInstance().getTime());
    String password = NonceGenerator.getInstance().MD5Gen(spId + spPassword + created);

    requestHeader.setSpId(spId);

    requestHeader.setSpPassword(password);
    requestHeader.setServiceId(serviceId);

   requestHeader.setLinkid(lnkid);
   requestHeader.setOA(spId);
   requestHeader.setFA(spId);

    requestHeader.setTimeStamp(created);
   // requestHeader;

    requestHeaderE.setRequestSOAPHeader(requestHeader);
    return requestHeaderE;
  }

  public static SendSmsServiceStub.SendSmsE createBody(String linkID,String addressme, String message, String code)
  {
    try
    {
      System.out.println("message: " + message);
      System.out.println("address: " + addressme);

      URI address = new URI("tel:" + addressme);

      URI endpoint = new URI("http://197.248.61.165:8089/Notification/SmsNotificationService");
      SendSmsServiceStub.ChargingInformation charging = new SendSmsServiceStub.ChargingInformation();
     /* charging.setAmount(new BigDecimal(1));
      charging.setCode("0");
      charging.setCurrency("KES");
      charging.setDescription("description");*/
      SendSmsServiceStub.SimpleReference sim = new SendSmsServiceStub.SimpleReference();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
      String created = sdf.format(Calendar.getInstance().getTime());
      sim.setCorrelator(correlator);
      //this.correlator = created;

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
    SendSmsServiceStub stub = new SendSmsServiceStub("http://127.0.0.1:8030/SendSmsService/services/SendSms");
             //  SendSmsServiceStub stub = new SendSmsServiceStub("http://196.201.216.13:8310/SendSmsService/services/SendSms");
	 //   SendSmsServiceStub stub = new SendSmsServiceStub("http://svc.safaricom.com:8310/SendSmsService/services/SendSms");
	       stub._getServiceClient().addHeader(header.getOMElement(RequestSOAPHeaderE.MY_QNAME, OMAbstractFactory.getSOAP11Factory()));
	 
	        SendSmsResponseE response = stub.sendSms(body);
      OMElement it=response.getOMElement(SendSmsServiceStub.SendSmsResponseE.MY_QNAME, OMAbstractFactory.getSOAP11Factory()).getFirstElement();
                System.out.println(correlator);


          /*Iterator iter = it.getAllDeclaredNamespaces();
           while (iter.hasNext()) {
            OMNamespace ns = (OMNamespace) iter.next();
            System.out.println(ns.getText());
        }*/
             /*while(it.hasNext()){
                Element el=(Element) it.next();
                
                System.out.println(it.getText());
            }*/
                System.out.println(it.getText());
    }//0716335660///0721869537p[
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
