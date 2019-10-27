/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslkenya.airtelrx.smpp;

/**
 *
 * @author tesla
 */
import com.pdslkenya.airtelrx.Airtelrx;
import com.pdslkenya.airtelrx.entity.Snd;
import com.pdslkenya.airtelrx.jpa.Bean;
import com.pdslkenya.airtelrx.jpa.util.Props;
import ie.omk.smpp.Address;
import ie.omk.smpp.AlreadyBoundException;
import ie.omk.smpp.Connection;
import ie.omk.smpp.SMPPException;
import ie.omk.smpp.event.ConnectionObserver;
import ie.omk.smpp.event.ReceiverExitEvent;
import ie.omk.smpp.event.SMPPEvent;
import ie.omk.smpp.message.BindTransmitter;
import ie.omk.smpp.message.BindTransmitterResp;
import ie.omk.smpp.message.InvalidParameterValueException;
import ie.omk.smpp.message.QuerySM;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.message.SMPPProtocolException;
import ie.omk.smpp.message.SubmitSM;
import ie.omk.smpp.message.SubmitSMResp;
import ie.omk.smpp.message.tlv.Tag;
import ie.omk.smpp.util.GSMConstants;
import ie.omk.smpp.version.VersionException;
import java.io.IOException;
import org.apache.log4j.Logger;

public class Bind implements ConnectionObserver {

    final Props props = Airtelrx.props;
    protected Connection connection = null;
    private final Object blocker = new Object();
    private final Logger logger = Logger.getLogger(Bind.class.getName());
    private Snd snd = null;
    private Bean bean;

    public boolean canSendSMS = false, shouldWait = true;

    public Bind(Snd snd, Bean bean) {
        //this.props = props;
        this.snd = snd;
        this.bean = bean;
    }

    @Override
    public void packetReceived(Connection source, SMPPPacket packet) {
        System.out.println("Packet received: Id = " + Integer.toHexString(packet.getCommandId()));
        switch (packet.getCommandId()) {
            // Bind transmitter response. Check it's status for success...
            case SMPPPacket.BIND_TRANSMITTER:
                logger.info("Received Bind Transmitter PDU");
                if (packet.getCommandStatus() != 0) {
                    logger.warn("Error binding to the SMSC. Error = " + packet.getCommandStatus());
                    shouldWait = false;
                } else {
                    logger.info("Successully bound to SMSC \"" + ((BindTransmitterResp) packet).getSystemId() + "\".\n\tSubmitting Message...");
                    canSendSMS = true;
                    shouldWait = false;
                    //send();
                }
                break;
            case SMPPPacket.SUBMIT_SM_RESP:
                logger.info("Received SubmitSM resp");
                if (packet.getCommandStatus() != 0) {

                    logger.warn("Message was not submitted to: " + snd.getSndTo() + ". Error code: " + packet.getCommandStatus());
                    /*
                     Update message status rejected + message id 
                     */
                    snd.setStatus(3);
                    bean.updateObject(snd);
                } else {
                    /*
                     // update the message_id in DB + status
                     //
                     */
                    snd.setStatus(1);
                    bean.updateObject(snd);
                    logger.info("Message Submitted to: " + snd.getSndTo() + " Id = " + ((SubmitSMResp) packet).getMessageId());
                }
                try {
                    // Unbind. The Connection's listener thread will stop itself...
                    logger.info("Unbinding SMPP connection...");
                    connection.unbind();
                } catch (IOException ex) {
                    logger.error("Error unbinding " + ex.toString(), ex);
                }
                break;
            case SMPPPacket.QUERY_SM_RESP:

                logger.info("Received QuerySM resp");

                if (packet.getCommandStatus() != 0) {

                    logger.warn("Error on querysm resp code: " + packet.getCommandStatus());

                } else {
                    /*
                     // update status in DB
                     //
                     */
                    if (packet.getMessageStatus() == 2) {
                        snd.setStatus(2);
                        bean.updateObject(snd);
                        logger.info("Message Submitted to: " + snd.getSndTo() + " Id = " + ((SubmitSMResp) packet).getMessageId());
                    }

                }

                try {
                    connection.unbind();
                } catch (IOException ex) {
                    logger.error("Error unbinding " + ex.toString(), ex);
                }
                break;
            case SMPPPacket.UNBIND:
                logger.info("Received unbind PDU... is SMSC shutting down?");
                break;
            case SMPPPacket.UNBIND_RESP:
                logger.info("Unbound successfully");
                synchronized (blocker) {
                    blocker.notify();
                }
                break;
            default:
                logger.info("Unknown response received! id = " + packet.getCommandId());
        }
    }

