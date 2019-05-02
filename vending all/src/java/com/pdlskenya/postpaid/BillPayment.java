package com.pdlskenya.postpaid;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import za.co.ipay.retail.system.bizswitch.IpayMessageWrapper;
import za.co.ipay.retail.system.bizswitch.IpayMessageWrapper2;

/**
 *
 * @author DigitalEye
 */
public class BillPayment {

    // Singleton Implementation: In the context of this app,  
    private static BillPayment _singletonInstance = null;
    BillPay pay = new BillPay();
    SSLClientTest sslconnect=new SSLClientTest();
    static DataStore data = new DataStore();
    static Connection con = null;
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

    // Default constructor is private to make sure it can't be accessed outside
    // the class
    public BillPayment() {
        con = data.connect();
    }

    /**
     * Returns single instance of ClientEmulator
     *
     * @return Instance of ClientEmulator
     */
    public static BillPayment getSingleClientEmulator() {
        if (_singletonInstance == null) {
            _singletonInstance = new BillPayment();
        }
        return _singletonInstance;
    }

    /**
     * Waits on response from server
     *
     * @param socket Server socket
     */
    public String readServerResponse(Socket socket) {
        String results = null;
        try {
            /*BufferedReader serverReader = new BufferedReader(
             new InputStreamReader(socket.getInputStream()));
            
             String serverResponse = null;
             while ((serverResponse = serverReader.readLine()) != null) {
             System.out.println("Server Response: " + serverResponse);
             }
             */
            BufferedInputStream serverReader = new BufferedInputStream(socket.getInputStream());
            IpayMessageWrapper2 wrap = new IpayMessageWrapper2();
            StringBuilder build = new StringBuilder();
            byte[] all = wrap.unWrap(serverReader);
            for (int n = 0; n < all.length; n++) {
                char c = (char) all[n];
                build.append(c);
                System.out.print(c);
            }
            results = build.toString();
            System.out.println("\n\nStringBuilder " + build.toString());
            //pay.responsePrepaid(build.toString());
            //pay.responsePostPay();
            return results;
        } catch (IOException ex) {
            System.out.println("Error: Unable to read server response\n\t" + ex);
        }
        return "ERROR";
    }

