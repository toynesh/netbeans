package pdsltextify;

import java.io.StringWriter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RequestsManager {

    static DataStore data = new DataStore();
    static int seq = 0;
    static int repCount = 0;
    ConnectionManager co = ConnectionManager.getConnectionManagerInstance();
    static Connection con = null;
    public String refe = null;

    public String payBillPrePaid(String account_no, String amount, String client, String term) throws Exception {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        refe = refere;
        String time = str;
        String seq = createSeq();
        String details = prePaidDocument(account_no, amount, seq, refere, time, client, term);
        String response = co.connection(details);
        System.out.println("RESPONSE   :" + response);
        return response;

    }

    public String prePaidDocument(String account_no, String amount, String seq, String reference, String time, String client, String term) {
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
            Element payreq = document.createElement("vendReq");
            billpay.appendChild(payreq);
            Element ref = document.createElement("ref");
            ref.setTextContent(reference);
            payreq.appendChild(ref);
            Element amt = document.createElement("amt");
            amt.setAttribute("cur", "KES");
            amt.setTextContent(amount);
            payreq.appendChild(amt);
            Element numTokens = document.createElement("numTokens");
            numTokens.setTextContent("1");
            payreq.appendChild(numTokens);
            Element meter = document.createElement("meter");
            meter.setTextContent(account_no);
            payreq.appendChild(meter);
            Element payType = document.createElement("payType");
            payType.setTextContent("cash");
            payreq.appendChild(payType);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);
            //System.out.println("Transformed   :" + result.getWriter().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public Object payBill(String msisdn, String account_no, String amount, String client, String term) throws Exception {

        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        refe = refere;
        String time = str;
        String response = null;
        String seq = createSeq();

        String detailsbuild = settlementBill(msisdn, account_no, amount, seq, refe, time, client, term);
        try {
            response = co.connection(detailsbuild);
            System.out.println("RESPONSE   :" + response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

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
            posRef.setTextContent(reference);
            payreq.appendChild(posRef);

            Element notify = document.createElement("notify");
            notify.setAttribute("notifyMethod", "sms");
            notify.setTextContent(msisdn);
            payreq.appendChild(notify);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);
            System.out.println("Transformed   :" + result.getWriter().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public String getDetailPrepaid(String account_no, String client) {
        String term = "00001";
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        String time = str;
        String seq = createSeq();

        String build = accountDetails(account_no, seq, time, refere, client, term);

        String details = co.connection(build);

        //System.out.println("Details :" + details);
        return details;
    }

    public String accountDetails(String account_no, String seq, String time, String reference, String client, String term) {
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
            Element custAccNum = document.createElement("meter");
            custAccNum.setTextContent(account_no);
            payreq.appendChild(custAccNum);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);
            System.out.println("Transformed xml :" + result.getWriter().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public String getDetailsAccPostPaid(String account_no, String client) {
        String term = "00001";
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        String time = str;
        String seq = createSeq();
        String build = postPaidAccountDetails(account_no, seq, time, refere, client, term);

        String details = co.connection(build);

        return details;

    }

    public String postPaidAccountDetails(String account_no, String seq, String time, String reference, String client, String term) {
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
            Element payreq = document.createElement("billInfoReq");
            billpay.appendChild(payreq);
            Element ref = document.createElement("ref");
            ref.setTextContent(reference);
            payreq.appendChild(ref);
            Element provider = document.createElement("providerName");
            provider.setTextContent("KPLC Provider");
            payreq.appendChild(provider);
            Element custAccNum = document.createElement("custAccNum");
            custAccNum.setTextContent(account_no);
            payreq.appendChild(custAccNum);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);
            System.out.println("Transformed xml :" + result.getWriter().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public String createSeq() {
        if (seq < 99999) {
            seq += 1;
        } else {
            seq = 1;
        }
        return Integer.toString(seq);
    }

    public String createRepCount() {
        repCount += 1;
        return Integer.toString(repCount);
    }

    public static String refIncrement() {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);

        String year = null;
        String hour = null;
        String day = null;
        String min = null;
        String sec = null;
        StringBuilder build = new StringBuilder();
        if (dt.getHourOfDay() < 9) {
            hour = "0" + dt.getMinuteOfHour();
        } else {
            hour = "" + dt.getMinuteOfHour();
        }
        if (dt.getMinuteOfHour() < 9) {
            min = "0" + dt.getMinuteOfHour();
        } else {
            min = "" + dt.getMinuteOfHour();
        }
        if (dt.getSecondOfMinute() < 9) {
            sec = "0" + dt.getSecondOfMinute();
        } else {
            sec = "" + dt.getSecondOfMinute();
        }
        if ((dt.getDayOfYear() < 99) && (dt.getDayOfYear() > 9)) {
            day = "0" + dt.getDayOfYear();
        } else if (dt.getDayOfYear() < 9) {
            day = "00" + dt.getDayOfYear();
        } else {
            day = "" + dt.getDayOfYear();
        }
        year = "" + dt.getYear();
        year = year.substring(year.length() - 1);
        build.append(year).append(day).append(hour).append(min).append(sec);
        try {
            data.createTables();
            String query = "select max(refnumber) from transaction";
            PreparedStatement prep = data.connect().prepareStatement(query);
            ResultSet rst = prep.executeQuery();
            if (rst.next()) {
                String ref = rst.getString(1);
                ref = ref.substring(ref.length() - 4);
                if (ref.equals("9999")) {
                    build.append("000");
                } else {
                    build.append("" + (Integer.parseInt(ref) + 1));
                }
                return build.toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
