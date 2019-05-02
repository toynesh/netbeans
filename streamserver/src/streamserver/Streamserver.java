/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streamserver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author coolie
 */
public class Streamserver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HttpStaticFileServer serve = new HttpStaticFileServer();
        try {
            // TODO code application logic here
            serve.startServer();
        } catch (Exception ex) {
            Logger.getLogger(Streamserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
