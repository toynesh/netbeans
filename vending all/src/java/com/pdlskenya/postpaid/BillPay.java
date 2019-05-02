package com.pdlskenya.postpaid;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author koks
 */
public class BillPay {

    static String xml = "<ipayMsg client=\"test\" term=\"00001\" seqNum=\"65\" time=\"2015-02-12 16:09:25 +0200\">\n"
            + "  <billPayMsg ver=\"1.6\">\n"
            + "    <payLastRes>\n"
            + "      <ref>50431609256518259</ref>\n"
            + "      <res code=\"billPay000\">Ok</res>\n"
            + "      <payRes>\n"
            + "        <ref>50431609255768257</ref>\n"
            + "        <res code=\"billPay000\">Ok</res>\n"
            + "        <providerName>KPLC Provider</providerName>\n"
            + "        <rctNum>150431609255823917</rctNum>\n"
            + "        <custAccNum>1234567-89</custAccNum>\n"
            + "        <transaction>\n"
            + "          <accDetail>\n"
            + "            <accId>1234567-89</accId>\n"
            + "            <accName>KPLC Provider</accName>\n"
            + "            <accType />\n"
            + "          </accDetail>\n"
            + "          <amt cur=\"ZAR\">1000</amt>\n"
            + "          <tax>0</tax>\n"
            + "          <rctNum />\n"
            + "        </transaction>\n"
            + "      </payRes>\n"
            + "      <payTime>2015-02-12 16:09:25 +0200</payTime>\n"
            + "    </payLastRes>\n"
            + "  </billPayMsg>\n"
            + "</ipayMsg> ";

    String response = "<ipayMsg client=\"test\" term=\"00001\" seqNum=\"35\" time=\"2015-02-12 16:08:46 +0200\">\n"
            + "  <billPayMsg ver=\"1.7\" finAdj=\"-1000\">\n"
            + "    <payRes>\n"
            + "      <ref>50431608466308219</ref>\n"
            + "      <res code=\"billPay000\">Ok</res>\n"
            + "      <providerName>KPLC Provider</providerName>\n"
            + "      <rctNum>150431608466380722</rctNum>\n"
            + "      <custAccNum>1234567-89</custAccNum>\n"
            + "      <transaction>\n"
            + "        <accDetail>\n"
            + "          <accId>1234567-89</accId>\n"
            + "          <accName>KPLC Provider</accName>\n"
            + "          <accType />\n"
            + "        </accDetail>\n"
            + "        <amt cur=\"ZAR\">1000</amt>\n"
            + "        <tax>0</tax>\n"
            + "        <rctNum />\n"
            + "      </transaction>\n"
            + "    </payRes>\n"
            + "  </billPayMsg>\n"
            + "</ipayMsg> ";
/*
    public static void main(String[] arg) {
        BillPay billpay = new BillPay();
        // billpay.getAcocuntDetails(xml);
        // billpay.settlementBill();
        billpay.responsePostPay();

    }
*/
    public void responsePostPay() {
        //String response = "";
        ArrayList array = new XML().xmlGet(response, "billPayMsg", "payRes");
        String receipt = "ReceiptNumber" + (String) new XML().xmlGet(response, "billPayMsg", "rctNum").get(0);
        String custAccNum = "custAccNum" + (String) new XML().xmlGet(response, "billPayMsg", "custAccNum").get(0);
        String amt = "amt" + (String) new XML().xmlGet(response, "billPayMsg", "amt").get(0);
        //String receipt = "ReceiptNumber" + (String) new XML().xmlGet(response, "billPayMsg", "rctNum").get(0);
        String attrib = new XML().xmlGetAttrib(response, "billPayMsg", "res", "code");
        System.out.println(array.size() + "The Attrib is " + attrib);

        if (attrib.equals("billPay000")) {
            System.out.println("The token generation is succesful");
        } else if (attrib.equals("billPay004")) {
            //Reversal Not Supported
            System.out.println("Reversal Not supported");
        } else if (attrib.equals("billPay002")) {
            //Service not available. Service provider is down   
        } else if (attrib.equals("billPay003")) {
            //No record of previous transactions
        } else if (attrib.equals("billPay005")) {
            //Non unique reference. The reference of the request message for this
            //client and terminal combination already exists in the system. 

        } else if (attrib.equals("billPay010")) {
            //Unknown account or meter number.

        } else if (attrib.equals("billPay013")) {
           //Invalid Amount. The upper or lower limit on the amount that may 
            //in a single transaction or over a period of time has been passed.

        } else if (attrib.equals("billPay015")) {
          // Amount too high. Request amount exceeds the maximum for this
            //payment. Try a smaller amount.
        } else if (attrib.equals("billPay016")) {
        //Amount too low. Request amount exceeds the minimum amount for this 
            //payment. Try a larger amount.
        } else if (attrib.equals("billPay018")) {
         //Multiple item payment not supported. Paying of multiple account items
            //not supported

        } else if (attrib.equals("billPay019")) {
          //Already reversed. The request cannot be processed due to the past 
            //transaction it refers to already having been reversed.

        } else if (attrib.equals("billPay020")) {
        // Transaction already completed. The request cannot be processed due
            // to the past transaction it refers to already having been completed.

        } else if (attrib.equals("billPay023")) {
        //Invalid PaymentType. The payment type specified in the request in not 
            //recognized.

        } else if (attrib.equals("billPay030")) {
        //  Invalid Format. The format of the request or response is incorrect.

        } else if (attrib.equals("billPay036")) {
        // Reprint by ref not supported. The supplier does not support reprints by 
            //reference.

        } else if (attrib.equals("billPay020")) {
        // Transaction already completed. The request cannot be processed due
            // to the past transaction it refers to already having been completed.

        }

        /*for(int n=0;n<array.size();n++){
         System.out.println("\n"+array.get(n));
         }*/
    }

