/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpesalogger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class Mpesalogger {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DataStore data = new DataStore();
        /*try {
            //test data 
            //LocalDate endDate = LocalDate.now(); //get current date
            //LocalDate startDate = endDate.minusDays(56); //minus 56 days to current date

            //System.out.println("startDate : " + startDate);
            //System.out.println("endDate : " + endDate);
            DateTime dt = new DateTime().minusDays(1);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            String currDate = fmt.print(dt);
            //System.out.println("Enter Date Like yyyy-MM-dd: ");
            //Scanner scanner = new Scanner(System.in);
            //String currDate = scanner.nextLine();
            /*for (LocalDate currentdate = startDate;
                    currentdate.isBefore(endDate) || currentdate.isEqual(endDate);
                    currentdate = currentdate.plusDays(1)) {
                String currDate = currentdate.toString();*/
 /*System.out.println("Logging Date: " + currDate);
                data.createTables(currDate.replaceAll("-", ""));
                Connection con = data.connect();
                List li = retrieveTransactionsByDate("254726402200", currDate, "Trnidard098");
                Iterator it = li.iterator();
                while (it.hasNext()) {
                    Tblmpesa table = (Tblmpesa) it.next();
                    System.out.println("************MPESA************");
                    System.out.println(new StringBuilder().append("Message Id\t\t ").append(table.getMessageId()).toString());
                    System.out.println(new StringBuilder().append("Account Number \t\t ").append(table.getMpesaAccountnumber()).toString());
                    System.out.println(new StringBuilder().append("Mpesa Allocated \t\t ").append(table.getMpesaAllocated()).toString());
                    System.out.println(new StringBuilder().append("Original text\t\t ").append(table.getMpesaOriginaltext()).toString());
                    System.out.println(new StringBuilder().append("Sender Mobile\t\t").append(table.getMpesaSendermobile()).toString());
                    System.out.println(new StringBuilder().append("Sender Name\t\t ").append(table.getMpesaSendername()).toString());
                    System.out.println(new StringBuilder().append("Mpesa Terminal\t\t").append(table.getMpesaTerminal()).toString());
                    System.out.println(new StringBuilder().append("Transaction Code\t\t ").append(table.getMpesaTxcode()).toString());
                    System.out.println(new StringBuilder().append("Mpesa Amount\t\t").append(table.getMpesaAmount()).toString());
                    System.out.println(new StringBuilder().append("Time\t\t").append(table.getMpesaDatetime().toString()).toString());
                    System.out.println(new StringBuilder().append("Mpesa Id\t\t").append(table.getMpesaId()).toString());

                    String values = "insert into mpesa" + currDate.replaceAll("-", "") + "(MessageId,MpesaAccountnumber,MpesaAllocated,MpesaOriginaltext,MpesaSendermobile,MpesaSendername,MpesaTerminal,MpesaTxcode,MpesaAmount,MpesaDatetime,MpesaId) values (?,?,?,?,?,?,?,?,?,?,?)";
                    try {
                        PreparedStatement prep = con.prepareStatement(values);
                        prep.setString(1, table.getMessageId().toString());
                        prep.setString(2, table.getMpesaAccountnumber());
                        prep.setString(3, table.getMpesaAllocated().toString());
                        prep.setString(4, table.getMpesaOriginaltext());
                        prep.setString(5, table.getMpesaSendermobile());
                        prep.setString(6, table.getMpesaSendername());
                        prep.setString(7, table.getMpesaTerminal());
                        prep.setString(8, table.getMpesaTxcode());
                        prep.setString(9, table.getMpesaAmount().toString());
                        prep.setString(10, table.getMpesaDatetime().toString());
                        prep.setString(11, table.getMpesaId().toString());
                        prep.execute();
                        prep.close();
                    } catch (SQLException ex) {
                        //Logger.getLogger(Mpesalogger.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //String res = data.sendPayment(currDate, table.getMpesaTxcode(), table.getMpesaAccountnumber(), table.getMpesaSendermobile(), table.getMpesaAmount().toString(), table.getMpesaSendername());
                    //System.out.println("Payments Response With Amount:" + res);
                }
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Mpesalogger.class.getName()).log(Level.SEVERE, null, ex);
                }
            //}
        } catch (InvalidCredentialsFault_Exception ex) {
            Logger.getLogger(Mpesalogger.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        DateTimeFormatter ofa = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime date = new DateTime().minusDays(2);
        //DateTime startDate = ofa.parseDateTime("20190119");
        //DateTime endDate = ofa.parseDateTime("20190218");
        //for (DateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            String dirDate = ofa.print(date);
            System.out.println("Date:=>> " + dirDate);

            String csvFile = "/home/ipay/ftp/mpesa/Mpesa_501200_" + dirDate + "235959.csv";
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";
            try {
                System.out.println("File: " + csvFile);
                br = new BufferedReader(new FileReader(csvFile));
                int ind = 1;
                data.createTables(dirDate);
                Connection con = data.connect();
                while ((line = br.readLine()) != null) {
                    // use comma as separator
                    String[] cells = line.split(cvsSplitBy);
                    if (ind > 1) {
                        if ((cells.length > 3) && (cells[3].equals("Completed")) && (!cells[0].isEmpty())) {
                            System.out.println(cells[0] + "||" + cells[1] + "||" + cells[2] + "||" + cells[3] + "||" + cells[4] + "||" + cells[5]);
                            String datee = cells[1];
                            String mpesa_code = cells[0];
                            String details = cells[2];
                            if ((!details.startsWith("Pay Utility Reversal")) && (!details.startsWith("Utility Account")) && (!details.startsWith("Organization Settlement")) && (!details.startsWith("Real Time")) && (!details.equals("DETAILS"))) {
                                String accsplit[] = details.split(" Acc. ");
                                String osplit[] = accsplit[0].split(" - ");
                                String account = accsplit[1];
                                String msisdn = osplit[0].replaceAll("Pay Bill ", "").replaceAll("Online ", "").replaceAll("from ", "");
                                String amount = cells[5];
                                String mpesa_sender = osplit[1];
                                System.out.println("Logging:" + datee + "||" + mpesa_code + "||" + account + "||" + msisdn + "||" + amount + "||" + mpesa_sender);
                                if (cells[4].equals("0")) {
                                    String values = "insert into mpesa" + dirDate + "(MessageId,MpesaAccountnumber,MpesaAllocated,MpesaOriginaltext,MpesaSendermobile,MpesaSendername,MpesaTerminal,MpesaTxcode,MpesaAmount,MpesaDatetime,MpesaId) values (?,?,?,?,?,?,?,?,?,?,?)";
                                    try {
                                        PreparedStatement prep = con.prepareStatement(values);
                                        prep.setString(1, mpesa_code);
                                        prep.setString(2, account);
                                        prep.setString(3, "0");
                                        prep.setString(4, null);
                                        prep.setString(5, msisdn);
                                        prep.setString(6, mpesa_sender);
                                        prep.setString(7, "501200");
                                        prep.setString(8, mpesa_code);
                                        prep.setString(9, amount);
                                        prep.setString(10, datee);
                                        prep.setString(11, mpesa_code);
                                        prep.execute();
                                        prep.close();
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Mpesalogger.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                    }
                    ind++;
                }
                con.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException ex) {
                Logger.getLogger(Mpesalogger.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            String csvFile2 = "/home/ipay/ftp/mpesa/Mpesa_501201_" + dirDate + "235959.csv";
            BufferedReader br2 = null;
            String line2 = "";
            String cvsSplitBy2 = ",";
            try {
                System.out.println("File: " + csvFile2);
                br2 = new BufferedReader(new FileReader(csvFile2));
                int ind = 1;
                //data.createTables(dirDate);
                Connection con = data.connect();
                while ((line2 = br2.readLine()) != null) {
                    // use comma as separator
                    String[] cells = line2.split(cvsSplitBy2);
                    if (ind > 1) {
                        if ((cells.length > 3) && (cells[3].equals("Completed")) && (!cells[0].isEmpty())) {
                            System.out.println(cells[0] + "||" + cells[1] + "||" + cells[2] + "||" + cells[3] + "||" + cells[4] + "||" + cells[5]);
                            String datee = cells[1];
                            String mpesa_code = cells[0];
                            String details = cells[2];
                            if ((!details.startsWith("Pay Utility Reversal")) && (!details.startsWith("Utility Account")) && (!details.startsWith("Organization Settlement")) && (!details.startsWith("Real Time")) && (!details.equals("DETAILS"))) {
                                String accsplit[] = details.split(" Acc. ");
                                String osplit[] = accsplit[0].split(" - ");
                                String account = accsplit[1];
                                String msisdn = osplit[0].replaceAll("Pay Bill ", "").replaceAll("Online ", "").replaceAll("from ", "");
                                String amount = cells[5];
                                String mpesa_sender = osplit[1];
                                System.out.println("Logging:" + datee + "||" + mpesa_code + "||" + account + "||" + msisdn + "||" + amount + "||" + mpesa_sender);
                                if (cells[4].equals("0")) {
                                    String values = "insert into mpesa" + dirDate + "(MessageId,MpesaAccountnumber,MpesaAllocated,MpesaOriginaltext,MpesaSendermobile,MpesaSendername,MpesaTerminal,MpesaTxcode,MpesaAmount,MpesaDatetime,MpesaId) values (?,?,?,?,?,?,?,?,?,?,?)";
                                    try {
                                        PreparedStatement prep = con.prepareStatement(values);
                                        prep.setString(1, mpesa_code);
                                        prep.setString(2, account);
                                        prep.setString(3, "0");
                                        prep.setString(4, null);
                                        prep.setString(5, msisdn);
                                        prep.setString(6, mpesa_sender);
                                        prep.setString(7, "501201");
                                        prep.setString(8, mpesa_code);
                                        prep.setString(9, amount);
                                        prep.setString(10, datee);
                                        prep.setString(11, mpesa_code);
                                        prep.execute();
                                        prep.close();
                                    } catch (SQLException ex) {
                                        //Logger.getLogger(Mpesalogger.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                    }
                    ind++;
                }
                con.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException ex) {
                Logger.getLogger(Mpesalogger.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (br2 != null) {
                    try {
                        br2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        //}
    }

    /*private static List<Tblmpesa> retrieveTransactionsByDate(String terminalMsisdn, String trxDate, String password) throws InvalidCredentialsFault_Exception {
        IpnWebRetrieval service = new IpnWebRetrieval();
        RetrieveData port = service.getRetrieveDataPort();
        return port.retrieveTransactionsByDate(terminalMsisdn, trxDate, password);
    }

    public static void disableSSLVerification() {
        TrustManager[] trustAllCerts = {new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    static {
        disableSSLVerification();
    }*/
}
