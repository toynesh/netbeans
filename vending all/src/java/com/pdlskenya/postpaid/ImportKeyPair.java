package com.pdlskenya.postpaid;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;

 
public class ImportKeyPair {
    /*
    public static void main(String[] args) throws Exception {
        String keyFile = args[0];
        String certFile = args[1];
        String password = args[2];
        String alias = args[3];
        String keyStoreName = args[4];
        
        byte[] keyFileBytes = readBytes(new File(keyFile));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyFileBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        
        byte[] certFileBytes = readBytes(new File(certFile));
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(certFileBytes));
        Certificate[] chain = new Certificate[] { certificate };
        
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
        keyStore.load(null);
        keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), chain);
        
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(keyStoreName);
            keyStore.store(fout, password.toCharArray());
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    */
    public static byte[] readBytes(File file) throws IOException {
        byte[] bytes;
        FileInputStream fin = null; 
        try {
            fin = new FileInputStream(file);

            // can only create int size arrays, so cast to int
            long length = file.length();

            if (length > Integer.MAX_VALUE)
                throw new IOException("File too large: size=" + length + " name=" + file.getName());

            bytes = new byte[(int) length];

            int offset = 0;
            int read = 0;
            do {
                read = fin.read(bytes, offset, bytes.length - offset);
                offset += read;
            } while (offset < bytes.length && read >= 0);

            if (offset < bytes.length) {
                throw new IOException("Could not read whole file: size=" + length + " read=" + read + " name=" + file.getName());
            }

            fin.close();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if(fin != null)
                    fin.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return bytes;
    }

}
