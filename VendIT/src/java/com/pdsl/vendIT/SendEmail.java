/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vendIT;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author juliuskun
 */
public class SendEmail {

    public static void sendSimpleMail(String tomail, String message) throws Exception {
        Email email = new SimpleEmail();
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("magondu@pdslkenya.com",
                "Lexi@2017"));
        email.setDebug(false);
        email.setHostName("mail.busgateway.is.co.za");
        email.setFrom("magondu@pdslkenya.com");
        email.setSubject("System! Acc/Meter with amount above 20000");
        email.setMsg(message);
        email.addTo(tomail);
        //email.setTLS(true);
        email.send();
        System.out.println("Mail sent!");
        Logger.getLogger(SendEmail.class.getName()).log(Level.INFO, null, "Mail sent!");
    }

    public static void main(String[] args) {
        SendEmail mailing = new SendEmail();
        try {
            mailing.sendSimpleMail("accounts@pdslkenya.com","Test from local");
            /*DataStore data = new DataStore();
            Connection conn = data.connect();
            String query = "SELECT * FROM user";

            // create the java statement
            Statement st = conn.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            // iterate through the java resultset
            while (rs.next()) {
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                Logger.getLogger(SendEmail.class.getName()).log(Level.INFO, firstName+' '+lastName);
            }*/
        } catch (Exception ex) {
            Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
