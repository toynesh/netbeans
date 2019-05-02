package metro_sdp;

import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import metro.sdp.ChargingInformation;
import metro.sdp.DeliveryInformation;
import metro.sdp.PolicyException_Exception;
import metro.sdp.SendSms;
import metro.sdp.SendSmsService;
import metro.sdp.ServiceException_Exception;
import metro.sdp.SimpleReference;
import metro.sdp.SmsFormat;

public class Metro_sdp
{
  public static void main(String[] args)
  {
    Metro_sdp sdp = new Metro_sdp();

    sdp.sendSdpSms("254728064120", "test", "testing", "704307");
  }

  public void sendSdpSms(String msisdn, String message, String correlator, String shortcode)
  {
    try {
      SimpleReference ref = new SimpleReference();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
      String created = sdf.format(Calendar.getInstance().getTime());

      URI endpoint = new URI("http://197.248.31.217:8084/Notification/SmsNotificationService");

      ref.setEndpoint(endpoint.toString());
      ref.setInterfaceName("SmsNotification");
      ref.setCorrelator(correlator);

      ChargingInformation charg = new ChargingInformation();
      ArrayList list = new ArrayList();
      list.add("tel:" + msisdn);
      List address = list;

      String msg = sendSms(address, shortcode, charg, message, ref);
      System.out.println("RESULTS: " + msg+""+endpoint.toString());
    }
    catch (URISyntaxException ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    } catch (PolicyException_Exception ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ServiceException_Exception ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void sendSdpSms(ArrayList addresses, String message, String correlator, String shortcode)
  {
    try
    {
      SimpleReference ref = new SimpleReference();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
      String created = sdf.format(Calendar.getInstance().getTime());

      URI endpoint = new URI("http://197.248.31.217:8084/Notification/SmsNotificationService");

      ref.setEndpoint(endpoint.toString());
      ref.setInterfaceName("SmsNotification");
      ref.setCorrelator(correlator);

      ChargingInformation charg = new ChargingInformation();

      List address = addresses;

      String msg = sendSms(address, shortcode, charg, message, ref);
      System.out.println("RESULTS: " + msg+""+endpoint.toString());
    }
    catch (URISyntaxException ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    } catch (PolicyException_Exception ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ServiceException_Exception ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public String sendSdpSms(ArrayList addresses, String message, String correlator, String shortcode, String uri)
  {
    try
    {
      SimpleReference ref = new SimpleReference();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
      String created = sdf.format(Calendar.getInstance().getTime());

      URI endpoint = new URI(uri);

      ref.setEndpoint(endpoint.toString());
      ref.setInterfaceName("SmsNotification");
      ref.setCorrelator(correlator);

      ChargingInformation charg = new ChargingInformation();

      List address = addresses;

      String msg = sendSms(address, shortcode, charg, message, ref);
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.INFO,  "RESULTS:"+msg);
      //System.out.println("RESULTS: " + msg);
      return msg;
    }
    catch (URISyntaxException ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    } catch (PolicyException_Exception ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ServiceException_Exception ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    }
    return "ERROR: SDP FAIL ";
  }
  
  public String sendSdpSms(String msisdn, String message, String correlator, String shortcode, String uri)
  {
    try
    {
      SimpleReference ref = new SimpleReference();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
      String created = sdf.format(Calendar.getInstance().getTime());

      URI endpoint = new URI(uri);

      ref.setEndpoint(endpoint.toString());
      ref.setInterfaceName("SmsNotification");
      ref.setCorrelator(correlator);

      ChargingInformation charg = new ChargingInformation();
      ArrayList list = new ArrayList();
      list.add("tel:" + msisdn);
      List address = list;

      String msg = sendSms(address, shortcode, charg, message, ref);
     Logger.getLogger(Metro_sdp.class.getName()).log(Level.INFO, "RESULTS:"+msg);
      //System.out.println("RESULTS: " + msg);
      return msg;
    }
    catch (URISyntaxException ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    } catch (PolicyException_Exception ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ServiceException_Exception ex) {
      Logger.getLogger(Metro_sdp.class.getName()).log(Level.SEVERE, null, ex);
    }
    return "ERROR: SDP FAIL";
  }

  private static String sendSms(List<String> addresses, String senderName, ChargingInformation charging, String message, SimpleReference receiptRequest)
    throws PolicyException_Exception, ServiceException_Exception
  {
    SendSmsService service = new SendSmsService();
    SendSms port = service.getSendSms();
    return port.sendSms(addresses, senderName, charging, message, receiptRequest);
  }

  private static List<DeliveryInformation> getSmsDeliveryStatus(String requestIdentifier) throws PolicyException_Exception, ServiceException_Exception {
    SendSmsService service = new SendSmsService();
    SendSms port = service.getSendSms();
    return port.getSmsDeliveryStatus(requestIdentifier);
  }

  private static String sendSmsRingtone(List<String> addresses, String senderName, ChargingInformation charging, String ringtone, SmsFormat smsFormat, SimpleReference receiptRequest) throws PolicyException_Exception, ServiceException_Exception
  {
    SendSmsService service = new SendSmsService();
    SendSms port = service.getSendSms();
    return port.sendSmsRingtone(addresses, senderName, charging, ringtone, smsFormat, receiptRequest);
  }
}