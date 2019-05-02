/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knecsaftest4;

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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class Knecsaftest4 {

    /**
     * @param args the command line arguments
     */
    static Transmitter trx = new Transmitter();
    static DataStore data = new DataStore();
    static List<String> tosend = new ArrayList<>();
    static int port = 7661;//live
    static String host = "192.168.9.96";//live
    static String systemId = "pdslmo";
    static String password = "pdsl#$*";
    /*static int port = 8070;
    static String host = "172.27.116.22";
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
    static DefaultSmppSessionHandler sessionHandler = new Knecsaftest4.ClientSmppSessionHandler();
    static SmppSession session0 = null;
    static Timer timer = new Timer();
    static int counter;

    public static SmppSession moSession() {
        // GET SERVER IP and port The name of the file to open.
        String fileName = "/opt/applications/smpptests/safmo.txt";
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
            Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "SmppTimeoutException: " + ex);
        } catch (SmppChannelException ex) {
            Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "SmppChannelException: " + ex);
        } catch (UnrecoverablePduException ex) {
            Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "UnrecoverablePduException: " + ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "InterruptedException: " + ex);
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
            Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "PDU request expired: {}", pduRequest);
        }

        @Override
        public PduResponse firePduRequestReceived(PduRequest pduRequest) {
            PduResponse response = pduRequest.createResponse();
            // do any logic here
            long startTime = System.nanoTime();
            counter = counter + 1;
            Runnable runnable = () -> {
                Logger.getLogger(Knecsaftest4.class.getName()).log(Level.INFO, "SMS Received: {}", pduRequest);
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
                    Logger.getLogger(Knecsaftest4.class.getName()).log(Level.INFO, "Inbox: From:" + from + " To:" + dest + " Msg:" + message + " Time:" + intime);
                    Runnable irunnable = () -> {
                        String insert = "INSERT INTO `knecsmstest`.`inbox` (`smsc`, `osmsc`, `sender`, `reciever`, `message`) VALUES ('SAFARICOM', '" + serviceT + "', '" + from + "', '" + dest + "', '" + message + "')";
                        data.insert(insert);
                    };
                    Thread it = new Thread(irunnable);
                    it.start();

                    trx.sendSMS(intime, dest, message, from);
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
    static int dis = 0;

    static class SayHello extends TimerTask {

        public void run() {
            dis = dis + 10;
            if (session0 == null) {
                Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "MO Session is null");
                Logger.getLogger(Knecsaftest4.class.getName()).log(Level.INFO, "Initializing Reciever");
                session0 = moSession();
            } else {
                try {
                    // demo of an "asynchronous" enquireLink call - send it, get a future,
                    // and then optionally choose to pick when we wait for it
                    WindowFuture<Integer, PduRequest, PduResponse> future0 = session0.sendRequestPdu(new EnquireLink(), 10000, true);
                    if (!future0.await()) {
                        Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "Failed to receive MO enquire_link_resp within specified time");
                    } else if (future0.isSuccess()) {
                        EnquireLinkResp enquireLinkResp2 = (EnquireLinkResp) future0.getResponse();
                        Logger.getLogger(Knecsaftest4.class.getName()).log(Level.INFO, "MO enquire_link_resp #2: commandStatus [" + enquireLinkResp2.getCommandStatus() + "=" + enquireLinkResp2.getResultMessage() + "]");
                    } else {
                        Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "Failed to properly receive MO enquire_link_resp: " + future0.getCause());
                    }
                } catch (RecoverablePduException ex) {
                    Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "RecoverablePduException" + ex);
                    session0 = null;
                } catch (UnrecoverablePduException ex) {
                    Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "UnrecoverablePduException" + ex);
                    session0 = null;
                } catch (SmppTimeoutException ex) {
                    Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "SmppTimeoutException" + ex);
                    session0 = null;
                } catch (SmppChannelException ex) {
                    Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "SmppChannelException" + ex);
                    session0 = null;
                } catch (InterruptedException ex) {
                    Logger.getLogger(Knecsaftest4.class.getName()).log(Level.SEVERE, "InterruptedException" + ex);
                    session0 = null;
                }
                Logger.getLogger(Knecsaftest4.class.getName()).log(Level.INFO, "MO DISCON COUNT" + dis);
                if (dis > 60) {
                    long startTime = System.nanoTime();
                    Logger.getLogger(Knecsaftest4.class.getName()).log(Level.INFO, "Check for MO Disconnection");
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
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
                    System.out.println("MO DISCONCHECK TOOK:" + duration + " nanoseconds");
                    System.out.println("MO DISCONCHECK TOOK:" + duration / 1000000 + " milliseconds");
                    System.out.println("MO DISCONCHECK TOOK:" + duration / 1000000 / 1000 + " seconds");
                }
            }
        }
    }

    private static String disman() {
        String res = "no";
        String fileName = "/opt/applications/smpptests/discon.txt";
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int index = 1;
            while ((line = bufferedReader.readLine()) != null) {
                if (index == 1) {
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
