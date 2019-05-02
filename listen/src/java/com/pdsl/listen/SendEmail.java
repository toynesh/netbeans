package com.pdsl.listen;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class SendEmail {

    public static void sendSimpleMail(String tomail, String message) throws Exception {
        Email email = new SimpleEmail();
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("magondu@pdslkenya.com", "Password.123"));

        email.setDebug(false);
        email.setHostName("mail.busgateway.is.co.za");
        email.setFrom("magondu@pdslkenya.com");
        email.setSubject("Manual Vend");
        email.setMsg(message);
        email.addTo(tomail);

        email.send();
        System.out.println("Mail sent!");
        Logger.getLogger(SendEmail.class.getName()).log(Level.INFO, null, "Mail sent!");
    }

    public static void main(String[] args) {
        SendEmail mailing = new SendEmail();
        try {
            DataStore data = new DataStore();
            Connection conn = data.connect();
            String query = "SELECT * FROM user";

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                Logger.getLogger(SendEmail.class.getName()).log(Level.INFO, firstName + ' ' + lastName);
            }
        } catch (Exception ex) {
            Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
