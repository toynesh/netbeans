/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customersmsupdate;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import metro_sdp.Metro_sdp;

/**
 *
 * @author coolie
 */
public class Customersmsupdate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Metro_sdp sdp = new Metro_sdp();

        try {
            File f = new File("/opt/applications/upload/senddata.xls");
            Workbook wb = Workbook.getWorkbook(f);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            int col = s.getColumns();

            for (int j = 1; j < row; j++) {
                Cell phone = s.getCell(2, j);
                Cell code = s.getCell(8, j);
                
                String msisdn = phone.getContents();
                String ref = code.getContents();
                String mesg = "Dear Customer. Sorry for the delay in sending your Token or bill payment update since yesterday. KPLC has scheduled maintenance currently underway and upon system restoration, we will send you outstanding tokens and confirmations.";
                sdp.sendSdpSms(msisdn, mesg, ref, "704307");
            }

        } catch (IOException ex) {
            Logger.getLogger(Customersmsupdate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(Customersmsupdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
