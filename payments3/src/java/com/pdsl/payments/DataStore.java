package com.pdsl.payments;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


//import com.kannel.process.Register;
//import com.kannel.process.processMessage;
//import com.kannel.resources.LoadProp;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
//import com.wingo.WingoLinuxServer;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import icow.LoadProp;
//import org.apache.log4j.Logger;
//import org.apache.log4j.BasicConfigurator;
/**
 *
 * @author cellular
 */
public class DataStore {
public Connection conn=null;
public Connection conn2=null;
//public LoadProp props=new LoadProp();
Properties prop=new Properties();
//static Logger logger=Logger.getLogger(DataStore.class) ;
//WingoLinuxServer wingo=new WingoLinuxServer();
//constructor
public DataStore(){
  //  prop=props.props();
  
  //  BasicConfigurator.configure();
    createTables();
    
}




private DataSource ds;
//connect to the database
public Connection connect2(){
     //Connection con = null;
     Connection conn = null;
		    try {
			    /*
				MysqlConnectionPoolDataSource dataSource=new MysqlConnectionPoolDataSource();
				//poole.set
//			MysqlDataSource dataSource = new MysqlDataSource();
	//		dataSource.setServerName("41.215.46.122");
                        dataSource.setServerName("127.0.0.1");
			dataSource.setDatabaseName("mobile_wallet");
                       // dataSource.setDatabaseName("mpesa");
			dataSource.setUser("root");
                        dataSource.setPassword("1root2");
                       // dataSource.setPassword("root");
                       //dataSource.setPassword("!Ki");
               //	Open a connection
			System.out.println("Connecting to the database...");
			conn = dataSource.getConnection();
                        this.conn=conn;*/
                          Context ctx = new InitialContext();
		ds = (DataSource)ctx.lookup("jdbc/_kplc");
			conn = ds.getConnection();
                        this.conn=conn;
			    // Execute a query
			 

    } catch(Exception e) {
      System.err.println("Exception: " + e.getMessage());
    
    }
                    
return conn;

}
/*public Connection connect(){
     //Connection con = null;
     Connection conn = null;
		    try {
			    
                        MysqlConnectionPoolDataSource dataSource=new MysqlConnectionPoolDataSource();
				//poole.set
//			MysqlDataSource dataSource = new MysqlDataSource();
	//		dataSource.setServerName("41.215.46.122");
                        dataSource.setServerName("127.0.0.1");
			dataSource.setDatabaseName("mobile_wallet");
                       // dataSource.setDatabaseName("mpesa");
			dataSource.setUser("root");
                        //dataSource.setPassword("!@kipkurui@");
                        dataSource.setPassword("1root2");
                       //dataSource.setPassword("!Ki");
               //	Open a connection
			System.out.println("Connecting to the database...");
			conn = dataSource.getConnection();
                        
                        this.conn=conn;
                          /*Context ctx = new InitialContext();
		ds = (DataSource)ctx.lookup("jdbc/_kplc");
			conn = ds.getConnection();/
                       // this.conn=conn;
			    // Execute a query
			 

    } catch(Exception e) {
      System.err.println("Exception: " + e.getMessage());
    
    }
                    
return conn;

}*/
public Connection connect(){
     Connection conn = null;
		    try {
			    
                        String myDriver = "org.gjt.mm.mysql.Driver";
                        String myUrl = "jdbc:mysql://localhost/mobile_wallet";
                        Class.forName(myDriver);
                        conn = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");
			System.out.println("Connecting to the database...");
                        this.conn=conn;

    } catch(Exception e) {
      System.err.println("Exception: " + e.getMessage());
    
    }
                    
return conn;

}
public Connection uconnect(){
     Connection conn = null;
		    try {
			    
                        String myDriver = "org.gjt.mm.mysql.Driver";
                        String myUrl = "jdbc:mysql://localhost/sms_login";
                        Class.forName(myDriver);
                        conn = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");
			System.out.println("Connecting to the database...");
                        this.conn=conn;

    } catch(Exception e) {
      System.err.println("Exception: " + e.getMessage());
    
    }
                    
return conn;

}
public Connection connectremote(){
     Connection conn = null;
		    try {
			    
                        String myDriver = "org.gjt.mm.mysql.Driver";
                        String myUrl = "jdbc:mysql://192.185.168.248:3306/biashara_pesapal";
                        Class.forName(myDriver);
                        conn = DriverManager.getConnection(myUrl, "biashara_pesa", "vs&5q)pm%1B9");
			System.out.println("Connecting to the database...");
                        this.conn=conn;

    } catch(Exception e) {
      System.err.println("Exception: " + e.getMessage());
    
    }
                    
return conn;

}
public Connection prefixconnect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/phoneprefixes";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "pdsluser", "P@Dsl949022");

            this.conn = conn;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }


    public void insert(String insert,Connection con) {
          this.conn=con;
          PreparedStatement pstm=null;
        try {
            
            System.out.println(insert);
            //Statement stm=conn.createStatement();
            pstm=conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
        } catch (SQLException ex) {
            //logger.error("Error on the database "+ex.getMessage());
            ex.printStackTrace();
            
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
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
    public void insert(String insert) {
          conn=connect();
          PreparedStatement pstm=null;
        try {
            
            System.out.println(insert);
            //Statement stm=conn.createStatement();
            pstm=conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
        } catch (SQLException ex) {
            //logger.error("Error on the database "+ex.getMessage());
            ex.printStackTrace();
            
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
                
            } catch (SQLException ex) {
             //   logger.error("Error on the database "+ex.getMessage());
                ex.printStackTrace();
                //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public void insert(ArrayList insert){
          conn=connect();
        try {
            for(int count=0;count<insert.size();count++){
    //            System.out.println(insert.get(count));
            Statement stm=conn.createStatement();
        //    System.out.println(""+insert.get(count));
            stm.execute(insert.get(count).toString());
            stm.close();
            }
        } catch (SQLException ex) {
      //      logger.error("Error on the database "+ex.getMessage());
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
    //            logger.error("Error on the database "+ex.getMessage());
                //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    public void createTables() {
    try {
        Connection con = connect();
        String delivery = "create table if not exists dlr_status(dlr_id INT NOT NULL AUTO_INCREMENT, msisdn varchar(50), correlator varchar(50), status varchar(200), dlr_time timestamp NULL DEFAULT CURRENT_TIMESTAMP, primary key(dlr_id))";
        Statement stm = con.createStatement();
        stm.execute(delivery);
        con.close();
        
        
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //main entry of the program
    public static void main(String[]args){
       
    }

}
