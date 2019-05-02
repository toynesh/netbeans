/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package delayalerts;

import com.pdsl.vending.VendResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author coolie
 */
public class Delayalerts {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DataStore data = new DataStore();
        Sdp sdp = new Sdp();
        PhonePrefixes prfx = new PhonePrefixes();
        String prevStatus = getStatus();
        VendResponse prepres = prepaidMeterQuery("900620", "14140904153");
        System.out.println("Ipay prepaidResponse message: " + prepres.getQueryRes());
        System.out.println("Ipay prepaidResponse Error: " + prepres.getPdslError());
        String prepdslerr = prepres.getPdslError();
        if (prepres.getQueryRes().contains("ServiceNotAvailable")) {
            if (prevStatus.equals("on")) {
                try {
                    Connection con = data.connectvendit();
                    String query = "select phone, login, notification from user where notification like 'yes' ";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        if (prfx.checkPhonePrefix(rs.getString(1)).equals("SAFARICOM")) {
                            sdp.sendSMS(rs.getString(1), "There is a prepaid system delay from KPLC", "DelayNotifys", "704307", "http://197.248.9.1005:8084/VendITNotification/SmsNotificationService");
                        } else if (prfx.checkPhonePrefix(rs.getString(1)).equals("AIRTELRX")) {
                            AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", rs.getString(1), "PdslKeT2", "VENDIT", "0", "There is a system delay from KPLC", "pdsl@999");
                            esdp.submitMessage();
                        } else {
                            EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(rs.getString(1)), "pdsl219", "pdsl219", rs.getString(1), "There is a system delay from KPLC", "VendIT");
                            esdp.submitMessage();
                        }
                    }
                    con.close();
                } catch (Exception ex) {
                }
                setStatus("off");

            }
        } else {
            if (prevStatus.equals("off")) {
                try {
                    Connection con = data.connectvendit();
                    String query = "select phone, login, notification from user where notification like 'yes' ";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        if (prfx.checkPhonePrefix(rs.getString(1)).equals("SAFARICOM")) {
                            sdp.sendSMS(rs.getString(1), "The prepaid delay/timeout is over", "DelayNotifys", "704307", "http://197.248.9.1005:8084/VendITNotification/SmsNotificationService");
                        } else if (prfx.checkPhonePrefix(rs.getString(1)).equals("AIRTELRX")) {
                            AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", rs.getString(1), "PdslKeT2", "VENDIT", "0", "The prepaid delay/timeout is over", "pdsl@999");
                            esdp.submitMessage();
                        } else {
                            EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(rs.getString(1)), "pdsl219", "pdsl219", rs.getString(1), "The prepaid delay/timeout is over", "VendIT");
                            esdp.submitMessage();
                        }
                    }
                    con.close();
                } catch (Exception ex) {
                }
                setStatus("on");
            }
        }
        VendResponse postpres = postpaidAccountQuery("900620", "36388668");
        System.out.println("Ipay postpaidResponse message: " + postpres.getQueryRes());
        System.out.println("Ipay postpaidResponse Error: " + postpres.getPdslError());
        String postpdslerr = postpres.getPdslError();

        if (null != postpdslerr) {
            if (postpdslerr.contains("ServiceNotAvailable")) {
                if (prevStatus.equals("on")) {
                    try {
                        Connection con = data.connectvendit();
                        String query = "select phone, login, notification from user where notification like 'yes' ";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        while (rs.next()) {
                            if (prfx.checkPhonePrefix(rs.getString(1)).equals("SAFARICOM")) {
                                sdp.sendSMS(rs.getString(1), "There is a postpaid system delay from KPLC", "DelayNotifys", "704307", "http://197.248.9.1005:8084/VendITNotification/SmsNotificationService");
                            } else if (prfx.checkPhonePrefix(rs.getString(1)).equals("AIRTELRX")) {
                                AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", rs.getString(1), "PdslKeT2", "VENDIT", "0", "There is a postpaid system delay from KPLC", "pdsl@999");
                                esdp.submitMessage();
                            } else {
                                EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(rs.getString(1)), "pdsl219", "pdsl219", rs.getString(1), "There is a postpaid system delay from KPLC", "VendIT");
                                esdp.submitMessage();
                            }
                        }
                        con.close();
                    } catch (Exception ex) {
                    }
                    setStatus("poff");

                }
            }
        } else {
            if (prevStatus.equals("poff")) {
                try {
                    Connection con = data.connectvendit();
                    String query = "select phone, login, notification from user where notification like 'yes' ";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        if (prfx.checkPhonePrefix(rs.getString(1)).equals("SAFARICOM")) {
                            sdp.sendSMS(rs.getString(1), "The postpaid delay/timeout is over", "DelayNotifys", "704307", "http://197.248.9.1005:8084/VendITNotification/SmsNotificationService");
                        } else if (prfx.checkPhonePrefix(rs.getString(1)).equals("AIRTELRX")) {
                            AirtelSMS esdp = new AirtelSMS("SMSSubmitReq", rs.getString(1), "PdslKeT2", "VENDIT", "0", "The postpaid delay/timeout is over", "pdsl@999");
                            esdp.submitMessage();
                        } else {
                            EquitelSMS esdp = new EquitelSMS("172.27.116.22", prfx.checkPhonePrefix(rs.getString(1)), "pdsl219", "pdsl219", rs.getString(1), "The postpaid delay/timeout is over", "VendIT");
                            esdp.submitMessage();
                        }
                    }
                    con.close();
                } catch (Exception ex) {
                }
                setStatus("on");
            }
        }

    }

    private static VendResponse prepaidMeterQuery(java.lang.String vendorcode, java.lang.String meter) {
        com.pdsl.vending.Requests_Service service = new com.pdsl.vending.Requests_Service();
        com.pdsl.vending.Requests port = service.getRequestsPort();
        return port.prepaidMeterQuery(vendorcode, meter);
    }

    private static VendResponse postpaidAccountQuery(java.lang.String vendorcode, java.lang.String account) {
        com.pdsl.vending.Requests_Service service = new com.pdsl.vending.Requests_Service();
        com.pdsl.vending.Requests port = service.getRequestsPort();
        return port.postpaidAccountQuery(vendorcode, account);
    }

    public static String getStatus() {

        String stt = "";
        try {
            FileReader reader = new FileReader("/home/julius/delaystatus.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            stt = bufferedReader.readLine();
            reader.close();

        } catch (IOException e) {
            //setStatus("off");
            e.printStackTrace();
        }
        return stt;
    }

    public static void setStatus(String stt) {
        try {
            FileWriter writer = new FileWriter("/home/julius/delaystatus.txt");
            writer.write(stt);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
