package com.pdlskenya.postpaid;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//import com.kannel.process.Register;
//import com.kannel.process.processMessage;
//import com.kannel.resources.LoadProp;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

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
        conn = connect();

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
            MysqlConnectionPoolDataSource dataSource=new MysqlConnectionPoolDataSource();
            //poole.set
	//MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName("127.0.0.1");
                        //dataSource.setServerName("41.215.46.122");
             dataSource.setDatabaseName("vending");
             dataSource.setUser("root");
             dataSource.setPassword("1root2");
//			 Open a connection
            //System.out.println("Connecting to the database...");
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ringtone");
            conn = ds.getConnection();
            this.conn = conn;
			    // Execute a query

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception: " + e.getMessage());

        }
        return conn;

    }

//connect to the database
    
   /*
    @Transactional  
    public Connection connect() {
            //Connection con = null;
            Connection con = null;
            try {
              Context ctx = new InitialContext();
                 ds = (DataSource) ctx.lookup("java:comp/env/jdbc/pdslipay");
                 con = ds.getConnection();
                 //conn.close();

             } catch (Exception e) {
                 System.err.println("Exception: " + e.getMessage());

             }

             return con;

         }
    */
    
    /* 
   
    <Resource name="jdbc/pdslipay"
              url="jdbc:mysql://localhost:3306/vending"
              driverClassName="com.mysql.jdbc.Driver"
              username="root"
              password="1root2"
              auth="Container"
              type="javax.sql.DataSource"
              maxActive="20"
              maxIdle="5"
              maxWait="50000"
     validationQuery="SELECT 1"
     testOnBorrow="true" 
     defaultAutoCommit ="false"
              />
   
  */
   
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
            //dataSource.set
            dataSource.setServerName("127.0.0.1");
            //dataSource.setServerName("192.168.0.139");
            dataSource.setDatabaseName("vending");
            dataSource.setUser("root");
            dataSource.setPassword("1root2");
                       //dataSource.setPassword("!@kipkurui@");
//			 Open a connection
            //System.out.println("Connecting to the database...");
            conn = dataSource.getConnection();
            this.conn = conn;
			    // Execute a query

        } catch (Exception e) {
            System.err.println("Exception while connecting: " + e.getMessage());

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
        //conn = connect();
        PreparedStatement pstm = null;
        try {

            //System.out.println(insert);
            //Statement stm=conn.createStatement();
            pstm = conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
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
                ex.printStackTrace();
                //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    //store the individuals sms to the database for  processing
    public void createTables() {
        try {
        	//String requests = "create table if not exists requests(reqid INT NOT NULL, clientid varchar(50), account_number varchar(50), amount varchar(50), msisdn varchar(50), status varchar(50), terminal varchar(50), response text, primary key(reqid))";
            String transaction = "create table if not exists transaction(vendid INT NOT NULL AUTO_INCREMENT, reqid INT, clientid varchar(50), seqnumber varchar(50), account_number varchar(50), amount varchar(50), status varchar(50), refnumber varchar(50) NOT NULL, time varchar(50), msisdn varchar(50), terminal varchar(50), primary key(vendid))";
            String reversals = "create table if not exists reversals(reversalid INT NOT NULL AUTO_INCREMENT, repcount varchar(50), clientid varchar(50), seqnumber varchar(50), original_ref varchar(50) NOT NULL, status varchar(50), refnumber varchar(50) NOT NULL, time varchar(50), msisdn varchar(50), terminal varchar(50), schedule varchar(20), primary key(reversalid))";
          
            Statement stm = conn.createStatement();

            //stm.execute(requests);
            stm.execute(transaction);
            stm.execute(reversals);
            
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
    public boolean checkRevStatus(String ac) {
        //Connection con = null;
        Connection connx = null;
        DataSource ds;
        
        
        try {
        	
        	 Context ctx = new InitialContext();
             ds = (DataSource) ctx.lookup("java:comp/env/jdbc/pdslpostpaid");
             connx = ds.getConnection();
             String query = "SELECT * FROM deliveryprocess WHERE account_no= '"+ ac +"'";
             Statement stm = connx.createStatement();
             ResultSet rst = stm.executeQuery(query);
             if(rst == null)
            	 return false;
             
             connx.close();

         } catch (Exception e) {
             System.err.println("Exception: " + e.getMessage());

         }

         return true;

     }
    public boolean updateRec(String ac) {
        //Connection con = null;
        Connection connx = null;
        DataSource ds;
        
        
        try {
        	
        	 Context ctx = new InitialContext();
             ds = (DataSource) ctx.lookup("java:comp/env/jdbc/pdslpostpaid");
             connx = ds.getConnection();
             Statement stm = connx.createStatement();
             ResultSet rst = stm.executeQuery(ac);
             if(rst == null)
            	 return false;
             
             connx.close();

         } catch (Exception e) {
             System.err.println("Exception: " + e.getMessage());

         }

         return true;

     }



    public Boolean saveRev(String ac) {
        //Connection con = null;
        Connection connx = null;
        DataSource ds;
        
        
        try {
        	
        	 Context ctx = new InitialContext();
             ds = (DataSource) ctx.lookup("java:comp/env/jdbc/pdslpostpaid");
             connx = ds.getConnection();
             Statement stm = connx.createStatement();
             ResultSet rst = stm.executeQuery(ac);

             connx.close();

         } catch (Exception e) {
             System.err.println("Exception: " + e.getMessage());

         }

         return true;

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
/*
    //main entry of the program
    public static void main(String[] args) {
        DataStore data = new DataStore();
        //data.checkTables();
        data.connect();
        //data.createTables();
        // data.insertInto("100","my food");
    }
*/
    //main entry of the program
    /*public static void main(String[]args){
     DataStore data=new DataStore();
     //data.connect();
     //data.createTables();
     // data.insertInto("100","my food");
     }*/
}
