/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdlskenya.postpaid;

import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author koks
 */
public class ReportProcessor {
    /*public static void main(String[]args){
        ReportProcessor report=new ReportProcessor();
        report.settlementBill("277283","2000", "billy jones");
        report.paymentReport("2738374","2000","Billy Jones", "1000","2030394");
        
    }*/
    
    public Element settlementBill(String account_no, String amount,String name) {
        StreamResult result = new StreamResult(new StringWriter());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Element root =null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            root = document.createElement("pdslMsg");
            document.appendChild(root);
            /*root.setAttribute("client", "EquityBank");
            root.setAttribute("term", "00001");
            root.setAttribute("seqNum", seq);
            root.setAttribute("time", time);*/

            Element billpay = document.createElement("detailMsg");
            root.appendChild(billpay);

            Element payreq = document.createElement("custName");
            payreq.setTextContent(name);
            billpay.appendChild(payreq);

            Element ref = document.createElement("balance");
            ref.setTextContent(amount);
            billpay.appendChild(ref);

            Element custAccNum = document.createElement("custAccNum");
            //custAccNum.setTextContent("1234567-89");
            custAccNum.setTextContent(account_no);
            billpay.appendChild(custAccNum);

            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //OutputFormat format = new OutputFormat(document);
            //format.setIndenting(true);
            //format.setOmitXMLDeclaration(true);

            DOMSource source = new DOMSource(document);
            //  StreamResult result = new StreamResult(new StringWriter());
            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
          //  transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.transform(source, result);
            System.out.println("Transformed xml :" + result.getWriter().toString());
            // System.out.println("File saved!");
            //Document document = builder.parse(new InputSource(new StringReader(strMsg)));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
        //return result.getWriter().toString();
    }
    
    
    public Element paymentReport(String account_no, String amount,String name,String balance,String receipt) {
        StreamResult result = new StreamResult(new StringWriter());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         Element root =null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
             root = document.createElement("pdslMsg");
           // root.setAttribute("version", "1.0");
            document.appendChild(root);
            /*root.setAttribute("client", "EquityBank");
            root.setAttribute("term", "00001");
            root.setAttribute("seqNum", seq);
            root.setAttribute("time", time);*/

            Element payreq = document.createElement("paymentMsg");
            root.appendChild(payreq);

            Element names = document.createElement("custName");
            names.setTextContent(name);
            payreq.appendChild(names);
            

            Element paid = document.createElement("amount");
            paid.setTextContent(amount);
            payreq.appendChild(paid);

            Element ref = document.createElement("balance");
            ref.setTextContent(balance);
            payreq.appendChild(ref);
            
            Element custAccNum = document.createElement("custAccNum");
            //custAccNum.setTextContent("1234567-89");
            custAccNum.setTextContent(account_no);
            payreq.appendChild(custAccNum);
            
            Element receiptNum = document.createElement("receiptNum");
            //custAccNum.setTextContent("1234567-89");
            receiptNum.setTextContent(receipt);
            payreq.appendChild(receiptNum);

            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //OutputFormat format = new OutputFormat(document);
            //format.setIndenting(true);
            //format.setOmitXMLDeclaration(true);

            DOMSource source = new DOMSource(document);
            //  StreamResult result = new StreamResult(new StringWriter());
            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
            //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);
            System.out.println("Transformed xml 2:" + result.getWriter());
            System.out.println("Transformed xml :" + result.getWriter().toString());
            // System.out.println("File saved!");
            //Document document = builder.parse(new InputSource(new StringReader(strMsg)));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
      //  return result.getWriter().toString();
    }
    
    public Element error(String message) {
        StreamResult result = new StreamResult(new StringWriter());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Element root =null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            root = document.createElement("pdslMsg");
            document.appendChild(root);
            /*root.setAttribute("client", "EquityBank");
            root.setAttribute("term", "00001");
            root.setAttribute("seqNum", seq);
            root.setAttribute("time", time);*/

            Element billpay = document.createElement("errorMsg");
            root.appendChild(billpay);

            Element payreq = document.createElement("netWorkError");
            payreq.setTextContent(message);
            billpay.appendChild(payreq);

            
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //OutputFormat format = new OutputFormat(document);
            //format.setIndenting(true);
            //format.setOmitXMLDeclaration(true);

            DOMSource source = new DOMSource(document);
            //  StreamResult result = new StreamResult(new StringWriter());
            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
          //  transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.transform(source, result);
            System.out.println("Transformed xml :" + result.getWriter().toString());
            // System.out.println("File saved!");
            //Document document = builder.parse(new InputSource(new StringReader(strMsg)));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
        //return result.getWriter().toString();
    }
    
}
