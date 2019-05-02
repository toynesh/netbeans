/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkpending;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import jxl.*;

/**
 *
 * @author coolie
 */
public class Checkpending {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            List<String> logmpesaexcel = new ArrayList<>();
            List<String> mpesa = new ArrayList<>();

            File f = new File("/home/coolie/Desktop/VendITFeb2018March/mpesalib/27th.xls");
            Workbook wb = Workbook.getWorkbook(f);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();

            for (int j = 6; j < row; j++) {
                Cell info = s.getCell(0, j);
                //String[] ms = info.getContents().split(" - ");
                // String msisdn = ms[0];
                System.out.println("Adding:" + info.getContents());
                logmpesaexcel.add(info.getContents());
            }

            DataStore data = new DataStore();
            Connection con = data.connect();
            String query2 = "SELECT `mpesa_code` FROM `mpesa` WHERE  `timestamp` BETWEEN '2018-02-27 00:00:00'AND '2018-02-27 23:59:59'";
            //String query2 = "SELECT `mpesa_code`,`mpesa_msisdn`,`mpesa_amt`,`mpesa_acc` FROM (SELECT * FROM `mpesa` WHERE  `mpesa_amt` >0 AND (`timestamp` BETWEEN '2018-03-12 00:00:00'AND '2018-03-12 23:59:59')) AS M WHERE `message` LIKE 'KPLC Mtr No:%' OR `message` LIKE 'PAID KSH:%'";
            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery(query2);
            while (rs2.next()) {
                System.out.println("MpesaT: " + rs2.getString(1));
                mpesa.add(rs2.getString(1));
                /*if (!logmpesaexcel.contains(rs2.getString(1))) {
                    if (!rs2.getString(1).contains("EQU_")) {
                    System.out.println(rs2.getString(2) + " , " + rs2.getString(3) + " , " + rs2.getString(1) + " , " + rs2.getString(4));
                    }
                    }*/
            }
            //System.out.println("Mesa Array Size: " + mpesa.size());
            con.close();

            File f2 = new File("/home/coolie/Desktop/VendITFeb2018March/OpenReports/February2018/PrePaid/27th.xls");
            Workbook wb2 = Workbook.getWorkbook(f2);
            Sheet s2 = wb2.getSheet(0);
            int row2 = s2.getRows();
            for (int j2 = 1; j2 < row2; j2++) {
                Cell phone2 = s2.getCell(2, j2);
                Cell amount = s2.getCell(4, j2);
                Cell ref = s2.getCell(8, j2);
                Cell meter = s2.getCell(3, j2);
                String msisdn2 = phone2.getContents();
                String amt = amount.getContents();
                String mpesaref = ref.getContents();
                String acc = meter.getContents();
                //System.out.println("Logging:"+mpesaref);
                if (!logmpesaexcel.contains(mpesaref)) {
                    if (!mpesa.contains(mpesaref)) {
                        if (!mpesaref.contains("EQU_")) {
                            if (!amt.equals("0")) {
                                System.out.println(msisdn2 + " : " + amt + " : " + mpesaref + " : " + acc);
                            }
                        }
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
