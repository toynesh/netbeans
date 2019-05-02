/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kannelserver;

import com.javacodegeeks.kannel.api.SMSManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author coolie
 */
public class Kannelserver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SMSManager smsManager = SMSManager.getInstance();

        // We can change the prefetch size of the background worker thread
        smsManager.setMessagesPrefetchSize(30);

        // We can change the send SMS rate
        smsManager.setMessagesSendRate(65);

        try {

            // Send SMS to a single destination
            smsManager.sendSMS("localhost", "2775", "smppclient2", "password", "22225", "2547278987", "the_message");

            /*// Send SMS to a single destination with a specific priority and
            // send rate
            smsManager.sendSMS("localhost", "13013", "foo", "bar", "sender_mobile_number", "receiver_mobile_number", "the_message", SMSManager.MESSAGE_PRIORITY_3);

            // Send SMS to multiple recipients
            List<String> recipientsGroupA = new ArrayList<String>();

            recipientsGroupA.add("receiver_mobile_number_1");
            recipientsGroupA.add("receiver_mobile_number_2");
            recipientsGroupA.add("receiver_mobile_number_3");
            recipientsGroupA.add("receiver_mobile_number_4");
            recipientsGroupA.add("receiver_mobile_number_5");

            smsManager.sendSMS("localhost", "13013", "foo", "bar", "sender_mobile_number", recipientsGroupA, "the_message");

            // Send SMS to multiple recipients with a specific priority and
            // send rate
            List<String> recipientsGroupB = new ArrayList<String>();

            recipientsGroupB.add("receiver_mobile_number_1");
            recipientsGroupB.add("receiver_mobile_number_2");
            recipientsGroupB.add("receiver_mobile_number_3");
            recipientsGroupB.add("receiver_mobile_number_4");
            recipientsGroupB.add("receiver_mobile_number_5");

            smsManager.sendBulkSMS("localhost", "13013", "foo", "bar", "sender_mobile_number", recipientsGroupB, "the_message", SMSManager.MESSAGE_PRIORITY_7);

            // Send a WAP Push request to a single mobile recipient
            smsManager.sendWAPPush("localhost", "8080", "receiver_mobile_number", SMSManager.WAP_PUSH_RECEIVER_TYPE_MOBILE, "the_message", "http://localhost", 3);
*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Stops the background worker
        // smsManager.stopSMSManagerWorker()
    }

}
