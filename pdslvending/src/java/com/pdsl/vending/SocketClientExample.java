/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vending;

/**
 *
 * @author coolie
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SocketClientExample {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException, Exception {
        System.out.println("Attempting connection to IPAY");

        // establish the socket connection to the server
        // using the local IP address, if server is running on some other IP, use that 
        //Socket socket = new Socket("41.204.194.188", 8902);
        //System.out.println("Socket Connected");

        InputStream inputStream = SocketClientExample.class.getClassLoader().getResourceAsStream("resource/keystore.jks");
        Socket socket = sslSocket("41.204.194.188", 9115, "changeit", "45000", inputStream);
        System.out.println("Socket Connected");
        // write to socket using OutputStream
        // Initializing request content
        String toipay = "<ipayMsg client=\"PdslEquity\" seqNum=\"2\" term=\"00001\" time=\"2018-10-24 05:01:48 +0300\"><billPayMsg ver=\"1.7\"><billInfoReq><ref>100000173152</ref><providerName>KPLC Provider</providerName><custAccNum>40417750</custAccNum></billInfoReq></billPayMsg></ipayMsg>";
        byte[] request = toipay.getBytes();

        System.out.println("Sending request to Socket Server");
        System.out.println("Request: " + toipay);
        BufferedOutputStream buff = new BufferedOutputStream(socket.getOutputStream());
        buff.write(wrap(request));
        buff.flush();
        System.out.println("Request was sent. Awaiting response.");

        BufferedInputStream serverReader = new BufferedInputStream(socket.getInputStream());
        byte[] message = unWrap(serverReader);

        String message2 = new String(message);
        System.out.println("Response: " + message2);
        socket.close();
    }

    public static Socket sslSocket(String hostname, int port, String keyStorePassword, String timeout2, InputStream in) {
        int timeout = -1;
        try {
            timeout = Integer.parseInt(timeout2);
            //System.setProperty("javax.net.debug", "ssl,handshake");//ssl logs
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(in, keyStorePassword.toCharArray());
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

            System.out.println("\n*** CREATED SSL ***");
            return socket;
        } catch (SocketTimeoutException e) {
            System.out.println("\nRequest timed out: timeout=" + timeout + " miliseconds");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n*** END SSL ***");
        return null;
    }

    public static byte[] wrap(byte[] msg) throws Exception {
        int len = msg.length;
        if (len > 65535) {
            throw new IllegalArgumentException("Exceeds 65535 bytes.");
        }
        byte firstByte = (byte) (len >>> 8);
        byte secondByte = (byte) len;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(len + 2);
        baos.write(firstByte);
        baos.write(secondByte);
        baos.write(msg);
        return baos.toByteArray();
    }

    public static byte[] unWrap(InputStream inputStream) throws Exception {
        int firstByte = inputStream.read();
        if (firstByte == -1) {
            throw new IOException("End of Stream while trying to read vli byte 1");
        }
        int firstByteValue = firstByte << 8;
        int secondByteValue = inputStream.read();
        if (secondByteValue == -1) {
            throw new IOException("End of Stream reading vli byte 2.");
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
                break; // Message is complete.
            }
// Either data was not yet available, or End of Stream.
            currentIndex += readLen;
            int nextByte = inputStream.read();
            if (nextByte == -1) {
                throw new IOException("End of Stream at " + currentIndex);
            }
            message[currentIndex++] = (byte) nextByte;
        }
        return message;
    }
}
