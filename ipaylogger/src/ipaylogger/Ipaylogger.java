/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipaylogger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author coolie
 */
public class Ipaylogger {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        //System.out.println("Enter Date Like yyyy-MM-dd: ");
        //Scanner scanner = new Scanner(System.in);
        //String currDate = scanner.nextLine();
        DateTime da = new DateTime().minusDays(2);
        DateTimeFormatter fa = DateTimeFormat.forPattern("yyyy-MM-dd");
        String currDate = fa.print(da);
        //String currDate = "2018-04-29";
        System.out.println("Mapping: " + currDate);
        Ipaylogger il = new Ipaylogger();
        if (il.checkTableExist(currDate).equals("none")) {
            String INPUT_GZIP_FILE = "/home/ipay/ftp/files/mpesa_" + currDate.replaceAll("-", "") + ".csv.gz";
            String OUTPUT_FILE = "/home/ipay/ftp/files/mpesa_" + currDate.replaceAll("-", "") + ".csv";

            Ipaylogger gZip = new Ipaylogger();
            gZip.gunzipIt(INPUT_GZIP_FILE, OUTPUT_FILE);

            String csvFile = "/home/ipay/ftp/files/mpesa_" + currDate.replaceAll("-", "") + ".csv";
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = "\\|";
            DataStore data = new DataStore();

            try {
                br = new BufferedReader(new FileReader(csvFile));
                int ind = 1;
                while ((line = br.readLine()) != null) {

                    // use comma as separator
                    String[] cells = line.split(cvsSplitBy);
                    if (ind > 1) {
                        String startDate = cells[0];
                        if (!startDate.equals("")) {
                            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                            DateTime sdate = formatter.parseDateTime(startDate);
                            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                            String str = fmt.print(sdate);

                            System.out.println("TableDate: " + str);
                            data.createTables(str.replaceAll("-", ""));
                            try {
                                System.out.println(cells[0] + "|" + cells[1] + "|" + cells[2] + "|" + cells[3] + "|" + cells[4] + "|" + cells[5] + "|" + cells[6] + "|" + cells[7] + "|" + cells[8] + "|" + cells[9]);
                                String values = "insert into ipay" + str.replaceAll("-", "") + "(req_sent,time_completed,phone_num,meter_account,amt,success,res,ref_received,external_ref,token) values (?,?,?,?,?,?,?,?,?,?)";
                                try {
                                    Connection con = data.connect();
                                    PreparedStatement prep = con.prepareStatement(values);
                                    prep.setString(1, cells[0]);
                                    prep.setString(2, cells[1]);
                                    prep.setString(3, cells[2]);
                                    prep.setString(4, cells[3]);
                                    prep.setString(5, cells[4]);
                                    prep.setString(6, cells[5]);
                                    prep.setString(7, cells[6]);
                                    prep.setString(8, cells[7]);
                                    prep.setString(9, cells[8]);
                                    prep.setString(10, cells[9]);
                                    prep.execute();
                                    prep.close();
                                    con.close();
                                } catch (SQLException ex) {
                                    //Logger.getLogger(Ipaylogger.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (Exception exx) {
                                //exx.printStackTrace();
                                //try {
                                System.out.println(cells[0] + "|" + cells[1] + "|" + cells[2] + "|" + cells[3] + "|" + cells[4] + "|" + cells[5] + "|" + cells[6] + "|" + cells[7] + "|" + cells[8]);
                                String values = "insert into ipay" + str.replaceAll("-", "") + "(req_sent,time_completed,phone_num,meter_account,amt,success,res,ref_received,external_ref) values (?,?,?,?,?,?,?,?,?)";
                                try {
                                    Connection con = data.connect();
                                    PreparedStatement prep = con.prepareStatement(values);
                                    prep.setString(1, cells[0]);
                                    prep.setString(2, cells[1]);
                                    prep.setString(3, cells[2]);
                                    prep.setString(4, cells[3]);
                                    prep.setString(5, cells[4]);
                                    prep.setString(6, cells[5]);
                                    prep.setString(7, cells[6]);
                                    prep.setString(8, cells[7]);
                                    prep.setString(9, cells[8]);
                                    prep.execute();
                                    prep.close();
                                    con.close();
                                } catch (SQLException ex) {
                                    //Logger.getLogger(Ipaylogger.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                //} catch (Exception cc) {

                                //}
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
        }else{
            System.out.println("==========Table Already created==========");
        }

    }

    public void gunzipIt(String INPUT_GZIP_FILE, String OUTPUT_FILE) {

        byte[] buffer = new byte[1024];

        try {

            GZIPInputStream gzis
                    = new GZIPInputStream(new FileInputStream(INPUT_GZIP_FILE));

            FileOutputStream out
                    = new FileOutputStream(OUTPUT_FILE);

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            gzis.close();
            out.close();

            System.out.println("Done Unzipping");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String checkTableExist(String currdate) {
        String res = "none";
        DataStore data = new DataStore();
        try {
            Connection con = data.connect();
            String query = "SELECT `id` FROM `ipay" + currdate.replaceAll("-", "") + "` LIMIT 1";
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