    public boolean responsePrepaid(String responses) {
        //String response = "";
        ArrayList array = new XML().xmlGet(responses, "elecMsg", "stdToken");
        String attrib = new XML().xmlGetAttrib(responses, "elecMsg", "res", "code");
        System.out.println("The Attrib is " + attrib);
        if (attrib.equals("elec000")) {
            System.out.println("The token generation is succesful");
            return true;
        } else if (attrib.equals("elec004")) {
            //Reversal Not Supported
            System.out.println("Reversal Not supported");
        } else if (attrib.equals("elec002")) {
            //Service not available. Service provider is down   
        } else if (attrib.equals("elec003")) {
            //No record of previous transactions
        } else if (attrib.equals("elec005")) {
            //Non unique reference. The reference of the request message for this
            //client and terminal combination already exists in the system. 

        } else if (attrib.equals("elec010")) {
            //Unknown account or meter number.

        } else if (attrib.equals("elec013")) {
           //Invalid Amount. The upper or lower limit on the amount that may 
            //in a single transaction or over a period of time has been passed.

        } else if (attrib.equals("elec015")) {
          // Amount too high. Request amount exceeds the maximum for this
            //payment. Try a smaller amount.
        } else if (attrib.equals("elec016")) {
        //Amount too low. Request amount exceeds the minimum amount for this 
            //payment. Try a larger amount.
        } else if (attrib.equals("elec018")) {
         //Multiple item payment not supported. Paying of multiple account items
            //not supported

        } else if (attrib.equals("elec019")) {
          //Already reversed. The request cannot be processed due to the past 
            //transaction it refers to already having been reversed.

        } else if (attrib.equals("elec020")) {
        // Transaction already completed. The request cannot be processed due
            // to the past transaction it refers to already having been completed.

        } else if (attrib.equals("elec023")) {
        //Invalid PaymentType. The payment type specified in the request in not 
            //recognized.

        } else if (attrib.equals("elec030")) {
        //  Invalid Format. The format of the request or response is incorrect.

        } else if (attrib.equals("elec036")) {
        // Reprint by ref not supported. The supplier does not support reprints by 
            //reference.

        } else if (attrib.equals("elec020")) {
        // Transaction already completed. The request cannot be processed due
            // to the past transaction it refers to already having been completed.

        }

        /*for(int n=0;n<array.size();n++){
         System.out.println("\n"+array.get(n));
         }*/
        return false;
    }

