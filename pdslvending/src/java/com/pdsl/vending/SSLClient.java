package com.pdsl.vending;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLClient {

    public Socket testsocket(String hostname, int port, String timeout2) {
        Socket socket = null;
        int timeout = -1;
        timeout = Integer.parseInt(timeout2);
        try {
            socket = new Socket(hostname, port);
            socket.setSoTimeout(timeout);
        } catch (IOException ex) {
            Logger.getLogger(SSLClient.class.getName()).log(Level.SEVERE, "Socket TIMEOUT"+timeout+" ERROR:"+ ex);
        }
        return socket;
    }

    public Socket livesocket(String hostname, int port, String keyStorePassword, String timeout2, InputStream in) {
        // System.out.println("START SSL TEST");
        //System.out.println("************************************************************************************************");
        // System.out.println("Arguments: <hostname> <port> <keystore> <password> <timeout in miliseconds> <debug('yes' to enable)> <optional: messageFileName>");
        // System.out.println("Note: Keystore and key password assumed to be the same for trust store and keystore");
        //  System.out.println("      If a messageFileName is not specified it will send a pingReq message");
        // System.out.println("************************************************************************************************\n");
        int timeout = -1;

        try {

            // String reqMessage = "<ipayMsg client=\"test\" term=\"00001\" seqNum=\"0\" time=\"2010-08-19 14:30:00 +0200\"><mngMsg ver=\"1.8\"><pingReq/></mngMsg></ipayMsg>";
            timeout = Integer.parseInt(timeout2);
            //if(args.length >=6 && args[5].equalsIgnoreCase("yes")) {
            //System.out.println("Debug is 'yes' so setting 'javax.net.debug' system property to 'ssl,handshake'");
            System.setProperty("javax.net.debug", "ssl,handshake");
            //}

            /*if(args.length == 7) {
             byte[] bytes = Files.readAllBytes(Paths.get(args[6]));
             reqMessage = new String(bytes, "UTF-8");
             System.out.println("Using reqMessage from file");
             }*/
            // System.out.println("Loading keystore: " + keyStoreFile + " password:" + keyStorePassword);
            KeyStore keyStore = KeyStore.getInstance("JKS");
            //FileInputStream fin = new FileInputStream(keyStoreFile);
            //keyStore.load(fin, keyStorePassword.toCharArray());
            keyStore.load(in, keyStorePassword.toCharArray());
            in.close();

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(keyStore);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, keyStorePassword.toCharArray());

            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextInt();

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), secureRandom);

            SSLSocketFactory sf = sslContext.getSocketFactory();
            System.out.println("Creating socket to: " + hostname + ":" + port);
            Socket socket = sf.createSocket(hostname, port);
            socket.setSoTimeout(timeout);
            /*
             System.out.println("\nSending request: \n" + reqMessage);
             OutputStream out = socket.getOutputStream();
             out.write(wrap(reqMessage.getBytes("UTF-8")));

             System.out.println("\nWaiting for response (timeout " + timeout + " miliseconds)...");
             InputStream in = socket.getInputStream();
             String response = new String(unWrap(in), "UTF-8");
             System.out.println("\nResponse: \n"  + response);
            
             // Close the socket
             in.close();
             out.close();
             */
            return socket;
        } catch (SocketTimeoutException e) {
            //System.out.println("\nRequest timed out: timeout=" + timeout + " miliseconds");
             Logger.getLogger(SSLClient.class.getName()).log(Level.SEVERE, "Socket TIMEOUT"+timeout+" ERROR:"+ e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n*** END SSL TEST ***");
        return null;
    }

    public static byte[] wrap(byte[] msg) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public static byte[] unWrap(InputStream inputStream) throws IOException {

        int firstByteValue = inputStream.read();
        if (firstByteValue == -1) {
            throw new IOException("End of Stream after reading byte 1");
        }
        firstByteValue = firstByteValue << 8;

        int secondByteValue = inputStream.read();
        if (secondByteValue == -1) {
            throw new IOException("End of Stream after reading byte 2. byte 1=" + Integer.toHexString(firstByteValue));
        }

        int len = firstByteValue + secondByteValue;

        byte[] message = new byte[len];
        int requestLen;
        int readLen;
        int currentIndex = 0;
        while (true) {
            requestLen = len - currentIndex;
            readLen = inputStream.read(message, currentIndex, requestLen);
            if (readLen == requestLen) {
                break;  // Message is complete.
            }

            // Either data was not yet available, or End of Stream.
            currentIndex += readLen;
            int nextByte = inputStream.read();
            if (nextByte == -1) {
                throw new IOException("End of Stream at " + currentIndex + " with vli=" + len + ". Individual hex bytes: firstByte=" + Integer.toHexString(firstByteValue) + " secondByte=" + Integer.toHexString(secondByteValue));
            }
            message[currentIndex++] = (byte) nextByte;
        }
        return message;
    }

}
