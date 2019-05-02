/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapps;

import metro_sdp.Metro_sdp;

/**
 *
 * @author juliuskun
 */
public class TestApps {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Metro_sdp sdp = new Metro_sdp();
        sdp.sendSdpSms("254728064120", "Ichieni mahhh", "JULCOL", "704307", "http://197.248.61.165:8084/Notification/SmsNotificationService");
    }
    
}
