/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdsltextify;

/**
 *
 * @author coolie
 */
public class Pdsltextify {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        RequestsManager req = new RequestsManager();

        try {
            //req.payBillPrePaid("88559966", "100", "TEXTIFY", "00001");
            //req.payBillPostPaid("88559966", "502333-36", "1000", "TEXTIFY", "00001");
            //req.getDetailsAccPostPaid("502333", "TEXTIFY");
            req.getDetailPrepaid("502333", "PdslAirtel");
            // req.payBill("254721178823", "7897844456", "10", "TEXTIFY", "00001");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
