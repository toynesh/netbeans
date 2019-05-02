/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chclient;

/**
 *
 * @author coolie
 */
public class Chclient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        ClientMain cm = new ClientMain();
        try {
            //cm.main(new String[]{args[0], args[1]});
            cm.main(new String[]{"HTTP", "1"});
        } catch (ArrayIndexOutOfBoundsException exception) {
            cm.main(new String[]{args[0]});
        }        
    }

}
