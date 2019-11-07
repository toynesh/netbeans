/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knecairtel;

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
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.List;
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
    static Transmitter trx = new Transmitter();
    static DataStore data = new DataStore();
    static MongoDatabase database = data.mongoconnect();
    static String host = "196.216.242.149";
    static int port = 3011;
    static String systemId = "Pdslkenya";
    static String password = "Pdsl2019";
    static List<String> tosend = new ArrayList<>();
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
    static DefaultSmppSessionHandler sessionHandler = new Knecairtel.ClientSmppSessionHandler();
    static SmppSession session0 = null;
    static Timer timer = new Timer();
    static int counter;

    public static SmppSession moSession() {
        // GET SERVER IP and port The name of the file to open.
        String fileName = "/opt/applications/smppclients/airmo.txt";
        //String fileName = "/home/julius/airmo.txt";
        String line = null;

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int index = 1;
            while ((line = bufferedReader.readLine()) != null) {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, line);
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
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "Error reading file '" + fileName + "'");
        }
        SmppSession msession = null;
        try {
            SmppSessionConfiguration config0 = new SmppSessionConfiguration();
            config0.setWindowSize(2000);
            config0.setName("pdsl.Session.0");
            config0.setType(SmppBindType.RECEIVER);
            config0.setSystemType("smpp");
            config0.setHost(host);
            config0.setPort(port);
            config0.setConnectTimeout(50000);
            config0.setSystemId(systemId);
            config0.setPassword(password);
            config0.getLoggingOptions().setLogBytes(true);
            config0.setRequestExpiryTimeout(30000);
            config0.setWindowMonitorInterval(15000);
            config0.setCountersEnabled(true);
            msession = clientBootstrap.bind(config0, sessionHandler);
        } catch (SmppTimeoutException ex) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "SmppTimeoutException: " + ex);
        } catch (SmppChannelException ex) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "SmppChannelException: " + ex);
        } catch (UnrecoverablePduException ex) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "UnrecoverablePduException: " + ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "InterruptedException: " + ex);
        }
        return msession;
    }

    static public void main(String[] args) {
        timer.schedule(new SayHello(), 0, 10000);
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
                        Runnable irunnable = () -> {
                            /*String insert = "INSERT INTO `knecsms`.`inbox` (`smsc`, `osmsc`, `sender`, `reciever`, `message`) VALUES ('AIRTEL', '" + serviceT + "', '" + from + "', '" + dest + "', '" + message + "')";
                        data.insert(insert);*/
                            MongoCollection<Document> collection = database.getCollection("inbox");
                            Document newInbox = new Document(
                                    "smsc", "AIRTEL")
                                    .append("osmsc", serviceT)
                                    .append("sender", from)
                                    .append("reciever", dest)
                                    .append("message", message);
                            collection.insertOne(newInbox);
                            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "newInbox Document inserted successfully");
                        };
                        Thread it = new Thread(irunnable);
                        it.start();

                        trx.sendSMS(intime, dest, message, from);
                    }else{
                        Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Dlry: From:" + from + " To:" + dest + " Msg:" + message + " Time:" + intime);
                        Runnable irunnable = () -> {
                            /*String insert = "INSERT INTO `knecsms`.`inbox` (`smsc`, `osmsc`, `sender`, `reciever`, `message`) VALUES ('AIRTEL', '" + serviceT + "', '" + from + "', '" + dest + "', '" + message + "')";
                        data.insert(insert);*/
                            MongoCollection<Document> collection = database.getCollection("dlry");
                            Document newInbox = new Document(
                                    "smsc", "AIRTEL")
                                    .append("osmsc", serviceT)
                                    .append("sender", from)
                                    .append("reciever", dest)
                                    .append("message", message);
                            collection.insertOne(newInbox);
                            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "newDlry Document inserted successfully");
                        };
                        Thread it = new Thread(irunnable);
                        it.start();
                    }
                }
            };
            Thread t = new Thread(runnable);
            t.start();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "MO COUNTER: " + counter);
            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "MO RES TOOK:" + duration + " nanoseconds");
            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "MO RES TOOK:" + duration / 1000000 + " milliseconds");
            Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "MO RES TOOK:" + duration / 1000000 / 1000 + " seconds");
            return response;
        }
    }
    static int dis = 0;

    static class SayHello extends TimerTask {

        public void run() {
            dis = dis + 10;
            if (session0 == null) {
                Logger.getLogger(Knecairtel.class.getName()).log(Level.WARNING, "MO Session is null");
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Initializing Reciever");
                session0 = moSession();
            } else {
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
                    session0 = null;
                } catch (UnrecoverablePduException ex) {
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "UnrecoverablePduException" + ex);
                    session0 = null;
                } catch (SmppTimeoutException ex) {
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "SmppTimeoutException" + ex);
                    session0 = null;
                } catch (SmppChannelException ex) {
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "SmppChannelException" + ex);
                    session0 = null;
                } catch (InterruptedException ex) {
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "InterruptedException" + ex);
                    session0 = null;
                }
                Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "MO DISCON COUNT" + dis);
                if (dis > 60) {
                    long startTime = System.nanoTime();
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "Check for MO Disconnection");
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    if (session0 != null) {
                        if (disman().equals("yes")) {
                            session0.unbind(3000);
                            session0.destroy();
                            session0 = null;
                        }
                    }
                    dis = 0;
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime);
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "MO DISCONCHECK TOOK:" + duration + " nanoseconds");
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "MO DISCONCHECK TOOK:" + duration / 1000000 + " milliseconds");
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, "MO DISCONCHECK TOOK:" + duration / 1000000 / 1000 + " seconds");
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
                if (index == 1) {
                    Logger.getLogger(Knecairtel.class.getName()).log(Level.INFO, line);
                    res = line;
                }
                index++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            Logger.getLogger(Knecairtel.class.getName()).log(Level.SEVERE, "Error reading file '" + fileName + "'");
        }
        return res;
    }

}
