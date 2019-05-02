/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knecairtel;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.commons.util.windowing.WindowFuture;
import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.pdu.EnquireLink;
import com.cloudhopper.smpp.pdu.EnquireLinkResp;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.tlv.Tlv;
import com.cloudhopper.smpp.type.Address;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.netty.channel.DefaultChannelFuture;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class Knecairtel {

    /**
     * @param args the command line arguments
     */
    static DataStore data = new DataStore();
    static Knecairtel2 k2 = new Knecairtel2();
    static Charging ch = new Charging();
    static String host = "41.223.59.92";
    static int port = 8188;
    static String systemId = "PDSLKNECMoMt";
    //static String password = "pdsl@1234";
    static String password = "pdsl@2019";
    static SmppSession session0 = null;
    static int counter;
    static int trxcounter;

    public static void main(String[] args) {
        // TODO code application logic here
        try {
            ScheduledThreadPoolExecutor monitorExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, new ThreadFactory() {
                private AtomicInteger sequence = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("SmppClientSessionWindowMonitorPool-" + sequence.getAndIncrement());
                    return t;
                }
            });
            DefaultSmppClient clientBootstrap = new DefaultSmppClient(Executors.newCachedThreadPool(), 1, monitorExecutor);
            DefaultChannelFuture.setUseDeadLockChecker(false);
            DefaultSmppSessionHandler sessionHandler = new ClientSmppSessionHandler();
            SmppSessionConfiguration config0 = new SmppSessionConfiguration();
            config0.setWindowSize(2000);
            config0.setName("pdsl.Session.0");
            config0.setType(SmppBindType.TRANSCEIVER);
            config0.setSystemType("smpp");
            config0.setHost(host);
            config0.setPort(port);
            config0.setConnectTimeout(10000);
            config0.setSystemId(systemId);
            config0.setPassword(password);
            config0.getLoggingOptions().setLogBytes(true);
            config0.setRequestExpiryTimeout(30000);
            config0.setWindowMonitorInterval(15000);
            config0.setCountersEnabled(true);
            session0 = clientBootstrap.bind(config0, sessionHandler);
            Timer timer = new Timer();
            timer.schedule(new SayHello(), 0, 10000);

            Runnable runnable = () -> {
                for (;;) {
                    try {
                        Connection con = data.connect();
                        String query = "SELECT id, sender, shortcode,outmessage FROM sms78 where  smsc='AIRTEL' and status=0 order by id desc";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        while (rs.next()) {
                            int snd_id = rs.getInt(1);
                            String snd_sender = rs.getString(3);
                            String snd_to = rs.getString(2);
                            String snd_txt = rs.getString(4);
                            String text160 = snd_txt;
                            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "AIRTEL SEND78 From:" + snd_sender + " To:" + snd_to + " Message:" + text160);
                            //String text160 = "\u20AC Lorem [ipsum] dolor sit amet, consectetur adipiscing elit. Proin feugiat, leo id commodo tincidunt, nibh diam ornare est, vitae accumsan risus lacus sed sem metus.";
                            String msgid = "NONE";
                            String sendresults = "NONE";
                            try {

                                ///charging
                                long chargestartTime = System.nanoTime();
                                String chres = ch.ChargeGW78(snd_to);
                                String resstatus = ch.xmlGetCElement(chres, "status");
                                System.out.println("Status: " + resstatus);
                                String reserro = ch.xmlGetCElement(chres, "errorMessage");
                                System.out.println("Charging Error: " + reserro);
                                long chargeendTime = System.nanoTime();
                                long chargeduration = (chargeendTime - chargestartTime);
                                System.out.println(snd_to + " CHARGING REQ RES TOOK:" + chargeduration + " nanoseconds");
                                System.out.println(snd_to + " CHARGING REQ RES TOOK:" + chargeduration / 1000000 + " milliseconds");
                                System.out.println(snd_to + " CHARGING REQ RES TOOK:" + chargeduration / 1000000 / 1000 + " seconds");

                                if (resstatus.equals("Success")) {
                                    Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Sending After Charge From:" + snd_sender + " To:" + snd_to + " Message:" + snd_txt);

                                    byte[] textBytes = CharsetUtil.encode(snd_txt, CharsetUtil.CHARSET_GSM);

                                    long submitstartTime = System.nanoTime();
                                    SubmitSm submit0 = new SubmitSm();
                                    submit0.setSourceAddress(new Address((byte) 0x00, (byte) 0x00, snd_sender));
                                    submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, snd_to));
                                    submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                                    if (textBytes != null && textBytes.length > 255) {
                                        submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                                    } else {
                                        submit0.setShortMessage(textBytes);
                                    }
                                    SubmitSmResp submitResp = session0.submit(submit0, 5000);
                                    Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);

                                    long submitendTime = System.nanoTime();
                                    long submitduration = (submitendTime - submitstartTime);
                                    System.out.println(snd_to + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                                    System.out.println(snd_to + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                                    System.out.println(snd_to + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                                    msgid = submitResp.getMessageId();
                                    sendresults = submitResp.getResultMessage();
                                } else if (resstatus.equals("Failure")) {
                                    if (reserro.startsWith("Insufficient")) {
                                        snd_txt = "Dear customer, you have insufficient airtime, please top up to enjoy this service";
                                        Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Sending Bulk From:" + snd_sender + " To:" + snd_to + " Message:" + snd_txt);

                                        byte[] textBytes = CharsetUtil.encode(snd_txt, CharsetUtil.CHARSET_GSM);

                                        long submitstartTime = System.nanoTime();
                                        SubmitSm submit0 = new SubmitSm();
                                        submit0.setSourceAddress(new Address((byte) 0x00, (byte) 0x00, snd_sender));
                                        submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, snd_to));
                                        submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                                        if (textBytes != null && textBytes.length > 255) {
                                            submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                                        } else {
                                            submit0.setShortMessage(textBytes);
                                        }
                                        SubmitSmResp submitResp = session0.submit(submit0, 5000);
                                        Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);

                                        long submitendTime = System.nanoTime();
                                        long submitduration = (submitendTime - submitstartTime);
                                        System.out.println(snd_to + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                                        System.out.println(snd_to + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                                        System.out.println(snd_to + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                                        msgid = submitResp.getMessageId();
                                        sendresults = submitResp.getResultMessage();
                                    } else {
                                        //do nothing
                                    }
                                } else {
                                    //do nothing
                                }
                            } catch (Exception myex) {
                                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SERVER TOO SO LONG: " + myex);
                            }
                            PreparedStatement prep = null;
                            String update = "update sms78 set msgid = ?,sendresults = ?,status = ? where id = ? ";
                            prep = con.prepareStatement(update);
                            prep.setString(1, msgid);
                            prep.setString(2, sendresults);
                            prep.setString(3, "1");
                            prep.setInt(4, snd_id);
                            prep.executeUpdate();
                            prep.close();
                        }
                        con.close();
                    } catch (Exception e) {
                    }
                }
            };
            Thread t = new Thread(runnable);
            t.start();
            //trx.sendSMS("2018-11-14 15:33:28", "20076", "Test Knecairtel from client", "254728064120");
            /*try {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Sending Wrong From Julius");

                byte[] textBytes = CharsetUtil.encode("ADUKO IRENE EDINAH INDEX: 27950601032 ENG 49C KIS 54C KSL = = MAT 49C SCI 34D SSR 48C TOTAL 234 KNEC HELPLINE 0800724900", CharsetUtil.CHARSET_GSM);

                SubmitSm submit0 = new SubmitSm();
                submit0.setSourceAddress(new Address((byte) 0x00, (byte) 0x00, "20076"));
                submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, "254782217794"));
                submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                if (textBytes != null && textBytes.length > 255) {
                    submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                } else {
                    submit0.setShortMessage(textBytes);
                }
                SubmitSmResp submitResp = session0.submit(submit0, 5000);
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);
            } catch (Exception myex) {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SERVER TOO SO LONG: " + myex);
            }*/
        } catch (SmppTimeoutException ex) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "SmppTimeoutException" + ex);
            String command = "/opt/applications/smppclients/airtel/airtel.sh >> /opt/applications/smppclients/airtel/nohup.out 2>&1";
            System.out.println("Command:=>> " + command);
            data.terminalCMD(command);
        } catch (SmppChannelException ex) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "SmppChannelException Lost Connection" + ex);
            String command = "/opt/applications/smppclients/airtel/airtel.sh >> /opt/applications/smppclients/airtel/nohup.out 2>&1";
            System.out.println("Command:=>> " + command);
            data.terminalCMD(command);
        } catch (UnrecoverablePduException ex) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "UnrecoverablePduException" + ex);
            String command = "/opt/applications/smppclients/airtel/airtel.sh >> /opt/applications/smppclients/airtel/nohup.out 2>&1";
            System.out.println("Command:=>> " + command);
            data.terminalCMD(command);
        } catch (InterruptedException ex) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "InterruptedException" + ex);
            String command = "/opt/applications/smppclients/airtel/airtel.sh >> /opt/applications/smppclients/airtel/nohup.out 2>&1";
            System.out.println("Command:=>> " + command);
            data.terminalCMD(command);
        }
    }

    /**
     * Could either implement SmppSessionHandler or only override select methods
     * by extending a DefaultSmppSessionHandler.
     */
    public static class ClientSmppSessionHandler extends DefaultSmppSessionHandler {

        public ClientSmppSessionHandler() {
            ///super(logger);
        }

        @Override
        public void firePduRequestExpired(PduRequest pduRequest) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "PDU request expired: {}", pduRequest);
        }

        @Override
        public PduResponse firePduRequestReceived(PduRequest pduRequest) {
            PduResponse response = pduRequest.createResponse();
            // do any logic here
            long startTime = System.nanoTime();
            counter = counter + 1;
            Runnable runnable = () -> {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SMS Received: {}", pduRequest);
                if (pduRequest.getCommandId() == SmppConstants.CMD_ID_DELIVER_SM) {
                    DeliverSm mo = (DeliverSm) pduRequest;
                    int length = mo.getShortMessageLength();
                    String from = mo.getSourceAddress().getAddress();
                    String dest = mo.getDestAddress().getAddress();
                    String serviceT = mo.getServiceType();
                    byte[] shortMessage = mo.getShortMessage();
                    String message = new String(shortMessage);
                    //processSms(dest_address.getAddress(), mo.getShortMessage().toString());
                    DateTime idt = new DateTime();
                    DateTimeFormatter ifmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                    String intime = ifmt.print(idt);
                    if (!message.contains("dlvrd")) {
                        Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Inbox: From:" + from + " To:" + dest + " Msg:" + message + " Time:" + intime);
                        if (dest.equals("20076")) {
                            String insert = "INSERT INTO `knecsms`.`inbox` (`smsc`, `osmsc`, `sender`, `reciever`, `message`) VALUES ('AIRTEL', '" + serviceT + "', '" + from + "', '" + dest + "', '" + message + "')";
                            data.insert(insert);
                            sendSMS(intime, dest, message, from);
                        } else {
                            String insert = "INSERT INTO `knecsms`.`inbox78` (`smsc`, `osmsc`, `sender`, `reciever`, `message`) VALUES ('AIRTEL', '" + serviceT + "', '" + from + "', '" + dest + "', '" + message + "')";
                            data.insert(insert);
                            try {
                                ToKnec.postInbox(intime.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""), from, message);
                            } catch (IOException ex) {
                                Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Dlry: From:" + from + " To:" + dest + " Msg:" + message + " Time:" + intime);
                        if (dest.equals("20076")) {
                            String insert = "INSERT INTO `knecsms`.`dlry` (`smsc`, `osmsc`, `sender`, `reciever`, `message`) VALUES ('AIRTEL', '" + serviceT + "', '" + from + "', '" + dest + "', '" + message + "')";
                            data.insert(insert);
                        } else {
                            String insert = "INSERT INTO `knecsms`.`dlry78` (`smsc`, `osmsc`, `sender`, `reciever`, `message`) VALUES ('AIRTEL', '" + serviceT + "', '" + from + "', '" + dest + "', '" + message + "')";
                            data.insert(insert);
                        }
                    }
                }
            };
            Thread t = new Thread(runnable);
            t.start();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            System.out.println("MO COUNTER: " + counter);
            System.out.println("MO RES TOOK:" + duration + " nanoseconds");
            System.out.println("MO RES TOOK:" + duration / 1000000 + " milliseconds");
            System.out.println("MO RES TOOK:" + duration / 1000000 / 1000 + " seconds");
            return response;
        }
    }

    private static void sendSMS(String intime, String dest, String message, String from) {
        if (session0 == null) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Session IS null");
        }
        if (session0 != null) {
            String snd_sender = dest;
            long startTime = System.currentTimeMillis();
            String snd_txt = data.getMessage(message);
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            System.out.println("QUERY RESULT TOOK:" + duration + " milliseconds");
            System.out.println("QUERY RESULT TOOK:" + duration / 1000 + " seconds");
            if (!snd_txt.equals("FAILED")) {
                String msgid = "NONE";
                String sendresults = "NONE";
                trxcounter = trxcounter + 1;
                if (snd_txt.contains("index number does not") || snd_txt.contains("results are not available")) {
                    try {
                        Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Sending Wrong index From:" + snd_sender + " To:" + from + " Message:" + snd_txt);

                        byte[] textBytes = CharsetUtil.encode(snd_txt, CharsetUtil.CHARSET_GSM);

                        long submitstartTime = System.nanoTime();
                        SubmitSm submit0 = new SubmitSm();
                        submit0.setSourceAddress(new Address((byte) 0x00, (byte) 0x00, snd_sender));
                        submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, from));
                        submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                        if (textBytes != null && textBytes.length > 255) {
                            submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                        } else {
                            submit0.setShortMessage(textBytes);
                        }
                        SubmitSmResp submitResp = session0.submit(submit0, 5000);
                        Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);

                        long submitendTime = System.nanoTime();
                        long submitduration = (submitendTime - submitstartTime);
                        System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                        System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                        System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                        msgid = submitResp.getMessageId();
                        sendresults = submitResp.getResultMessage();
                    } catch (Exception myex) {
                        Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SERVER TOO SO LONG: " + myex);
                    }
                } else {
                    try {

                        ///charging
                        long chargestartTime = System.nanoTime();
                        String chres = ch.ChargeGW(from);
                        String resstatus = ch.xmlGetCElement(chres, "status");
                        System.out.println("Status: " + resstatus);
                        String reserro = ch.xmlGetCElement(chres, "errorMessage");
                        System.out.println("Charging Error: " + reserro);
                        long chargeendTime = System.nanoTime();
                        long chargeduration = (chargeendTime - chargestartTime);
                        System.out.println(from + " CHARGING REQ RES TOOK:" + chargeduration + " nanoseconds");
                        System.out.println(from + " CHARGING REQ RES TOOK:" + chargeduration / 1000000 + " milliseconds");
                        System.out.println(from + " CHARGING REQ RES TOOK:" + chargeduration / 1000000 / 1000 + " seconds");

                        if (resstatus.equals("Success")) {
                            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Sending After Charge From:" + snd_sender + " To:" + from + " Message:" + snd_txt);

                            byte[] textBytes = CharsetUtil.encode(snd_txt, CharsetUtil.CHARSET_GSM);

                            long submitstartTime = System.nanoTime();
                            SubmitSm submit0 = new SubmitSm();
                            submit0.setSourceAddress(new Address((byte) 0x00, (byte) 0x00, snd_sender));
                            submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, from));
                            submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                            if (textBytes != null && textBytes.length > 255) {
                                submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                            } else {
                                submit0.setShortMessage(textBytes);
                            }
                            SubmitSmResp submitResp = session0.submit(submit0, 5000);
                            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);

                            long submitendTime = System.nanoTime();
                            long submitduration = (submitendTime - submitstartTime);
                            System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                            System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                            System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                            msgid = submitResp.getMessageId();
                            sendresults = submitResp.getResultMessage();
                        } else if (resstatus.equals("Failure")) {
                            if (reserro.startsWith("Insufficient")) {
                                snd_txt = "Dear customer, you have insufficient airtime, please top up to enjoy this service";
                                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Sending Bulk From:" + snd_sender + " To:" + from + " Message:" + snd_txt);

                                byte[] textBytes = CharsetUtil.encode(snd_txt, CharsetUtil.CHARSET_GSM);

                                long submitstartTime = System.nanoTime();
                                SubmitSm submit0 = new SubmitSm();
                                submit0.setSourceAddress(new Address((byte) 0x00, (byte) 0x00, snd_sender));
                                submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, from));
                                submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                                if (textBytes != null && textBytes.length > 255) {
                                    submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                                } else {
                                    submit0.setShortMessage(textBytes);
                                }
                                SubmitSmResp submitResp = session0.submit(submit0, 5000);
                                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);

                                long submitendTime = System.nanoTime();
                                long submitduration = (submitendTime - submitstartTime);
                                System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                                System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                                System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                                msgid = submitResp.getMessageId();
                                sendresults = submitResp.getResultMessage();
                            } else {
                                //do nothing
                            }
                        } else {
                            //do nothing
                        }
                    } catch (Exception myex) {
                        Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SERVER TOO SO LONG: " + myex);
                    }
                }
                DateTime ndt = new DateTime();
                DateTimeFormatter nfmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                String nntime = nfmt.print(ndt);
                System.out.println("Current Time:" + nntime);
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SAVING:" + intime + "||" + from + "||" + snd_sender + "||" + message + "||" + snd_txt + "||" + msgid + "||" + sendresults);
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "sendWindow.size: {}", session0.getSendWindow().getSize());
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Window data. max size : " + session0.getSendWindow().getMaxSize() + " size : " + session0.getSendWindow().getSize() + " free : " + session0.getSendWindow().getFreeSize() + " pending : " + session0.getSendWindow().getPendingOfferCount());
                long savestartTime = System.nanoTime();
                if (msgid == null) {
                    msgid = "NONE";
                }
                final String mid = msgid;
                final String sr = sendresults;
                String insert = "insert into sms (time_recieved,smsc,sender,shortcode,inmessage,outmessage,msgid,sendresults) values (?,?,?,?,?,?,?,?)";
                try {
                    Connection con = data.connect();
                    PreparedStatement prep = con.prepareStatement(insert);
                    prep.setString(1, intime);
                    prep.setString(2, "AIRTEL");
                    prep.setString(3, from);
                    prep.setString(4, snd_sender);
                    prep.setString(5, message);
                    prep.setString(6, snd_txt);
                    prep.setString(7, mid);
                    prep.setString(8, sr);
                    prep.execute();
                    prep.close();
                    con.close();
                } catch (SQLException sq) {
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "SAVING ERROR!!!!!!!!!!!!!!!!!!" + sq);
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "WUUIIII SAVING ERROR!!!!!!!!!!!!!!!!!!" + sq);
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "WOOIII SAVING ERROR!!!!!!!!!!!!!!!!!!" + sq);
                }
                long saveendTime = System.nanoTime();
                long saveduration = (saveendTime - savestartTime);
                System.out.println("SAVE TOOK:" + saveduration + " nanoseconds");
                System.out.println("SAVE TOOK:" + saveduration / 1000000 + " milliseconds");
                System.out.println("SAVE TOOK:" + saveduration / 1000000 / 1000 + " seconds");
                System.out.println("TRX COUNTER: " + trxcounter);
            } else {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "GET MESSAGE FROM TONY FAILED");
            }
        } else {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "TRX Session is NULL!!!");
        }
    }

    static class SayHello extends TimerTask {

        public void run() {
            try {
                // demo of an "asynchronous" enquireLink call - send it, get a future,
                // and then optionally choose to pick when we wait for it
                WindowFuture<Integer, PduRequest, PduResponse> future0 = session0.sendRequestPdu(new EnquireLink(), 10000, true);
                if (!future0.await()) {
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "Failed to receive MO enquire_link_resp within specified time");
                } else if (future0.isSuccess()) {
                    EnquireLinkResp enquireLinkResp2 = (EnquireLinkResp) future0.getResponse();
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "MO enquire_link_resp #2: commandStatus [" + enquireLinkResp2.getCommandStatus() + "=" + enquireLinkResp2.getResultMessage() + "]");
                } else {
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "Failed to properly receive MO enquire_link_resp: " + future0.getCause());
                }
            } catch (RecoverablePduException ex) {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "RecoverablePduException" + ex);
                String command = "/opt/applications/smppclients/airtel/airtel.sh >> /opt/applications/smppclients/airtel/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (UnrecoverablePduException ex) {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "UnrecoverablePduException" + ex);
                String command = "/opt/applications/smppclients/airtel/airtel.sh >> /opt/applications/smppclients/airtel/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (SmppTimeoutException ex) {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "SmppTimeoutException" + ex);
                String command = "/opt/applications/smppclients/airtel/airtel.sh >> /opt/applications/smppclients/airtel/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (SmppChannelException ex) {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "SmppChannelException" + ex);
                String command = "/opt/applications/smppclients/airtel/airtel.sh >> /opt/applications/smppclients/airtel/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (InterruptedException ex) {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "InterruptedException" + ex);
                String command = "/opt/applications/smppclients/airtel/airtel.sh >> /opt/applications/smppclients/airtel/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            }
        }
    }

}
