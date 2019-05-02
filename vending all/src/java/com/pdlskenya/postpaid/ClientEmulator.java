package com.pdlskenya.postpaid;

import ipayhttp.Ipayhttp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;




//import static pdslipay.BillPayment.con;
import za.co.ipay.retail.system.bizswitch.IpayMessageWrapper;
import za.co.ipay.retail.system.bizswitch.IpayMessageWrapper2;


/**
 *
 * @author DigitalEye
 */
public class ClientEmulator {

    // Singleton Implementation: In the context of this app,  
    private static ClientEmulator _singletonInstance = null;
    BillPay pay = new BillPay();
    static DataStore data = new DataStore();
    static int seq = 0;
    static int repCount = 0;
    
    static Connection con = null;
   
    public ClientEmulator() {
        con = data.connect();
    }

    public static ClientEmulator getSingleClientEmulator() {
        if (_singletonInstance == null) {
            _singletonInstance = new ClientEmulator();
        }
        return _singletonInstance;
    }
    
    public String getresponse(int reqid) throws SQLException{
		String response = null;
        String query = "SELECT * from requests where reqid = '"+reqid+"'";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        while (rs.next()) { 
        	response = rs.getString("response");
        }
		return response;
	}

    public String readServerResponse(Socket socket) {
        String results = null;
        try {
           
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
            ex.printStackTrace();
        }
        return "ERROR";
    }

    
    public String connection(String build) {
        BufferedOutputStream buff = null;
        String results = "ERROR";
        SSLClientTest sslconnect=new SSLClientTest();
        Socket socket = null;
        InputStream inputStream
              = getClass().getClassLoader().getResourceAsStream("resource/keystore.jks");
        socket=sslconnect.main2("41.204.194.188", 9137, "changeit", "6000", inputStream);
        //results= sendPost(build);
        results=new BillPayment().connection(build);
                System.out.println(results+" :Info: Message has been sent..." + build);
        return results;
    }

