package com.pdsl.vending;

/**
 * Tony Muthamia
 *
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLException;

public class ConnectionManager2 {

    private static ConnectionManager2 connectionManagerInstance = null;
    static int port = 9137; //LIVE 9417 //TEST 9115
    static Socket socket = null;
    static InputStream inputStream = null;
    public static SSLClientTest sslConnect = new SSLClientTest();

    private ConnectionManager2() {

    }

    public static ConnectionManager2 getConnectionManager2Instance() {
        if (connectionManagerInstance == null) {
            connectionManagerInstance = new ConnectionManager2();
        }
        return connectionManagerInstance;
    }

    /**
     * Waits on response from server
     *
     * @param socket Server socket
     */
    public String readServerResponse(Socket socket) {
        String results = "ERROR";
        try {
            BufferedInputStream serverReader = new BufferedInputStream(socket.getInputStream());
            IpayMessageWrapper wrap = new IpayMessageWrapper();
            StringBuilder build = new StringBuilder();
            byte[] all = wrap.unWrap(serverReader);
            for (int n = 0; n < all.length; n++) {
                char c = (char) all[n];
                build.append(c);
                System.out.print(c);
            }
            results = build.toString();
            Logger.getLogger(ConnectionManager2.class.getName()).log(Level.INFO,"\n\nStringBuilder " + build.toString());
            //pay.responsePrepaid(build.toString());
            //pay.responsePostPay();
            return results;
        } catch (IOException ex) {

            results = "TIMEOUT";

            Logger.getLogger(ConnectionManager2.class.getName()).log(Level.SEVERE,"Error: Unable to read server response\n\t" + ex);
        }
        return results;
    }

    public String connection(String build, String timeout) {
        BufferedOutputStream buff = null;
        String results = "TIMEOUT";

        try {
            //For SSL ENABLED CONNECTION
            if (inputStream == null || socket == null || socket.isConnected() == false) {
                inputStream = ConnectionManager2.class.getClassLoader().getResourceAsStream("resource/keystore.jks");
                socket = sslConnect.sslSocket("41.204.194.188", port, "changeit", "6000", inputStream); //Equitel PdslEquity
                //socket =sslConnect.sslSocket("41.204.194.188", port, "changeit", timeout, inputStream); //8956 8994//LIVE 9417 //TEST 9115
                //socket=sslConnect.sslSocket("41.204.194.188", 9115, "changeit", "6000", inputStream);   // airtel test PdslAirtel
                socket.setKeepAlive(true);
            }// TEXTIFY 9102
            //socket = new Socket("41.204.194.188", 3520);
            //socket.setSoTimeout(Integer.valueOf(timeout));

            IpayMessageWrapper wrap = new IpayMessageWrapper();

            buff = new BufferedOutputStream(socket.getOutputStream());
            buff.write(wrap.wrap(build.getBytes()));
            buff.flush();
            Logger.getLogger(ConnectionManager2.class.getName()).log(Level.INFO,"Info: Message has been sent..." + build);
            // Wait for server response
            results = getConnectionManager2Instance().readServerResponse(socket);

            return results;

        } catch (SocketException ex) {
            ex.printStackTrace();
            Logger.getLogger(ConnectionManager2.class.getName()).log(Level.INFO,"CONNECTTION ERROR >>> " + ex.getMessage());
            try {
                inputStream = ConnectionManager2.class.getClassLoader().getResourceAsStream("resource/keystore.jks");
                //socket =sslConnect.sslSocket("41.204.194.188", port, "changeit", timeout, inputStream); //8956 8994//LIVE 9417 //TEST 9115
                socket = sslConnect.sslSocket("41.204.194.188", port, "changeit", "6000", inputStream); //Equitel PdslEquity
                socket.setKeepAlive(true);
                IpayMessageWrapper wrap = new IpayMessageWrapper();

                buff = new BufferedOutputStream(socket.getOutputStream());
                buff.write(wrap.wrap(build.getBytes()));
                buff.flush();

                Logger.getLogger(ConnectionManager2.class.getName()).log(Level.INFO,"Info: Message has been sent..." + build);
                // Wait for server response
                results = getConnectionManager2Instance().readServerResponse(socket);
            } catch (IOException e) {
                e.printStackTrace();
                results = "TIMEOUT";
            }

        } catch (SSLException el) {
            el.printStackTrace();
            Logger.getLogger(ConnectionManager2.class.getName()).log(Level.INFO,"CONNECTTION ERROR >>> " + el.getMessage());
            try {
                inputStream = ConnectionManager2.class.getClassLoader().getResourceAsStream("resource/keystore.jks");
                //socket =sslConnect.sslSocket("41.204.194.188", port, "changeit", timeout, inputStream); //8956 8994//LIVE 9417 //TEST 9115
                socket = sslConnect.sslSocket("41.204.194.188", port, "changeit", "6000", inputStream); //Equitel PdslEquity
                socket.setKeepAlive(true);
                IpayMessageWrapper wrap = new IpayMessageWrapper();

                buff = new BufferedOutputStream(socket.getOutputStream());
                buff.write(wrap.wrap(build.getBytes()));
                buff.flush();

                Logger.getLogger(ConnectionManager2.class.getName()).log(Level.INFO,"Info: Message has been sent..." + build);
                // Wait for server response
                results = getConnectionManager2Instance().readServerResponse(socket);
            } catch (IOException e) {
                e.printStackTrace();
                results = "TIMEOUT";
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger(ConnectionManager2.class.getName()).log(Level.INFO,"CONNECTTION ERROR >>> " + e.getMessage());
            results = "TIMEOUT";
        }
        return results;
    }

}
