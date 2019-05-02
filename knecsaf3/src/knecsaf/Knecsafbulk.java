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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
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
public class Knecsafbulk {

    /**
     * @param args the command line arguments
     */
    DataStore data = new DataStore();
    int port = 7662;//live
    static String host = "192.168.9.21";//live
    static String systemId = "pdslblk";
    static String password = "pd$lbu!";
    /*static int port = 8070;
    static String host = "172.27.116.44";
    static String systemId = "pavel";
    static String password = "dfsew";*/
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
    SmppSession trxsession2 = null;
    Timer timer3 = new Timer();
    int counter;

    public Knecsafbulk() {
        timer3.schedule(new Knecsafbulk.SayHello3(), 0, 12000);
    }

    public SmppSession bulkSession() {
        // GET SERVER IP and port The name of the file to open.
        String fileName = "/opt/applications/smppclients/safbulk2.txt";
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
        SmppSession bsession = null;
        try {
            DefaultChannelFuture.setUseDeadLockChecker(false);
            SmppSessionConfiguration config0 = new SmppSessionConfiguration();
            config0.setWindowSize(2000);
            config0.setName("pdsl.Session.0");
            config0.setType(SmppBindType.TRANSMITTER);
            config0.setSystemType("smpp");
            config0.setHost(host);//live
            config0.setPort(port);//live
            config0.setConnectTimeout(10000);
            config0.setSystemId(systemId);
            config0.setPassword(password);
            config0.getLoggingOptions().setLogBytes(true);
            config0.setRequestExpiryTimeout(30000);
            config0.setWindowMonitorInterval(15000);
            config0.setCountersEnabled(true);
            bsession = clientBootstrap.bind(config0, sessionHandler);

        } catch (SmppTimeoutException ex) {
            Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "SmppTimeoutException: " + ex);
        } catch (SmppChannelException ex) {
            Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "SmppChannelException: " + ex);
        } catch (UnrecoverablePduException ex) {
            Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "UnrecoverablePduException: " + ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "InterruptedException: " + ex);
        }
        return bsession;
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
            Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "PDU request expired: {}", pduRequest);
        }

        @Override
        public PduResponse firePduRequestReceived(PduRequest pduRequest) {
            PduResponse response = pduRequest.createResponse();
            // do any logic here
            Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "SMS Received: {}", pduRequest);

            return response;
        }
    }

    public void sendSMS(String intime, String dest, String message, String from) {
        if (trxsession2 == null) {
            Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "BULK Session IS null");
        }
        if (trxsession2 != null) {
            //Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "Session not null");
            String snd_sender = dest;
            String snd_txt = message;

            String msgid = "NONE";
            String sendresults = "NONE";
            long submitstartTime = System.nanoTime();
            counter = counter + 1;
            try {
                Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "Sending From:" + snd_sender + " To:" + from + " Message:" + snd_txt);
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
                SubmitSmResp submitResp = trxsession2.submit(submit0, 5000);
                Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);
                msgid = submitResp.getMessageId();
                sendresults = submitResp.getResultMessage();
            } catch (Exception myex) {
                Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "SERVER TOO SO LONG: " + myex);
            }
            long submitendTime = System.nanoTime();
            long submitduration = (submitendTime - submitstartTime);
            System.out.println("bulkSUBMIT REQ RES TOOK:" + submitduration + " nanoseconds");
            System.out.println("bulkSUBMIT REQ RES TOOK:" + submitduration / 1000000 + " milliseconds");
            System.out.println("bulkSUBMIT REQ RES TOOK:" + submitduration / 1000000 / 1000 + " seconds");
            DateTime ndt = new DateTime();
            DateTimeFormatter nfmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            String nntime = nfmt.print(ndt);
            System.out.println("Current Time:" + nntime);
            Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "sendWindow.size: {}", trxsession2.getSendWindow().getSize());
            Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "Window data. max size : " + trxsession2.getSendWindow().getMaxSize() + " size : " + trxsession2.getSendWindow().getSize() + " free : " + trxsession2.getSendWindow().getFreeSize() + " pending : " + trxsession2.getSendWindow().getPendingOfferCount());
            if (msgid == null) {
                msgid = "NONE";
            }
            final String mid = msgid;
            final String sr = sendresults;
            long savestartTime = System.nanoTime();
            Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "BULK SAVING:" + intime + "||" + from + "||" + snd_sender + "||" + message + "||" + snd_txt + "||" + msgid + "||" + sendresults);
            Runnable runnable = () -> {
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
            };
            Thread t = new Thread(runnable);
            t.start();
            long saveendTime = System.nanoTime();
            long saveduration = (saveendTime - savestartTime);
            System.out.println("SAVE TOOK:" + saveduration + " nanoseconds");
            System.out.println("SAVE TOOK:" + saveduration / 1000000 + " milliseconds");
            System.out.println("SAVE TOOK:" + saveduration / 1000000 / 1000 + " seconds");
            System.out.println("BULK COUNTER: " + counter);
        } else {
            Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "bulk Session is NULL!!!");
        }
    }
    int dis = 0;

    class SayHello3 extends TimerTask {

        public void run() {
            dis = dis + 10;
            if (trxsession2 == null) {
                Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "BULK Session is null");
                Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "Initializing BULK Transciever");
                trxsession2 = bulkSession();
            } else {
                try {
                    // demo of an "asynchronous" enquireLink call - send it, get a future,
                    // and then optionally choose to pick when we wait for it
                    WindowFuture<Integer, PduRequest, PduResponse> future03 = trxsession2.sendRequestPdu(new EnquireLink(), 10000, true);
                    if (!future03.await()) {
                        Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "Failed to  receive BULK enquire_link_resp within specified time");
                    } else if (future03.isSuccess()) {
                        EnquireLinkResp enquireLinkResp3 = (EnquireLinkResp) future03.getResponse();
                        Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "BULK enquire_link_resp #2: commandStatus [" + enquireLinkResp3.getCommandStatus() + "=" + enquireLinkResp3.getResultMessage() + "]");
                    } else {
                        Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "Failed to properly receive BULK enquire_link_resp: " + future03.getCause());
                    }
                } catch (RecoverablePduException ex) {
                    Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "RecoverablePduException" + ex);
                    trxsession2 = null;
                } catch (UnrecoverablePduException ex) {
                    Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "UnrecoverablePduException" + ex);
                    trxsession2 = null;
                } catch (SmppTimeoutException ex) {
                    Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "SmppTimeoutException" + ex);
                    trxsession2 = null;
                } catch (SmppChannelException ex) {
                    Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "SmppChannelException" + ex);
                    trxsession2 = null;
                } catch (InterruptedException ex) {
                    Logger.getLogger(Knecsafbulk.class.getName()).log(Level.SEVERE, "InterruptedException" + ex);
                    trxsession2 = null;
                }
                Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "BULK DISCON COUNT" + dis);
                if (dis > 80) {
                    long startTime = System.nanoTime();
                    Logger.getLogger(Knecsafbulk.class.getName()).log(Level.INFO, "Check for BULK Disconnection");
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    if (trxsession2 != null) {
                        if (disman().equals("yes")) {
                            trxsession2.unbind(3000);
                            trxsession2.destroy();
                            trxsession2 = null;
                        }
                        dis = 0;
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime);
                        System.out.println("BULK DISCONCHECK TOOK:" + duration + " nanoseconds");
                        System.out.println("BULK DISCONCHECK TOOK:" + duration / 1000000 + " milliseconds");
                        System.out.println("BULK DISCONCHECK TOOK:" + duration / 1000000 / 1000 + " seconds");
                    }
                }
            }
        }
    }

    private static String disman() {
        String res = "no";
        String fileName = "/opt/applications/smppclients/discon.txt";
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int index = 1;
            while ((line = bufferedReader.readLine()) != null) {
                if (index == 3) {
                    System.out.println(line);
                    res = line;
                }
                index++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        return res;
    }
}
