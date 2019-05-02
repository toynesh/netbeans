/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kplcbillalertsms;

import java.sql.*;
import org.fluttercode.datafactory.impl.DataFactory;
import java.util.Calendar;

/**
 *
 * @author coolie
 */
public class BillAlerts {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        System.out.println("HOUR_OF_DAY " + timeOfDay);

        if (timeOfDay >= 7 && timeOfDay < 17) {
            //deleteBillAlertMessages("1");
            //insertBillAlertMessages("5");
            getBillAlertMessages();
        } else {
            System.out.println("IT IS NIGHT TIME: NO SENDING BILL ALERTS!!!");
        }
    }

    public static void getBillAlertMessages() {
        DataStore data = new DataStore();
        Connection conn = data.kplcconnect();
        try {
            String query = "SELECT * from BILL_ALERTS LIMIT 10";
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(query);

            while (results.next()) {
                System.out.println("DB..........");
                System.out.println(results.getInt(1) + "  " + results.getString(2) + "  " + results.getString(3));
            }
            conn.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void insertBillAlertMessages(String id) {

        try {
            DataStore data = new DataStore();
            Connection conn = data.kplcconnect();
            PreparedStatement preparedStatement = null;
            try {
                String insertTableSQL = "INSERT INTO BILL_ALERTS"
                        + "(BILL_ALERT_ID, BILL_ALERT_DATE, BILL_ALERT_STATUS, MESSAGE) VALUES"
                        + "(?,?,?,?)";
                preparedStatement = conn.prepareStatement(insertTableSQL);

                preparedStatement.setInt(1, Integer.valueOf(id));
                preparedStatement.setTimestamp(2, getCurrentTimeStamp());
                preparedStatement.setInt(3, 0);
                preparedStatement.setString(4, "YOU have ni bill todayyy");

                // execute insert SQL stetement
                preparedStatement.executeUpdate();

                System.out.println("Record is inserted into BILL_ALERTS table!");
                conn.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    conn.close();
                    preparedStatement.close();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deleteBillAlertMessages(String id) {

        DataStore data = new DataStore();
        Connection conn = data.kplcconnect();
        String query = "delete from BILL_ALERTS where BILL_ALERT_ID=" + id;

        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery(query);
            conn.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void startSendSMS() {
        DataFactory df = new DataFactory();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            String name = df.getFirstName() + " " + df.getLastName();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            new Thread(new Runnable() {
                public void run() {
                    SendSMS sms = new SendSMS();
                    sms.sendSMS(name);
                }
            }).start();
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("TIME: " + elapsedTime);

    }

    private static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

}
