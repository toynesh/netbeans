/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knecsafdlry;

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
public class Knecsafdlry {

    /**
     * @param args the command line arguments
     */
    static DataStore data = new DataStore();
    static Knecsafdlry2 dlry2 = new Knecsafdlry2();
    static List<String> tosend = new ArrayList<>();
    //static String host = "192.168.9.96";
    static int port = 7662;//live
    static String host = "192.168.9.21";//live
    //static String host = "196.201.213.88"; //vpn
    //static int port = 7661;
    static String systemId = "pdsldr";
    static String password = "PDSL!@#";
    static SmppSession session0 = null;
    static int counter;

    public static void main(String[] args) {
        // TODO code application logic here
        // GET SERVER IP and port The name of the file to open.
        String fileName = "/opt/applications/safaricom/safdlry.txt";
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
            config0.setType(SmppBindType.RECEIVER);
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
        } catch (SmppTimeoutException ex) {
            Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "SmppTimeoutException" + ex);
            String command = "/opt/applications/safaricom/safaricomdlry/safaricomdlry.sh >> /opt/applications/safaricom/safaricomdlry/nohup.out 2>&1";
            System.out.println("Command:=>> " + command);
            data.terminalCMD(command);
        } catch (SmppChannelException ex) {
            Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "SmppChannelException Lost Connection" + ex);
            String command = "/opt/applications/safaricom/safaricomdlry/safaricomdlry.sh >> /opt/applications/safaricom/safaricomdlry/nohup.out 2>&1";
            System.out.println("Command:=>> " + command);
            data.terminalCMD(command);
        } catch (UnrecoverablePduException ex) {
            Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "UnrecoverablePduException" + ex);
            String command = "/opt/applications/safaricom/safaricomdlry/safaricomdlry.sh >> /opt/applications/safaricom/safaricomdlry/nohup.out 2>&1";
            System.out.println("Command:=>> " + command);
            data.terminalCMD(command);
        } catch (InterruptedException ex) {
            Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "InterruptedException" + ex);
            String command = "/opt/applications/safaricom/safaricomdlry/safaricomdlry.sh >> /opt/applications/safaricom/safaricomdlry/nohup.out 2>&1";
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
            Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "PDU request expired: {}", pduRequest);
        }

        @Override
        public PduResponse firePduRequestReceived(PduRequest pduRequest) {
            PduResponse response = pduRequest.createResponse();
            // do any logic here
            Runnable runnable = () -> {
                Logger.getLogger(Knecsafdlry.class.getName()).log(Level.INFO, "SMS Received: {}", pduRequest);
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
                    Logger.getLogger(Knecsafdlry.class.getName()).log(Level.INFO, "Dlr1: From:" + from + " To:" + dest + " Msg:" + message + " Time:" + intime);
                    String insert = "INSERT INTO `knecsms`.`dlry` (`smsc`, `osmsc`, `sender`, `reciever`, `message`) VALUES ('SAFARICOM', '" + serviceT + "', '" + from + "', '" + dest + "', '" + message + "')";
                    data.insert(insert);

                }
            };
            Thread t = new Thread(runnable);
            t.start();
            return response;
        }
    }

    static class SayHello extends TimerTask {

        public void run() {
            try {
                // demo of an "asynchronous" enquireLink call - send it, get a future,
                // and then optionally choose to pick when we wait for it
                WindowFuture<Integer, PduRequest, PduResponse> future0 = session0.sendRequestPdu(new EnquireLink(), 10000, true);
                if (!future0.await()) {
                    Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "Failed to receive DLRY enquire_link_resp within specified time");
                } else if (future0.isSuccess()) {
                    EnquireLinkResp enquireLinkResp2 = (EnquireLinkResp) future0.getResponse();
                    Logger.getLogger(Knecsafdlry.class.getName()).log(Level.INFO, "DLRY enquire_link_resp #2: commandStatus [" + enquireLinkResp2.getCommandStatus() + "=" + enquireLinkResp2.getResultMessage() + "]");
                } else {
                    Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "Failed to properly receive DLRY enquire_link_resp: " + future0.getCause());
                }
            } catch (RecoverablePduException ex) {
                Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "RecoverablePduException" + ex);
                String command = "/opt/applications/safaricom/safaricomdlry/safaricomdlry.sh >> /opt/applications/safaricom/safaricomdlry/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (UnrecoverablePduException ex) {
                Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "UnrecoverablePduException" + ex);
                String command = "/opt/applications/safaricom/safaricomdlry/safaricomdlry.sh >> /opt/applications/safaricom/safaricomdlry/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (SmppTimeoutException ex) {
                Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "SmppTimeoutException" + ex);
                String command = "/opt/applications/safaricom/safaricomdlry/safaricomdlry.sh >> /opt/applications/safaricom/safaricomdlry/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (SmppChannelException ex) {
                Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "SmppChannelException" + ex);
                String command = "/opt/applications/safaricom/safaricomdlry/safaricomdlry.sh >> /opt/applications/safaricom/safaricomdlry/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            } catch (InterruptedException ex) {
                Logger.getLogger(Knecsafdlry.class.getName()).log(Level.SEVERE, "InterruptedException" + ex);
                String command = "/opt/applications/safaricom/safaricomdlry/safaricomdlry.sh >> /opt/applications/safaricom/safaricomdlry/nohup.out 2>&1";
                System.out.println("Command:=>> " + command);
                data.terminalCMD(command);
            }
        }
    }

}
