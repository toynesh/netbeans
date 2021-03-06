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
import com.cloudhopper.smpp.util.DeliveryReceipt;
import com.cloudhopper.smpp.util.DeliveryReceiptException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.jboss.netty.channel.DefaultChannelFuture;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class Transmitter {

    static DataStore data = new DataStore();
    static MongoDatabase database = data.mongoconnect();
    static Charging ch = new Charging();
    static String host = "196.216.242.149";
    int port = 8080;
    static String systemId = "Pdslkenya1";
    static String password = "PDSL2019";
    static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    static ScheduledThreadPoolExecutor monitorExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, new ThreadFactory() {
        private AtomicInteger sequence = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("SmppClientSessionWindowMonitorPool-" + sequence.getAndIncrement());
            return t;
        }
    });
    static DefaultSmppClient clientBootstrap = new DefaultSmppClient(Executors.newCachedThreadPool(), 1, monitorExecutor);
    static DefaultSmppSessionHandler sessionHandler = new ClientSmppSessionHandler();
    SmppSession trxsession = null;
    Timer timer2 = new Timer();
    int counter;

    public Transmitter() {;
        timer2.schedule(new Transmitter.SayHello2(), 0, 11000);
    }

    private SmppSession mtSession() {
        // GET SERVER IP and port The name of the file to open.
        String fileName = "/opt/applications/smppclients/airtx.txt";
        //String fileName = "/home/julius/airtx.txt";
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int index = 1;
            while ((line = bufferedReader.readLine()) != null) {
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, line);
                if (index == 1) {
                    host = line;
                }
                if (index == 2) {
                    port = parseInt(line);
                }
                if (index == 3) {
                    systemId = line;
                }
                if (index == 4) {
                    password = line;
                }
                index++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "Error reading file '" + fileName + "'");
        }
        SmppSession rsession = null;
        try {
            DefaultChannelFuture.setUseDeadLockChecker(false);
            SmppSessionConfiguration config0 = new SmppSessionConfiguration();
            config0.setWindowSize(2000);
            config0.setName("pdsl.Session.0");
            config0.setType(SmppBindType.TRANSCEIVER);
            config0.setSystemType("smpp");
            config0.setHost(host);//live
            config0.setPort(port);//live;
            config0.setConnectTimeout(10000);
            config0.setSystemId(systemId);
            config0.setPassword(password);
            config0.getLoggingOptions().setLogBytes(true);
            config0.setRequestExpiryTimeout(30000);
            config0.setWindowMonitorInterval(15000);
            config0.setCountersEnabled(true);
            rsession = clientBootstrap.bind(config0, sessionHandler);
        } catch (SmppTimeoutException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "SmppTimeoutException: " + ex);
        } catch (SmppChannelException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "SmppChannelException: " + ex);
        } catch (UnrecoverablePduException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "UnrecoverablePduException: " + ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "InterruptedException: " + ex);
        }
        return rsession;
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
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "PDU request expired: {}", pduRequest);
        }

        @Override
        public PduResponse firePduRequestReceived(PduRequest pduRequest) {
            PduResponse response = pduRequest.createResponse();
            // do any logic here
            long startTime = System.nanoTime();
            Runnable runnable = () -> {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "SMS Received: {}", pduRequest);
                if (pduRequest.getCommandId() == SmppConstants.CMD_ID_DELIVER_SM) {
                    try {
                        DeliverSm mo = (DeliverSm) pduRequest;
                        int length = mo.getShortMessageLength();
                        String from = mo.getSourceAddress().getAddress();
                        String dest = mo.getDestAddress().getAddress();
                        String serviceT = mo.getServiceType();
                        byte[] shortMessage = mo.getShortMessage();
                        String message = new String(shortMessage);
                        DeliveryReceipt dlr = DeliveryReceipt.parseShortMessage(message, DateTimeZone.UTC, false);
                        //processSms(dest_address.getAddress(), mo.getShortMessage().toString());
                        DateTime idt = new DateTime();
                        DateTimeFormatter ifmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                        String intime = ifmt.print(idt);
                        Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Dlry: From:" + from + " To:" + dest + " Msg:" + message + " Time:" + intime);
                        Runnable irunnable = () -> {
                            /*String insert = "INSERT INTO `knecsms`.`inbox` (`smsc`, `osmsc`, `sender`, `reciever`, `message`) VALUES ('AIRTEL', '" + serviceT + "', '" + from + "', '" + dest + "', '" + message + "')";
                                data.insert(insert);*/
                            MongoCollection<Document> collection = database.getCollection("dlry");
                            Document newInbox = new Document(
                                    "smsc", "AIRTEL")
                                    .append("time", intime)
                                    .append("messageID", dlr.getMessageId())
                                    .append("reciever", from)
                                    .append("sender", dest)
                                    .append("message", dlr.getText());
                            collection.insertOne(newInbox);
                            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "newDlry Document inserted successfully");
                        };
                        Thread it = new Thread(irunnable);
                        it.start();
                    } catch (DeliveryReceiptException ex) {
                        Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            Thread t = new Thread(runnable);
            t.start();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "DLRY RES TOOK:" + duration + " nanoseconds");
            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "DLRY RES TOOK:" + duration / 1000000 + " milliseconds");
            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "DLRY RES TOOK:" + duration / 1000000 / 1000 + " seconds");
            return response;
        }
    }

    public void sendSMS(String intime, String dest, String message, String from) {
        if (trxsession == null) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "TRX Session IS null");
        }
        if (trxsession != null) {
            //Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Session not null");
            String snd_sender = dest;
            long startTime = System.currentTimeMillis();
            String snd_txt = data.getMessage(message);
            //String snd_txt = "QUERIED";
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "QUERY RESULT TOOK:" + duration + " milliseconds");
            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "QUERY RESULT TOOK:" + duration / 1000 + " seconds");
            if (!snd_txt.equals("FAILED")) {
                String msgid = "NONE";
                String sendresults = "NONE";
                //int bulkst = 0;
                long submitstartTime = System.nanoTime();
                counter = counter + 1;
                if (snd_txt.contains("does not exist") || snd_txt.contains("results are not available")) {
                    //bulkst = 1;
                    try {
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Sending Wrong index From:" + snd_sender + " To:" + from + " Message:" + snd_txt);

                        byte[] textBytes = CharsetUtil.encode(snd_txt, CharsetUtil.CHARSET_GSM);
                        SubmitSm submit0 = new SubmitSm();
                        submit0.setSourceAddress(new Address((byte) 0x00, (byte) 0x00, snd_sender));
                        submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, from));
                        submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                        if (textBytes != null && textBytes.length > 255) {
                            submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                        } else {
                            submit0.setShortMessage(textBytes);
                        }
                        SubmitSmResp submitResp = trxsession.submit(submit0, 5000);
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);

                        long submitendTime = System.nanoTime();
                        long submitduration = (submitendTime - submitstartTime);
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                        msgid = submitResp.getMessageId();
                        sendresults = submitResp.getResultMessage();
                    } catch (Exception myex) {
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SERVER TOO SO LONG: " + myex);
                    }
                } else {
                    try {

                        ///charging
                        long chargestartTime = System.nanoTime();
                        String chres = ch.ChargeGW(from);
                        String resstatus = ch.xmlGetCElement(chres, "status");
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Status: " + resstatus);
                        String reserro = ch.xmlGetCElement(chres, "errorMessage");
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Charging Error: " + reserro);
                        long chargeendTime = System.nanoTime();
                        long chargeduration = (chargeendTime - chargestartTime);
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " CHARGING REQ RES TOOK:" + chargeduration + " nanoseconds");
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " CHARGING REQ RES TOOK:" + chargeduration / 1000000 + " milliseconds");
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " CHARGING REQ RES TOOK:" + chargeduration / 1000000 / 1000 + " seconds");

                        if (resstatus.equals("Success")) {
                            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Sending After Charge From:" + snd_sender + " To:" + from + " Message:" + snd_txt);

                            byte[] textBytes = CharsetUtil.encode(snd_txt, CharsetUtil.CHARSET_GSM);
                            SubmitSm submit0 = new SubmitSm();
                            submit0.setSourceAddress(new Address((byte) 0x00, (byte) 0x00, snd_sender));
                            submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, from));
                            submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                            if (textBytes != null && textBytes.length > 255) {
                                submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                            } else {
                                submit0.setShortMessage(textBytes);
                            }
                            SubmitSmResp submitResp = trxsession.submit(submit0, 5000);
                            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);

                            long submitendTime = System.nanoTime();
                            long submitduration = (submitendTime - submitstartTime);
                            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                            msgid = submitResp.getMessageId();
                            sendresults = submitResp.getResultMessage();
                        } else if (resstatus.equals("Failure")) {
                            if (reserro.startsWith("Insufficient")) {
                                snd_txt = "Dear customer, you have insufficient airtime, please top up to enjoy this service";
                                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Sending Bulk From:" + snd_sender + " To:" + from + " Message:" + snd_txt);

                                byte[] textBytes = CharsetUtil.encode(snd_txt, CharsetUtil.CHARSET_GSM);

                                SubmitSm submit0 = new SubmitSm();
                                submit0.setSourceAddress(new Address((byte) 0x00, (byte) 0x00, snd_sender));
                                submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, from));
                                submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                                if (textBytes != null && textBytes.length > 255) {
                                    submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                                } else {
                                    submit0.setShortMessage(textBytes);
                                }
                                SubmitSmResp submitResp = trxsession.submit(submit0, 5000);
                                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);

                                long submitendTime = System.nanoTime();
                                long submitduration = (submitendTime - submitstartTime);
                                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                                msgid = submitResp.getMessageId();
                                sendresults = submitResp.getResultMessage();
                            } else {
                                //do nothing
                                snd_txt = "Dear customer, an error occured trying to query results, please try again later";
                                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Sending Bulk From:" + snd_sender + " To:" + from + " Message:" + snd_txt);

                                byte[] textBytes = CharsetUtil.encode(snd_txt, CharsetUtil.CHARSET_GSM);

                                SubmitSm submit0 = new SubmitSm();
                                submit0.setSourceAddress(new Address((byte) 0x00, (byte) 0x00, snd_sender));
                                submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, from));
                                submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                                if (textBytes != null && textBytes.length > 255) {
                                    submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                                } else {
                                    submit0.setShortMessage(textBytes);
                                }
                                SubmitSmResp submitResp = trxsession.submit(submit0, 5000);
                                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);

                                long submitendTime = System.nanoTime();
                                long submitduration = (submitendTime - submitstartTime);
                                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                                msgid = submitResp.getMessageId();
                                sendresults = submitResp.getResultMessage();
                            }
                        } else {
                            //do nothing
                            snd_txt = "Dear customer, an error occured trying to query results, please try again later";
                            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Sending Bulk From:" + snd_sender + " To:" + from + " Message:" + snd_txt);

                            byte[] textBytes = CharsetUtil.encode(snd_txt, CharsetUtil.CHARSET_GSM);

                            SubmitSm submit0 = new SubmitSm();
                            submit0.setSourceAddress(new Address((byte) 0x00, (byte) 0x00, snd_sender));
                            submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, from));
                            submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                            if (textBytes != null && textBytes.length > 255) {
                                submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                            } else {
                                submit0.setShortMessage(textBytes);
                            }
                            SubmitSmResp submitResp = trxsession.submit(submit0, 5000);
                            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);

                            long submitendTime = System.nanoTime();
                            long submitduration = (submitendTime - submitstartTime);
                            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                            msgid = submitResp.getMessageId();
                            sendresults = submitResp.getResultMessage();
                        }
                    } catch (Exception myex) {
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SERVER TOO SO LONG: " + myex);
                    }
                }
                long submitendTime = System.nanoTime();
                long submitduration = (submitendTime - submitstartTime);
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                DateTime ndt = new DateTime();
                DateTimeFormatter nfmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                String nntime = nfmt.print(ndt);
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Current Time:" + nntime);
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "sendWindow.size: {}", trxsession.getSendWindow().getSize());
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Window data. max size : " + trxsession.getSendWindow().getMaxSize() + " size : " + trxsession.getSendWindow().getSize() + " free : " + trxsession.getSendWindow().getFreeSize() + " pending : " + trxsession.getSendWindow().getPendingOfferCount());
                //if (bulkst == 0) {
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SAVING:" + intime + "||" + from + "||" + snd_sender + "||" + message + "||" + snd_txt + "||" + msgid + "||" + sendresults);
                long savestartTime = System.nanoTime();
                if (msgid == null) {
                    msgid = "NONE";
                }
                final String mid = msgid;
                final String sr = sendresults;
                final String snd_txt_save = snd_txt;
                Runnable runnable = () -> {
                    /*String insert = "insert into sms (time_recieved,smsc,sender,shortcode,inmessage,outmessage,msgid,sendresults) values (?,?,?,?,?,?,?,?)";
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
                            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "SAVING ERROR!!!!!!!!!!!!!!!!!!" + sq);
                            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "WUUIIII SAVING ERROR!!!!!!!!!!!!!!!!!!" + sq);
                            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "WOOIII SAVING ERROR!!!!!!!!!!!!!!!!!!" + sq);
                        }*/
                    MongoCollection<Document> collection = database.getCollection("sms");
                    Document newSMS = new Document(
                            "time_recieved", intime)
                            .append("smsc", "AIRTEL")
                            .append("sender", from)
                            .append("shortcode", snd_sender)
                            .append("inmessage", message)
                            .append("outmessage", snd_txt_save)
                            .append("msgid", mid)
                            .append("sendresults", sr)
                            .append("deliverystatus", "MessageSent");
                    collection.insertOne(newSMS);
                    Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "newSMS Document inserted successfully");
                };
                Thread t = new Thread(runnable);
                t.start();
                long saveendTime = System.nanoTime();
                long saveduration = (saveendTime - savestartTime);
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SAVE TOOK:" + saveduration + " nanoseconds");
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SAVE TOOK:" + saveduration / 1000000 + " milliseconds");
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SAVE TOOK:" + saveduration / 1000000 / 1000 + " seconds");
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "TRX COUNTER: " + counter);
                //}
            } else {
                Logger.getLogger(Transmitter.class.getName()).log(Level.WARNING, "GET MESSAGE FROM TONY FAILED");
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SAVING:" + intime + "||" + from + "||" + snd_sender + "||" + message + "||" + "QUERYING FAILED");
                final String snd_txt_save = snd_txt;
                long savestartTime = System.nanoTime();
                Runnable runnable = () -> {
                    /*String insert = "insert into sms (time_recieved,smsc,sender,shortcode,inmessage,outmessage,msgid,sendresults) values (?,?,?,?,?,?,?,?)";
                        try {
                            Connection con = data.connect();
                            PreparedStatement prep = con.prepareStatement(insert);
                            prep.setString(1, intime);
                            prep.setString(2, "SAFARICOM");
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
                            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "SAVING ERROR!!!!!!!!!!!!!!!!!!" + sq);
                            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "WUUIIII SAVING ERROR!!!!!!!!!!!!!!!!!!" + sq);
                            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "WOOIII SAVING ERROR!!!!!!!!!!!!!!!!!!" + sq);
                        }*/
                    MongoCollection<Document> collection = database.getCollection("sms");
                    Document newSMS = new Document(
                            "time_recieved", intime)
                            .append("smsc", "SAFARICOM")
                            .append("sender", from)
                            .append("shortcode", snd_sender)
                            .append("inmessage", message)
                            .append("outmessage", "QUERYING FAILED")
                            .append("msgid", "")
                            .append("sendresults", snd_txt_save)
                            .append("deliverystatus", "");
                    collection.insertOne(newSMS);
                    Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "newSMS Document inserted successfully");
                };
                Thread t = new Thread(runnable);
                t.start();
                long saveendTime = System.nanoTime();
                long saveduration = (saveendTime - savestartTime);
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SAVE TOOK:" + saveduration + " nanoseconds");
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SAVE TOOK:" + saveduration / 1000000 + " milliseconds");
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SAVE TOOK:" + saveduration / 1000000 / 1000 + " seconds");
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "TRX COUNTER: " + counter);
            }
        } else {
            Logger.getLogger(Transmitter.class.getName()).log(Level.WARNING, "TRX Session is NULL!!!");
        }
    }
    int dis = 0;

    class SayHello2 extends TimerTask {

        public void run() {
            dis = dis + 10;
            if (trxsession == null) {
                Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "TRX Session is null");
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Initializing Transciever");
                trxsession = mtSession();
            } else {
                try {
                    // demo of an "asynchronous" enquireLink call - send it, get a future,
                    // and then optionally choose to pick when we wait for it
                    WindowFuture<Integer, PduRequest, PduResponse> future02 = trxsession.sendRequestPdu(new EnquireLink(), 10000, true);
                    if (!future02.await()) {
                        Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "Failed to  receive TRX enquire_link_resp within specified time");
                    } else if (future02.isSuccess()) {
                        EnquireLinkResp enquireLinkResp2 = (EnquireLinkResp) future02.getResponse();
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "TRX enquire_link_resp #2: commandStatus [" + enquireLinkResp2.getCommandStatus() + "=" + enquireLinkResp2.getResultMessage() + "]");
                    } else {
                        Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "Failed to properly receive TRX enquire_link_resp: " + future02.getCause());
                    }
                } catch (RecoverablePduException ex) {
                    Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "RecoverablePduException" + ex);
                    trxsession = null;
                } catch (UnrecoverablePduException ex) {
                    Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "UnrecoverablePduException" + ex);
                    trxsession = null;
                } catch (SmppTimeoutException ex) {
                    Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "SmppTimeoutException" + ex);
                    trxsession = null;
                } catch (SmppChannelException ex) {
                    Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "SmppChannelException" + ex);
                    trxsession = null;
                } catch (InterruptedException ex) {
                    Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "InterruptedException" + ex);
                    trxsession = null;
                }
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "TX DISCON COUNT" + dis);
                if (dis > 70) {
                    long startTime = System.nanoTime();
                    Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Check for TX Disconnection");
                    Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "=====================================================================");
                    if (trxsession != null) {
                        if (disman().equals("yes")) {
                            trxsession.unbind(3000);
                            trxsession.destroy();
                            trxsession = null;
                        }
                        dis = 0;
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime);
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "TX DISCONCHECK TOOK:" + duration + " nanoseconds");
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "TX DISCONCHECK TOOK:" + duration / 1000000 + " milliseconds");
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "TX DISCONCHECK TOOK:" + duration / 1000000 / 1000 + " seconds");
                    }
                }
            }
        }
    }

    private static String disman() {
        String res = "no";
        String fileName = "/opt/applications/smppclients/discon.txt";
        //String fileName = "/home/julius/discon.txt";
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int index = 1;
            while ((line = bufferedReader.readLine()) != null) {
                if (index == 2) {
                    Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, line);
                    res = line;
                }
                index++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "Error reading file '" + fileName + "'");
        }
        return res;
    }
}
