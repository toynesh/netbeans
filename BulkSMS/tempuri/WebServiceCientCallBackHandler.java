package org.tempuri;
import org.apache.log4j.Logger;
import org.tempuri.KenyaAPIStub.USSD_MENUResponse;
import org.tempuri.KenyaAPIStub.MKINFOResponse;
import org.tempuri.KenyaAPIStub.MPESA_PDSLResponse;
import org.tempuri.KenyaAPIStub.SMSAPIResponse;
public class WebServiceCientCallBackHandler extends KenyaAPICallbackHandler{
	 private static final Logger logger_c = Logger.getLogger(WebServiceCientCallBackHandler.class);  
	         
	 
	 
	          @Override  
	          public Object getClientData()  
	           {  
	               return super.getClientData();  
	          }  
	          
	          @Override  
	          public void receiveResultuSSD_MENU(USSD_MENUResponse result_p)   
			        {  
			             super.receiveResultuSSD_MENU(result_p);  
			             WebServiceCientUtils.logUssdResponseDetails(result_p.getUSSD_MENUResult());  
			          }  
			         @Override  
			         public void receiveErroruSSD_MENU(java.lang.Exception e_p)  
			         {  
			              super.receiveErroruSSD_MENU(e_p);  
			              logger_c.error("An error occurred calling USSD Service", e_p);  
			      }  
		          
	          
	          @Override  
	         public void receiveResultmPESA_PDSL(MPESA_PDSLResponse result_p)   
		        {  
		             super.receiveResultmPESA_PDSL(result_p);  
		             WebServiceCientUtils.logMpesaResponseDetails(result_p.getMPESA_PDSLResult());  
		          }  
		         @Override  
		         public void receiveErrormPESA_PDSL(java.lang.Exception e_p)  
		         {  
		              super.receiveErrormPESA_PDSL(e_p);  
		              logger_c.error("An error occurred calling MPESA Service", e_p);  
		      }  
		         
		         @Override  
		         public void receiveResultmKINFO(MKINFOResponse result) {
		        	 super.receiveResultmKINFO(result);
		        	 WebServiceCientUtils.logMkinfoResponseDetails(result.getMKINFOResult());
		           }

		          /**
		           * auto generated Axis2 Error handler
		           * override this method for handling error response from mKINFO operation
		           */
		           @Override  
		            public void receiveErrormKINFO(Exception e_p) {
		        	   super.receiveErrormKINFO(e_p);
		        	   logger_c.error("An error occurred calling MPESA Service", e_p);
		            }
	          
	          
	         @Override  
	         public void receiveResultsMSAPI(SMSAPIResponse result_p)   
	        {  
	             super.receiveResultsMSAPI(result_p);  
	             WebServiceCientUtils.logSMSAPIResponseDetails(result_p.getSMSAPIResult());  
	          }  
	         @Override  
	        public void receiveErrorsMSAPI(java.lang.Exception e_p)  
	         {  
	              super.receiveErrorsMSAPI(e_p);  
	              logger_c.error("An error occurred calling MSAPI Service", e_p);  
	      }  
}
