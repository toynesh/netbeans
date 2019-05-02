/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knecsaf;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.commons.util.windowing.WindowFuture;
import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
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
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.sql.*;
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
public class Transmitter {

    DataStore data = new DataStore();
    static Knecsafbulk bulk = new Knecsafbulk();
    SmppSession trxsession = null;
    int port = 7662;//live
    static String host = "192.168.9.21";//live
    int counter;

    public Transmitter() {
        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Transmitter initialized");
        trxSMPPSession();
    }

    private void trxSMPPSession() {
        // GET SERVER IP and port The name of the file to open.
        String fileName = "/opt/applications/smppclients/saftx.txt";
        String line = null;

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int index = 1;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (index == 1) {
                    host = line;
                }
                if (index == 2) {
                    port = parseInt(line);
                }
                index++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }

        ScheduledThreadPoolExecutor monitorExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, new ThreadFactory() {
            private AtomicInteger sequence = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("SmppClientSessionWindowMonitorPool-" + sequence.getAndIncrement());
                return t;
            }
        });
        DefaultChannelFuture.setUseDeadLockChecker(false);
        DefaultSmppClient clientBootstrap = new DefaultSmppClient(Executors.newCachedThreadPool(), 1, monitorExecutor);

        try {
            DefaultSmppSessionHandler sessionHandler2 = new Transmitter.ClientSmppSessionHandler();
            SmppSessionConfiguration config02 = new SmppSessionConfiguration();
            config02.setWindowSize(2000);
            config02.setName("pdsl.Session.0");
            config02.setType(SmppBindType.TRANSMITTER);
            config02.setSystemType("smpp");
            //config02.setHost("192.168.9.96");
            //config02.setPort(6692);
            //config02.setHost("196.201.213.88");//vpn
            config02.setHost(host);//live
            config02.setPort(port);//live
            config02.setConnectTimeout(10000);
            config02.setSystemId("pdsl");
            config02.setPassword("PDSL!@#");
            config02.getLoggingOptions().setLogBytes(true);
            config02.setRequestExpiryTimeout(30000);
            config02.setWindowMonitorInterval(15000);
            config02.setCountersEnabled(true);
            trxsession = clientBootstrap.bind(config02, sessionHandler2);
            Timer timer2 = new Timer();
            timer2.schedule(new Transmitter.SayHello2(), 0, 11000);
            //bulk.sendSMS("2018-11-14 15:33:28", "20076", "Test Bulk from client", "254728064120");
        } catch (SmppTimeoutException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "SmppTimeoutException" + ex);
            String command = "/opt/applications/smppclients/safaricom5/safaricom5.sh >> /opt/applications/smppclients/safaricom5/nohup.out 2>&1";
            System.out.println("Command:=>> " + command);
            data.terminalCMD(command);
        } catch (SmppChannelException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "SmppChannelException" + ex);
            String command = "/opt/applications/smppclients/safaricom5/safaricom5.sh >> /opt/applications/smppclients/safaricom5/nohup.out 2>&1";
            System.out.println("Command:=>> " + command);
            data.terminalCMD(command);
        } catch (UnrecoverablePduException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "UnrecoverablePduException" + ex);
            String command = "/opt/applications/smppclients/safaricom5/safaricom5.sh >> /opt/applications/smppclients/safaricom5/nohup.out 2>&1";
            System.out.println("Command:=>> " + command);
            data.terminalCMD(command);
        } catch (InterruptedException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "InterruptedException" + ex);
            String command = "/opt/applications/smppclients/safaricom5/safaricom5.sh >> /opt/applications/smppclients/safaricom5/nohup.out 2>&1";
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
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "PDU request expired: {}", pduRequest);
        }

        @Override
        public PduResponse firePduRequestReceived(PduRequest pduRequest) {
            PduResponse response = pduRequest.createResponse();
            // do any logic here
            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SMS Received: {}", pduRequest);

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
            System.out.println("QUERY RESULT TOOK:" + duration + " milliseconds");
            System.out.println("QUERY RESULT TOOK:" + duration / 1000 + " seconds");
            if (!snd_txt.equals("FAILED")) {
                String msgid = "NONE";
                String sendresults = "NONE";
                int bulkst = 0;
                long submitstartTime = System.nanoTime();
                counter = counter + 1;
                if (snd_txt.contains("results are not available")) {
                    bulkst = 1;
                    bulk.sendSMS(intime, dest, snd_txt, from);
                } else {
                    try {
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Sending From:" + snd_sender + " To:" + from + " Message:" + snd_txt);

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
                        msgid = submitResp.getMessageId();
                        sendresults = submitResp.getResultMessage();
                        if (!sendresults.equals("OK")) {
                            bulkst = 1;
                            Runnable runnable = () -> {
                                bulk.sendSMS(intime, dest, "Dear customer, you have insufficient airtime, please top up to enjoy this service", from);
                            };
                            Thread t = new Thread(runnable);
                            t.start();
                        }
                    } catch (Exception myex) {
                        Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SERVER TOO SO LONG: " + myex);
                    }
                }
                long submitendTime = System.nanoTime();
                long submitduration = (submitendTime - submitstartTime);
                System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
                System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
                System.out.println(from + " SUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
                DateTime ndt = new DateTime();
                DateTimeFormatter nfmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                String nntime = nfmt.print(ndt);
                System.out.println("Current Time:" + nntime);
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "SAVING:" + intime + "||" + from + "||" + snd_sender + "||" + message + "||" + snd_txt + "||" + msgid + "||" + sendresults);
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "sendWindow.size: {}", trxsession.getSendWindow().getSize());
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "Window data. max size : " + trxsession.getSendWindow().getMaxSize() + " size : " + trxsession.getSendWindow().getSize() + " free : " + trxsession.getSendWindow().getFreeSize() + " pending : " + trxsession.getSendWindow().getPendingOfferCount());
                long savestartTime = System.nanoTime();
                if (msgid == null) {
                    msgid = "NONE";
                }
                final String mid = msgid;
                final String sr = sendresults;
                if (bulkst == 0) {
                    String insert = "insert into sms (time_recieved,smsc,sender,shortcode,inmessage,outmessage,msgid,sendresults) values (?,?,?,?,?,?,?,?)";
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
                    }
                    long saveendTime = System.nanoTime();
                    long saveduration = (saveendTime - savestartTime);
                    System.out.println("SAVE TOOK:" + saveduration + " nanoseconds");
                    System.out.println("SAVE TOOK:" + saveduration / 1000000 + " milliseconds");
                    System.out.println("SAVE TOOK:" + saveduration / 1000000 / 1000 + " seconds");
                    System.out.println("TRX COUNTER: " + counter);
                }
            } else {
                Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "GET MESSAGE FROM TONY FAILED");
            }
        } else {
            Logger.getLogger(Transmitter.class.getName()).log(Level.INFO, "TRX Session is NULL!!!");
        }
    }

    class SayHello2 extends TimerTask {

        public void run() {
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
                String command = "/opt/applications/smppclients/safaricom5/safaricom5.sh >> /opt/applications/smppclients/safaricom5/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (UnrecoverablePduException ex) {
                Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "UnrecoverablePduException" + ex);
                String command = "/opt/applications/smppclients/safaricom5/safaricom5.sh >> /opt/applications/smppclients/safaricom5/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (SmppTimeoutException ex) {
                Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "SmppTimeoutException" + ex);
                String command = "/opt/applications/smppclients/safaricom5/safaricom5.sh >> /opt/applications/smppclients/safaricom5/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (SmppChannelException ex) {
                Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "SmppChannelException" + ex);
                String command = "/opt/applications/smppclients/safaricom5/safaricom5.sh >> /opt/applications/smppclients/safaricom5/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (InterruptedException ex) {
                Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, "InterruptedException" + ex);
                String command = "/opt/applications/smppclients/safaricom5/safaricom5.sh >> /opt/applications/smppclients/safaricom5/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            }
        }
    }
}
