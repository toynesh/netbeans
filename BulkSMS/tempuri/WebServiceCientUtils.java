package org.tempuri;
import org.apache.log4j.Logger;
import org.tempuri.KenyaAPICallbackHandler;
public class WebServiceCientUtils extends KenyaAPICallbackHandler
{

		private static final Logger logger_c = Logger.getLogger(WebServiceCientUtils.class);
		protected final static String SERVICE_ENDPOINT = "http://14.141.212.201/kenyaSMSAPI/KenyaAPI.asmx?WSDL";
		private static final String NEW_LINE = "\n";


		protected static void logUssdResponseDetails(String account_p)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(NEW_LINE);
			sb.append("Account Number: ").append(account_p).append(NEW_LINE);
			System.out.println(account_p);
			
			logger_c.debug(sb);
		}
		protected static void logMpesaResponseDetails(String account_p)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(NEW_LINE);
			sb.append("Account Number: ").append(account_p).append(NEW_LINE);
		
			System.out.println(account_p);
			logger_c.debug(sb);
		}
		protected static void logMkinfoResponseDetails(String account_p)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(NEW_LINE);
			sb.append("Account Number: ").append(account_p).append(NEW_LINE);
		
			System.out.println(account_p);
			logger_c.debug(sb);
		}
		protected static void logSMSAPIResponseDetails(String account_p)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(NEW_LINE);
			sb.append("Account Number: ").append(account_p).append(NEW_LINE);
			System.out.println(account_p);
			
			logger_c.debug(sb);
		}
}