    public String connection(String build) {
        BufferedOutputStream buff = null;
        String results = "ERROR";
        try {
           // ClassLoader cl = this.getClass().getClassLoader();
           // File file  = new File(cl.getResource("images/save.gif").toURI());
            Socket socket = null;//new Socket("41.204.194.188", 8956);
            InputStream inputStream
                    = getClass().getClassLoader().getResourceAsStream("resource/keystore.jks");
            socket=sslconnect.main2("41.204.194.188", 9137, "changeit", "6000", inputStream);
            // Wait for the server to accept connection before reading the xml file
            /*BufferedReader reader = new BufferedReader( new FileReader (args[2]));
             String line;
             StringBuilder  stringBuilder = new StringBuilder();
             while((line = reader.readLine() ) != null) {
             stringBuilder.append(line);
             }*/
            // String build="<ipayMsg client=\"Mobikash\" seqNum=\"36\" term=\"00001\" time=\"2015-02-23 12:05:39 +0200\"><elecMsg ver=\"2.37\"><vendReq useAdv=\"false\"><ref>50431608466308219</ref><numTokens>1</numTokens><meter>903</meter><payType>cash</payType><amt cur=\"ZAR\">1000</amt></vendReq></elecMsg></ipayMsg>";
            //String build = "<ipayMsg client=\"EquityBank\" term=\"00001\" seqNum=\"35\" time=\"2015-02-12 16:08:46 +0200\">    <billPayMsg ver=\"1.7\">     <payReq>       <ref>50431608466308219</ref>       <providerName>KPLC Provider</providerName>     <custAccNum>1234567-89</custAccNum>  <payType>cash</payType> <payment>         <accId>1234567-89</accId> 		<amt cur=\"ZAR\">1000</amt></payment><posRef>posRef</posRef><notify notifyMethod=\"sms\">0821234567</notify>   </payReq> </billPayMsg></ipayMsg> ";
            //   String build="<ipayMsg client=\"ipay\" term=\"1\" seqNum=\"2\" time=\"2002-05-16 10:57:00 +0200\">  <elecMsg ver=\"2.37\">    <custInfoReq>      <ref>136105700003</ref>            <meter>123456789</meter>    </custInfoReq>  </elecMsg> </ipayMsg>";
            //  String build="<ipayMsg client=\"CraftSilicon\" term=\"0001\" seqNum=\"0\" time=\"2002-05-16 10:55:30 +0200\">  <elecMsg ver=\"2.37\">    <vendReq>      <ref>136105500001</ref>            <amt cur=\"ZAR\">11400</amt>      <numTokens>2</numTokens>      <meter>123456789</meter>      <payType pan=\"503615909394876134\">creditCard</payType>    </vendReq>  </elecMsg> </ipayMsg>";
            //  String build = "<ipayMsg client=\"Mobikash\" term=\"00002\" seqNum=\"0\" time=\"2015-02-23 12:05:39 +0200\"><elecMsg ver=\"2.37\"><vendReq useAdv=\"false\"><ref>136105500010</ref><amt cur=\"ZAR\">11400</amt><numTokens>1</numTokens><meter>903</meter></vendReq></elecMsg></ipayMsg>";
            // Send xml data to server 
            //build = getDetails("254723156864", "384938-01", "2000");
            // build = getDetailsAcc("384938-01");
            IpayMessageWrapper wrap = new IpayMessageWrapper();
            //PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            buff = new BufferedOutputStream(socket.getOutputStream());
            buff.write(wrap.wrap(build.getBytes()));
            buff.flush();
            //writer..println(build);
            System.out.println("Info: Message has been sent..." + build);
            // Wait for server response
            results = getSingleClientEmulator().readServerResponse(socket);
            socket.close();
            return results;
        } catch (IOException ex) {
            Logger.getLogger(ClientEmulator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                buff.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientEmulator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return results;
    }

    public static void main2(String[] args) {

        BillPayment client = new BillPayment();
        client.getDetailsAcc("2929394-01");
    }

    public void completeTransactions() {

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClientEmulator client = new ClientEmulator();

        // Make sure command line arguments are valid
        /*if (args.length < 3) {
         System.out.println("Error: Please specify host name, port number and data file.\nExiting...");
         return;
         */
        try {

            // Open a socket to the server
            //Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
            Socket socket = new Socket("41.204.194.188", 9137);

            // Wait for the server to accept connection before reading the xml file 
            /*BufferedReader reader = new BufferedReader( new FileReader (args[2]));
             String line;
             StringBuilder  stringBuilder = new StringBuilder();
             while((line = reader.readLine() ) != null) {
             stringBuilder.append(line);
             }*/
            // String build="<ipayMsg client=\"Mobikash\" seqNum=\"36\" term=\"00001\" time=\"2015-02-23 12:05:39 +0200\"><elecMsg ver=\"2.37\"><vendReq useAdv=\"false\"><ref>50431608466308219</ref><numTokens>1</numTokens><meter>903</meter><payType>cash</payType><amt cur=\"ZAR\">1000</amt></vendReq></elecMsg></ipayMsg>";
            // String build = "<ipayMsg client=\"EquityBank\" term=\"00002\" seqNum=\"35\" time=\"2015-02-12 16:08:46 +0200\">    <billPayMsg ver=\"1.7\">     <payReq>       <ref>50431608466308219</ref>       <providerName>KPLC Provider</providerName>     <custAccNum>1234567-89</custAccNum>  <payType>cash</payType> <payment>         <accId>1234567-89</accId> 		<amt cur=\"ZAR\">1000</amt></payment><posRef>posRef</posRef><notify notifyMethod=\"sms\">0821234567</notify>   </payReq> </billPayMsg></ipayMsg> ";
            String build = "<ipayMsg client=\"EquityBank\" term=\"00002\" seqNum=\"56\" time=\"2015-02-12 16:09:17 +0200\">  <billPayMsg ver=\"1.7\">\n"
                    + "    <payRevReq repCount=\"1\" origTime=\"2015-02-12 16:09:17 +0200\">\n"
                    + "      <ref>50431609173158248</ref>\n"
                    + "      <origRef>50431608466308219</origRef>\n"
                    + "      <providerName>KPLC Provider</providerName>\n"
                    + "    </payRevReq>\n"
                    + "  </billPayMsg>\n"
                    + "</ipayMsg> ";
//   String build="<ipayMsg client=\"ipay\" term=\"1\" seqNum=\"2\" time=\"2002-05-16 10:57:00 +0200\">  <elecMsg ver=\"2.37\">    <custInfoReq>      <ref>136105700003</ref>            <meter>123456789</meter>    </custInfoReq>  </elecMsg> </ipayMsg>";
            //  String build="<ipayMsg client=\"CraftSilicon\" term=\"0001\" seqNum=\"0\" time=\"2002-05-16 10:55:30 +0200\">  <elecMsg ver=\"2.37\">    <vendReq>      <ref>136105500001</ref>            <amt cur=\"ZAR\">11400</amt>      <numTokens>2</numTokens>      <meter>123456789</meter>      <payType pan=\"503615909394876134\">creditCard</payType>    </vendReq>  </elecMsg> </ipayMsg>";
            //  String build = "<ipayMsg client=\"Mobikash\" term=\"00002\" seqNum=\"0\" time=\"2015-02-23 12:05:39 +0200\"><elecMsg ver=\"2.37\"><vendReq useAdv=\"false\"><ref>136105500010</ref><amt cur=\"ZAR\">11400</amt><numTokens>1</numTokens><meter>903</meter></vendReq></elecMsg></ipayMsg>";
            // Send xml data to server 
            //build = getDetails("254723156864", "384938-01", "2000");
            //   build = getDetailsAcc("384938-01");
            IpayMessageWrapper wrap = new IpayMessageWrapper();
            //PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedOutputStream buff = new BufferedOutputStream(socket.getOutputStream());
            buff.write(wrap.wrap(build.getBytes()));
            buff.flush();
            //writer..println(build);
            System.out.println("Info: Message has been sent..." + build);
            // Wait for server response
            getSingleClientEmulator().readServerResponse(socket);

        } catch (IOException | NumberFormatException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public String getDetails(String msisdn, String account_no, String amount) {
        String seq = "0";
        String refere = refIncrement();
        String time = "2015-02-12 16:08:46 +0200";
        String details = settlementBill(msisdn, account_no, amount, seq, refere, time);
        String response2 = connection(details);
        //String response = "";

        ArrayList array = new XML().xmlGet(response2, "billPayMsg", "payRes");
        String attrib = new XML().xmlGetAttrib(response2, "billPayMsg", "res", "code");
        System.out.println("The Attrib is " + attrib);

        if (attrib.equals("billPay000")) {
            System.out.println("The token generation is succesful");
            return response2;
        } else if (attrib.equals("billPay004")) {
            //Reversal Not Supported
            System.out.println("Reversal Not supported");
        } else if (attrib.equals("billPay002")) {
            //Service not available. Service provider is down   
            String reversal = connection(reversals(refere));
            String attrib2 = new XML().xmlGetAttrib(response2, "billPayMsg", "res", "code");
            if (attrib2.equals("billPay000")) {

            } else {

            }
        } else if (attrib.equals("billPay003")) {
            //No record of previous transactions
        } else if (attrib.equals("billPay005")) {
            //Non unique reference. The reference of the request message for this
            //client and terminal combination already exists in the system. 

        } else if (attrib.equals("billPay010")) {
            //Unknown account or meter number.
            System.out.println("UNKNOWN ACCOUNT/METER NUMBER");
            return response2;

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

        return "ERROR";
    }

    public void timeout(String socket, String message) {

    }

    public String getDetailsAcc(String account_no) {
        String seq = "0";
        String refere = refIncrement();
        String time = "2015-02-12 16:08:46 +0200";

        String build = accountDetails(account_no, seq, time, refere);
        //String details="";//connection(build);
        String details = connection(build);

        System.out.println("Details :" + details);
        //details=response;
        return details;

    }

    public String settlementBill(String msisdn, String account_no, String amount, String seq, String reference, String time) {
        StreamResult result = new StreamResult(new StringWriter());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            Element root = document.createElement("ipayMsg");
            document.appendChild(root);
            root.setAttribute("client", "EquityBank");
            root.setAttribute("term", "00001");
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
            String insert = "INSERT INTO `vending`.`transaction` (`clientid`, `seqnumber`, `account_number`, `msisdn`, `status`, `refnumber`, `time`)"
                    + " VALUES ('EquityBank', " + seq + ", " + account_no + ", " + msisdn + ", 'incomplete', '" + reference + "', '" + time + "')";
            data.insert(insert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public static String accountDetails(String account_no, String seq, String time, String reference) {
        StreamResult result = new StreamResult(new StringWriter());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            Element root = document.createElement("ipayMsg");
            document.appendChild(root);
            root.setAttribute("client", "EquityBank");
            root.setAttribute("term", "00001");
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
            //custAccNum.setTextContent("1234567-89");
            custAccNum.setTextContent(account_no);
            payreq.appendChild(custAccNum);
            /*
             Element payType = document.createElement("payType");
             payType.setTextContent("cash");
             payreq.appendChild(payType);

             Element payment = document.createElement("payment");
             payreq.appendChild(payment);*/

            /*Element accId = document.createElement("accId");
             accId.setTextContent(account_no);
             payment.appendChild(accId);*/
            /*
             Element amt = document.createElement("amt");
             amt.setAttribute("cur", "KES");
             amt.setTextContent(amount);
             payment.appendChild(amt);*/
            /*
             Element posRef = document.createElement("posRef");
             posRef.setTextContent("payreq");
             payreq.appendChild(posRef);

             Element notify = document.createElement("notify");
             notify.setAttribute("notifyMethod", "sms");
             notify.setTextContent(msisdn);
             payreq.appendChild(notify);*/
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
            String insert = "INSERT INTO `vending`.`transaction` (`clientid`, `seqnumber`, `account_number`, `status`, `refnumber`, `time`)"
                    + " VALUES ('EquityBank', " + seq + ", '" + account_no + "', 'incomplete', '" + reference + "', '" + time + "')";
            data.insert(insert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public static String reversals(String originalref) {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String seq = "0";
        String refere = refIncrement();
        String time = str;
        StreamResult result = new StreamResult(new StringWriter());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            Element root = document.createElement("ipayMsg");
            document.appendChild(root);
            root.setAttribute("client", "EquityBank");
            root.setAttribute("term", "00001");
            root.setAttribute("seqNum", seq);
            root.setAttribute("time", time);

            Element billpay = document.createElement("billPayMsg");
            root.appendChild(billpay);

            billpay.setAttribute("ver", "1.7");

            Element payreq = document.createElement("payRevReq");
            billpay.appendChild(payreq);

            Element ref = document.createElement("ref");
            ref.setTextContent(refere);
            payreq.appendChild(ref);

            Element origref = document.createElement("origRef");
            origref.setTextContent(originalref);
            payreq.appendChild(origref);

            Element provider = document.createElement("providerName");
            provider.setTextContent("KPLC Provider");
            payreq.appendChild(provider);

            /*Element custAccNum = document.createElement("custAccNum");
             //custAccNum.setTextContent("1234567-89");
             custAccNum.setTextContent(account_no);
             payreq.appendChild(custAccNum);*/
            /*
             Element payType = document.createElement("payType");
             payType.setTextContent("cash");
             payreq.appendChild(payType);

             Element payment = document.createElement("payment");
             payreq.appendChild(payment);*/

            /*Element accId = document.createElement("accId");
             accId.setTextContent(account_no);
             payment.appendChild(accId);*/
            /*
             Element amt = document.createElement("amt");
             amt.setAttribute("cur", "KES");
             amt.setTextContent(amount);
             payment.appendChild(amt);*/
            /*
             Element posRef = document.createElement("posRef");
             posRef.setTextContent("payreq");
             payreq.appendChild(posRef);

             Element notify = document.createElement("notify");
             notify.setAttribute("notifyMethod", "sms");
             notify.setTextContent(msisdn);
             payreq.appendChild(notify);*/
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
            String insert = "INSERT INTO `vending`.`reversals` (`originalref`, `ref`,`status`)"
                    + " VALUES ('" + originalref + "', '" + refere + "', 'incomplete', ')";
            data.insert(insert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public static String refIncrement() {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        System.out.println(str);
        String year = null;
        String hour = null;
        String day = null;
        String min = null;
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
        if (dt.getDayOfYear() < 99 && dt.getDayOfYear() > 9) {
            day = "0" + dt.getDayOfYear();
        } else if (dt.getDayOfYear() < 9) {
            day = "00" + dt.getDayOfYear();
        } else {
            day = "" + dt.getDayOfYear();
        }
        year = "" + dt.getYear();
        year = year.substring(year.length() - 1);
        build.append(year).append(day).append(hour).append(min);
        System.out.println(year);
        System.out.println(day);

        System.out.println(min);

        System.out.println(build.toString());
        try {
            String query = "select max(refnumber) from transaction";
            PreparedStatement prep = con.prepareStatement(query);
            ResultSet rst = prep.executeQuery();
            if (rst.next()) {
                String ref=rst.getString(1);
                ref=ref.substring(ref.length()-4);
                if(ref.equals("9999")){
                 build.append("000");
                }else{
                    build.append(""+(Integer.parseInt(ref)+1));
                }
                //int ref = rst.getInt(1) + 1;
                
                //return "" + ref;
                return build.toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientEmulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
