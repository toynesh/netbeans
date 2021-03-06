/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subagtables;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author juliuskun
 */
public class DataStore {
    public Connection conn = null;
    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/pdslvending";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");

            this.conn = conn;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables(String tdate) {
        try {
            Connection conn = connect();
            //serviceid = "6014702000147264";
            String users = "create table if not exists users(id INT NOT NULL AUTO_INCREMENT, full_names varchar(200), uname varchar(200), upass varchar(1000), utype varchar(100), vendor_code int, primary key(id))";
            String vendors = "create table if not exists vendors(id INT NOT NULL AUTO_INCREMENT, vendor_name varchar(200), vendor_code int, prepaid varchar(200), postpaid varchar(200), airtime varchar(200), status varchar(200), primary key(id), unique(vendor_code))";
            String reversals = "create table if not exists reversals(id INT NOT NULL AUTO_INCREMENT, vendor_code int, originalref varchar(500), account varchar(200), status int default '0', primary key(id))";
            String whitelist = "create table if not exists whitelist(id INT NOT NULL AUTO_INCREMENT, vendor_code int, ip varchar(100), port varchar(50), primary key(id))";
            String reference = "create table if not exists reference(id INT NOT NULL AUTO_INCREMENT, time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, refe varchar(255) DEFAULT NULL, primary key(id))";
            String comlimit = "create table if not exists comlimit" + tdate + "(id INT NOT NULL AUTO_INCREMENT, tran_account varchar(200), primary key(id), unique(tran_account))";
            Statement stm = conn.createStatement();
            stm.execute(users);
            stm.execute(vendors);
            stm.execute(reversals);
            stm.execute(whitelist);
            stm.execute(reference);
            stm.execute(comlimit);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Connection conn = connect();
            String transactions = "create table transactions" + tdate + "(id INT NOT NULL AUTO_INCREMENT, vendor_code int, clientid varchar(50), seqnumber varchar(50), refnumber varchar(500), terminal varchar(50), ipaytime varchar(50), tran_type varchar(200), tran_account varchar(200), tran_amt double default 0, tran_depo_amt double default 0, tran_commission double default 0, tran_response text, ipay_ref varchar(500), tran_date timestamp default current_timestamp, status int default '0', primary key(id))";
            Statement stm = conn.createStatement();
            stm.execute(transactions);

            DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy");
            String year = fmt.print(dt);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
            String month = formatter.print(dt);

            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            SimpleDateFormat formatm = new SimpleDateFormat("MMM");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            String lastmonth = formatm.format(cal.getTime()) + format.format(cal.getTime());
            System.out.println("Previous Month:" + lastmonth);

            String query1 = "SELECT `vendor_code` FROM `vendors`";
            Statement stm1 = conn.createStatement();
            ResultSet rs1 = stm1.executeQuery(query1);
            while (rs1.next()) {
                System.out.println("Inseting for vendor:" + rs1.getString(1));
                String query2 = "SELECT MAX(`ipay_ref`),SUM(`tran_depo_amt`),SUM(`tran_amt`),SUM(`tran_commission`) FROM `transactions" + lastmonth + "` WHERE  `vendor_code`=" + rs1.getString(1) + "";
                //System.out.println(query2);
                Statement stm2 = conn.createStatement();
                ResultSet rs2 = stm2.executeQuery(query2);
                if (rs2.isBeforeFirst()) {
                    while (rs2.next()) {
                        if (rs2.getString(1) == null) {
                            String values = "insert into transactions" + month + year + "(vendor_code,tran_type,refnumber,tran_amt,tran_depo_amt,status) values (?,?,?,?,?,?)";
                            PreparedStatement prep = conn.prepareStatement(values);
                            prep.setString(1, rs1.getString(1));
                            prep.setString(2, "BALANCE BROUGHT FORWARD");
                            prep.setString(3, "pdslauto");
                            prep.setString(4, "0.0");
                            prep.setString(5, "0.0");
                            prep.setInt(6, 1);
                            prep.execute();
                            prep.close();

                            String values2 = "insert into transactions" + month + year + "(vendor_code,tran_type,refnumber,tran_depo_amt,status) values (?,?,?,?,?)";
                            PreparedStatement prep2 = conn.prepareStatement(values2);
                            prep2.setString(1, rs1.getString(1));
                            prep2.setString(2, "COMMISSION LAST MONTH");
                            prep2.setString(3, "pdslauto");
                            prep2.setDouble(4, 0.0);
                            prep2.setInt(5, 1);
                            prep2.execute();
                            prep2.close();
                        } else {
                            String values = "insert into transactions" + month + year + "(vendor_code,tran_type,ipay_ref,refnumber,tran_amt,tran_depo_amt,status) values (?,?,?,?,?,?,?)";
                            PreparedStatement prep = conn.prepareStatement(values);
                            prep.setString(1, rs1.getString(1));
                            prep.setString(2, "BALANCE BROUGHT FORWARD");
                            prep.setString(3, rs2.getString(1));
                            prep.setString(4, rs2.getString(1));
                            prep.setString(5, rs2.getString(3));
                            prep.setString(6, rs2.getString(2));
                            prep.setInt(7, 1);
                            prep.execute();
                            prep.close();

                            double commission = Math.round(Double.parseDouble(rs2.getString(4)) * 100.0) / 100.0;
                            String values2 = "insert into transactions" + month + year + "(vendor_code,tran_type,refnumber,tran_depo_amt,status) values (?,?,?,?,?)";
                            PreparedStatement prep2 = conn.prepareStatement(values2);
                            prep2.setString(1, rs1.getString(1));
                            prep2.setString(2, "COMMISSION LAST MONTH");
                            prep2.setString(3, rs2.getString(1));
                            prep2.setDouble(4, commission);
                            prep2.setInt(5, 1);
                            prep2.execute();
                            prep2.close();
                        }
                    }
                } else {
                    String values = "insert into transactions" + month + year + "(vendor_code,tran_type,refnumber,tran_amt,tran_depo_amt,status) values (?,?,?,?,?,?)";
                    PreparedStatement prep = conn.prepareStatement(values);
                    prep.setString(1, rs1.getString(1));
                    prep.setString(2, "BALANCE BROUGHT FORWARD");
                    prep.setString(3, "pdslauto");
                    prep.setString(4, "0.0");
                    prep.setString(5, "0.0");
                    prep.setInt(6, 1);
                    prep.execute();
                    prep.close();

                    String values2 = "insert into transactions" + month + year + "(vendor_code,tran_type,refnumber,tran_depo_amt,status) values (?,?,?,?,?)";
                    PreparedStatement prep2 = conn.prepareStatement(values2);
                    prep2.setString(1, rs1.getString(1));
                    prep2.setString(2, "COMMISSION LAST MONTH");
                    prep2.setString(3, "pdslauto");
                    prep2.setDouble(4, 0.0);
                    prep2.setInt(5, 1);
                    prep2.execute();
                    prep2.close();
                }
            }
            conn.close();
        } catch (SQLException ex) {
            //Aready created
            System.out.println("Transaction Table already created");
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
