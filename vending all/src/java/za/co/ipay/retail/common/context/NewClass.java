/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.ipay.retail.common.context;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author tony test
 */
public class NewClass {
	
	 public static void main2(String[] args) {
		 NewClass n = new NewClass();
		 n.settlementBill("254721178823", "256325465-56", "500", "60546", "5423", "2015-11-02", "equity", "donno"); 
		 
	 }
	    
     public String settlementBill(String msisdn, String account_no, String amount, String seq, String reference, String time, String client, String term) {
     StreamResult result = new StreamResult(new StringWriter());
     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     try {
         DocumentBuilder builder = factory.newDocumentBuilder();

         Document document = builder.newDocument();
         Element root = document.createElement("ipayMsg");
         document.appendChild(root);
         root.setAttribute("client", client);
         root.setAttribute("term", term);
         root.setAttribute("seqNum", seq);
         root.setAttribute("time", time);

         Element billpay = document.createElement("billPayMsg");
         root.appendChild(billpay);

         billpay.setAttribute("ver", "1.7");

         Element payreq = document.createElement("payReq");
         billpay.appendChild(payreq);

         Element ref = document.createElement("ref");
         ref.setTextContent(reference);
         payreq.appendChild(ref);

         Element provider = document.createElement("providerName");
         provider.setTextContent("KPLC Provider");
         payreq.appendChild(provider);

         Element custAccNum = document.createElement("custAccNum");
         //custAccNum.setTextContent("1234567-89");
         custAccNum.setTextContent(account_no);
         payreq.appendChild(custAccNum);

         Element payType = document.createElement("payType");
         payType.setTextContent("cash");
         payreq.appendChild(payType);

         Element payment = document.createElement("payment");
         payreq.appendChild(payment);

         Element accId = document.createElement("accId");
         accId.setTextContent(account_no);
         payment.appendChild(accId);

         Element amt = document.createElement("amt");
         amt.setAttribute("cur", "KES");
         amt.setTextContent(amount);
         payment.appendChild(amt);

         Element posRef = document.createElement("posRef");
         posRef.setTextContent("payreq");
         payreq.appendChild(posRef);

         Element notify = document.createElement("notify");
         notify.setAttribute("notifyMethod", "sms");
         notify.setTextContent(msisdn);
         payreq.appendChild(notify);

         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         //OutputFormat format = new OutputFormat(document);
         //format.setIndenting(true);
         //format.setOmitXMLDeclaration(true);

         DOMSource source = new DOMSource(document);
         //  StreamResult result = new StreamResult(new StringWriter());
         // Output to console for testing
         // StreamResult result = new StreamResult(System.out);
         transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
         transformer.transform(source, result);
         System.out.println("Transformed xml :" + result.getWriter().toString());
         // System.out.println("File saved!");
         //Document document = builder.parse(new InputSource(new StringReader(strMsg)));
         
         //LOG TRANS;
        
     } catch (Exception e) {
         e.printStackTrace();
     }
     System.out.println( result.getWriter().toString());
     return result.getWriter().toString();
 }
    
}
