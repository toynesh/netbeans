/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vending;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLException;

/**
 *
 * @author coolie
 */
public class ConnectionManager {
//private static ConnectionManager connectionManagerInstance = null;

    static int port = 9137; //LIVE 9417 //TEST 9115

    /**
     * private ConnectionManager() {
     *
     * }
     * public static ConnectionManager getConnectionManagerInstance() {
     * if(connectionManagerInstance == null) { connectionManagerInstance = new
     * ConnectionManager(); } return connectionManagerInstance; } /** Waits on
     * response from server
     *
     * @param socket Server socket
     */
    String readServerResponse(Socket socket) {
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
            serverReader.close();
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO,"\n\nStringBuilder " + build.toString());
            //pay.responsePrepaid(build.toString());
            //pay.responsePostPay();
            return results;
        } catch (IOException ex) {

            results = "TIMEOUT";

            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE,"Error: Unable to read server response\n\t" + ex);
        }
        return results;
    }

    String connection(String build, String timeout) {
        BufferedOutputStream buff = null;
        String results = "TIMEOUT";
        Socket socket = null;
        InputStream inputStream = null;
        SSLClientTest sslConnect = new SSLClientTest();

        try {
            //For SSL ENABLED CONNECTION
            if (inputStream == null || socket == null || socket.isConnected() == false) {
                inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream("resource/keystore.jks");
                socket = sslConnect.sslSocket("41.204.194.188", port, "changeit", timeout, inputStream); //8956 8994//LIVE 9417 //TEST 9115
                //socket.setKeepAlive(true); 
            }// TEXTIFY 9102
            //socket = new Socket("41.204.194.188", 3520);
            //socket.setSoTimeout(Integer.valueOf(timeout));

            IpayMessageWrapper wrap = new IpayMessageWrapper();

            buff = new BufferedOutputStream(socket.getOutputStream());
            buff.write(wrap.wrap(build.getBytes()));
            buff.flush();

            Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO,"Info: Message has been sent..." + build);
            // Wait for server response
            results = readServerResponse(socket);
            try {
                socket.close();
                Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO,"CLOSING SOCKETT CONNECTION");
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }

            return results;

        } catch (SocketException ex) {
            ex.printStackTrace();
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE,"CONNECTTION ERROR >>> " + ex.getMessage());
            try {
                inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream("resource/keystore.jks");
                socket = sslConnect.sslSocket("41.204.194.188", port, "changeit", timeout, inputStream); //8956 8994//LIVE 9417 //TEST 9115
                socket.setKeepAlive(true);
                IpayMessageWrapper wrap = new IpayMessageWrapper();

                buff = new BufferedOutputStream(socket.getOutputStream());
                buff.write(wrap.wrap(build.getBytes()));
                buff.flush();

                Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO,"Info: Message has been sent..." + build);
                // Wait for server response
                results = readServerResponse(socket);
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
                results = "TIMEOUT";
            }

        } catch (SSLException el) {
            el.printStackTrace();
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO,"CONNECTTION ERROR >>> " + el.getMessage());
            try {
                inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream("resource/keystore.jks");
                socket = sslConnect.sslSocket("41.204.194.188", port, "changeit", timeout, inputStream); //8956 8994//LIVE 9417 //TEST 9115
                socket.setKeepAlive(true);
                IpayMessageWrapper wrap = new IpayMessageWrapper();

                buff = new BufferedOutputStream(socket.getOutputStream());
                buff.write(wrap.wrap(build.getBytes()));
                buff.flush();

                Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO,"Info: Message has been sent..." + build);
                // Wait for server response
                results = readServerResponse(socket);
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
                results = "TIMEOUT";
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE,"CONNECTTION ERROR >>> " + e.getMessage());
            results = "TIMEOUT";
        } catch (NullPointerException ex) {
            results = "NOCONNECTION";
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return results;
    }

}
