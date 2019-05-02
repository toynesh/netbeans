package com.pdsl.sdp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Node;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
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

        /*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(strMsg)));

            //print( document.getDocumentElement(), "");
            //Iterator t=document.
            // NodeList list=new NodeList();
            NodeList el = document.getElementsByTagName("ns1:linkid");
            NodeList el2 = document.getElementsByTagName("smsServiceActivationNumber");
            NodeList el3 = document.getElementsByTagName("senderAddress");
            NodeList el4 = document.getElementsByTagName("ns2:correlator");
            NodeList el5 = document.getElementsByTagName("message");
            int len = el.getLength();
            System.out.println(" Lenght of the node : " + len);
            if (len >= 1) {
                for (int n = 0; n < len; n++) {
                    System.out.println(" Code is  : " + n);
                    String shortcode = el2.item(0).getTextContent();
                    String msisdn = el3.item(0).getTextContent();
                    String linkid = el.item(0).getTextContent();
                    String correlator = el4.item(0).getTextContent();
                    String message = el5.item(0).getTextContent();
                    String msidn[] = msisdn.split(":");
                    msisdn = "254" + msidn[1];
                    msidn = shortcode.split(":");
                    shortcode = msidn[1];
                    System.err.println("message  :  " + message);
                    //  System.out.println(" Nodes value : "+el.item(n).getTextContent());   
                    new LinkIdController().insert(linkid, msisdn, shortcode, linkid, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}
