package chclient;

/*
 * #%L
 * ch-smpp
 * %%
 * Copyright (C) 2009 - 2015 Cloudhopper by Twitter
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.commons.util.windowing.WindowFuture;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.type.Address;
import com.cloudhopper.smpp.pdu.EnquireLink;
import com.cloudhopper.smpp.pdu.EnquireLinkResp;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.tlv.Tlv;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author joelauer (twitter: @jjlauer or
 * <a href="http://twitter.com/jjlauer" target=window>http://twitter.com/jjlauer</a>)
 */
public class ClientMain {

    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);

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
        //config0.setType(SmppBindType.TRANSCEIVER);
        config0.setType(SmppBindType.TRANSMITTER);//saf
        String shortcode = "22225";

        // GET SERVER DETAILS The name of the file to open.
        String fileName = "smsc.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int index = 1;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (index == 1) {
                    String linesplit[] = line.split(">>");
                    config0.setHost(linesplit[1]);
                }
                if (index == 2) {
                    String linesplit[] = line.split(">>");
                    config0.setPort(Integer.parseInt(linesplit[1]));
                }
                if (index == 3) {
                    String linesplit[] = line.split(">>");
                    config0.setSystemId(linesplit[1]);
                }
                if (index == 4) {
                    String linesplit[] = line.split(">>");
                    config0.setPassword(linesplit[1]);
                }
                if (index == 5) {
                    String linesplit[] = line.split(">>");
                    shortcode = linesplit[1];
                }
                index++;
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        config0.setSystemType("SMPP");
        config0.getLoggingOptions().setLogBytes(true);
        // to enable monitoring (request expiration)
        config0.setRequestExpiryTimeout(30000);
        config0.setWindowMonitorInterval(15000);
        config0.setCountersEnabled(true);

        List<String> messagefile = new ArrayList<>();
        List<String> simfile = new ArrayList<>();
        //add messages to array
       /* String mfileName = "messages.txt";
        String mline = null;
        try {
            FileReader mfileReader = new FileReader(mfileName);
            BufferedReader mbufferedReader = new BufferedReader(mfileReader);
            while ((mline = mbufferedReader.readLine()) != null) {
                messagefile.add(mline);
            }
            mbufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + mfileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + mfileName + "'");
        }
        //add phones to array
        String pfileName = "simwhitelist.txt";
        String pline = null;
        try {
            FileReader pfileReader = new FileReader(pfileName);
            BufferedReader pbufferedReader = new BufferedReader(pfileReader);
            while ((pline = pbufferedReader.readLine()) != null) {
                simfile.add(pline);
            }
            pbufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + pfileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + pfileName + "'");
        }
        System.out.println("Messages Size: " + messagefile.size());
        System.out.println("Sims Size: " + simfile.size());*/
        //
        // create session, enquire link, submit an sms, close session
        //
        SmppSession session0 = null;

        //OPTIONS
        String msisdn = "";
        String MESSAGE = "1";
        String multip = "off";
        String message = "No options selected. Sending Default message.";
        byte[] mybyte = CharsetUtil.encode(message, CharsetUtil.CHARSET_GSM);

        try {
            // create session a session by having the bootstrap connect a
            // socket, send the bind request, and wait for a bind response
            session0 = clientBootstrap.bind(config0, sessionHandler);

            //System.out.println("Press any key to send enquireLink #1");
            //System.in.read();
            // demo of a "synchronous" enquireLink call - send it and wait for a response
            EnquireLinkResp enquireLinkResp1 = session0.enquireLink(new EnquireLink(), 10000);
            logger.info("enquire_link_resp #1: commandStatus [" + enquireLinkResp1.getCommandStatus() + "=" + enquireLinkResp1.getResultMessage() + "]");

            //System.out.println("Press any key to send enquireLink #2");
            //System.in.read();
            // demo of an "asynchronous" enquireLink call - send it, get a future,
            // and then optionally choose to pick when we wait for it
            WindowFuture<Integer, PduRequest, PduResponse> future0 = session0.sendRequestPdu(new EnquireLink(), 10000, true);
            if (!future0.await()) {
                logger.error("Failed to receive enquire_link_resp within specified time");
            } else if (future0.isSuccess()) {
                EnquireLinkResp enquireLinkResp2 = (EnquireLinkResp) future0.getResponse();
                logger.info("enquire_link_resp #2: commandStatus [" + enquireLinkResp2.getCommandStatus() + "=" + enquireLinkResp2.getResultMessage() + "]");
            } else {
                logger.error("Failed to properly receive enquire_link_resp: " + future0.getCause());
            }

            //Scanner in = new Scanner(System.in);
            //System.out.println("Enter PULL,PUSH or HTTP");
            //String opt = in.next();
            String opt = args[0];
            if (opt.equals("HTTP")) {
                MESSAGE = "http";
            } else if (opt.equals("PUSH")) {
                //System.out.println("Enter Option Like 11 sim+case");
                //String ucase = in.next();
                String ucase = args[1];
                String simm = ucase.substring(0, 1);
                System.out.println("Sim" + simm);
                String cs = ucase.substring(1);
                System.out.println("Case" + cs);
                MESSAGE = ucase.substring(1);
                if (!((Integer.parseInt(simm) - 1) > simfile.size())) {
                    if (Integer.parseInt(cs) < 5 && Integer.parseInt(cs) > 0) {
                        for (int w = 0; w < simfile.size(); w++) {
                            if (w == (Integer.parseInt(simm)) - 1) {
                                msisdn = simfile.get(w);
                                System.out.println("msisdn: " + msisdn);
                            }
                        }
                        for (int w = 0; w < messagefile.size(); w++) {
                            if (w == (Integer.parseInt(cs)) - 1) {
                                message = messagefile.get(w);
                                System.out.println("message: " + message);
                            }
                        }
                    } else if (Integer.parseInt(cs) == 5) {
                        multip = "on";
                        for (int w = 0; w < simfile.size(); w++) {
                            if (w == (Integer.parseInt(simm)) - 1) {
                                msisdn = simfile.get(w);
                            }
                        }
                        System.out.println("Sending Multi Activated");

                    } else {
                        System.out.println("You have entered an Invalid Option");
                    }
                } else {
                    System.out.println("Invalid Sim Selected");
                }
            } else if (opt.equals("PULL")) {
                try {
                    FileWriter writer = new FileWriter("pulldata.txt");
                    writer.write("none");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Waiting for the Pull Request. Press any key once Done Sending");
                System.in.read();
                String pulldata = "none";
                try {
                    FileReader reader = new FileReader("pulldata.txt");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    pulldata = bufferedReader.readLine();
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!pulldata.equals("none")) {
                    pulldata = pulldata.replaceAll("0x00 0x00 \\[", "");
                    pulldata = pulldata.replaceAll("\\]", "");
                    String pulldatasplt[] = pulldata.split("<<");
                    MESSAGE = pulldatasplt[2];
                    msisdn = pulldatasplt[0];
                    shortcode = pulldatasplt[1];
                    if (Integer.parseInt(MESSAGE) > 2 && Integer.parseInt(MESSAGE) <= 5) {
                        if (simfile.contains(msisdn)) {
                            if (Integer.parseInt(MESSAGE) == 5) {
                                multip = "on";
                            } else {
                                for (int w = 0; w < messagefile.size(); w++) {
                                    if (w == (Integer.parseInt(MESSAGE)) - 1) {
                                        message = messagefile.get(w);
                                        System.out.println("message: " + message);
                                    }
                                }
                            }
                        } else {
                            System.out.println("Sim " + msisdn + " not found in the whitelist");
                        }
                    } else if (Integer.parseInt(MESSAGE) > 5) {
                        System.out.println("Pull Reqest " + MESSAGE + " not found ");
                    } else {
                        for (int w = 0; w < messagefile.size(); w++) {
                            if (w == (Integer.parseInt(MESSAGE)) - 1) {
                                message = messagefile.get(w);
                                System.out.println("message: " + message);
                            }
                        }
                    }
                } else {
                    System.out.println("No Pull Request sent");
                }
            } else {
                System.out.println("You have entered an Invalid Option");
            }

            //System.out.println("Press any key to send submit #1");
            //System.in.read(); 
            SubmitSm sm = new SubmitSm();
            Address ad = new Address();
            ad.setNpi((byte) 0x01);
            ad.setTon((byte) 0x02);
            ad.setAddress(msisdn);
            //ad.setAddress("254773451491");
            sm.setDestAddress(ad);
            Address src = new Address();
            src.setNpi((byte) 0x01);
            src.setTon((byte) 0x02);
            src.setAddress(shortcode);
            //src.setAddress(source);
            sm.setSourceAddress(src);

            if (MESSAGE.equalsIgnoreCase("1")) {
                //NORMAL TEXT
                sm.setProtocolId((byte) 0x00);
                sm.setDataCoding((byte) 0x00);
                sm.setEsmClass((byte) 0x00);
                mybyte = fromHex(message);
                sm.setShortMessage(mybyte);
                System.out.println("SENDING NORMAL TEXT");

            } else if (MESSAGE.equalsIgnoreCase("2")) {
                // LONG TEXT
                sm.setProtocolId((byte) 0x00);
                sm.setDataCoding((byte) 0x00);
                sm.setEsmClass((byte) 0x00);
                ByteArrayOutputStream tempbuff = new ByteArrayOutputStream();
                String[] splittedMsg = splitByNumber(message, 160);
                int totalSegments = splittedMsg.length;
                String display = "";
                for (int s = 0; s < totalSegments; s++) {
                    display = display + splittedMsg[s];
                    if (s % 2 == 1) {
                        System.out.println("Segment " + s + " :" + display);
                        display = "";
                    } else if (s == totalSegments - 1) {
                        System.out.println("Segment " + s + " :" + display);
                        display = "";
                    }
                    tempbuff.write(fromHex(splittedMsg[s]));
                }
                mybyte = tempbuff.toByteArray();
                if (mybyte != null && mybyte.length > 255) {
                    sm.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, mybyte, "message_payload"));
                } else {
                    sm.setShortMessage(mybyte);
                }
                System.out.println("SENDING LONG TEXT");

            } else if (MESSAGE.equalsIgnoreCase("3")) {
                //NORMAL BINARY
                sm.setProtocolId((byte) 0x7F);
                sm.setDataCoding((byte) 0xF6);
                sm.setEsmClass((byte) 0x40);
                mybyte = fromHex(message);
                sm.setShortMessage(mybyte);
                System.out.println("SENDING NORMAL BINARY");
            } else if (MESSAGE.equalsIgnoreCase("4")) {
                sm.setProtocolId((byte) 0x7F);
                sm.setDataCoding((byte) 0xF6);
                sm.setEsmClass((byte) 0x40);
                ByteArrayOutputStream tempbuff = new ByteArrayOutputStream();
                String[] splittedMsg = splitByNumber(message, 140);
                int totalSegments = splittedMsg.length;
                String display = "";
                for (int s = 0; s < totalSegments; s++) {
                    display = display + splittedMsg[s];
                    if (s % 2 == 1) {
                        System.out.println("Segment " + s + " :" + display);
                        display = "";
                    } else if (s == totalSegments - 1) {
                        System.out.println("Segment " + s + " :" + display);
                        display = "";
                    }
                    tempbuff.write(fromHex(splittedMsg[s]));
                }
                mybyte = tempbuff.toByteArray();
                if (mybyte != null && mybyte.length > 255) {
                    sm.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, mybyte, "message_payload"));
                } else {
                    sm.setShortMessage(mybyte);
                }
                System.out.println("SENDING LONG BINARY");

            } else if (MESSAGE.equalsIgnoreCase("http")) {
                String sres = "";
                try {
                    com.gmalto.test.CurrentParams_Service service = new com.gmalto.test.CurrentParams_Service();
                    com.gmalto.test.CurrentParams port = service.getCurrentParamsPort();
                    // TODO initialize WS operation arguments here
                    java.lang.String getem = "";
                    // TODO process result here
                    java.lang.String result = port.params(getem);
                    System.out.println("Http server Result = " + result);
                    sres = result;
                } catch (Exception ex) {
                    // TODO handle custom exceptions here
                }
                String sresSplit[] = sres.split("<<");
                String phone = sresSplit[0];
                String type = sresSplit[1];
                String msg = sresSplit[2];

                ad.setAddress(phone);

                ByteArrayOutputStream tempbuff = new ByteArrayOutputStream();
                String[] splittedMsg;
                String printclass = "";
                if (type.equals("bin")) {
                    splittedMsg = splitByNumber(msg, 140);
                    sm.setProtocolId((byte) 0x7F);
                    sm.setDataCoding((byte) 0xF6);
                    //sm.setEsmClass((byte) 0x40);
                    sm.setEsmClass((byte) 0x00);
                    printclass = "Binary";
                } else {
                    splittedMsg = splitByNumber(msg, 160);
                    sm.setProtocolId((byte) 0x00);
                    sm.setDataCoding((byte) 0x00);
                    sm.setEsmClass((byte) 0x00);
                    printclass = "Text";
                }
                int totalSegments = splittedMsg.length;
                String display = "";
                for (int s = 0; s < totalSegments; s++) {
                    display = display + splittedMsg[s];
                    if (s % 2 == 1) {
                        System.out.println("Segment " + s + " :" + display);
                        display = "";
                    } else if (s == totalSegments - 1) {
                        System.out.println("Segment " + s + " :" + display);
                        display = "";
                    }
                    tempbuff.write(fromHex(splittedMsg[s]));
                }
                mybyte = tempbuff.toByteArray();
                if (mybyte != null && mybyte.length > 255) {
                    sm.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, mybyte, "message_payload"));
                } else {
                    sm.setShortMessage(mybyte);
                }
                if (totalSegments == 1) {
                    System.out.println("Sending NORMAL " + printclass);
                } else {
                    System.out.println("Sending LONG " + printclass);
                }

            }

            /*sm.setCommandStatus(0);
            sm.setRegisteredDelivery((byte) 0x01);
            sm.setPriority((byte) 0x01);
            sm.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
            session0.submit(sm, 1000);

            session0.close();
            System.out.println("SENT SUCCESS");*/
            if (multip.equals("on")) {
                for (int x = 0; x <= messagefile.size() - 1; x++) {
                    if (x >= 4) {
                        System.out.println(x);
                        message = messagefile.get(x);
                        System.out.println("message: " + message);
                        try {
                            sendLongMessage(msisdn, message, shortcode);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }                        
                    System.out.println("SENDING MULTI");
                    }
                }
            } else {
                sm.setRegisteredDelivery((byte) 0x01);
                sm.setPriority((byte) 0x01);
                //sm.setShortMessage(mybyte);

                SubmitSmResp submitResp = session0.submit(sm, 10000);
            }
            //String text160 = "\u20AC Lorem [ipsum] dolor sit amet, consectetur adipiscing elit. Proin feugiat, leo id commodo tincidunt, nibh diam ornare est, vitae accumsan risus lacus sed sem metus.";
            //byte[] textBytes = CharsetUtil.encode(text160, CharsetUtil.CHARSET_GSM);
            //SubmitSm submit0 = new SubmitSm();
            // add delivery receipt
            //submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
            //submit0.setSourceAddress(new Address((byte)0x03, (byte)0x00, "40404"));
            //sm.setSourceAddress(new Address((byte) 0x03, (byte) 0x00, shortcode));
            //sm.setDestAddress(new Address((byte) 0x01, (byte) 0x01, msisdn));

            logger.info("sendWindow.size: {}", session0.getSendWindow().getSize());

            System.out.println("Press any key to unbind and close sessions");
            System.in.read();

            session0.unbind(5000);
        } catch (Exception e) {
            logger.error("", e);
            System.out.println(e);
        }

        if (session0 != null) {
            logger.info("Cleaning up session... (final counters)");
            if (session0.hasCounters()) {
                logger.info("tx-enquireLink: {}", session0.getCounters().getTxEnquireLink());
                logger.info("tx-submitSM: {}", session0.getCounters().getTxSubmitSM());
                logger.info("tx-deliverSM: {}", session0.getCounters().getTxDeliverSM());
                logger.info("tx-dataSM: {}", session0.getCounters().getTxDataSM());
                logger.info("rx-enquireLink: {}", session0.getCounters().getRxEnquireLink());
                logger.info("rx-submitSM: {}", session0.getCounters().getRxSubmitSM());
                logger.info("rx-deliverSM: {}", session0.getCounters().getRxDeliverSM());
                logger.info("rx-dataSM: {}", session0.getCounters().getRxDataSM());
            }

            session0.destroy();
            // alternatively, could call close(), get outstanding requests from
            // the sendWindow (if we wanted to retry them later), then call shutdown()
        }

        // this is required to not causing server to hang from non-daemon threads
        // this also makes sure all open Channels are closed to I *think*
        logger.info("Shutting down client bootstrap and executors...");
        clientBootstrap.destroy();
        executor.shutdownNow();
        monitorExecutor.shutdownNow();

        logger.info("Done. Exiting");

    }

    private static String[] splitByNumber(String text, int number) {

        int inLength = text.length();
        int arLength = inLength / number;
        int left = inLength % number;
        if (left > 0) {
            ++arLength;
        }
        String ar[] = new String[arLength];
        String tempText = text;
        for (int x = 0; x < arLength; ++x) {

            if (tempText.length() > number) {
                ar[x] = tempText.substring(0, number);
                tempText = tempText.substring(number);
            } else {
                ar[x] = tempText;
            }

        }

        return ar;
    }

    private static byte[] fromHex(String s) throws UnsupportedEncodingException {
        byte bs[] = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            bs[i / 2] = (byte) Integer.parseInt(s.substring(i, i + 2), 16);
        }
        return bs;
        //return new String(bs, "UTF8");1l
    }

    public static void sendLongMessage(String msisdn, String message, String senderAddr) throws Exception {
        byte sourceTon = (byte) 0x03;
        if (senderAddr != null && senderAddr.length() > 0) {
            sourceTon = (byte) 0x05;
        }

        byte[] textBytes = CharsetUtil.encode(message, CharsetUtil.CHARSET_ISO_8859_15);

        int maximumMultipartMessageSegmentSize = 140;
        byte[] byteSingleMessage = textBytes;
        byte[][] byteMessagesArray = splitUnicodeMessage(byteSingleMessage, maximumMultipartMessageSegmentSize);
        // submit all messages
        for (int i = 0; i < byteMessagesArray.length; i++) {
            SubmitSm submit0 = new SubmitSm();
            submit0.setEsmClass(SmppConstants.ESM_CLASS_UDHI_MASK);
            submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
            submit0.setSourceAddress(new Address(sourceTon, (byte) 0x00, senderAddr));
            submit0.setDestAddress(new Address((byte) 0x03, (byte) 0x00, msisdn));
            submit0.setShortMessage(byteMessagesArray[i]);
            getSession().submit(submit0, 10000);
        }
    }

    private static byte[][] splitUnicodeMessage(byte[] aMessage, Integer maximumMultipartMessageSegmentSize) {
        final byte UDHIE_HEADER_LENGTH = 0x05;
        final byte UDHIE_IDENTIFIER_SAR = 0x00;
        final byte UDHIE_SAR_LENGTH = 0x03;

        // determine how many messages have to be sent
        int numberOfSegments = aMessage.length / maximumMultipartMessageSegmentSize;
        int messageLength = aMessage.length;
        if (numberOfSegments > 255) {
            numberOfSegments = 255;
            messageLength = numberOfSegments * maximumMultipartMessageSegmentSize;
        }
        if ((messageLength % maximumMultipartMessageSegmentSize) > 0) {
            numberOfSegments++;
        }

        // prepare array for all of the msg segments
        byte[][] segments = new byte[numberOfSegments][];

        int lengthOfData;

        // generate new reference number
        byte[] referenceNumber = new byte[1];
        new Random().nextBytes(referenceNumber);

        // split the message adding required headers
        for (int i = 0; i < numberOfSegments; i++) {
            if (numberOfSegments - i == 1) {
                lengthOfData = messageLength - i * maximumMultipartMessageSegmentSize;
            } else {
                lengthOfData = maximumMultipartMessageSegmentSize;
            }
            // new array to store the header
            segments[i] = new byte[6 + lengthOfData];

            // UDH header
            // doesn't include itself, its header length
            segments[i][0] = UDHIE_HEADER_LENGTH;
            // SAR identifier
            segments[i][1] = UDHIE_IDENTIFIER_SAR;
            // SAR length
            segments[i][2] = UDHIE_SAR_LENGTH;
            // reference number (same for all messages)
            segments[i][3] = referenceNumber[0];
            // total number of segments
            segments[i][4] = (byte) numberOfSegments;
            // segment number
            segments[i][5] = (byte) (i + 1);
            // copy the data into the array
            System.arraycopy(aMessage, (i * maximumMultipartMessageSegmentSize), segments[i], 6, lengthOfData);
        }
        return segments;
    }

    private static SmppSession getSession() {

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

        DefaultSmppClient clientBootstrap = new DefaultSmppClient(Executors.newCachedThreadPool(), 1, monitorExecutor);

        DefaultSmppSessionHandler sessionHandler = new ClientSmppSessionHandler();

        SmppSessionConfiguration config0 = new SmppSessionConfiguration();
        config0.setWindowSize(1);
        config0.setName("Tester.Session.0");
        config0.setType(SmppBindType.TRANSCEIVER);
        String shortcode = "22225";

        // GET SERVER DETAILS The name of the file to open.
        String fileName = "smsc.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int index = 1;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (index == 1) {
                    String linesplit[] = line.split(">>");
                    config0.setHost(linesplit[1]);
                }
                if (index == 2) {
                    String linesplit[] = line.split(">>");
                    config0.setPort(Integer.parseInt(linesplit[1]));
                }
                if (index == 3) {
                    String linesplit[] = line.split(">>");
                    config0.setSystemId(linesplit[1]);
                }
                if (index == 4) {
                    String linesplit[] = line.split(">>");
                    config0.setPassword(linesplit[1]);
                }
                if (index == 5) {
                    String linesplit[] = line.split(">>");
                    shortcode = linesplit[1];
                }
                index++;
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        config0.setSystemType("SMPP");
        config0.getLoggingOptions().setLogBytes(true);
        // to enable monitoring (request expiration)
        config0.setRequestExpiryTimeout(30000);
        config0.setWindowMonitorInterval(15000);
        config0.setCountersEnabled(true);

        //
        // create session, enquire link, submit an sms, close session
        //
        SmppSession session0 = null;
        try {
            session0 = clientBootstrap.bind(config0, sessionHandler);
        } catch (SmppTimeoutException | SmppChannelException | UnrecoverablePduException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //take the session
        return session0;
    }

    private static String getMessage() {
        String stt = "";
        try {
            FileReader reader = new FileReader("messages.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            stt = bufferedReader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stt;
    }

    /**
     * Could either implement SmppSessionHandler or only override select methods
     * by extending a DefaultSmppSessionHandler.
     */
    public static class ClientSmppSessionHandler extends DefaultSmppSessionHandler {

        public ClientSmppSessionHandler() {
            super(logger);
        }

        @Override
        public void firePduRequestExpired(PduRequest pduRequest) {
            logger.warn("PDU request expired: {}", pduRequest);
        }

        @Override
        public PduResponse firePduRequestReceived(PduRequest pduRequest) {
            PduResponse response = pduRequest.createResponse();

            // do any logic here
            logger.info("SMS Received: {}", pduRequest);
            if (pduRequest.getCommandId() == SmppConstants.CMD_ID_DELIVER_SM) {
                DeliverSm mo = (DeliverSm) pduRequest;
                int length = mo.getShortMessageLength();
                Address source_address = mo.getSourceAddress();
                Address dest_address = mo.getDestAddress();
                byte[] shortMessage = mo.getShortMessage();
                String SMS = new String(shortMessage);
                //processSms(dest_address.getAddress(), mo.getShortMessage().toString());

                logger.info(source_address + ", " + dest_address + ", " + SMS);

                System.out.println(source_address + ", " + dest_address + ", " + SMS);
                try {
                    FileWriter writer = new FileWriter("pulldata.txt");
                    writer.write(source_address + "<<" + dest_address + "<<" + SMS + "<<");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return response;
        }

    }

}
