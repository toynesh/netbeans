package com.pdsl.checkacc;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.xml.sax.InputSource;

public class ProcessRequest {

    static DataStore data = new DataStore();
    static int seq = 0;
    static int repCount = 0;
    ConnectionManager co = ConnectionManager.getConnectionManagerInstance();
    static Connection con = null;
    public String refe = null;

    public String _getPostPaidDetailsAcc(String account_no, String client, String term) {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        String time = str;
        String seq = createSeq();
        String response = null;

        String build = _postPaidAccountDetails(account_no, seq, time, refere, client, term);
        String details = this.co.connection(build);

        if ((details.equals("")) || (details.equals(null)) || (details.equals("ERROR"))) {
            response = "Empty Details or error/timeout Response";
        } else {
            String attrib = new XML().xmlGetAttrib(details, "billPayMsg", "res", "code");
            String msg = checkCode(attrib);
            if (msg.equalsIgnoreCase("Successful")) {
                String customer = new XML().xmlGetCustomer(details);
                String phone = new XML().xmlGetPhone(details);
                String account = new XML().xmlGetAccount(details);
                double bal = 0;
                if (new XML().xmlGetBalance(details) != "none") {
                    String balance = new XML().xmlGetBalance(details);
                    double conver = 0.01;
                    bal = Double.parseDouble(balance) * conver;
                }
                response = "Owner:" + customer + " Phone:" + phone + " Account:" + account + " Balance: Kshs." + bal;
            } else {
                response = msg;
            }
        }
        return response;
    }

    public static String _postPaidAccountDetails(String account_no, String seq, String time, String reference, String client, String term) {
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
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public String _getPrePaidDetailsAcc(String account_no, String client, String term) {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        String time = str;
        String seq = createSeq();
        String response = null;

        String build = _prePaidAccountDetails(account_no, seq, time, refere, client, term);
        String details = this.co.connection(build);
        try {
            if ((details.equals("")) || (details.equals(null)) || (details.equals("ERROR"))) {
                response = "Empty Details or error/timeout Response";
            } else {
                String attrib = new XML().xmlGetAttrib(details, "elecMsg", "res", "code");
                ArrayList array = new XML().xmlGetAttrib(details, "elecMsg", "customer");
                String msg = checkCode(attrib);
                if (msg.equalsIgnoreCase("Successful")) {
                    response = "Owner:" + array;
                } else {
                    response = msg;
                }
            }
        } catch (Exception e) {
            response = "Errors trying to extract meter details";
            Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, "error Processing check prepaidaccount request:-");
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

    public String checkCode(String attrib) {
        if ((attrib.equals("billPay000")) || (attrib.equals("elec000"))) {
            return "Successful";
        }
        if ((attrib.equals("billPay001")) || (attrib.equals("elec001"))) {
            return "General Error";
        }
        if ((attrib.equals("billPay002")) || (attrib.equals("elec002"))) {
            return "Service not available. Service provider is down.";
        }
        if ((attrib.equals("billPay003")) || (attrib.equals("elec003"))) {
            return "No record of previous transaction";
        }
        if ((attrib.equals("billPay004")) || (attrib.equals("elec004"))) {
            return "Reversal Not supported";
        }
        if ((attrib.equals("billPay005")) || (attrib.equals("elec005"))) {
            return "Non unique reference.";
        }
        if ((attrib.equals("billPay010")) || (attrib.equals("elec010"))) {
            return "wrong meter";
        }
        if ((attrib.equals("billPay013")) || (attrib.equals("elec013"))) {
            return "The upper or lower limit on the amount has been passed";
        }
        if ((attrib.equals("billPay015")) || (attrib.equals("elec015"))) {
            return "Request amount exceeds the maximum";
        }
        if ((attrib.equals("billPay016")) || (attrib.equals("elec016"))) {
            return "Amount too low";
        }
        if ((attrib.equals("billPay018")) || (attrib.equals("elec018"))) {
            return "Multiple item payment not supported.";
        }
        if ((attrib.equals("billPay019")) || (attrib.equals("elec019"))) {
            return "Already reversed.";
        }
        if ((attrib.equals("billPay020")) || (attrib.equals("elec020"))) {
            return "Transaction already completed.";
        }
        if ((attrib.equals("billPay023")) || (attrib.equals("elec023"))) {
            return "The payment type specified in the request in not recognized.";
        }
        if ((attrib.equals("billPay030")) || (attrib.equals("elec030"))) {
            return "The format of the request or response is incorrect.";
        }
        if ((attrib.equals("billPay036")) || (attrib.equals("elec036"))) {
            return "The supplier does not support reprints by reference.";
        }
        if ((attrib.equals("billPay040")) || (attrib.equals("elec040"))) {
            return " The client system is disabled.";
        }
        if ((attrib.equals("billPay041")) || (attrib.equals("elec041"))) {
            return "metre lenth invalid";
        }
        if ((attrib.equals("billPay042")) || (attrib.equals("elec042"))) {
            return "Client blocked";
        }
        if ((attrib.equals("billPay043")) || (attrib.equals("elec043"))) {
            return "Provide a proper customer account number or meter number";
        }
        if ((attrib.equals("billPay044")) || (attrib.equals("elec044"))) {
            return "Meter identification is required for this type of account payment.";
        }
        if ((attrib.equals("billPay900")) || (attrib.equals("elec900"))) {
            return "General system error";
        }
        if ((attrib.equals("billPay901")) || (attrib.equals("elec901"))) {
            return "Unsupported message version number.";
        }
        if ((attrib.equals("billPay902")) || (attrib.equals("elec902"))) {
            return "Invalid Reference.";
        }
        return "CodeErr";
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
            String query = "select max(refnumber) from checkaccounts";
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
            Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlStr)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Element error(String message) {
        StreamResult result = new StreamResult(new StringWriter());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Element root = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            root = document.createElement("pdslMsg");
            document.appendChild(root);
            Element billpay = document.createElement("errorMsg");
            root.appendChild(billpay);

            Element payreq = document.createElement("netWorkError");
            payreq.setTextContent(message);
            billpay.appendChild(payreq);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }
}
