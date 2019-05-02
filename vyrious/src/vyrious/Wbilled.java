/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vyrious;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 *
 * @author coolie
 */
public class Wbilled {

    public static void main(String[] args) {
        DataStore data = new DataStore();

        List<String> umsisdn = new ArrayList<>();
        try {
            File f = new File("/home/coolie/Desktop/in.xls");
            Workbook wb = Workbook.getWorkbook(f);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            int col = s.getColumns();
            for (int j = 0; j < row; j++) {
                Cell pcell = s.getCell(3, j);
                String msisdn = pcell.getContents();
                umsisdn.add(msisdn);
            }
            System.out.println("Done adding INsms" +umsisdn.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            File f = new File("/home/coolie/Desktop/qsms.xls");
            Workbook wb = Workbook.getWorkbook(f);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            int col = s.getColumns();
            int wncont = 0;
            PrintWriter writer = new PrintWriter("/home/coolie/Desktop/wmultibilled.csv", "UTF-8");
            List<String> imsisdn = new ArrayList<>();
            for (int j = 0; j < row; j++) {
                Cell rid = s.getCell(0, j);
                Cell pdate = s.getCell(1, j);
                Cell psmsc = s.getCell(2, j);
                Cell pcell = s.getCell(3, j);
                Cell pscode = s.getCell(4, j);
                Cell incell = s.getCell(5, j);
                Cell podate = s.getCell(6, j);
                Cell pmsg = s.getCell(7, j);
                Cell pmid = s.getCell(8, j);
                Cell pres = s.getCell(9, j);
                Cell pdlrstatus = s.getCell(10, j);

                String id = rid.getContents();
                String date = pdate.getContents();
                String smsc = psmsc.getContents();
                String msisdn = pcell.getContents();
                String shortcode = pscode.getContents();
                String index = incell.getContents();
                String outdate = podate.getContents();
                String message = pmsg.getContents();
                String msgid = pmid.getContents();
                String sres = pres.getContents();
                String dlr = pdlrstatus.getContents();
                //System.out.println(msisdn+ " : "+index);
                //String snd_txt = data.getMessage(index);
                //System.out.println(j + ": Response: " + snd_txt);
                //if (!snd_txt.contains("the index number does not exist")) {
                if (umsisdn.contains(msisdn)) {
                    String snd_txt = data.getMessage(index);
                    System.out.println(j + ": Response: " + snd_txt);
                    if (!snd_txt.contains("the index number does not exist")) {
                        if (!imsisdn.contains(msisdn)) {
                            imsisdn.add(msisdn);
                            //writer.println(id+","+date+","+smsc+","+msisdn+","+shortcode+","+index+","+outdate+","+message+","+msgid+","+sres+","+dlr);
                            //wncont++;
                        } else {
                            writer.println(id + "," + date + "," + smsc + "," + msisdn + "," + shortcode + "," + index + "," + outdate + "," + message + "," + msgid + "," + sres + "," + dlr);
                            wncont++;
                        }
                    }
                }
            }
            writer.close();
            System.out.println("Total Count: " + wncont);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