    public String refe = null;
        public Object payBill(String msisdn, String account_no, String amount, String client, String term) throws Exception {
    	    	 
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        refe = refere;
        String time = str;
        String seq = createSeq();
        
        String name = null;
        String bal= null;
        String detailsbuild = accountDetails(account_no, seq, time, refere, client, term);
        String detailscon = connection(detailsbuild);
        try{
        	 if(detailscon.equals("") || detailscon.equals(null)|| detailscon.equals("ERROR")){    
        		 //response = error("Provider Unreachable");
             }else{
        	String attrib = new XML().xmlGetAttrib(detailscon, "billPayMsg", "res", "code");

            System.out.println("The Attrib is " + attrib);
            String msg = checkCode(attrib);
            if(msg.equalsIgnoreCase("Successful")){
        	ArrayList array = new XML().xmlGet(detailscon, "billPayMsg", "billInfoRes", "customer", "name");
        	name = (String) array.get(2);
        	ArrayList array2 = new XML().xmlGet(detailscon, "billPayMsg", "billInfoRes", "accDetail", "balance");
        	String balance = (String) array2.get(2);
                double amt = Double.parseDouble(amount);
        	double dvalue = Double.parseDouble(balance);
        	double conver = 0.01;
                double amt2 = amt * conver;
        	double dvalue2 = dvalue * conver;
                double bala = dvalue2 - amt2;
        	bal = String.valueOf(bala);
            }else{
            	//response = error(msg);
            }
            }

           }catch (Exception e) {
            	System.out.println("PARSE ERR:=" + e);
                error("Provider Unreachable");
            }
        
        
        String details = settlementBill(msisdn, account_no, amount, seq, refere, time, client, term);

        String response1 = connection(details);
        Object response = null;
        try{
        	if(response1.equals("") || response1.equals(null)|| response1.equals("ERROR")){    
       		 response = error("Provider Unreachable");
            }else{
       	String attrib = new XML().xmlGetAttrib(response1, "billPayMsg", "res", "code");

           System.out.println("The Attrib is " + attrib);
           String msg = checkCode(attrib);
           if(msg.equalsIgnoreCase("Successful")){
       	ArrayList array = new XML().xmlGet(response1, "billPayMsg", "payRes", "custAccNum");
       	String accno = (String) array.get(1);
       	ArrayList array3 = new XML().xmlGet(response1, "billPayMsg", "payRes", "rctNum");
       	String recptno = (String) array3.get(1);
       	ArrayList array4 = new XML().xmlGet(response1, "billPayMsg", "payRes", "providerName");
       	String provider = (String) array4.get(1);
        double amt = Double.parseDouble(amount);
        double conver = 0.01;
        double amt2 = amt * conver;
        amount = String.valueOf(amt2);
       	response = new ClientEmulator().PayBillResponse(client, term, seq, time, account_no, recptno, amount, provider, refere, msg, name, bal);
        Logger.getLogger(StartPay.class.getName()).log(Level.INFO, "Complete pay: "+response);
       	String update = "update transaction set status = ? where refnumber = ?";
        PreparedStatement preparedStmt = con.prepareStatement(update);
        preparedStmt.setString(1, "complete");
        preparedStmt.setString(2, refere);
        preparedStmt.executeUpdate();
           }else if(msg.equalsIgnoreCase("Unknown account or meter number.")){
                response = unknownAcc("Unknown Account Number");
            }else{
        	   response = error("Provider Unreachable");
        	   Logger.getLogger(StartPay.class.getName()).log(Level.INFO, "Incomplete pay: "+response);
        	   String update = "update transaction set status = ? where refnumber = ?";
               PreparedStatement preparedStmt = con.prepareStatement(update);
               preparedStmt.setString(1, "incomplete");
               preparedStmt.setString(2, refere);
               preparedStmt.executeUpdate();
               StartInitialPayReversalRequest objc =  new StartInitialPayReversalRequest();
               objc.startInitialPayRevReq(refere, client, term, account_no,msisdn);
           }
           }
            
        }catch (Exception e) {
        	System.out.println("No response from the server=" + e);
                response = error("Provider Unreachable");
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
            posRef.setTextContent(reference);
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
            System.out.println("Transformed   :" + result.getWriter().toString());
            // System.out.println("File saved!");
            //Document document = builder.parse(new InputSource(new StringReader(strMsg)));
            String insert = "INSERT INTO `vending`.`transaction` (`clientid`, `seqnumber`, `account_number`, `amount`, `msisdn`, `status`, `refnumber`, `terminal`, `time`)"
                    + " VALUES ('"+client+"', '" + seq + "', '" + account_no + "', '" + amount + "', '" + msisdn + "', 'sent', '" + reference + "', '" + term + "', '" + time + "')";
            data.insert(insert);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }
        
        public Element PayBillResponse(String client, String term, String seq, String time, String account_no, String recieptno, String amount, String prov , String reference, String message, String name, String balance) {
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
            receiptNum.setTextContent(recieptno);
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
        }
        
 
    public String initialPayRevReq(String originalRef, String client, String term, String account_no, String msisdn) throws Exception {
    	DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        String time = str;
        String seq = createSeq();
        String build = firstReversals(originalRef,  seq, client, term, refere, time, account_no,msisdn);
        //String details=""//connection(build);
        String response2 = "";
        try{
        	//Thread.sleep(20000);
        	response2 = connection(build);
        	System.out.println("RESPONSE:: 2: "+response2);
        	if(response2.equals("") || response2.equals(null)|| response2.equals("ERROR")){    
       		 System.out.println("Initial Reversal  Timeouts at 20secs!!!");
           }else{
        		String attrib = new XML().xmlGetAttrib(response2, "billPayMsg", "res", "code");

                System.out.println("The Attrib is " + attrib);
                String msg = checkCode(attrib);
                if(msg.equalsIgnoreCase("Successful")){
                	/*
                	String query2 = "SELECT * from transaction where refnumber = '"+originalRef+"' ORDER BY vendid DESC LIMIT 1";
                    Statement stm2 = con.createStatement();
                    ResultSet rst2 = stm2.executeQuery(query2);
                    while (rst2.next()) {
                    	String status = rst2.getString("status");
                    	System.out.println("Status: "+status);
                    	if(status.equals("revbackup")){
                    		String insert = "INSERT INTO `vending`.`transaction` (`clientid`, `seqnumber`, `account_number`, `status`, `refnumber`, `msisdn`, `terminal`, `time`)"
                                    + " VALUES ('"+client+"', '" + seq + "', '" + account_no + "', 'initialpayrevcomplete', '" + originalRef + "', '" + msisdn + "', '" + term + "', '" + time + "')";
                            data.insert(insert);	
                            System.out.println("Initial Reversal complete");
                    	}
                    }*/
                	String update = "update transaction set status = ? where refnumber = ?";
                    PreparedStatement preparedStmt = con.prepareStatement(update);
                    preparedStmt.setString(1, "initialpayrevcomplete");
                    preparedStmt.setString(2, originalRef);
                    preparedStmt.executeUpdate();
      
                }else{
                	/*
                	String insert = "INSERT INTO `vending`.`transaction` (`clientid`, `seqnumber`, `account_number`, `status`, `refnumber`, `terminal`, `msisdn`, `time`)"
                            + " VALUES ('"+client+"', '" + createSeq() + "', '" + account_no + "', 'initialpayrevincomplete', '" + originalRef + "', '" + term + "', '" + msisdn+ "', '" + time + "')";
                    data.insert(insert);*/
                    String update = "update transaction set status = ? where refnumber = ?";
                    PreparedStatement preparedStmt = con.prepareStatement(update);
                    preparedStmt.setString(1, "initialpayrevincomplete");
                    preparedStmt.setString(2, originalRef);
                    preparedStmt.executeUpdate();
                  //Save in reversals table
                    String insert2 = "INSERT INTO `vending`.`reversals` (`repcount`,`clientid`, `seqnumber`, `original_ref`, `status`, `refnumber`, `terminal`, `msisdn`, `time`)"
                            + " VALUES ('0','" + client + "', " + seq + ", " + originalRef + ", 'incomplete', '" + refere + "', '" + time + "', '" + msisdn + "', '" + time + "')";
                    data.insert(insert2);
                	
                    Logger.getLogger(StartPay.class.getName()).log(Level.INFO, "First Pay Reversal request Has an error code!!!");
                }
               }
           
            }catch (Exception e) {
            	System.out.println("PARSE ERR:=" + e);
                
            }
           
        return response2;

    }

    public static String firstReversals(String originalref, String seq, String client, String term, String refere, String time, String account_no, String msisdn) {
        
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
            /*
            String insert = "INSERT INTO `vending`.`transaction` (`clientid`, `seqnumber`, `account_number`, `status`, `refnumber`, `msisdn`, `terminal`, `time`)"
                    + " VALUES ('"+client+"', '" + seq + "', '" + account_no + "', 'revbackup', '" + originalref + "', '" + msisdn + "', '" + term + "', '" + time + "')";
            data.insert(insert);
            System.out.println("Initial Reversal backedup");*/
            String update = "update transaction set status = ? where refnumber = ?";
            PreparedStatement preparedStmt = con.prepareStatement(update);
            preparedStmt.setString(1, "revbackup");
            preparedStmt.setString(2, refere);
            preparedStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }
    
        public Object subsequentPayRevReq(String originalRef, String originalTime, String client, String term, String msisdn, String revvid) throws Exception {
    	DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        String time = str;
        String repC = createRepCount();
        String seq = createSeq();
        String build = subsequentReversals(originalRef, originalTime, repC, seq, client, term, refere, time);
        //String details="";//connection(build);
        String response3 = "";
        Object response = null;
        
        try{
        	//Thread.sleep(20000);
        	System.out.println(build);
        	response3 = connection(build);
        	System.out.println("RESPONSE:: 3: "+response3);
            	if(response3.equals("") || response3.equals(null)|| response3.equals("ERROR")){ 
                    response = error("Provider Unreachable");
            		//Save in reversals table
             	   String update = "update reversals set repcount = ?, seqnumber = ?,time = ?, schedule = ? where reversalid = ?";
                    PreparedStatement preparedStmt = con.prepareStatement(update);
                    preparedStmt.setString(1, repC);
                    preparedStmt.setString(2, seq);
                    preparedStmt.setString(3, time);
                    preparedStmt.setString(4, "started");
                    preparedStmt.setString(5, revvid);
                    preparedStmt.executeUpdate();
                }else{
           	String attrib = new XML().xmlGetAttrib(response3, "billPayMsg", "res", "code");

               System.out.println("The Attrib is " + attrib);
               String msg = checkCode(attrib);
               if(msg.equalsIgnoreCase("Successful")){
        	
            String update = "update reversals set status = ? where reversalid = ?";
            PreparedStatement preparedStmt = con.prepareStatement(update);
            preparedStmt.setString(1, "rev complete at repcount: "+repC);
            preparedStmt.setString(2, revvid);
            preparedStmt.executeUpdate();
            //System.out.println(revvid);
            String update2 = "update transaction set status = ? where refnumber = ?";
            PreparedStatement preparedStmt2= con.prepareStatement(update2);
            preparedStmt2.setString(1, "subrevcomplete");
            preparedStmt2.setString(2, originalRef);
            preparedStmt2.executeUpdate();
            System.out.println("Subseq Reversal complete");
            response = error("Subseq Reversal complete");
           
               }else{
            	 //Save in reversals table
            	   String update = "update reversals set repcount = ?, seqnumber = ?,time = ?, schedule = ? where reversalid = ?";
                   PreparedStatement preparedStmt = con.prepareStatement(update);
                   preparedStmt.setString(1, repC);
                   preparedStmt.setString(2, seq);
                   preparedStmt.setString(3, time);
                   preparedStmt.setString(4, "started");
                   preparedStmt.setString(5, revvid);
                   preparedStmt.executeUpdate();
                   response = error(msg);
            	 
               }
              }
            
            }catch (Exception e) {
            	System.out.println("PARSE ERR:=" + e);
                response = error("Provider Unreachable");
            }

        return response;

    }

    public static String subsequentReversals(String originalRef, String originalTime, String repCount,String seq, String client, String term, String refere, String time) {
        
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

            Element payreq = document.createElement("payRevReq");
            billpay.appendChild(payreq);
            payreq.setAttribute("repCount", repCount);
            payreq.setAttribute("origTime", originalTime);

            Element ref = document.createElement("ref");
            ref.setTextContent(refere);
            payreq.appendChild(ref);

            Element origref = document.createElement("origRef");
            origref.setTextContent(originalRef);
            payreq.appendChild(origref);

            Element provider = document.createElement("providerName");
            provider.setTextContent("KPLC Provider");
            payreq.appendChild(provider);

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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }



    //  billInfoReq //
    
    public Object getDetailsAcc(String account_no, String client, String term) {
    	DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        String time = str;
        String seq = createSeq();
        Object response = "before request";

        String build = accountDetails(account_no, seq, time, refere, client, term);
        java.util.logging.Logger.getLogger(ClientEmulator.class.getName())
        .log(java.util.logging.Level.INFO, "BUILD REQUEST :" +build);
        String details = connection(build);
        java.util.logging.Logger.getLogger(ClientEmulator.class.getName())
        .log(java.util.logging.Level.INFO, "RESPONSE DETAILS :" +details);

        System.out.println("Details :" + details);
    
        try{
        	 if(details.equals("") || details.equals(null)|| details.equals("ERROR")){    
        		 response = error("Provider Unreachable");
             }else{
        	String attrib = new XML().xmlGetAttrib(details, "billPayMsg", "res", "code");

            System.out.println("The Attrib is " + attrib);
            String msg = checkCode(attrib);
            if(msg.equalsIgnoreCase("Successful")){
        	ArrayList array = new XML().xmlGet(details, "billPayMsg", "billInfoRes", "customer", "custAccNum");
        	String accno = (String) array.get(2);
        	ArrayList array2 = new XML().xmlGet(details, "billPayMsg", "billInfoRes", "customer", "name");
        	String name = (String) array2.get(2);
        	ArrayList array3 = new XML().xmlGet(details, "billPayMsg", "billInfoRes", "accDetail", "balance");
        	String balance = (String) array3.get(2);
        	double dvalue = Double.parseDouble(balance);
        	double conver = 0.01;
        	double dvalue2 = dvalue * conver;
        	String balance2 = String.valueOf(dvalue2);
        	ArrayList array4 = new XML().xmlGet(details, "billPayMsg", "billInfoRes", "accDetail", "accName");
        	String provider = (String) array4.get(2);
        	response = new ClientEmulator().accountDetailsResponse(client, term, seq, time, account_no, name, balance2, provider, refere,msg);
            }else if(msg.equalsIgnoreCase("Unknown account or meter number.")){
                response = unknownAcc("Unknown Account Number");
            }else{
            	response = error("Provider Unreachable");
            }
            }

           }catch (Exception e) {
            	System.out.println("PARSE ERR:=" + e);
                error("Provider Unreachable");
                
            }
        

       return response;

    }

    public static String accountDetails(String account_no, String seq, String time, String reference, String client, String term) {
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
            //custAccNum.setTextContent("1234567-89");
            custAccNum.setTextContent(account_no);
            payreq.appendChild(custAccNum);

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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }
    public Element accountDetailsResponse(String client, String term, String seq, String time, String account_no, String name, String balance, String prov , String reference, String message) {
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
            ref.setTextContent(balance);
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
    }

       public String getPayLastReq(String client, String term, String provName, String provRef) {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        String time = str;
        String seq = createSeq();

        String build = payLastReq(seq, time, client, term, refere, provName, provRef);
       
        String response4 = connection(build);
        String response = null;
        try{
        	 if(response4.equals("") || response4.equals(null)|| response4.equals("ERROR")){    
        		 response = new ClientEmulator().payLastReqResponse(client, term, seq, time, " ", " ", " ", refere, " ", " ", "FAILED");
             }else{
            	 
        	String attrib = new XML().xmlGetAttrib(response4, "billPayMsg", "res", "code");
            System.out.println("The Attrib is " + attrib);
            String msg = checkCode(attrib);
            if(msg.equalsIgnoreCase("Successful")){
        	ArrayList array = new XML().xmlGet(response4, "billPayMsg", "payLastRes", "payRes", "custAccNum");
        	String accno = (String) array.get(2);
        	ArrayList array2 = new XML().xmlGet(response4, "billPayMsg", "payLastRes", "payTime");
        	String paytime = (String) array2.get(1);
        	ArrayList array3 = new XML().xmlGet(response4, "billPayMsg", "payLastRes", "payRes", "transaction", "amt");
        	String amt = (String) array3.get(3);
        	ArrayList array4 = new XML().xmlGet(response4, "billPayMsg", "payLastRes", "payRes", "providerName");
        	String provider = (String) array4.get(2);
        	response = new ClientEmulator().payLastReqResponse(client, term, seq, time, accno, amt, provider, refere, provRef, paytime, msg);
            }else{
            	response = new ClientEmulator().payLastReqResponse(client, term, seq, time, " ", " ", " ", refere, " ", " ", msg);
            }
            }
             
            }catch (Exception e) {
            	System.out.println("PARSE ERR:=" + e);
                
            }
        return response;

    }

    public static String payLastReq(String seq, String time, String client, String term, String reference, String provName, String provRef) {
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

            Element payLastReq = document.createElement("payLastReq");
            billpay.appendChild(payLastReq);

            Element ref = document.createElement("ref");
            ref.setTextContent(reference);
            payLastReq.appendChild(ref);
            Element prevRef = document.createElement("prevRef");
            prevRef.setTextContent(provRef);
            payLastReq.appendChild(prevRef);
            
            Element provN = document.createElement("providerName");
            provN.setTextContent(provName);
            payLastReq.appendChild(provN);
            
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
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public String payLastReqResponse(String client, String term, String seq, String time, String account_no, String amt, String prov , String reference, String prevreference, String paytime, String message) {
    	StreamResult result = new StreamResult(new StringWriter());
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            Element pdsl = document.createElement("pdslMsg");
            document.appendChild(pdsl);
            Element root = document.createElement("ipayMsg");
            pdsl.appendChild(root);
            
            root.setAttribute("client", client);
            root.setAttribute("term", term);
            root.setAttribute("seqNum", seq);
            root.setAttribute("time", time);

            Element billpay = document.createElement("billPayMsg");
            root.appendChild(billpay);

            billpay.setAttribute("ver", "1.7");

            Element payLastRes = document.createElement("payLastRes");
            billpay.appendChild(payLastRes);

            Element ref = document.createElement("ref");
            ref.setTextContent(reference);
            payLastRes.appendChild(ref);
            
            Element response = document.createElement("response");
            response.setTextContent(message);
            payLastRes.appendChild(response);
            
            Element payRes = document.createElement("payRes");
            billpay.appendChild(payRes);
            
            Element prevref = document.createElement("ref");
            prevref.setTextContent(prevreference);
            payRes.appendChild(prevref);

            Element provider = document.createElement("providerName");
            provider.setTextContent(prov);
            payRes.appendChild(provider);

            Element custAccNum = document.createElement("custAccNum");
            //custAccNum.setTextContent("1234567-89");
            custAccNum.setTextContent(account_no);
            payRes.appendChild(custAccNum);
            
            Element transaction = document.createElement("transaction");
            payRes.appendChild(transaction);
            
            Element custAccId = document.createElement("custAccId");
            //custAccNum.setTextContent("1234567-89");
            custAccId.setTextContent(account_no);
            transaction.appendChild(custAccId);
            
            Element payamt = document.createElement("amt");
            payamt.setAttribute("cur", "KES");
            payamt.setTextContent(amt);
            transaction.appendChild(payamt);
            
            Element tax = document.createElement("amt");
            tax.setTextContent("0");
            transaction.appendChild(tax);

            Element payTime = document.createElement("payTime");
            payTime.setTextContent(paytime);
            payLastRes.appendChild(payTime);

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

    public String getProviderList(String client, String term) {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = refIncrement();
        String time = str;
        String seq = createSeq();

        String build = providerListReq(seq, time, client, term, refere);
        //String details="";//connection(build);
        String response5 = connection(build);
        String response = null;
        
        try{
       	 if(response5.equals("") || response5.equals(null)|| response5.equals("ERROR")){    
       		response = new ClientEmulator().providerListReqResponse(client, term, seq, time, " ", " ", " ", " ", " ", " ", "FAILED");
         }else{
    	String attrib = new XML().xmlGetAttrib(response5, "billPayMsg", "res", "code");

        System.out.println("The Attrib is " + attrib);
        String msg = checkCode(attrib);
        if(msg.equalsIgnoreCase("Successful")){
    	ArrayList array = new XML().xmlGet(response5, "billPayMsg", "providerListRes", "provider", "providerName");
    	String provider = (String) array.get(2);
    	ArrayList array2 = new XML().xmlGet(response5, "billPayMsg", "providerListRes", "provider", "allowMultiAccPayments");
    	String multiAccPayments = (String) array2.get(2);
    	ArrayList array3 = new XML().xmlGet(response5, "billPayMsg", "providerListRes", "provider", "requireNotifySms");
    	String notifySms = (String) array3.get(2);
    	ArrayList array4 = new XML().xmlGet(response5, "billPayMsg", "providerListRes", "provider", "allowSearchByMeter");
    	String searchByMeter = (String) array4.get(2);
    	ArrayList array5 = new XML().xmlGet(response5, "billPayMsg", "providerListRes", "provider", "externalRefField");
    	String refField = (String) array5.get(2);
    	response = new ClientEmulator().providerListReqResponse(client, term, seq, time, provider, multiAccPayments, notifySms, searchByMeter, refField, refere, msg);
        }else{
        	response = new ClientEmulator().providerListReqResponse(client, term, seq, time, " ", " ", " ", " ", " ", " ", msg);
        }
        }
        }catch (Exception e) {
        	System.out.println("PARSE ERR:=" + e);
            
        }

        return response;

    }
    


    public static String providerListReq(String seq, String time, String client, String term, String reference) {
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

            Element payreq = document.createElement("providerListReq");
            billpay.appendChild(payreq);

            Element ref = document.createElement("ref");
            ref.setTextContent(reference);
            payreq.appendChild(ref);


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
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public String providerListReqResponse(String client, String term, String seq, String time, String prov, String MultiAccPayments, String NotifySms, String SearchByMeter, String RefField , String reference, String message) {
    	StreamResult result = new StreamResult(new StringWriter());
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            Element pdsl = document.createElement("pdslMsg");
            document.appendChild(pdsl);
            Element root = document.createElement("ipayMsg");
            pdsl.appendChild(root);
            
            root.setAttribute("client", client);
            root.setAttribute("term", term);
            root.setAttribute("seqNum", seq);
            root.setAttribute("time", time);

            Element billpay = document.createElement("billPayMsg");
            root.appendChild(billpay);

            billpay.setAttribute("ver", "1.7");

            Element providerListRes = document.createElement("providerListRes");
            billpay.appendChild(providerListRes);

            Element ref = document.createElement("ref");
            ref.setTextContent(reference);
            providerListRes.appendChild(ref);
            
            Element response = document.createElement("response");
            response.setTextContent(message);
            providerListRes.appendChild(response);
            
            Element provider = document.createElement("payment");
            providerListRes.appendChild(provider);

            Element providerName = document.createElement("providerName");
            providerName.setTextContent(prov);
            provider.appendChild(providerName);

            Element allowMultiAccPayments = document.createElement("allowMultiAccPayments");
            allowMultiAccPayments.setTextContent(MultiAccPayments);
            provider.appendChild(allowMultiAccPayments);
            
            Element requireNotifySms = document.createElement("requireNotifySms");
            requireNotifySms.setTextContent(NotifySms);
            provider.appendChild(requireNotifySms);

            Element allowSearchByMeter = document.createElement("allowSearchByMeter");
            allowSearchByMeter.setTextContent(SearchByMeter);
            provider.appendChild(allowSearchByMeter);
            
            Element externalRefField = document.createElement("externalRefField");
            externalRefField.setTextContent(RefField);
            provider.appendChild(externalRefField);

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
    public Element unknownAcc(String message) {
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

            Element payreq = document.createElement("customerError");
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
    
    public String createSeq(){
    	
    	if(seq < 99999){
    		seq++;
    	}else{
    	   seq = 1;	
    	}
    		
		return Integer.toString(seq);
    }
 public String createRepCount(){
    	
    	
    		repCount++;
    	
    		
		return Integer.toString(repCount);
    }
 
    public String checkCode(String attrib){
    	
   	 if (attrib.equals("billPay000")) {
            System.out.println("succesful");
            return "Successful";
        } else if (attrib.equals("billPay001")) {
            //General error
            System.out.println("General err");
            return "General Error";
        } 
        else if (attrib.equals("billPay002")) {
            //Service not available. Service provider is down 
        	System.out.println("Service not available. Service provider is down.");
       	 return "Service not available. Service provider is down.";
            
        } 
        else if (attrib.equals("billPay003")) {
            //No rec of prev trans..ref value not recognized
        	//the payment being reversed was not found, so nothing to reverse
       	 System.out.println("No record of previous transaction");
       	 return "No record of previous transaction";
        } 
        else if (attrib.equals("billPay004")) {
            //Reversals are not supported by the bill provider.
            System.out.println("Reversal Not supported");
            return "Reversal Not supported";
        } else if (attrib.equals("billPay005")) {
            //Non unique reference. The reference of the request message for this
            //client and terminal combination already exists in the system. 
       	 return "Non unique reference.";

        } else if (attrib.equals("billPay010")) {
            //Unknown account or meter number.
            System.out.println("UNKNOWN ACCOUNT/METER NUMBER");
            return "Unknown account or meter number.";

        } else if (attrib.equals("billPay013")) {
            //Invalid Amount. The upper or lower limit on the amount that may 
            //in a single transaction or over a period of time has been passed.
       	 
       	 return "The upper or lower limit on the amount has been passed";

        } else if (attrib.equals("billPay015")) {
            // Amount too high. Request amount exceeds the maximum for this
            //payment. Try a smaller amount.
       	 return "Request amount exceeds the maximum";
        } else if (attrib.equals("billPay016")) {
            //Amount too low. Request amount exceeds the minimum amount for this 
            //payment. Try a larger amount.
       	 return "Amount too low";
        } else if (attrib.equals("billPay018")) {
            //Multiple item payment not supported. Paying of multiple account items
            //not supported
       	 return "Multiple item payment not supported.";

        } else if (attrib.equals("billPay019")) {
            //Already reversed. The request cannot be processed due to the past 
            //transaction it refers to already having been reversed.
       	 return "Already reversed.";

        } else if (attrib.equals("billPay020")) {
             
       	 return "Transaction already completed.";

        } else if (attrib.equals("billPay023")) {        
            
       	 return "The payment type specified in the request in not recognized.";

        } else if (attrib.equals("billPay030")) {
              
       	 return "The format of the request or response is incorrect.";

        } else if (attrib.equals("billPay036")) {
            
            return "The supplier does not support reprints by reference.";
        } 
        else if (attrib.equals("billPay040")) {
     
              return " The client system is disabled.";
        } 
        else if (attrib.equals("billPay041")) {            
            
              return "metre lenth invalid";
        } 
        else if (attrib.equals("billPay042")) {
            
             return "Client blocked";
        } 
        else if (attrib.equals("billPay043")) {	
            
              return "Provide a proper customer account number or meter number";
        } 
        else if (attrib.equals("billPay044")) {
             
           return "Meter identification is required for this type of account payment.";
        }
        else if (attrib.equals("billPay900")) {
            // General system error. (see message for details)
        	//
        	return "General system error";
           
        }
        else if (attrib.equals("billPay901")) {
            // Unsupported message version number.
        	//of account payment.
       	 return "Unsupported message version number.";
        	
        }
        else if (attrib.equals("billPay902")) {
            // Invalid Reference. The value of the <ref> element in invalid (e.g. It's too
       	// long or empty)
       	 return "Invalid Reference.";
        }
        else if (attrib.equals("billPay020")) {
            // Transaction already completed. The request cannot be processed due
            // to the past transaction it refers to already having been completed.
        	return "Transaction already completed.";
        }
		return "CodeErr";

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
        String sec=null;
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
        
        
        if (dt.getDayOfYear() < 99 && dt.getDayOfYear() > 9) {
            day = "0" + dt.getDayOfYear();
        } else if (dt.getDayOfYear() < 9) {
            day = "00" + dt.getDayOfYear();
        } else {
            day = "" + dt.getDayOfYear();
        }
        year = "" + dt.getYear();
        year = year.substring(year.length() - 1);
        build.append(year).append(day).append(hour).append(min).append(sec);
        System.out.println(year);
        System.out.println(day);

        System.out.println(min);

        System.out.println(build.toString());
        try {
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
                //int ref = rst.getInt(1) + 1;

                //return "" + ref;
                return build.toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientEmulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    } // HTTP POST request
 
   
}