    @Override
    public void update(Connection source, SMPPEvent event) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        switch (event.getType()) {
            case SMPPEvent.RECEIVER_EXIT:
                receiverExit(source, (ReceiverExitEvent) event);
                logger.info("Receiver exit update received");
                break;
            case SMPPEvent.RECEIVER_EXCEPTION:
                logger.warn("Receiver exception update received");
                break;
            case SMPPEvent.RECEIVER_START:
                logger.info("Receiver start update received");
                break;
            default:
                break;
        }
    }

    private void receiverExit(Connection source, ReceiverExitEvent receiverExitEvent) {
        if (receiverExitEvent.getReason() != ReceiverExitEvent.EXCEPTION) {
            if (receiverExitEvent.getReason() == ReceiverExitEvent.BIND_TIMEOUT) {
                logger.warn("Bind timed out waiting for response.");
            }
            logger.info("Receiver thread has exited normally.");
        } else {
            Throwable t = receiverExitEvent.getException();
            logger.warn("Receiver thread died due to an exception");
            logger.error("Error: ", t);
        }

        synchronized (blocker) {
            blocker.notify();
        }
    }

    public void send() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try {
            Address destination = new Address(GSMConstants.GSM_TON_UNKNOWN, GSMConstants.GSM_NPI_UNKNOWN, snd.getSndTo());
            SubmitSM sm = (SubmitSM) connection.newInstance(SMPPPacket.SUBMIT_SM);
            sm.setDestination(destination);
            sm.setMessageText(snd.getSndTxt());

            connection.sendRequest(sm);
            // add object to memcached
        } catch (IOException ex) {
            logger.error("I/O Exception: " + ex.toString());
        } catch (SMPPException ex) {
            logger.error("Submit_sm SMPP Exception: " + ex.toString());
        }
    }

    public void query() {
        try {
            Address source = new Address(GSMConstants.GSM_TON_UNKNOWN, GSMConstants.GSM_NPI_UNKNOWN, snd.getSndSender());
            QuerySM qsm = (QuerySM) connection.newInstance(SMPPPacket.QUERY_SM);
            qsm.setMessageId(snd.getSndmessageId());
            qsm.setSource(source);
            connection.sendRequest(qsm);
            // add object to memcashed
        } catch (IOException ex) {
            logger.error("I/O Exception: " + ex.toString());
        } catch (SMPPException ex) {
            logger.error("Query_sm SMPP Exception: " + ex.toString());
        }
    }

    public void execute() throws AlreadyBoundException, InvalidParameterValueException, SMPPProtocolException, VersionException, IOException, IllegalArgumentException, InterruptedException, Exception {
        connection = new Connection(props.getSmscHost(), Integer.parseInt(props.getSmscPort()), true);
        connection.addObserver(this);
        connection.autoAckLink(true);
        connection.autoAckMessages(true);
        logger.info("Binding to SMSC as a transmitter...");
        connection.bind(Connection.TRANSMITTER, props.getSmscSysId(), props.getSmscPasswd(), props.getSmscType(), Integer.parseInt(props.getsTone()), Integer.parseInt(props.getsNpi()), props.getSmscSC());
        synchronized (blocker) {
            blocker.wait();
        }
    }
}
