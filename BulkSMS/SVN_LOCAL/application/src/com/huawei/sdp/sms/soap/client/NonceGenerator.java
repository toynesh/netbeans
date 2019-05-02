package com.huawei.sdp.sms.soap.client;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.BASE64Encoder;

public class NonceGenerator {

    private static final NonceGenerator instance = new NonceGenerator();

    private NonceGenerator() {

    }

    public static NonceGenerator getInstance() {
	return instance;
    }

    public String getNonce(String spID) {
	BASE64Encoder encoder = new BASE64Encoder();
	return encoder.encode((spID + String
		.valueOf(System.currentTimeMillis())).getBytes());
    }
    
    
    public String MD5Gen(String spid){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
           md.update(spid.getBytes());
    
           byte byteData[] = md.digest();
    
           //convert the byte to hex format method 1
           StringBuffer sb = new StringBuffer();
           for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
           }
    
        //   System.out.println(spid+" Digest(in hex format):: " + sb.toString());
           
           return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(NonceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
 public String MDGen(String spid) {
        try {
            String s="This is a test";
            MessageDigest m=MessageDigest.getInstance("MD5");
            m.update(s.getBytes(),0,s.length());
            String digest=new BigInteger(1,m.digest()).toString(16);
            System.out.println(spid+" MD5: "+digest);
            return digest;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(NonceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }    

}