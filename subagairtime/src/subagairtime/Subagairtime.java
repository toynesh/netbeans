/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subagairtime;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author coolie
 */
public class Subagairtime {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            ProcessRequest rq = new ProcessRequest();
            //rq._getPrePaidDetailsAcc("14140057598", "PDSLEQUITY", "00001");
            rq._airTimeTokenBuy("safaricom", "saf_20", "PDSLEQUITY", "00001");
            //Thread.sleep(5000);//5secs
            //rq._vendRev(rq.getORef(), "2018-05-01 07:12:03 +0300", 1, "PDSLEQUITY", "00200");
            //Thread.sleep(5000);//5secs
             //rq._vendRev(rq.getORef(), "2018-05-01 07:12:03 +0300", 1, "PDSLEQUITY", "00300");
            //Thread.sleep(5000);//5secs
            //rq._vendRev(rq.getORef(), "2018-05-01 07:12:03 +0300", 2, "PDSLEQUITY", "00001");
            
            //KEYCHANGE*/
            //Thread.sleep(5000);//5secs
            //rq._vendRev(rq.getORef(), "2018-05-01 07:12:03 +0300", 2, "PDSLEQUITY", "00700");
        } catch (Exception ex) {
            Logger.getLogger(Subagairtime.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
