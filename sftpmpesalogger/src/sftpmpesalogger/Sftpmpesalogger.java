/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sftpmpesalogger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.LocalDate;

/**
 *
 * @author coolie
 */
public class Sftpmpesalogger {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Sftpmpesalogger logger = new Sftpmpesalogger();
        DateTime da = new DateTime().minusDays(1);
        DateTimeFormatter ofa = DateTimeFormat.forPattern("yyyyMMdd");
        String dirDate = ofa.print(da);
        //String command = "lftp sftp://SP100258:Nairobi\\#21@192.168.9.37:15423  -e \"mirror /working /opt/applications/sftpmpesalogger/sftpgets;bye\"";
        String command = "lftp sftp://SP100258:Kenya\\$321@192.168.9.37:15423  -e \"mirror /working /opt/applications/sftpmpesalogger/sftpgets;bye\"";
        System.out.println("Command:=>> " + command);
        logger.terminalCMD(command);
        
        

        /*LocalDate endDate = LocalDate.now().minusDays(1); //get current date
        LocalDate startDate = endDate.minusDays(7); //minus 7 days to current date

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        for (LocalDate currentdate = startDate;
                currentdate.isBefore(endDate) || currentdate.isEqual(endDate);
                currentdate = currentdate.plusDays(1)) {
            dirDate = ofa.print(currentdate);*/
        
        
        
        
            String csvFile = "/opt/applications/sftpmpesalogger/sftpgets/" + dirDate + "23/Mpesa_501200_" + dirDate + "235959.csv";

            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";
            try {
                System.out.println("File: " + csvFile);
                br = new BufferedReader(new FileReader(csvFile));
                int ind = 1;
                while ((line = br.readLine()) != null) {
                    // use comma as separator
                    String[] cells = line.split(cvsSplitBy);
                    if (ind > 1) {
                        if ((cells.length > 3) && (cells[3].equals("Completed"))) {
                            System.out.println(cells[0] + "||" + cells[1] + "||" + cells[2] + "||" + cells[3] + "||" + cells[4] + "||" + cells[5]);
                            String datee = cells[1];
                            String mpesa_code = cells[0];
                            String details = cells[2];
                            if ((!details.startsWith("Pay Utility Reversal")) && (!details.startsWith("Utility Account")) && (!details.startsWith("Organization Settlement")) && (!details.startsWith("Real Time"))) {
                                String accsplit[] = details.split(" Acc. ");
                                String osplit[] = accsplit[0].split(" - ");
                                String account = accsplit[1];
                                String msisdn = osplit[0].replaceAll("Pay Bill ", "").replaceAll("Online ", "").replaceAll("from ", "");
                                String amount = cells[5];
                                String mpesa_sender = osplit[1];
                                System.out.println("Logging:" + datee + "||" + mpesa_code + "||" + account + "||" + msisdn + "||" + amount + "||" + mpesa_sender);
                                if (cells[4].equals("0")) {
                                    String res = logger.sendPayment(datee, mpesa_code, account, msisdn, amount, mpesa_sender);
                                    System.out.println("Payments Response:" + res);
                                }
                            }
                        }
                    }
                    ind++;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        //}
    }

    private void terminalCMD(String command) {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        pb.redirectErrorStream(true);
        try {
            Process shell = pb.start();
        } catch (Exception exp) {
            System.out.println("Exception--->" + exp.getMessage());
        }
        // close the stream

        System.out.println("Done---->");
    }

    public String sendReq(String param) throws IOException {
        String request = " http://172.27.116.21:8084/VendIT/Render?";
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(param.getBytes().length));
        connection.setUseCaches(false);

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(param);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        out.close();
        connection.disconnect();
        return response.toString();
    }

    public String sendPayment(String transtime, String transID, String refNO, String msisdn, String amount, String name) {
        String url = "id=3630&orig=MPESA&tstamp=" + transtime + "&mpesa_code=" + transID + "&mpesa_acc=" + refNO + "&mpesa_msisdn=" + msisdn + "&mpesa_amt=" + amount + "&mpesa_sender=" + name + "&mpesa_verify=onicha";
        String res;
        try {
            res = sendReq(url);
        } catch (IOException e) {
            e.printStackTrace();
            res = "FAILED";
        }
        return res;
    }
}
