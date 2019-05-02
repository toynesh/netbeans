package com.pdsl.vending;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IpayConnectionManager {

    private static IpayConnectionManager connectionManagerInstance = null;
    static Socket socket = null;
    InputStream inputStream;
    public static SSLClient sslconnect = new SSLClient();

    private IpayConnectionManager() {
        // Exists only to defeat instantiation.
    }

    public static IpayConnectionManager getConnectionManagerInstance() {
        if (connectionManagerInstance == null) {
            connectionManagerInstance = new IpayConnectionManager();
        }
        return connectionManagerInstance;
    }

    /**
     * Waits on response from server
     *
     * @param socket Server socket
     */
    public String readServerResponse(Socket socket) {
        String results = null;
        try {
            BufferedInputStream serverReader = new BufferedInputStream(socket.getInputStream());
            IpayMessageWrapper wrap = new IpayMessageWrapper();
            StringBuilder build = new StringBuilder();
            byte[] all = wrap.unWrap(serverReader);
            for (int n = 0; n < all.length; n++) {
                char c = (char) all[n];
                build.append(c);
                // System.out.print(c);
            }
            results = build.toString();
            //System.out.println("\n\nStringBuilder " + build.toString());
            //pay.responsePrepaid(build.toString());
            //pay.responsePostPay();
            return results;
        } catch (IOException ex) {
            // System.out.println("Error: Unable to read server response\n\t" + ex);
        }
        return "ERROR";
    }

    public byte[] wrap(byte[] msg) {
        int len = msg.length;
        if (len > 65535) {
            throw new IllegalArgumentException("Message length exceeds 65535 bytes.");
        }

        byte firstByte = (byte) (len >>> 8);
        byte secondByte = (byte) len;

        ByteArrayOutputStream baos = new ByteArrayOutputStream(len + 2);
        baos.write(firstByte);
        baos.write(secondByte);
        try {
            baos.write(msg);
        } catch (IOException ioe) {
        }
        return baos.toByteArray();
    }

    public String connection(String build) {
        BufferedOutputStream buff = null;
        String results = "ERROR";

        try {
            if (inputStream == null) {
                inputStream = getClass().getClassLoader().getResourceAsStream("resource/keystore.jks");
                //socket=sslconnect.livesocket("41.204.194.188", 8956, "changeit", "6000", inputStream);   // Equitel test
                socket = sslconnect.livesocket("41.204.194.188", 9137, "changeit", "6000", inputStream); //Equitel PdslEquity
                //socket=sslconnect.livesocket("41.204.194.188", 9115, "changeit", "6000", inputStream);   // airtel test PdslAirtel
                //socket = sslconnect.livesocket("41.204.194.188", 9417, "changeit", "6000", inputStream);   // airtel
            }
        } catch (Exception ex) {
            Logger.getLogger(IpayConnectionManager.class.getName()).log(Level.SEVERE, "LOST CONNECTION" + ex);
            getConnectionManagerInstance();
        }
        //socket = sslconnect.testsocket("41.204.194.188", 9115, "6000");
        //System.out.println("SOCKET: " + socket);
        try {
            IpayMessageWrapper wrap = new IpayMessageWrapper();

            buff = new BufferedOutputStream(socket.getOutputStream());
            buff.write(wrap.wrap(build.getBytes()));
            buff.flush();

            //System.out.println("Info: Message has been sent..." + build);
            Logger.getLogger(IpayConnectionManager.class.getName()).log(Level.INFO, "Msg to Ipay: " + build);
            // Wait for server response
            results = getConnectionManagerInstance().readServerResponse(socket);
            Logger.getLogger(IpayConnectionManager.class.getName()).log(Level.INFO, "Msg from Ipay: " + results);

            return results;
        } catch (IOException ex) {
            Logger.getLogger(IpayConnectionManager.class.getName()).log(Level.SEVERE, "ERROR SENDING AND RECIEVING BYTES:" + ex);
            
        }
        return results;
    }

}
