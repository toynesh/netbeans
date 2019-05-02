/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tryipay;

import com.pdslkenya.reconnectus.Ipay;

/**
 *
 * @author coolie
 */
public class Tryipay {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String results = "NONE";
        try {
            results = new Ipay().sendRequest("20180804215934", "254728064120", "MH50VYHCX8test", "JULIUS KARIUKI", "1", "20180804215934", "234");
        } catch (Exception x) {
        }
        System.out.println("Res: " + results);
    }

}
