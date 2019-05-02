package com.pdsl.datastore;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//import com.kannel.process.Register;
//import com.kannel.process.processMessage;
//import com.kannel.resources.LoadProp;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author cellular
 */
public class DataStore {

    public Connection conn = null;
//public LoadProp props=new LoadProp();
    Properties prop = new Properties();
//WingoLinuxServer wingo=new WingoLinuxServer();
//constructor

    public DataStore() {
  //  prop=props.props();
        //  conn=connect2();

    //createTables();
    }
    private DataSource ds;

//connect to the database
    public Connection connect2() {
        //Connection con = null;
        Connection conn = null;
        try {
			    // Register the JDBC driver
            // String driver = "com.mysql.jdbc.Driver";
            // Class.forName(driver);

//			  Create the DataSource object
            //MysqlConnectionPoolDataSource dataSource=new MysqlConnectionPoolDataSource();
            //poole.set
//			MysqlDataSource dataSource = new MysqlDataSource();
            //dataSource.setServerName("127.0.0.1");
                        /*dataSource.setServerName("41.215.46.122");
             dataSource.setDatabaseName("dspDelivery");
             dataSource.setUser("hesbon");
             dataSource.setPassword("he52hu");*/
//			 Open a connection
            //System.out.println("Connecting to the database...");
            Context ctx = new InitialContext();
            //ds = (DataSource) ctx.lookup("java:/jdbc/notify");
            ds = (DataSource) ctx.lookup("java:jboss/datasources/notify");
            conn = ds.getConnection();

            this.conn = conn;
			    // Execute a query

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());

        }

        return conn;

    }

