/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equitelrxtest;

import com.cloudhopper.commons.charset.CharsetUtil;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
public class Equitelrxtest {

    /**
     * @param args the command line arguments
     */
    static public void main(String[] args) throws Exception {
        //
        // setup 3 things required for any session we plan on creating
        //

        // for monitoring thread use, it's preferable to create your own instance
        // of an executor with Executors.newCachedThreadPool() and cast it to ThreadPoolExecutor
        // this permits exposing thinks like executor.getActiveCount() via JMX possible
        // no point renaming the threads in a factory since underlying Netty 
        // framework does not easily allow you to customize your thread names
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        // to enable automatic expiration of requests, a second scheduled executor
        // is required which is what a monitor task will be executed with - this
        // is probably a thread pool that can be shared with between all client bootstraps
        ScheduledThreadPoolExecutor monitorExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, new ThreadFactory() {
            private AtomicInteger sequence = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("SmppClientSessionWindowMonitorPool-" + sequence.getAndIncrement());
                return t;
            }
        });

        // a single instance of a client bootstrap can technically be shared
        // between any sessions that are created (a session can go to any different
        // number of SMSCs) - each session created under
        // a client bootstrap will use the executor and monitorExecutor set
        // in its constructor - just be *very* careful with the "expectedSessions"
        // value to make sure it matches the actual number of total concurrent
        // open sessions you plan on handling - the underlying netty library
        // used for NIO sockets essentially uses this value as the max number of
        // threads it will ever use, despite the "max pool size", etc. set on
        // the executor passed in here
        DefaultSmppClient clientBootstrap = new DefaultSmppClient(Executors.newCachedThreadPool(), 1, monitorExecutor);

        //
        // setup configuration for a client session
        //
        DefaultSmppSessionHandler sessionHandler = new ClientSmppSessionHandler();

        SmppSessionConfiguration config0 = new SmppSessionConfiguration();
        config0.setWindowSize(1);
        config0.setName("Tester.Session.0");
        config0.setType(SmppBindType.TRANSCEIVER);
        config0.setHost("196.216.242.149");
        config0.setPort(3012);
        config0.setConnectTimeout(10000);
        config0.setSystemId("pdslkena");
        config0.setPassword("pdslk@n@");
        config0.getLoggingOptions().setLogBytes(true);
        // to enable monitoring (request expiration)
        config0.setRequestExpiryTimeout(30000);
        config0.setWindowMonitorInterval(15000);
        config0.setCountersEnabled(true);

        //
        // create session, enquire link, submit an sms, close session
        //
        //SmppSession session0 = null;
        final SmppSession session0 = clientBootstrap.bind(config0, sessionHandler);
        try {
            // create session a session by having the bootstrap connect a
            // socket, send the bind request, and wait for a bind response

            /*System.out.println("Press any key to send enquireLink #1");
            System.in.read(); disabled*/
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        // demo of a "synchronous" enquireLink call - send it and wait for a response
                        //Logger.getLogger(Equitelrxtest.class.getName()).log(Level.SEVERE,
                        EnquireLinkResp enquireLinkResp1 = session0.enquireLink(new EnquireLink(), 10000);
                        Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "enquire_link_resp #1: commandStatus [" + enquireLinkResp1.getCommandStatus() + "=" + enquireLinkResp1.getResultMessage() + "]");
                        if (session0 == null) {
                            throw new RuntimeException();
                        }
                        /*System.out.println("Press any key to send enquireLink #2");
                        System.in.read();*/
                        // demo of an "asynchronous" enquireLink call - send it, get a future,
                        // and then optionally choose to pick when we wait for it
                        /*WindowFuture<Integer, PduRequest, PduResponse> future0 = session0.sendRequestPdu(new EnquireLink(), 10000, true);
                        if (!future0.await()) {
                            Logger.getLogger(Equitelrxtest.class.getName()).log(Level.SEVERE,"Failed to receive enquire_link_resp within specified time");
                        } else if (future0.isSuccess()) {
                            EnquireLinkResp enquireLinkResp2 = (EnquireLinkResp) future0.getResponse();
                            Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO,"enquire_link_resp #2: commandStatus [" + enquireLinkResp2.getCommandStatus() + "=" + enquireLinkResp2.getResultMessage() + "]");
                        } else {
                            Logger.getLogger(Equitelrxtest.class.getName()).log(Level.SEVERE,"Failed to properly receive enquire_link_resp: " + future0.getCause());
                        }*/
                    } catch (RecoverablePduException ex) {
                        java.util.logging.Logger.getLogger(Equitelrxtest.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnrecoverablePduException ex) {
                        java.util.logging.Logger.getLogger(Equitelrxtest.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SmppTimeoutException ex) {
                        java.util.logging.Logger.getLogger(Equitelrxtest.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SmppChannelException ex) {
                        java.util.logging.Logger.getLogger(Equitelrxtest.class.getName()).log(Level.SEVERE, null, ex);
                        System.exit(0);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(Equitelrxtest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }, 0, 10000);

            /*System.out.println("Press any key to send submit #1");
            System.in.read();*/
            DataStore data = new DataStore();
            Connection con = null;
            for (;;) {
                try {
                    con = data.connect();
                    String query = "SELECT snd_id, snd_sender, snd_to,snd_txt FROM snd where  snd_smsc='EQUITELRX' and status=0 order by snd_id desc";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        int snd_id = rs.getInt(1);
                        String snd_sender = rs.getString(2);
                        String snd_to = rs.getString(3);
                        String snd_txt = rs.getString(4);
                        String text160 = snd_txt;
                        Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "From:" + snd_sender + " To:" + snd_to + " Message:" + text160);
                        //String text160 = "\u20AC Lorem [ipsum] dolor sit amet, consectetur adipiscing elit. Proin feugiat, leo id commodo tincidunt, nibh diam ornare est, vitae accumsan risus lacus sed sem metus.";
                        byte[] textBytes = CharsetUtil.encode(text160, CharsetUtil.CHARSET_GSM);

                        SubmitSm submit0 = new SubmitSm();
                        // add delivery receipt
                        //submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
                        submit0.setSourceAddress(new Address((byte) 0x03, (byte) 0x00, snd_sender));
                        submit0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, snd_to));
                        if (textBytes != null && textBytes.length > 255) {
                            submit0.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
                        } else {
                            submit0.setShortMessage(textBytes);
                        }
                        SubmitSmResp submitResp = session0.submit(submit0, 10000);
                        Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "sendWindow.size: {}", session0.getSendWindow().getSize());
                        Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "SubmitSmResp: " + submitResp);
                        PreparedStatement prep = null;
                        String update = "update snd set status = ? where snd_id = ? ";
                        prep = con.prepareStatement(update);
                        prep.setString(1, "1");
                        prep.setInt(2, snd_id);
                        prep.executeUpdate();
                        prep.close();
                    }
                    con.close();
                } catch (Exception e) {
                }
            }
            //System.out.println("Press any key to unbind and close sessions");
            //System.in.read();

            //session0.unbind(5000);
        } catch (Exception e) {
            Logger.getLogger(Equitelrxtest.class.getName()).log(Level.SEVERE, "", e);
        }

        if (session0 != null) {
            Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "Cleaning up session... (final counters)");
            if (session0.hasCounters()) {
                Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "tx-enquireLink: {}", session0.getCounters().getTxEnquireLink());
                Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "tx-submitSM: {}", session0.getCounters().getTxSubmitSM());
                Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "tx-deliverSM: {}", session0.getCounters().getTxDeliverSM());
                Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "tx-dataSM: {}", session0.getCounters().getTxDataSM());
                Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "rx-enquireLink: {}", session0.getCounters().getRxEnquireLink());
                Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "rx-submitSM: {}", session0.getCounters().getRxSubmitSM());
                Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "rx-deliverSM: {}", session0.getCounters().getRxDeliverSM());
                Logger.getLogger(Equitelrxtest.class.getName()).log(Level.INFO, "rx-dataSM: {}", session0.getCounters().getRxDataSM());
            }

            session0.destroy();
            // alternatively, could call close(), get outstanding requests from
            // the sendWindow (if we wanted to retry them later), then call shutdown()
        }

        // this is required to not causing server to hang from non-daemon threads
        // this also makes sure all open Channels are closed to I *think*
        Logger.getLogger(Equitelrxtest.class
                .getName()
        ).log(Level.INFO, "Shutting down client bootstrap and executors...");
        clientBootstrap.destroy();

        executor.shutdownNow();

        monitorExecutor.shutdownNow();

        Logger.getLogger(Equitelrxtest.class
                .getName()
        ).log(Level.INFO, "Done. Exiting");
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
            Logger.getLogger(Equitelrxtest.class.getName()).log(Level.SEVERE, "PDU request expired: {}", pduRequest);
        }

        @Override
        public PduResponse firePduRequestReceived(PduRequest pduRequest) {
            PduResponse response = pduRequest.createResponse();

            // do any logic here
            return response;
        }

    }
}