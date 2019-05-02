/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gmsmodem;

/**
 *
 * @author julius
 */
public class JavaLibPath {
    public static void main(String[] args) {
        String libPathProperty = System.getProperty("java.library.path");
        System.out.println(libPathProperty);
    }
}