//connect to the database
    public Connection connect() {
        //Connection con = null;
        Connection conn = null;
        try {
			    // Register the JDBC driver
            // String driver = "com.mysql.jdbc.Driver";
            // Class.forName(driver);

//			  Create the DataSource object
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            //poole.set
//			MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName("127.0.0.1");
            // dataSource.setServerName("192.168.100.202");
            dataSource.setDatabaseName("dspDelivery");
            dataSource.setUser("root");
            dataSource.setPassword("1root2");
//			 Open a connection
            //System.out.println("Connecting to the database...");
            conn = dataSource.getConnection();
            this.conn = conn;
			    // Execute a query

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());

        }
        return conn;

    }

    public void insert(String insert, Connection con) {
        this.conn = con;
        PreparedStatement pstm = null;
        try {

            //System.err.println(insert);
            //Statement stm=conn.createStatement();
            pstm = conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
            //     logger.log(Level.INFO,insert);
        } catch (SQLException ex) {
            //logger.error("Error on the database "+ex.getMessage());
            ex.printStackTrace();

            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstm.close();
                //conn.close();

            } catch (SQLException ex) {
                //   logger.error("Error on the database "+ex.getMessage());
                //log.error(ex.printStackTrace());
                ex.printStackTrace();
                //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void insert(String insert) {
        conn = connect();
        PreparedStatement pstm = null;
        try {

            System.out.println(insert);
            //Statement stm=conn.createStatement();
            pstm = conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
            conn.close();
        } catch (SQLException ex) {
            //logger.error("Error on the database "+ex.getMessage());
            ex.printStackTrace();

            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            /*try {
            //    pstm.close();
                //conn.close();

            } catch (SQLException ex) {
                //   logger.error("Error on the database "+ex.getMessage());
                ex.printStackTrace();
                //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }

    }

    //store the individuals sms to the database for  processing
    public void createTables() {
        System.out.println("Creating tables");
        try {
            String inbound = "create table if not exists inbound(inboundid INT NOT NULL AUTO_INCREMENT, msisdn varchar(20) , creation_timestamp TIMESTAMP ,messages varchar(255),processed varchar(3) NOT NULL DEFAULT 'N' , primary key(inboundid))";
            String messages = "create table if not exists messages(messageID INT NOT NULL AUTO_INCREMENT, msisdn varchar(20) , creation_timestamp TIMESTAMP ,messages varchar(255),processed varchar(3) NOT NULL DEFAULT 'N' , primary key(messageID))";
            // String validated = "create table validated(idno INT NOT NULL AUTO_INCREMENT, pnumber varchar(20),  time_sent VARCHAR(100),sender_number varchar(255) , PRIMARY KEY(idno))";
            String validated = "create table if not exists validated(idno INT NOT NULL AUTO_INCREMENT, pnumber varchar(20),  time_sent VARCHAR(100),sender_number varchar(255),played varchar(10) NOT NULL DEFAULT 'N' , PRIMARY KEY(idno))";
            String winning = "create table if not exists winning(idno INT NOT NULL AUTO_INCREMENT, pnumber varchar(20), time_done TIMESTAMP,lucky_number varchar(255),winning_number varchar(20),matching varchar(50), PRIMARY KEY(idno))";
            String winners = "create table if not exists winners(idno INT NOT NULL AUTO_INCREMENT, pnumber varchar(20), time_done TIMESTAMP,lucky_number varchar(255),winning_number varchar(20),matching varchar(50), PRIMARY KEY(idno))";
            String register = "create table if not exists registration(msisdn varchar(20) NOT NULL primary key, creation_timestamp timestamp,first_Name varchar(255) NOT NULL DEFAULT '*',last_name varchar(50) NOT NULL DEFAULT '*')";
            String registerface = "create table if not exists registrationface(msisdn varchar(20) NOT NULL primary key, date_sent timestamp,first_Name varchar(255) NOT NULL DEFAULT '*',last_name varchar(50) NOT NULL DEFAULT '*')";
            String outbound = "create table if not exists outbound(outboundid INT NOT NULL AUTO_INCREMENT, msisdn varchar(20) , send_timestamp TIMESTAMP ,message varchar(255),primary key(outboundid))";
            String outboundface = "create table if not exists outboundface(outboundid INT NOT NULL AUTO_INCREMENT, msisdn varchar(20) , send_timestamp TIMESTAMP ,message varchar(255),primary key(outboundid))";
            String micell = "create table if not exists micell(pnumber varchar(20) not null primary key , dob int (10), town varchar(255), constitnc varchar(50), gender varchar(6))";
            String arch = "create table if not exists archive(idno int NOT NULL AUTO_INCREMENT,date varchar(30),time_done TIMESTAMP, done varchar(3) ,primary key(idno))";
            String admins = "create table if not exists admins(pnumber varchar(20) NOT NULL primary key, Date_sent varchar(100),first_Name varchar(255) NOT NULL DEFAULT '*',last_name varchar(50) NOT NULL DEFAULT '*')";
            String holdtables = "create table if not exists hold(idno int not null auto_increment,date varchar(30),hold varchar(3),primary key(idno))";
            String checkthread = "create table if not exists thread(idno int not null auto_increment,date varchar(30),hold varchar(3),primary key(idno))";
            Statement stm = conn.createStatement();

            stm.execute(inbound);
            //stm.execute(validated);
            stm.execute(register);
            stm.execute(registerface);
            /*stm.execute(winning);
             stm.execute(winners);*/
            stm.execute(outbound);
            stm.execute(outboundface);
            stm.execute(messages);
            /*stm.execute(admins);
             stm.execute(holdtables);*/
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* insert received messages into the database
     *
     */
    public void insertInto(String pnumber, String message) {
        //checkcutoff();
        try {
            connect();//connect to the database when called from the jsp page

            String insert = "INSERT INTO inbound(msisdn,message,process_status) values(?,?,?)";
            PreparedStatement prep = conn.prepareStatement(insert);
            prep.setString(1, pnumber);
            prep.setString(2, message);
            prep.setInt(3, 0);
            prep.execute();
            System.out.println("Message has been saved");
            //wingo.start();
            //conn.commit();
            //  new processMessage().processes();
            //stm.execute(insert);
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkcutoff() {
        try {
            System.out.println("Entering the cutoff time");
            //while(true){
            for (;;) {
                System.out.println("Looping");
                String cutdate = prop.getProperty("date.cutoff.date");
                String check = "select * from hold where date='" + cutdate + "'";
                Statement stm = conn.createStatement();
                ResultSet rst = stm.executeQuery(check);
                int breaker = 0;
                //rst.last();
                while (rst.next()) {
                    String date = rst.getString(2);
                    String pend = rst.getString(3);
                    System.out.println("current date");
                    if (cutdate.equals(date)) {

                        if (pend.equals("Y")) {

                            try {
                                System.out.println("Sleeping");
                                Thread.sleep(10000);
                                System.out.println("woken up");
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            System.out.println("Changing breaker");
                            breaker = 1;
                        }
                    }

                }
                if (breaker == 1) {
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    /* insert received messages into the database
     *
     */
    public void insertInto2(String pnumber, String message, String activenumber) {
        //checkcutoff();
        try {
            //connect2();//connect to the database when called from the jsp page
            boolean flag = true;
            flag = check(pnumber);
            //System.out.println(" Nimepatikana"+pnumber);
            if (flag == true) {
                //  System.out.println(" Nimepatikana");
                String insert = "INSERT INTO inbound(msisdn,message,process_status,s_code) values(?,?,?,?)";
                PreparedStatement prep = conn.prepareStatement(insert);
                prep.setString(1, pnumber);
                prep.setString(2, message);
                prep.setInt(3, 0);
                prep.setString(4, activenumber);
                prep.execute();
                System.out.println("Message code" + activenumber + " has been saved");
            }
            //wingo.start();
            //conn.commit();
            //  new processMessage().processes();
            //stm.execute(insert);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean check(String number) {
        boolean flag = false;
        try {

            String query = "SELECT * FROM restricted where msisdn='" + number + "'";
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(query);
            if (rst.next()) {
                System.out.println(" Nimepatikana");
                flag = true;
            }

        } catch (SQLException ex) {
            //ex.printStackTrace();
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    public void checkTables() {
        try {
            String query = "SELECT msisdn,cardID from inbound where creation_timestamp between '2010-10-22 10:58:17' and '2010-10-22 10:58:20";
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(query);
            while (rst.next()) {

                String msisdn = rst.getString(1);
                String icardID = "" + rst.getInt(2);
                String query2 = "select msisdn from inbound where msisdn='" + msisdn + "'";
                Statement stm2 = conn.createStatement();
                ResultSet rst2 = stm2.executeQuery(query2);

                while (rst2.next()) {
                    System.err.println("The match found is " + rst2.getBigDecimal(1));
                }
//rst2.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //main entry of the program
    public static void main(String[] args) {
        DataStore data = new DataStore();
        //data.checkTables();
        //data.connect();
        data.createTables();
        // data.insertInto("100","my food");
    }

    //main entry of the program
    /*public static void main(String[]args){
     DataStore data=new DataStore();
     //data.connect();
     //data.createTables();
     // data.insertInto("100","my food");
     }*/
}
