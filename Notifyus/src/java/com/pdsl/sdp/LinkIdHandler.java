package com.pdsl.sdp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import org.w3c.dom.NodeList;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class LinkIdHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        System.out.println("Server : handleMessage()......");

        Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        //for response message only, true for outbound messages, false for inbound
        if (!isRequest) {

            try {
                SOAPMessage soapMsg = context.getMessage();
                SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
                SOAPHeader soapHeader = soapEnv.getHeader();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                soapMsg.writeTo(out);
                String strMsg = new String(out.toByteArray());

                //    System.err.println("RESULTS :  "+strMsg);
                Nodeprocess(strMsg);

                // soapMsg.writeTo(System.out);
                //if no header, add one
                /*Iterator it2=soapMsg.getSOAPHeader().extractAllHeaderElements();
                    while(it2.hasNext()){
                        System.err.println("Results : for "+it2.next());
                    }
                 */
                //  soapMsg.writeTo(System.out);
                if (soapHeader == null) {
                    soapHeader = soapEnv.addHeader();
                    //throw exception
                    generateSOAPErrMessage(soapMsg, "No SOAP header.");
                }

                //Get client mac address from SOAP header
                Iterator it = soapHeader.extractHeaderElements(SOAPConstants.URI_SOAP_ACTOR_NEXT);

                //if no header block for next actor found? throw exception
                if (it == null || !it.hasNext()) {
                    //	generateSOAPErrMessage(soapMsg, "No header block for next actor.");
                } else {

                }

                //if no mac address found? throw exception
/*	            Node macNode = (Node) it.next();
	            String macValue = (macNode == null) ? null : macNode.getValue();
	          
	            if (macValue == null){
	            	generateSOAPErrMessage(soapMsg, "No mac address in header block.");
	            }
                 */
                //if mac address is not match, throw exception
                /*if(!macValue.equals("90-4C-E5-44-B9-8F1")){
	            	generateSOAPErrMessage(soapMsg, "Invalid mac address, access is denied.");
	            }*/
                //tracking
                // soapMsg.writeTo(System.out);
            } catch (SOAPException e) {
                System.err.println(e);
            } catch (IOException e) {
                System.err.println(e);
            }

        }

        //continue other handler chain
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {

        System.out.println("Server : handleFault()......");

        return true;
    }

    @Override
    public void close(MessageContext context) {
        System.out.println("Server : close()......");
    }

    @Override
    public Set<QName> getHeaders() {
        System.out.println("Server : getHeaders()......");
        return null;
    }

    private void generateSOAPErrMessage(SOAPMessage msg, String reason) {
        try {
            SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
            SOAPFault soapFault = soapBody.addFault();
            soapFault.setFaultString(reason);
            throw new SOAPFaultException(soapFault);
        } catch (SOAPException e) {
        }
    }

    public void Nodeprocess(String strMsg) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(strMsg)));

            //print( document.getDocumentElement(), "");
            //Iterator t=document.
            // NodeList list=new NodeList();
            System.out.println("XML: " + strMsg);
            NodeList el = document.getElementsByTagName("ns1:linkid");
            NodeList el2 = document.getElementsByTagName("smsServiceActivationNumber");
            NodeList el3 = document.getElementsByTagName("senderAddress");
            NodeList el33 = document.getElementsByTagName("address");
            NodeList el4 = document.getElementsByTagName("ns2:correlator");
            NodeList el5 = document.getElementsByTagName("message");
            int len = el.getLength();
            System.out.println(" Lenght of the node : " + len);
            if (len < 1) {
                el = document.getElementsByTagName("ns1:traceUniqueID");
                len = el.getLength();
            }
            System.out.println("Final Lenght of the node : " + len);
            if (len >= 1) {
                for (int n = 0; n < len; n++) {
                    System.out.println(" Code is  : " + n);
                    
                    String shortcode = "none";
                    try{
                    shortcode = el2.item(0).getTextContent();
                    }catch(Exception ex){
                        //ex.printStackTrace();
                    }
                    System.out.println("Shortcode:" + shortcode);
                    
                    String msisdn = "254";
                    try{
                    msisdn = el3.item(0).getTextContent();
                    }catch(Exception ex){
                        try{
                        msisdn = el33.item(0).getTextContent();
                        //ex.printStackTrace();
                        }catch(Exception ex2){
                            //ex2.printStackTrace();
                        }
                    }
                    System.out.println("msisdn:" + msisdn);
                    
                    String linkid = "none";
                    try{
                    linkid = el.item(0).getTextContent();
                    }catch(Exception ex){
                        //ex.printStackTrace();
                    }
                    System.out.println("linkid:" + linkid);
                    
                    
                    String correlator = "none";
                    try{
                    correlator = el4.item(0).getTextContent();
                    }catch(Exception ex){
                        //ex.printStackTrace();
                    }
                    System.out.println("correlator:" + correlator);
                    
                    String message = "none";
                    try{
                    message = el5.item(0).getTextContent();
                    }catch(Exception ex){
                        //ex.printStackTrace();
                    }
                    System.out.println("message:" + message);
                    
                    if(!msisdn.equals("254")){
                    String[] msidnarr = msisdn.split(":");
                    msisdn = "254" + msidnarr[1];
                    try{
                    String[] shortcodearr = shortcode.split(":");
                    shortcode = shortcodearr[1];
                    }catch(Exception ex){
                        //ex.printStackTrace();
                    }
                    try{
                    String[] shortcodearr = shortcode.split(" ");
                    shortcode = shortcodearr[1];
                    }catch(Exception ex){
                        //ex.printStackTrace();
                    }
                    System.out.println("Formatted msisdn:" + msisdn);
                    System.out.println("Formatted shortcode:" + shortcode);
                    }

                    new LinkIdController().insert(linkid, msisdn, shortcode, linkid, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
