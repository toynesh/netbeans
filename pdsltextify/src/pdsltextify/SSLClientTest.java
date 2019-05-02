package pdsltextify;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public class SSLClientTest {

   
    public Socket sslSocket(String hostname, int port, String keyStorePassword, String timeout2, InputStream in) {
        System.out.println("START SSL TEST");
        System.out.println("************************************************************************************************");
        System.out.println("Arguments: <hostname> <port> <keystore> <password> <timeout in miliseconds> <debug('yes' to enable)> <optional: messageFileName>");
        System.out.println("Note: Keystore and key password assumed to be the same for trust store and keystore");
        System.out.println("      If a messageFileName is not specified it will send a pingReq message");
        System.out.println("************************************************************************************************\n");
        int timeout = -1;

        try {
            timeout = Integer.parseInt(timeout2);
            System.out.println("Debug is 'yes' so setting 'javax.net.debug' system property to 'ssl,handshake'");
            System.setProperty("javax.net.debug", "ssl,handshake");
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
