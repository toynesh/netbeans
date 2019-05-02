package com.pdsl.checkacc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

public class SSLClientTest {

    public void connect() {
        OutputStream out = null;
        try {
            ClassLoader cl = getClass().getClassLoader();

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resource/keystore.jks");

            Socket socket = main2("41.204.194.188", 9137, "changeit", "60000", inputStream);
            String reqMessage = "<ipayMsg client=\"EquityBank\" seqNum=\"0\" term=\"00001\" time=\"2015-10-12 10:51:45 +0300\">\n        <billPayMsg ver=\"1.7\"><billInfoReq>\n        <ref>528551512123</ref>\n        <providerName>KPLC Provider</providerName>\n        <custAccNum>903</custAccNum>\n        </billInfoReq>\n        </billPayMsg>\n</ipayMsg>";

            out = socket.getOutputStream();
            out.write(wrap(reqMessage.getBytes("UTF-8")));

            InputStream in = socket.getInputStream();
            String response = new String(unWrap(in), "UTF-8");

            in.close();
            out.close();
            return;
        } catch (IOException ex) {
            Logger.getLogger(SSLClientTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(SSLClientTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Socket main2(String hostname, int port, String keyStorePassword, String timeout2, InputStream in) {
        int timeout = -1;
        try {
            timeout = Integer.parseInt(timeout2);

            System.setProperty("javax.net.debug", "ssl,handshake");

            KeyStore keyStore = KeyStore.getInstance("JKS");

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

            return socket;
        } catch (SocketTimeoutException e) {
            System.out.println("\nRequest timed out: timeout=" + timeout + " miliseconds");
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

    public static byte[] unWrap(InputStream inputStream)
            throws IOException {
        int firstByteValue = inputStream.read();
        if (firstByteValue == -1) {
            throw new IOException("End of Stream after reading byte 1");
        }
        firstByteValue <<= 8;

        int secondByteValue = inputStream.read();
        if (secondByteValue == -1) {
            throw new IOException("End of Stream after reading byte 2. byte 1=" + Integer.toHexString(firstByteValue));
        }
        int len = firstByteValue + secondByteValue;

        byte[] message = new byte[len];

        int currentIndex = 0;
        for (;;) {
            int requestLen = len - currentIndex;
            int readLen = inputStream.read(message, currentIndex, requestLen);
            if (readLen == requestLen) {
                break;
            }
            currentIndex += readLen;
            int nextByte = inputStream.read();
            if (nextByte == -1) {
                throw new IOException("End of Stream at " + currentIndex + " with vli=" + len + ". Individual hex bytes: firstByte=" + Integer.toHexString(firstByteValue) + " secondByte=" + Integer.toHexString(secondByteValue));
            }
            message[(currentIndex++)] = ((byte) nextByte);
        }
        return message;
    }
}
