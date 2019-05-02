package com.pdlskenya.postpaid;

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

    public void connect() {

        OutputStream out = null;
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream inputStream
                    = getClass().getClassLoader().getResourceAsStream("resource/keystore.jks");
            //File file  = new File(inputStream);
            // Socket socket = new Socket("41.204.194.188", 8956);
            Socket socket = main2("41.204.194.188", 9137, "changeit", "60000", inputStream);
            String reqMessage = "<ipayMsg client=\"EquityBank\" seqNum=\"0\" term=\"00001\" time=\"2015-10-12 10:51:45 +0300\">\n"
                    + "        <billPayMsg ver=\"1.7\"><billInfoReq>\n"
                    + "        <ref>528551512123</ref>\n"
                    + "        <providerName>KPLC Provider</providerName>\n"
                    + "        <custAccNum>903</custAccNum>\n"
                    + "        </billInfoReq>\n"
                    + "        </billPayMsg>\n"
                    + "</ipayMsg>";
            System.out.println("\nSending request: \n" + reqMessage);
            out = socket.getOutputStream();
            out.write(wrap(reqMessage.getBytes("UTF-8")));
            System.out.println("\nWaiting for response (timeout 60000 miliseconds)...");
            InputStream in = socket.getInputStream();
            String response = new String(unWrap(in), "UTF-8");
            System.out.println("\nResponse: \n"  + response);
            // Close the socket
            in.close();
            out.close();
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
/*
    public static void main(String[] args) {

        SSLClientTest test = new SSLClientTest();
        test.connect();
        /*
         System.out.println("START SSL TEST");
         System.out.println("************************************************************************************************");
         System.out.println("Arguments: <hostname> <port> <keystore> <password> <timeout in miliseconds> <debug('yes' to enable)> <optional: messageFileName>");
         System.out.println("Note: Keystore and key password assumed to be the same for trust store and keystore");
         System.out.println("      If a messageFileName is not specified it will send a pingReq message");
         System.out.println("************************************************************************************************\n");
         int timeout = -1;
            
         try {
         int port;
         String hostname;
         String keyStoreFile;
         String keyStorePassword;
         String reqMessage = "<ipayMsg client=\"test\" term=\"00001\" seqNum=\"0\" time=\"2010-08-19 14:30:00 +0200\"><mngMsg ver=\"1.8\"><pingReq/></mngMsg></ipayMsg>";
                
         if(args.length < 6) {
         System.out.println("Incorrect arguments! Exiting...");
         System.exit(0);
         }
         hostname = args[0];
         port = Integer.parseInt(args[1]);
         keyStoreFile = args[2];
         keyStorePassword = args[3];
         timeout = Integer.parseInt(args[4]);
         if(args.length >=6 && args[5].equalsIgnoreCase("yes")) {
         System.out.println("Debug is 'yes' so setting 'javax.net.debug' system property to 'ssl,handshake'");
         System.setProperty("javax.net.debug", "ssl,handshake");
         }
                
         if(args.length == 7) {
         byte[] bytes = Files.readAllBytes(Paths.get(args[6]));
         reqMessage = new String(bytes, "UTF-8");
         System.out.println("Using reqMessage from file");
         }
                
         System.out.println("Loading keystore: " + keyStoreFile + " password:" + keyStorePassword);
         KeyStore keyStore = KeyStore.getInstance("JKS");
         FileInputStream fin = new FileInputStream(keyStoreFile);
         keyStore.load(fin, keyStorePassword.toCharArray());
         fin.close();
                
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
         } catch(SocketTimeoutException e) {
         System.out.println("\nRequest timed out: timeout=" + timeout + " miliseconds");
         } catch(Exception e) {
         e.printStackTrace();
         }
         System.out.println("\n*** END SSL TEST ***");
        
    }
*/
    public Socket main2(String hostname, int port, String keyStorePassword, String timeout2, InputStream in) {
        System.out.println("START SSL TEST");
        System.out.println("************************************************************************************************");
        System.out.println("Arguments: <hostname> <port> <keystore> <password> <timeout in miliseconds> <debug('yes' to enable)> <optional: messageFileName>");
        System.out.println("Note: Keystore and key password assumed to be the same for trust store and keystore");
        System.out.println("      If a messageFileName is not specified it will send a pingReq message");
        System.out.println("************************************************************************************************\n");
        int timeout = -1;

        try {

            // String reqMessage = "<ipayMsg client=\"test\" term=\"00001\" seqNum=\"0\" time=\"2010-08-19 14:30:00 +0200\"><mngMsg ver=\"1.8\"><pingReq/></mngMsg></ipayMsg>";
            timeout = Integer.parseInt(timeout2);
            //if(args.length >=6 && args[5].equalsIgnoreCase("yes")) {
            System.out.println("Debug is 'yes' so setting 'javax.net.debug' system property to 'ssl,handshake'");
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