    public void getList() {

    }

    public void buildxml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            Element root = document.createElement("ipayMsg");
            document.appendChild(root);
            root.setAttribute("client", "Mobikash");
            root.setAttribute("term", "00001");
            root.setAttribute("seqNum", "36");
            root.setAttribute("time", "2015-02-23 12:05:39 +0200");

            Element billpay = document.createElement("elecMsg");
            root.appendChild(billpay);

            billpay.setAttribute("version", "2.3");

            Element payreq = document.createElement("vendReq");
            billpay.appendChild(payreq);

            Element ref = document.createElement("ref");
            ref.setTextContent("50431608466308219");
            payreq.appendChild(ref);

            Element numTokens = document.createElement("numTokens");
            numTokens.setTextContent("1");
            payreq.appendChild(numTokens);

            Element meter = document.createElement("meter");
            meter.setTextContent("123456756786");
            payreq.appendChild(meter);

            Element payType = document.createElement("payType");
            payType.setTextContent("cash");
            payreq.appendChild(payType);

            Element payment = document.createElement("payment");
            payreq.appendChild(payment);

            Element amt = document.createElement("amt");
            amt.setAttribute("cur", "KES");
            amt.setTextContent("1000");
            payment.appendChild(amt);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(System.out);

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);

            // System.out.println("File saved!");
//Document document = builder.parse(new InputSource(new StringReader(strMsg)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAcocuntDetails(String strMsg) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(strMsg)));

            NodeList el = document.getElementsByTagName("payRes");

            String payLastRes = el.item(0).getTextContent();

            System.err.println("payLastRes  :  " + payLastRes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void settlementBill() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            Element root = document.createElement("ipayMsg");
            document.appendChild(root);
            root.setAttribute("client", "test");
            root.setAttribute("term", "00001");
            root.setAttribute("seqNum", "35");
            root.setAttribute("time", "Yes");

            Element billpay = document.createElement("billPayMsg");
            root.appendChild(billpay);

            billpay.setAttribute("version", "1.7");

            Element payreq = document.createElement("payReq");
            billpay.appendChild(payreq);

            Element ref = document.createElement("ref");
            ref.setTextContent("50431608466308219");
            payreq.appendChild(ref);

            Element provider = document.createElement("providerName");
            provider.setTextContent("KPLC Provider");
            payreq.appendChild(provider);

            Element custAccNum = document.createElement("custAccNum");
            custAccNum.setTextContent("1234567-89");
            payreq.appendChild(custAccNum);

            Element payType = document.createElement("payType");
            payType.setTextContent("cash");
            payreq.appendChild(payType);

            Element payment = document.createElement("payment");
            payreq.appendChild(payment);

            Element accId = document.createElement("accId");
            accId.setTextContent("1234567-89");
            payment.appendChild(accId);

            Element amt = document.createElement("amt");
            amt.setAttribute("cur", "KES");
            amt.setTextContent("1000");
            payment.appendChild(amt);

            Element posRef = document.createElement("posRef");
            posRef.setTextContent("payreq");
            payreq.appendChild(posRef);

            Element notify = document.createElement("notify");
            notify.setAttribute("notifyMethod", "sms");
            notify.setTextContent("0821234567");
            payreq.appendChild(notify);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            // StreamResult result = new StreamResult(System.out);
            StreamResult result = new StreamResult(new StringWriter());
            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);
            System.out.println("Transformed xml :" + result.getWriter().toString());
//Document document = builder.parse(new InputSource(new StringReader(strMsg)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
