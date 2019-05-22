package com.pdsl.vending;

import java.io.StringWriter;
import static java.lang.Double.parseDouble;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
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

    static DataStore data = new DataStore();
    static int seq = 0;
    static int repCount = 0;
    ConnectionManager co = new ConnectionManager();
    String initialtimeout = "45000";
    public String refe = null;

    public String _payPostpaidBill(String vendorcode, String msisdn, String account_no, String amount, String client, String term) throws Exception {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = RefClass.newRef();
        String time = str;
        String seq = RefClass.createSeq();
        String response = "";

        //String amount = "100.07";
        BigDecimal amt2 = new BigDecimal(amount);
        String amount2 = toCents(amt2);
        String ipayamt=new DecimalFormat("#").format(parseDouble(amount2));        
        System.out.println(amount);
        System.out.println("Ipay Amount"+ipayamt);
        
        String build = _postPaidBillSettlement(msisdn, account_no, ipayamt, seq, refere, time, client, term);
        String details = co.connection(build, initialtimeout);//this.co.connection(build);

        double conver = 0.016;
        if (vendorcode.equals("900620")) {
            conver = 0.0;
        }
        double commission = Double.parseDouble(amount) * conver;
        if (Double.parseDouble(amount) > 100000) {
            commission = 100000 * conver;
        }
        String maxLmt = data.chechMaxAmnt(account_no);
        if (maxLmt.equals("found")) {
            commission = 0;
        }
        DateTime tdt = new DateTime();
        DateTimeFormatter tfmt = DateTimeFormat.forPattern("yyyy");
        String year = tfmt.print(tdt);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
        String month = formatter.print(tdt);

        Connection con = data.connect();
        if (details.equals("FAILED")) {
            response = "ServiceNotAvailable Ref:" + refere;

            String values = "insert into transactions" + month + year + "(vendor_code,clientid,seqnumber,refnumber,terminal,ipaytime,tran_type,tran_account,tran_amt,tran_commission,tran_response,ipay_ref,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement prep = con.prepareStatement(values);
                prep.setString(1, vendorcode);
                prep.setString(2, client);
                prep.setString(3, seq);
                prep.setString(4, refere);
                prep.setString(5, term);
                prep.setString(6, time);
                prep.setString(7, "postpaid");
                prep.setString(8, account_no);
                prep.setString(9, amount);
                prep.setDouble(10, commission);
                prep.setString(11, response);
                prep.setString(12, refere);
                prep.setInt(13, 0);
                prep.execute();
                prep.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
            }

            //store in reversals
            String values2 = "insert into reversals(vendor_code,originalref,account) values (?,?,?)";
            try {
                PreparedStatement prep = con.prepareStatement(values2);
                prep.setString(1, vendorcode);
                prep.setString(2, refere);
                prep.setString(3, account_no);
                prep.execute();
                prep.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if ((details.equals("")) || (details.equals(null)) || (details.equals("ERROR")) || (details.equals("TIMEOUT")) || (details.equals("NOCONNECTION"))) {
            response = "Unsuccessfulmsg:ServiceNotAvailable";
        } else {
            /*String values = "insert into transactions" + month + year + "(vendor_code,clientid,seqnumber,refnumber,terminal,ipaytime,tran_type,tran_account,tran_amt,tran_commission,tran_response,ipay_ref,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement prep = con.prepareStatement(values);
                prep.setString(1, vendorcode);
                prep.setString(2, client);
                prep.setString(3, seq);
                prep.setString(4, refere);
                prep.setString(5, term);
                prep.setString(6, time);
                prep.setString(7, "postpaid");
                prep.setString(8, account_no);
                prep.setString(9, amount);
                prep.setDouble(10, commission);
                prep.setString(11, "Waiting");
                prep.setString(12, refere);
                prep.setInt(13, 1);
                prep.execute();
                prep.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            String ipayXmlRes = new IpayXML().xmlGetAttrib(details, "billPayMsg", "res", "code");
            String xmlResSplit[] = ipayXmlRes.split("<<");
            String attrib = xmlResSplit[0];
            String ipaymsg = xmlResSplit[1];
            String msg = checkCode(attrib);
            if (msg.equalsIgnoreCase("Successful")) {
                String rctNum = new IpayXML().xmlGetRctNum(details);
                response = rctNum + ":" + refere;
                String values = "insert into transactions" + month + year + "(vendor_code,clientid,seqnumber,refnumber,terminal,ipaytime,tran_type,tran_account,tran_amt,tran_commission,tran_response,ipay_ref,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                try {
                    PreparedStatement prep = con.prepareStatement(values);
                    prep.setString(1, vendorcode);
                    prep.setString(2, client);
                    prep.setString(3, seq);
                    prep.setString(4, refere);
                    prep.setString(5, term);
                    prep.setString(6, time);
                    prep.setString(7, "postpaid");
                    prep.setString(8, account_no);
                    prep.setString(9, amount);
                    prep.setDouble(10, commission);
                    prep.setString(11, "Paid:" + amount + " For Account:" + account_no + " Reciept:" + rctNum);
                    prep.setString(12, refere);
                    prep.setInt(13, 1);
                    prep.execute();
                    prep.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
                /*String update = "update transactions" + month + year + " set tran_response = ? WHERE refnumber = ?";
                PreparedStatement preparedStmt = con.prepareStatement(update);
                preparedStmt.setString(1, "Paid:" + amount + " For Account:" + account_no + " Reciept:" + rctNum);
                preparedStmt.setString(2, refere);
                preparedStmt.executeUpdate();
                System.out.println("Updated Response");*/

            } else {
                if (ipaymsg.contains("Unknown meter")) {
                    response = "Unsuccessfulmsg:" + ipaymsg;
                } else if (ipaymsg.contains("unknown/not registered")) {
                    response = "Unsuccessfulmsg:Wrong or Unregistered Meter";
                } else {
                    response = "Unsuccessfulmsg:ServiceNotAvailable";
                }
            }

        }

        con.close();
        return response;
    }

    public String _postPaidBillSettlement(String msisdn, String account_no, String amount, String seq, String reference, String time, String client, String term) {
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

            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    public String _getPostPaidDetailsAcc(String account_no, String client, String term) {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = RefClass.newRef();
        String time = str;
        String seq = RefClass.createSeq();
        String response = null;

        String build = _postPaidAccountDetails(account_no, seq, time, refere, client, term);
        String details = co.connection(build, initialtimeout);//this.co.connection(build);

        if ((details.equals("")) || (details.equals(null)) || (details.equals("ERROR")) || (details.equals("TIMEOUT")) || (details.equals("NOCONNECTION"))) {
            response = "ServiceNotAvailable";
        } else {
            try {
                String ipayXmlRes = new IpayXML().xmlGetAttrib(details, "billPayMsg", "res", "code");
                String xmlResSplit[] = ipayXmlRes.split("<<");
                String attrib = xmlResSplit[0];
                String ipaymsg = xmlResSplit[1];
                String msg = checkCode(attrib);
                if (msg.equalsIgnoreCase("Successful")) {
                    String customer = new IpayXML().xmlGetCustomer(details);
                    String phone = new IpayXML().xmlGetPhone(details);
                    String account = new IpayXML().xmlGetAccount(details);
                    double bal = 0;
                    if (new IpayXML().xmlGetBalance(details) != "none") {
                        String balance = new IpayXML().xmlGetBalance(details);
                        double conver = 0.01;
                        bal = Double.parseDouble(balance) * conver;
                    }
                    String duedate = "";
                    if (new IpayXML().xmlGetDueDate(details) != "none") {
                        duedate = new IpayXML().xmlGetDueDate(details);
                    }
                    response = "Owner:" + customer + " Balance: Kshs." + bal + " DueDate:" + duedate;
                } else {
                    if (ipaymsg.contains("Unknown meter")) {
                        response = ipaymsg;
                    } else if (ipaymsg.contains("not registered")) {
                        response = "Unsuccessfulmsg:Wrong or Unregistered Meter";
                    } else if (ipaymsg.contains("unknown/not registered")) {
                        response = "Unsuccessfulmsg:Wrong or Unregistered Meter";
                    } else {
                        response = "ServiceNotAvailable";
                    }
                }
            } catch (Exception en) {
                response = "ServiceNotAvailable";
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

    //>>>>>>>>>>>>>>>>PREPAID!!!!!!!11111111
    public String _prePaidTokenBuy(String vendorcode, String msisdn, String account_no, String amount, String client, String term) throws Exception {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = RefClass.newRef();
        String time = str;
        String seq = RefClass.createSeq();
        String response = "";

        //String amount = "100.07";
        BigDecimal amt2 = new BigDecimal(amount);
        String amount2 = toCents(amt2);
        String ipayamt=new DecimalFormat("#").format(parseDouble(amount2));        
        Logger.getLogger(ProcessRequest.class.getName()).log(Level.INFO,"Converted Amount: "+amount);
        Logger.getLogger(ProcessRequest.class.getName()).log(Level.INFO,"Ipay Amount: "+ipayamt);

        String build = _prePaidTokenRequest(msisdn, account_no, ipayamt, seq, refere, time, client, term);
        String details = co.connection(build, initialtimeout);//this.co.connection(build);

        double conver = 0.016;
        if (vendorcode.equals("900620")) {
            conver = 0.0;
        }
        double commission = Double.parseDouble(amount) * conver;
        if (Double.parseDouble(amount) > 50000) {
            commission = 50000 * conver;
        }
        String maxLmt = data.chechMaxAmnt(account_no);
        System.out.println("maxLmt: " + maxLmt);
        if (maxLmt.equals("found")) {
            commission = 0;
        }
        DateTime tdt = new DateTime();
        DateTimeFormatter tfmt = DateTimeFormat.forPattern("yyyy");
        String year = tfmt.print(tdt);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
        String month = formatter.print(tdt);

        Connection con = data.connect();
        if (details.equals("FAILED")) {
            response = "ServiceNotAvailable Ref:" + refere;
            String values = "insert into transactions" + month + year + "(vendor_code,clientid,seqnumber,refnumber,terminal,ipaytime,tran_type,tran_account,tran_amt,tran_commission,tran_response,ipay_ref,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement prep = con.prepareStatement(values);
                prep.setString(1, vendorcode);
                prep.setString(2, client);
                prep.setString(3, seq);
                prep.setString(4, refere);
                prep.setString(5, term);
                prep.setString(6, time);
                prep.setString(7, "prepaid");
                prep.setString(8, account_no);
                prep.setString(9, amount);
                prep.setDouble(10, commission);
                prep.setString(11, response);
                prep.setString(12, refere);
                prep.setInt(13, 0);
                prep.execute();
                prep.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
            }

            //store in reversals
            String values2 = "insert into reversals(vendor_code,originalref,account) values (?,?,?)";
            try {
                PreparedStatement prep = con.prepareStatement(values2);
                prep.setString(1, vendorcode);
                prep.setString(2, refere);
                prep.setString(3, account_no);
                prep.execute();
                prep.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if ((details.equals("")) || (details.equals(null)) || (details.equals("ERROR")) || (details.equals("TIMEOUT")) || (details.equals("NOCONNECTION"))) {
            response = "Unsuccessfulmsg:ServiceNotAvailable";
        } else {
            String ipayXmlRes = new IpayXML().xmlGetAttrib(details, "elecMsg", "res", "code");
            String xmlResSplit[] = ipayXmlRes.split("<<");
            String attrib = xmlResSplit[0];
            String ipaymsg = xmlResSplit[1];
            String msg = checkCode(attrib);
            if (msg.equalsIgnoreCase("Successful")) {
                String token = new IpayXML().xmlGetToken(details);
                String units = new IpayXML().xmlGetAttrib(details, "elecMsg", "stdToken", "units");
                String elec = new IpayXML().xmlGetAttrib(details, "elecMsg", "stdToken", "amt");
                units = units.replaceAll("<<", "");
                units = units.replaceAll(token, "");
                elec = elec.replaceAll("<<", "");
                elec = elec.replaceAll(token, "");
                double charges = parseDouble(amount) - (parseDouble(elec) / 100);
                response = token + ":" + units + ":" + refere + ":" + parseDouble(elec) / 100 + ":" + Math.round(charges * 100.0) / 100.0;

                String values = "insert into transactions" + month + year + "(vendor_code,clientid,seqnumber,refnumber,terminal,ipaytime,tran_type,tran_account,tran_amt,tran_commission,tran_response,ipay_ref,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                try {
                    PreparedStatement prep = con.prepareStatement(values);
                    prep.setString(1, vendorcode);
                    prep.setString(2, client);
                    prep.setString(3, seq);
                    prep.setString(4, refere);
                    prep.setString(5, term);
                    prep.setString(6, time);
                    prep.setString(7, "prepaid");
                    prep.setString(8, account_no);
                    prep.setString(9, amount);
                    prep.setDouble(10, commission);
                    prep.setString(11, "Token:" + token + " Units:" + units + " Elec:" + parseDouble(elec) / 100 + " Charges:" + Math.round(charges * 100.0) / 100.0 + "");
                    prep.setString(12, refere);
                    prep.setInt(13, 1);
                    prep.execute();
                    prep.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                if (ipaymsg.contains("Unknown meter")) {
                    response = "Unsuccessfulmsg:" + ipaymsg;
                } else if (ipaymsg.contains("Incorrect Meter Number")) {
                    response = "Unsuccessfulmsg:" + ipaymsg;
                } else if (ipaymsg.contains("Luhn validation")) {
                    response = "Unsuccessfulmsg:Wrong or Unregistered Meter";
                } else if (ipaymsg.contains("not registered")) {
                    response = "Unsuccessfulmsg:Wrong or Unregistered Meter";
                } else if (ipaymsg.contains("unknown/not registered")) {
                    response = "Unsuccessfulmsg:Wrong or Unregistered Meter";
                } else if (ipaymsg.contains("time based charges")) {
                    String[] strsplit = ipaymsg.substring(3).split(" ");
                    String inAmt = strsplit[6];
                    String charge = strsplit[12];
                    response = "Unsuccessfulmsg:The meter has accumulated a time based charge of " + charge + "";

                } else if (ipaymsg.contains("The debt amount due")) {//Meter 37196700647 is blocked
                    response = "Unsuccessfulmsg:The meter has accumulated a time based charge; Please retry with an amount higher than (" + amount + ")";
                } else if (ipaymsg.contains("is blocked")) {
                    response = "Unsuccessfulmsg:The meter " + account_no + " is blocked. Outstanding Debt ALERT ID-010081 Contact the Support services for assistance if required.";
                } else {
                    response = "Unsuccessfulmsg:ServiceNotAvailable";
                }

            }

        }

        con.close();
        return response;
    }

    public String _prePaidTokenRequest(String msisdn, String account_no, String amount, String seq, String reference, String time, String client, String term) {
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

            Element elecMsg = document.createElement("elecMsg");
            root.appendChild(elecMsg);

            elecMsg.setAttribute("ver", "2.37");

            Element vendReq = document.createElement("vendReq");
            elecMsg.appendChild(vendReq);

            Element ref = document.createElement("ref");
            ref.setTextContent(reference);
            vendReq.appendChild(ref);

            Element amt = document.createElement("amt");
            amt.setAttribute("cur", "KES");
            amt.setTextContent(amount);
            vendReq.appendChild(amt);

            Element numTokens = document.createElement("numTokens");
            numTokens.setTextContent("1");
            vendReq.appendChild(numTokens);

            Element meter = document.createElement("meter");
            meter.setTextContent(account_no);
            vendReq.appendChild(meter);

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

    public String _getPrePaidDetailsAcc(String account_no, String client, String term) {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = RefClass.newRef();
        String time = str;
        String seq = RefClass.createSeq();
        String response = null;

        String build = _prePaidAccountDetails(account_no, seq, time, refere, client, term);
        String details = co.connection(build, initialtimeout);//this.co.connection(build);
        if ((details.equals("")) || (details.equals(null)) || (details.equals("ERROR")) || (details.equals("TIMEOUT")) || (details.equals("NOCONNECTION"))) {
            response = "ServiceNotAvailable";
        } else {
            try {
                String ipayXmlRes = new IpayXML().xmlGetAttrib(details, "elecMsg", "res", "code");
                String xmlResSplit[] = ipayXmlRes.split("<<");
                String attrib = xmlResSplit[0];
                String ipaymsg = xmlResSplit[1];
                String msg = checkCode(attrib);
                if (msg.equalsIgnoreCase("Successful")) {
                    ArrayList array = new IpayXML().xmlGetAttrib(details, "elecMsg", "customer");
                    response = "Owner:" + array;
                } else {
                    if (ipaymsg.contains("Unknown meter")) {
                        response = ipaymsg;
                    } else if (ipaymsg.contains("Incorrect Meter Number")) {
                        response = ipaymsg;
                    } else if (ipaymsg.contains("Luhn validation")) {
                        response = "Wrong or Unregistered Meter";
                    } else if (ipaymsg.contains("unknown/not registered")) {
                        response = "Wrong or Unregistered Meter";
                    } else {
                        response = "ServiceNotAvailable";
                    }
                }
            } catch (Exception en) {
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
    public String _airTimeTokenBuy(String vendorcode, String msisdn, String account_no, String amount, String client, String term) throws Exception {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss Z");
        String str = fmt.print(dt);
        String refere = RefClass.newRef();
        String time = str;
        String seq = RefClass.createSeq();
        String response = "";

        String build = _airTimeTokenRequest(msisdn, account_no, amount, seq, refere, time, client, term);
        //String typesbuild = _airTimeTokenTypes(seq, refere, time, client, term);
        //String typesdetails = co.connection(typesbuild, initialtimeout);//this.co.connection(build);
        String details = co.connection(build, initialtimeout);//this.co.connection(build);

        String commamt = "0";
        double conver = 0.016;
        if (amount.startsWith("saf_")) {
            conver = 0.06;
            commamt = amount.replaceAll("saf_", "");
        } else if (amount.startsWith("air_")) {
            conver = 0.06;
            commamt = amount.replaceAll("air_", "");
        } else {
            conver = 0.085;
            commamt = amount.replaceAll("ornge_", "");
        }
        System.out.println("commant:" + commamt);
        double commission = Double.parseDouble(commamt) * conver;
        System.out.println("commission:" + commission);

        DateTime tdt = new DateTime();
        DateTimeFormatter tfmt = DateTimeFormat.forPattern("yyyy");
        String year = tfmt.print(tdt);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
        String month = formatter.print(tdt);

        Connection con = data.connect();
        if (details.equals("FAILED")) {
            response = "ServiceNotAvailable Ref:" + refere;

            String values = "insert into transactions" + month + year + "(vendor_code,clientid,seqnumber,refnumber,terminal,ipaytime,tran_type,tran_account,tran_amt,tran_commission,tran_response,ipay_ref,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement prep = con.prepareStatement(values);
                prep.setString(1, vendorcode);
                prep.setString(2, client);
                prep.setString(3, seq);
                prep.setString(4, refere);
                prep.setString(5, term);
                prep.setString(6, time);
                prep.setString(7, "airtime");
                prep.setString(8, amount);
                prep.setString(9, commamt);
                prep.setDouble(10, commission);
                prep.setString(11, response);
                prep.setString(12, refere);
                prep.setInt(13, 0);
                prep.execute();
                prep.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
            }

            //store in reversals
            String values2 = "insert into reversals(vendor_code,originalref,account) values (?,?,?)";
            try {
                PreparedStatement prep = con.prepareStatement(values2);
                prep.setString(1, vendorcode);
                prep.setString(2, refere);
                prep.setString(3, amount);
                prep.execute();
                prep.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if ((details.equals("")) || (details.equals(null)) || (details.equals("ERROR")) || (details.equals("TIMEOUT")) || (details.equals("NOCONNECTION"))) {
            response = "Unsuccessfulmsg:ServiceNotAvailable";
        } else {
            String ipayXmlRes = new IpayXML().xmlGetAttrib(details, "cellMsg", "res", "code");
            String xmlResSplit[] = ipayXmlRes.split("<<");
            String attrib = xmlResSplit[0];
            String ipaymsg = xmlResSplit[1];
            String msg = checkCode(attrib);
            if (msg.equalsIgnoreCase("Successful")) {
                String token = new IpayXML().xmlGetToken(details);
                String units = new IpayXML().xmlGetAttrib(details, "cellMsg", "stdToken", "units");
                units = units.replaceAll("<<", "");
                units = units.replaceAll(token, "");
                response = token + ":" + units + ":" + refere;

                String values = "insert into transactions" + month + year + "(vendor_code,clientid,seqnumber,refnumber,terminal,ipaytime,tran_type,tran_account,tran_amt,tran_commission,tran_response,ipay_ref,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                try {
                    PreparedStatement prep = con.prepareStatement(values);
                    prep.setString(1, vendorcode);
                    prep.setString(2, client);
                    prep.setString(3, seq);
                    prep.setString(4, refere);
                    prep.setString(5, term);
                    prep.setString(6, time);
                    prep.setString(7, "airtime");
                    prep.setString(8, amount);
                    prep.setString(9, commamt);
                    prep.setDouble(10, commission);
                    prep.setString(11, "Token:" + token + " Units:" + units + "");
                    prep.setString(12, refere);
                    prep.setInt(13, 1);
                    prep.execute();
                    prep.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //response = msg;
                response = "Unsuccessfulmsg:ServiceNotAvailable";
            }

        }

        con.close();
        return response;
    }

    public String _airTimeTokenRequest(String msisdn, String account_no, String amount, String seq, String reference, String time, String client, String term) {
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

    /*public String _airTimeTokenTypes(String seq, String reference, String time, String client, String term) {
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

            Element allTokenTypesInfoReq = document.createElement("allTokenTypesInfoReq");
            cellMsg.appendChild(allTokenTypesInfoReq);
            
            allTokenTypesInfoReq.setAttribute("custVer", "false");            

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }*/
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
    public static String toCents(BigDecimal amount) {
    	
    	return amount.multiply(new BigDecimal(100)).toString();
    }

}
