/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdslvendingrecon;

import com.pdsl.vending.VendResponse;

/**
 *
 * @author coolie
 */
public class Pdslvendingrecon {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
                com.pdsl.vending.Requests_Service service = new com.pdsl.vending.Requests_Service();
                com.pdsl.vending.Requests port = service.getRequestsPort();
                // TODO initialize WS operation arguments here
                java.lang.String vendorcode = "1001";
                java.lang.String account = " 2232720-01";
                
                // TODO process result here
                VendResponse result = port.postpaidAccountQuery(vendorcode, account);
                //System.out.println(result);
            } catch (Exception ex) {
                // TODO handle custom exceptions here
                System.out.println("Not found");
            }
    }
    
}
