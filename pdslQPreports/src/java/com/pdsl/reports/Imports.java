/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdsl.reports;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class Imports {

    public static void main(String[] args) {
        /*String csvFile = "/home/coolie/Desktop/prepaid.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        DataStore data = new DataStore();

        try {
            br = new BufferedReader(new FileReader(csvFile));
            int ind = 1;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] cells = line.split(cvsSplitBy);
                if (ind > 1) {
                    String otime = cells[0].replace("\"", "");
                    String status = cells[2].replace("\"", "");
                    String severity = cells[3].replace("\"", "");
                    String duration = cells[4].replace("\"", "");

                    System.out.println(otime + "|" + status + "|" + severity + "|" + duration);

                    String hduration = "0";
                    String mduration = "0";
                    String sduration = "0";

                    if (duration.contains("h")) {
                        if (duration.contains("m")) {
                            if (duration.contains("s")) {
                                System.out.println("Comparison 1");
                                String[] dsplit = duration.split(" ");
                                hduration = dsplit[0].replaceAll("h", "");
                                mduration = dsplit[1].replaceAll("m", "");
                                sduration = dsplit[2].replaceAll("s", "");
                            }
                        }
                    }
                    if (!duration.contains("h")) {
                        if (duration.contains("m")) {
                            if (duration.contains("s")) {
                                System.out.println("Comparison 2");
                                String[] dsplit = duration.split(" ");
                                mduration = dsplit[0].replaceAll("m", "");
                                sduration = dsplit[1].replaceAll("s", "");
                            }
                        }
                    }
                    if (duration.contains("h")) {
                        if (!duration.contains("m")) {
                            if (!duration.contains("s")) {
                                System.out.println("Comparison 3");
                                hduration = duration.replaceAll("h", "");
                            }
                        }
                    }
                    if (duration.contains("m")) {
                        if (!duration.contains("h")) {
                            if (!duration.contains("s")) {
                                System.out.println("Comparison 4");
                                mduration = duration.replaceAll("m", "");
                            }
                        }
                    }
                    if (duration.contains("s")) {
                        if (!duration.contains("m")) {
                            if (!duration.contains("h")) {
                                System.out.println("Comparison 5");
                                sduration = duration.replaceAll("s", "");
                            }
                        }
                    }
                    //System.out.println("Date: " + otime);
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                    DateTime sdate = formatter.parseDateTime(otime);
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                    String str = fmt.print(sdate);

                    //System.out.println("TableDate: " + str);
                    data.createTables(str.replaceAll("-", ""));

                    String values = "insert into evg" + str.replaceAll("-", "") + "(otime,status,severity,duration,hduration,mduration,sduration) values (?,?,?,?,?,?,?)";
                    try {
                        Connection con = data.connect();
                        PreparedStatement prep = con.prepareStatement(values);
                        prep.setString(1, otime);
                        prep.setString(2, status);
                        prep.setString(3, severity);
                        prep.setString(4, duration);
                        prep.setString(5, hduration);
                        prep.setString(6, mduration);
                        prep.setString(7, sduration);
                        prep.execute();
                        prep.close();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Imports.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                ind++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/

        //==========================Commercial Reports=================================
        String csvFile = "/home/coolie/Desktop/April/SafePay Dibon Prepaid April 2018.xls";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = "	";
        DataStore data = new DataStore();

        try {
            br = new BufferedReader(new FileReader(csvFile));
            int ind = 1;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] cells = line.split(cvsSplitBy);
                if (ind > 1) {
                    String datesold = cells[0].replace("\"", "");
                    String retailer = cells[1].replace("\"", "");
                    String txtype = cells[2].replace("\"", "");
                    String reference = cells[3].replace("\"", "");
                    String retailvalue = cells[4].replace("\"", "");
                    String mancovalue = cells[5].replace("\"", "");
                    String commissionvalue = cells[6].replace("\"", "");

                    if (retailvalue.equals("")) {
                        retailvalue = "0.0";
                    }
                    if (mancovalue.equals("")) {
                        mancovalue = "0.0";
                    }
                    if (commissionvalue.equals("")) {
                        commissionvalue = "0.0";
                    }

                    System.out.println(datesold + "|" + retailer + "|" + txtype + "|" + reference + "|" + retailvalue + "|" + mancovalue + "|" + commissionvalue);

                    //System.out.println("Date: " + otime);
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm a");
                    DateTime sdate = formatter.parseDateTime(datesold);
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");
                    String str = fmt.print(sdate);
                    data.createTables(retailer + "<<" + str.replaceAll("-", ""));

                    Connection con = data.connect();
                    String rvalues = "insert into retailers(retailer) values (?)";
                    try {
                        PreparedStatement rprep = con.prepareStatement(rvalues);
                        rprep.setString(1, retailer);
                        rprep.execute();
                        rprep.close();
                    } catch (SQLException ex) {
                        //Logger.getLogger(Imports.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     try {
                        String values = "insert into commercialTx" + retailer + str.replaceAll("-", "") + "(datesold,retailer,txtype,reference,retailvalue,mancovalue,commissionvalue) values (?,?,?,?,?,?,?)";

                        PreparedStatement prep = con.prepareStatement(values);
                        prep.setString(1, datesold);
                        prep.setString(2, retailer);
                        prep.setString(3, txtype);
                        prep.setString(4, reference);
                        prep.setString(5, retailvalue);
                        prep.setString(6, mancovalue);
                        prep.setString(7, commissionvalue);
                        prep.execute();
                        prep.close();
                    } catch (SQLException ex) {
                        //Logger.getLogger(Imports.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            con.close();
                        } catch (SQLException ef) {
                        }
                    }
                }
                ind++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String checkTableExist(String tablpr) {
        String res = "none";
        DataStore data = new DataStore();
        try {
            Connection con = data.connect();
            String query = "SELECT `id` FROM `commercialTx" + tablpr + "` LIMIT 1";
            System.out.println(query);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    res = "found";
                }
            }
            con.close();
        } catch (SQLException ex) {
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

}
