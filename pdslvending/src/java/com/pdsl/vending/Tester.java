/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.vending;

import static java.lang.Double.parseDouble;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *
 * @author julius
 */
public class Tester {

    public static void main(String[] args) {
        String amount = "100.07";
        BigDecimal amt2 = new BigDecimal(amount);
        amount = toCents(amt2);
        String ipayamt=new DecimalFormat("#").format(parseDouble(amount));        
        System.out.println(amount);
        System.out.println("Ipay Amount"+ipayamt);

    }
    public static String toCents(BigDecimal amount) {
    	
    	return amount.multiply(new BigDecimal(100)).toString();
    }
}
