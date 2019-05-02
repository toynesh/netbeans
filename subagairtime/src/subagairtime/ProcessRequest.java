package subagairtime;

import java.io.StringWriter;
import static java.lang.Double.parseDouble;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProcessRequest {

    static int seq = 0;
    static int repCount = 0;
    IpayConnectionManager co = IpayConnectionManager.getConnectionManagerInstance();
    String initialtimeout = "45000";
    public String refe = null;

    public String _getPrePaidDetailsAcc(String account_no, String client, String term) {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = RefClass.newRef();
        String time = str;
        String seq = RefClass.createSeq();
        String response = null;

        String build = _prePaidAccountDetails(account_no, seq, time, refere, client, term);
        String details = this.co.connection(build);
        if ((details.equals("")) || (details.equals(null)) || (details.equals("ERROR")) || (details.equals("TIMEOUT"))) {
            response = "ServiceNotAvailable";
        } else {
            String attrib = new IpayXML().xmlGetAttrib(details, "elecMsg", "res", "code");
            String msg = checkCode(attrib);
            if (msg.equalsIgnoreCase("Successful")) {
                ArrayList array = new IpayXML().xmlGetAttrib(details, "elecMsg", "customer");
                response = "Owner:" + array;
            } else {
                response = "ServiceNotAvailable";
            }
        }
        return response;
    }

    public static String _prePaidAccountDetails(String account_no, String seq, String time, String reference, String client, String term) {
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

            Element billpay = document.createElement("elecMsg");
            root.appendChild(billpay);

            billpay.setAttribute("ver", "2.37");

            Element payreq = document.createElement("custInfoReq");
            billpay.appendChild(payreq);

            Element ref = document.createElement("ref");
            ref.setTextContent(reference);
            payreq.appendChild(ref);

            Element meter = document.createElement("meter");

            meter.setTextContent(account_no);
            payreq.appendChild(meter);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    //>>>>>>>>>>>>>>>>AIRTIME!!!!!!!11111111
    public String _airTimeTokenBuy(String account_no, String amount, String client, String term) {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = RefClass.newRef();
        String time = str;
        String seq = RefClass.createSeq();
        String response = "";

        String build = _airTimeTokenRequest(account_no, amount, seq, refere, time, client, term);
        setORef(refere);
        String details = this.co.connection(build);

        String commamt = "0";
        if (amount.startsWith("saf_")) {
            commamt = amount.replaceAll("saf_", "");
        } else if (amount.startsWith("air_")) {
            commamt = amount.replaceAll("air_", "");
        } else {
            commamt = amount.replaceAll("ornge_", "");
        }
        System.out.println("commant:" + commamt);
        double conver = 0.016;
        double commission = Double.parseDouble(commamt) * conver;
        System.out.println("commission:" + commission);

        DateTime tdt = new DateTime();
        DateTimeFormatter tfmt = DateTimeFormat.forPattern("yyyy");
        String year = tfmt.print(tdt);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
        String month = formatter.print(tdt);

        if ((details.equals("")) || (details.equals(null)) || (details.equals("ERROR")) || (details.equals("TIMEOUT"))) {
            response = "ServiceNotAvailable Ref:" + refere;
        } else {
            String attrib = new IpayXML().xmlGetAttrib(details, "cellMsg", "res", "code");
            String msg = checkCode(attrib);
            if (msg.equalsIgnoreCase("Successful")) {
                String token = new IpayXML().xmlGetToken(details);
                String units = new IpayXML().xmlGetAttrib(details, "cellMsg", "stdToken", "units");
                response = token + ":" + units + ":" + refere;

            } else {
                //response = msg;
                response = "Unsuccessfulmsg:ServiceNotAvailable";
            }

        }

        return response;
    }

    public String _airTimeTokenRequest(String account_no, String amount, String seq, String reference, String time, String client, String term) {
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

            Element cellMsg = document.createElement("cellMsg");
            root.appendChild(cellMsg);

            cellMsg.setAttribute("ver", "2.19");

            Element vendReq = document.createElement("vendReq");
            cellMsg.appendChild(vendReq);

            Element ref = document.createElement("ref");
            ref.setTextContent(reference);
            vendReq.appendChild(ref);

            Element tokenType = document.createElement("tokenType");
            tokenType.setTextContent(amount);
            vendReq.appendChild(tokenType);

            Element numTokens = document.createElement("numTokens");
            numTokens.setTextContent("1");
            vendReq.appendChild(numTokens);

            Element network = document.createElement("network");
            network.setTextContent(account_no);
            vendReq.appendChild(network);

            Element payType = document.createElement("payType");
            payType.setTextContent("cash");
            vendReq.appendChild(payType);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public String _vendRev(String origref, String origtime, int repcount, String client, String term) {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = RefClass.newRef();
        String time = str;
        String seq = RefClass.createSeq();
        String response = "";

        String build = _vendRevRequest(origref, origtime, repcount, seq, refere, time, client, term);
        String details = this.co.connection(build);
        DateTime tdt = new DateTime();
        DateTimeFormatter tfmt = DateTimeFormat.forPattern("yyyy");
        String year = tfmt.print(tdt);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
        String month = formatter.print(tdt);

        if ((details.equals("")) || (details.equals(null)) || (details.equals("ERROR")) || (details.equals("TIMEOUT"))) {
            response = "ServiceNotAvailable Ref:" + refere;
        } else {
            String attrib = new IpayXML().xmlGetAttrib(details, "cellMsg", "res", "code");
            String msg = checkCode(attrib);
            if (msg.equalsIgnoreCase("Successful")) {
                //String token = new IpayXML().xmlGetToken(details);
                //String units = new IpayXML().xmlGetAttrib(details, "cellMsg", "stdToken", "units");
                //response = token + ":" + units + ":" + refere;

            } else {
                //response = msg;
                response = "Unsuccessfulmsg:ServiceNotAvailable";
            }

        }

        return response;
    }

    public String _vendRevRequest(String origref, String origtime, int repcount, String seq, String reference, String time, String client, String term) {
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

            Element cellMsg = document.createElement("cellMsg");
            root.appendChild(cellMsg);

            cellMsg.setAttribute("ver", "2.19");

            Element vendRevReq = document.createElement("vendRevReq");
            if (repcount > 0) {
                vendRevReq.setAttribute("repCount", Integer.toString(repcount));
                vendRevReq.setAttribute("origTime", origtime);
                cellMsg.appendChild(vendRevReq);
            } else {
                vendRevReq.setAttribute("origTime", origtime);
                cellMsg.appendChild(vendRevReq);
            }

            Element ref = document.createElement("ref");
            ref.setTextContent(reference);
            vendRevReq.appendChild(ref);

            Element origRef = document.createElement("origRef");
            origRef.setTextContent(origref);
            vendRevReq.appendChild(origRef);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    private String oreff = "";

    public void setORef(String ref) {
        this.oreff = ref;
    }
    public String getORef() {
        return oreff;
    }

    //=================================================================================================
    public String checkCode(String attrib) {
        if ((attrib.equals("billPay000")) || (attrib.equals("elec000")) || (attrib.equals("cell000"))) {
            return "Successful";
        }
        if ((attrib.equals("billPay001")) || (attrib.equals("elec001")) || (attrib.equals("cell001"))) {
            return "An error occurred while processing the vend Request";
        }
        if ((attrib.equals("billPay002")) || (attrib.equals("elec002")) || (attrib.equals("cell002"))) {
            return "Service not available. Service provider is down.";
        }
        if ((attrib.equals("billPay003")) || (attrib.equals("elec003")) || (attrib.equals("cell003"))) {
            return "No record of previous transaction";
        }
        if ((attrib.equals("billPay004")) || (attrib.equals("elec004")) || (attrib.equals("cell004"))) {
            return "Reversal Not supported";
        }
        if ((attrib.equals("billPay005")) || (attrib.equals("elec005")) || (attrib.equals("cell005"))) {
            return "Non unique reference.";
        }
        if ((attrib.equals("billPay010")) || (attrib.equals("elec010")) || (attrib.equals("cell010"))) {
            return "Wrong Meter or Account";
        }
        if ((attrib.equals("billPay013")) || (attrib.equals("elec013")) || (attrib.equals("cell013"))) {
            return "The upper or lower limit on the amount has been passed";
        }
        if ((attrib.equals("billPay015")) || (attrib.equals("elec015")) || (attrib.equals("cell015"))) {
            return "Request amount exceeds the maximum";
        }
        if ((attrib.equals("billPay016")) || (attrib.equals("elec016")) || (attrib.equals("cell016"))) {
            return "Amount too low";
        }
        if ((attrib.equals("billPay018")) || (attrib.equals("elec018")) || (attrib.equals("cell018"))) {
            return "Multiple item payment not supported.";
        }
        if ((attrib.equals("billPay019")) || (attrib.equals("elec019")) || (attrib.equals("cell019"))) {
            return "Already reversed.";
        }
        if ((attrib.equals("billPay020")) || (attrib.equals("elec020")) || (attrib.equals("cell020"))) {
            return "Transaction already completed.";
        }
        if ((attrib.equals("billPay023")) || (attrib.equals("elec023")) || (attrib.equals("cell023"))) {
            return "The payment type specified in the request in not recognized.";
        }
        if ((attrib.equals("billPay030")) || (attrib.equals("elec030")) || (attrib.equals("cell030"))) {
            return "The format of the request or response is incorrect.";
        }
        if ((attrib.equals("billPay036")) || (attrib.equals("elec036")) || (attrib.equals("cell036"))) {
            return "The supplier does not support reprints by reference.";
        }
        if ((attrib.equals("billPay040")) || (attrib.equals("elec040")) || (attrib.equals("cell040"))) {
            return " The client system is disabled.";
        }
        if ((attrib.equals("billPay041")) || (attrib.equals("elec041")) || (attrib.equals("cell041"))) {
            return "Meter lenth invalid";
        }
        if ((attrib.equals("billPay042")) || (attrib.equals("elec042")) || (attrib.equals("cell042"))) {
            return "Client blocked";
        }
        if ((attrib.equals("billPay043")) || (attrib.equals("elec043")) || (attrib.equals("cell043"))) {
            return "Provide a proper customer account number or meter number";
        }
        if ((attrib.equals("billPay044")) || (attrib.equals("elec044")) || (attrib.equals("cell044"))) {
            return "Meter identification is required for this type of account payment.";
        }
        if ((attrib.equals("billPay900")) || (attrib.equals("elec900")) || (attrib.equals("cell900"))) {
            return "General system error";
        }
        if ((attrib.equals("billPay901")) || (attrib.equals("elec901")) || (attrib.equals("cell901"))) {
            return "Unsupported message version number.";
        }
        if ((attrib.equals("billPay902")) || (attrib.equals("elec902")) || (attrib.equals("cell902"))) {
            return "Invalid Reference.";
        }
        return "Service momentarily unavailable";
    }

}
