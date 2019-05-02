/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vending;

import static java.lang.Double.parseDouble;
import java.math.BigDecimal;

/**
 *
 * @author julius
 */
public class Tester {
    public static void main(String[] args){
        BigDecimal payamt = new BigDecimal("4719.02");
        payamt = payamt.multiply(new BigDecimal("100"));
        String amt = payamt.toString();
        String ipayamt = amt.substring(0, amt.length() - 2);
        //String ipayamt = amt;
        ipayamt= ipayamt.replace(".", "");
        System.out.println(amt);
        System.out.println(ipayamt);

    }
}
