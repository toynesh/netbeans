package com.pdsl.equity;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//import com.kannel.process.Register;
//import com.kannel.process.processMessage;
//import com.kannel.resources.LoadProp;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.io.File;
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
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringEscapeUtils;
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

    public Connection conn = null;
    public Connection conn2 = null;

//public LoadProp props=new LoadProp();
    Properties prop = new Properties();
//static Logger logger=Logger.getLogger(DataStore.class) ;
//WingoLinuxServer wingo=new WingoLinuxServer();
//constructor

    public DataStore() {
        //  prop=props.props();

        //  BasicConfigurator.configure();
        //createTables();
    }

    private DataSource ds;
//connect to the database

    public Connection connect2() {
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
            ds = (DataSource) ctx.lookup("jdbc/_kplc");
            conn = ds.getConnection();
            this.conn = conn;
            // Execute a query

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());

        }

        return conn;

    }

    public Connection connect() {
        //Connection con = null;
        Connection conn = null;
        try {

            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            //poole.set
//			MysqlDataSource dataSource = new MysqlDataSource();
            //		dataSource.setServerName("41.215.46.122");
            dataSource.setServerName("127.0.0.1");
            dataSource.setDatabaseName("equity");
            // dataSource.setDatabaseName("mpesa");
            dataSource.setUser("pdsluser");
            //dataSource.setPassword("!@kipkurui@");
            dataSource.setPassword("P@Dsl949022");
            //dataSource.setPassword("!Ki");
            //	Open a connection
            System.out.println("Connecting to the database...");
            conn = dataSource.getConnection();

            this.conn = conn;
            /*Context ctx = new InitialContext();
		ds = (DataSource)ctx.lookup("jdbc/_kplc");
			conn = ds.getConnection();*/
            // this.conn=conn;
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

            System.out.println(insert);
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

    public void insert(String insert) {
        conn = connect();
        PreparedStatement pstm = null;
        try {

            System.out.println(insert);
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
                conn.close();

            } catch (SQLException ex) {
                //   logger.error("Error on the database "+ex.getMessage());
                ex.printStackTrace();
                //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void insert(ArrayList insert) {
        conn = connect();
        try {
            for (int count = 0; count < insert.size(); count++) {
                //            System.out.println(insert.get(count));
                Statement stm = conn.createStatement();
                //    System.out.println(""+insert.get(count));
                stm.execute(insert.get(count).toString());
                stm.close();
            }
        } catch (SQLException ex) {
            //      logger.error("Error on the database "+ex.getMessage());
            //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                //            logger.error("Error on the database "+ex.getMessage());
                //Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    //main entry of the program
    public static void main(String[] args) {
        DataStore data = new DataStore();
        PreparedStatement prep = null;
        Connection con = null;
        try {
            File f = new File("/home/coolie/Desktop/vendors.xls");
            Workbook wb = Workbook.getWorkbook(f);
            Sheet s = wb.getSheet(1);
            int row = s.getRows();
            int col = s.getColumns();
            for (int j = 2; j < row; j++) {
                String ag_code = StringEscapeUtils.unescapeJava(s.getCell(1, j).getContents());
                String ag_name = StringEscapeUtils.unescapeJava(s.getCell(2, j).getContents());
                String description = StringEscapeUtils.unescapeJava(s.getCell(3, j).getContents());
                String retailer_type = StringEscapeUtils.unescapeJava(s.getCell(4, j).getContents());
                String eazzybiz = StringEscapeUtils.unescapeJava(s.getCell(6, j).getContents());
                String cheque = StringEscapeUtils.unescapeJava(s.getCell(7, j).getContents());
                String rtgs = StringEscapeUtils.unescapeJava(s.getCell(8, j).getContents());
                System.out.println(ag_code + "||" + ag_name + "||" + description + "||" + retailer_type + "||" + eazzybiz + "||" + cheque + "||" + rtgs);
                if(eazzybiz.equals("0")){
                    eazzybiz=null;
                }
                if(cheque.equals("0")){
                    cheque=null;
                }
                if(rtgs.equals("0")){
                    rtgs=null;
                }
                con = data.connect();
                String values = "insert into aggregators(ag_code,ag_name,description,retailer_type,eazzybiz,cheque,rtgs) values (?,?,?,?,?,?,?)";
                prep = con.prepareStatement(values);
                prep.setString(1, ag_code);
                prep.setString(2, ag_name);
                prep.setString(3, description);
                prep.setString(4, retailer_type);
                prep.setString(5, eazzybiz);
                prep.setString(6, cheque);
                prep.setString(7, rtgs);
                prep.execute();
                prep.close();
                con.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, "db update error: " + ex);
            System.out.println("db update error: " + ex);
        }
    }

}
