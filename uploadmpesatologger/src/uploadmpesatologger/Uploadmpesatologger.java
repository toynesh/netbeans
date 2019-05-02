/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uploadmpesatologger;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class Uploadmpesatologger {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /*DateTimeFormatter ofa = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime startDate = ofa.parseDateTime("20180902");
        DateTime endDate = ofa.parseDateTime("20180909");
        for (DateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            String dirDate = ofa.print(date);
            System.out.println("Date:=>> " + dirDate);
            String command = "curl -T /opt/applications/sftpmpesalogger/sftpgets/" + dirDate + "23/Mpesa_501200_" + dirDate + "235959.csv ftp://197.248.61.162/mpesa/ --user ipay:pdslipay!";
            System.out.println("Command:=>> " + command);
            Uploadmpesatologger up = new Uploadmpesatologger();
            up.terminalCMD(command);
        }*/
        
        DateTimeFormatter ofa = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime da = new DateTime().minusDays(2);
        //DateTime da = ofa.parseDateTime("20180909");
        String dirDate = ofa.print(da);
        
        
        
        /*LocalDate endDate = LocalDate.now().minusDays(1); //get current date
        LocalDate startDate = endDate.minusDays(7); //minus 7 days to current date

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        for (LocalDate currentdate = startDate;
                currentdate.isBefore(endDate) || currentdate.isEqual(endDate);
                currentdate = currentdate.plusDays(1)) {
            dirDate = ofa.print(currentdate);*/
        
        
        
        String command = "curl -T /opt/applications/sftpmpesalogger/sftpgets/" + dirDate + "23/Mpesa_501200_" + dirDate + "235959.csv ftp://197.248.61.162/mpesa/ --user ipay:pdslipay!";
        System.out.println("Command:=>> " + command);
        String command2 = "curl -T /opt/applications/sftpmpesalogger/sftpgets/" + dirDate + "23/Mpesa_501201_" + dirDate + "235959.csv ftp://197.248.61.162/mpesa/ --user ipay:pdslipay!";
        System.out.println("Command:=>> " + command2);
        Uploadmpesatologger up = new Uploadmpesatologger();
        up.terminalCMD(command);
        up.terminalCMD(command2);
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
}
