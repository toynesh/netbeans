package pdsltextify;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import za.co.ipay.retail.system.bizswitch.IpayMessageWrapper;
import za.co.ipay.retail.system.bizswitch.IpayMessageWrapper2;

public class ConnectionManager {

    private static ConnectionManager connectionManagerInstance = null;
    static Socket socket = null;
    InputStream inputStream = null;
    public static SSLClientTest sslConnect = new SSLClientTest();

    private ConnectionManager() {

    }

    public static ConnectionManager getConnectionManagerInstance() {
        if (connectionManagerInstance == null) {
            connectionManagerInstance = new ConnectionManager();
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
            IpayMessageWrapper2 wrap = new IpayMessageWrapper2();
            StringBuilder build = new StringBuilder();
            byte[] all = wrap.unWrap(serverReader);
            for (int n = 0; n < all.length; n++) {
                char c = (char) all[n];
                build.append(c);
                System.out.print(c);
            }
            results = build.toString();
            System.out.println("\n\nStringBuilder " + build.toString());
            //pay.responsePrepaid(build.toString());
            //pay.responsePostPay();
            return results;
        } catch (IOException ex) {
            System.out.println("Error: Unable to read server response\n\t" + ex);
        }
        return "ERROR";
    }

    public String connection(String build) {
        BufferedOutputStream buff = null;
        String results = "ERROR";

        try {

            if (inputStream == null) {
                //inputStream = getClass().getClassLoader().getResourceAsStream("resource/keystore.jks");
                //socket = sslConnect.sslSocket("41.204.194.188", 9102, "clientpw", "6000", inputStream);
                inputStream = getClass().getClassLoader().getResourceAsStream("keystore.jks");
                socket = sslConnect.sslSocket("41.204.194.188", 9417, "changeit", "6000", this.inputStream);//pdsl
                
            }

            //socket = new Socket("41.204.194.188", 9102);
            IpayMessageWrapper wrap = new IpayMessageWrapper();

            buff = new BufferedOutputStream(socket.getOutputStream());
            buff.write(wrap.wrap(build.getBytes()));
            buff.flush();

            System.out.println("Info: Message has been sent..." + build);
            // Wait for server response
            results = getConnectionManagerInstance().readServerResponse(socket);

            return results;
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);

        }
        return results;
    }

}
