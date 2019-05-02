/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smppclient;

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

/**
 *
 * @author coolie
 */
public class Smppclient {

    /**
     * @param args the command line arguments
     */
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
    static DefaultSmppSessionHandler sessionHandler = new Smppclient.ClientSmppSessionHandler();
    static Timer timer = new Timer();

    private static SmppSession clientSession() {
        SmppSession csession = null;
        try {
            SmppSessionConfiguration config0 = new SmppSessionConfiguration();
            config0.setWindowSize(1);
            config0.setName("Tester.Session.0");
            config0.setType(SmppBindType.TRANSCEIVER);
            config0.setHost("172.27.116.22");
            config0.setPort(8070);
            config0.setConnectTimeout(10000);
            config0.setSystemId("pavel");
            config0.setPassword("dfsew");
            config0.getLoggingOptions().setLogBytes(true);
            // to enable monitoring (request expiration)
            config0.setRequestExpiryTimeout(30000);
            config0.setWindowMonitorInterval(15000);
            config0.setCountersEnabled(true);
            csession = clientBootstrap.bind(config0, sessionHandler);
        } catch (SmppTimeoutException ex) {
            Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, "SmppTimeoutException: " + ex);
        } catch (SmppChannelException ex) {
            Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, "SmppChannelException: " + ex);
        } catch (UnrecoverablePduException ex) {
            Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, "UnrecoverablePduException: " + ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, "InterruptedException: " + ex);
        }
        return csession;
    }

    static int counter;
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
            Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, "PDU request expired: {}", pduRequest);
        }

        @Override
        public PduResponse firePduRequestReceived(PduRequest pduRequest) {
            PduResponse response = pduRequest.createResponse();

            // do any logic here
            Logger.getLogger(Smppclient.class.getName()).log(Level.INFO, "SMS Received: {}", pduRequest);
            if (pduRequest.getCommandId() == SmppConstants.CMD_ID_DELIVER_SM) {
                DeliverSm mo = (DeliverSm) pduRequest;
                int length = mo.getShortMessageLength();
                String from = mo.getSourceAddress().getAddress();
                String dest = mo.getDestAddress().getAddress();
                String serviceT = mo.getServiceType();
                byte[] shortMessage = mo.getShortMessage();
                String message = new String(shortMessage);
                //processSms(dest_address.getAddress(), mo.getShortMessage().toString());
                Logger.getLogger(Smppclient.class.getName()).log(Level.INFO, "Inbox: From:" + from + " To:" + dest + " Msg:" + message+ " MsgId:" + mo.getDefaultMsgId());
                counter = counter+1;
                System.out.println("Counter: "+counter);
            }

            return response;
        }

    }

    public static void main(String[] args) {
        // TODO code application logic here 
        List<SmppSession> ses = new ArrayList<SmppSession>();
        for (int i = 1; i <= 100; i++) {
            ses.add(clientSession());
        }
        System.out.println("Sessions size " + ses.size());
        for (int x = 0; x <= ses.size()-1; x++) {
            timer.schedule(new SayHello(ses.get(x)), 0, 100);
        }
    }

    static class SayHello extends TimerTask {
        SmppSession session0 = null;
        SayHello(SmppSession ses) {
            session0 = ses;
        }

        public void run() {
            if (session0 == null) {
                Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, session0 + " Client Session is null");
                Logger.getLogger(Smppclient.class.getName()).log(Level.INFO, session0 + " Initializing Client");
                session0 = clientSession();
            } else {
                try {
                    // demo of an "asynchronous" enquireLink call - send it, get a future,
                    // and then optionally choose to pick when we wait for it
                    WindowFuture<Integer, PduRequest, PduResponse> future0 = session0.sendRequestPdu(new EnquireLink(), 10000, true);
                    if (!future0.await()) {
                        Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, session0 + " Failed to receive MO enquire_link_resp within specified time");
                    } else if (future0.isSuccess()) {
                        EnquireLinkResp enquireLinkResp2 = (EnquireLinkResp) future0.getResponse();
                        Logger.getLogger(Smppclient.class.getName()).log(Level.INFO, session0 + " MO enquire_link_resp #2: commandStatus [" + enquireLinkResp2.getCommandStatus() + "=" + enquireLinkResp2.getResultMessage() + "]");
                    } else {
                        Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, session0 + " Failed to properly receive MO enquire_link_resp: " + future0.getCause());
                    }
                } catch (RecoverablePduException ex) {
                    Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, session0 + " RecoverablePduException" + ex);
                    session0 = null;
                } catch (UnrecoverablePduException ex) {
                    Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, session0 + " UnrecoverablePduException" + ex);
                    session0 = null;
                } catch (SmppTimeoutException ex) {
                    Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, session0 + " SmppTimeoutException" + ex);
                    session0 = null;
                } catch (SmppChannelException ex) {
                    Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, session0 + " SmppChannelException" + ex);
                    session0 = null;
                } catch (InterruptedException ex) {
                    Logger.getLogger(Smppclient.class.getName()).log(Level.SEVERE, session0 + " InterruptedException" + ex);
                    session0 = null;
                }
            }
        }
    }
}
