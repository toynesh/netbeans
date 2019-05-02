package org.tempuri;
import java.rmi.RemoteException;

import org.apache.axis2.transport.http.HTTPConstants;
import org.tempuri.KenyaAPIStub;
import org.tempuri.KenyaAPIStub.USSD_MENU;
import org.tempuri.KenyaAPIStub.MKINFO;
import org.tempuri.KenyaAPIStub.MKINFOResponse;
import org.tempuri.KenyaAPIStub.USSD_MENUResponse;
import org.tempuri.WebServiceCientUtils;
import org.tempuri.WebServiceCientCallBackHandler;
import org.tempuri.KenyaAPIStub.MPESA_PDSL;
import org.tempuri.KenyaAPIStub.SMSAPI;
public class MobiServiceAsyncHandler {
	
	public String getMenu(String re) throws RemoteException, InterruptedException  {
		
		System.out.println("Starting main");
		KenyaAPIStub stub = new KenyaAPIStub(WebServiceCientUtils.SERVICE_ENDPOINT);
		
		USSD_MENU ussdmenureq = new USSD_MENU();
	
		
		ussdmenureq.setRequestData(re);

		USSD_MENUResponse menures = stub.uSSD_MENU(ussdmenureq);
		return menures.getUSSD_MENUResult();
		
	}
       public String getMkinfo(String re) throws RemoteException, InterruptedException  {
		
		System.out.println("Starting main");
		KenyaAPIStub stub = new KenyaAPIStub(WebServiceCientUtils.SERVICE_ENDPOINT);
		int timeout = 500000000;
		stub._getServiceClient().getOptions().setProperty(
                HTTPConstants.SO_TIMEOUT, new Integer(timeout));
        stub._getServiceClient().getOptions().setProperty(
                HTTPConstants.CONNECTION_TIMEOUT, new Integer(timeout));
		MKINFO inforeq = new MKINFO();
		
		
		inforeq.setRequestData(re);
		
         MKINFOResponse infores = stub.mKINFO(inforeq);
		 WebServiceCientUtils.logMkinfoResponseDetails(infores.getMKINFOResult());
		 System.out.println(infores.getMKINFOResult());
		 return infores.getMKINFOResult();
	}


}
