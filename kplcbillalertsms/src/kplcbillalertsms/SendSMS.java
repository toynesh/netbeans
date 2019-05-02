/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kplcbillalertsms;

import metro_sdp.Metro_sdp;

/**
 *
 * @author coolie
 */
public class SendSMS {

    public void sendSMS(String message) {

        long startTime = System.currentTimeMillis();
        Metro_sdp sdp = new Metro_sdp();
        //sdp.sendSdpSms( "254721178823", "Test KPLC sms" , "MPESAPDSL",  "704307"); 
        System.out.println("NAME IS " + message);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("TIME SDP: " + elapsedTime);
    }

}
