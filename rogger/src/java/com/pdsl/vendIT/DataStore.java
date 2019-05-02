package com.pdsl.vendIT;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DataStore
{
  public Connection conn = null;
  public Connection conn2 = null;
  Properties prop = new Properties();
  private DataSource ds;
  
  public DataStore()
  {
    createTables();
  }
  
  public Connection connect2()
  {
    Connection conn = null;
    try
    {
      Context ctx = new InitialContext();
      this.ds = ((DataSource)ctx.lookup("jdbc/_kplc"));
      conn = this.ds.getConnection();
      this.conn = conn;
    }
    catch (Exception e)
    {
      System.err.println("Exception: " + e.getMessage());
    }
    return conn;
  }
  
  public Connection connect()
  {
    Connection conn = null;
    try
    {
      String myDriver = "org.gjt.mm.mysql.Driver";
      String myUrl = "jdbc:mysql://localhost/mobile_wallet";
      Class.forName(myDriver);
      conn = DriverManager.getConnection(myUrl, "root", "1root2");
      System.out.println("Connecting to the database...");
      this.conn = conn;
    }
    catch (Exception e)
    {
      System.err.println("Exception: " + e.getMessage());
    }
    return conn;
  }
  
  public Connection connectvendit()
  {
    Connection conn = null;
    try
    {
      String myDriver = "org.gjt.mm.mysql.Driver";
      String myUrl = "jdbc:mysql://localhost/sms_login";
      Class.forName(myDriver);
      conn = DriverManager.getConnection(myUrl, "root", "1root2");
      System.out.println("Connecting to vendit users database...");
      this.conn = conn;
    }
    catch (Exception e)
    {
      System.err.println("Exception: " + e.getMessage());
    }
    return conn;
  }
  
  public void insert(String insert, Connection con)
  {
    this.conn = con;
    PreparedStatement pstm = null;
    try
    {
      System.out.println(insert);
      
      pstm = this.conn.prepareStatement(insert);
      pstm.execute();
      pstm.close(); return;
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      try
      {
        pstm.close();
      }
      catch (SQLException ex)
      {
        ex.printStackTrace();
      }
    }
  }
  
  public void insert(String insert)
  {
    this.conn = connect();
    PreparedStatement pstm = null;
    try
    {
      System.out.println(insert);
      
      pstm = this.conn.prepareStatement(insert);
      pstm.execute();
      pstm.close(); return;
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      try
      {
        pstm.close();
        this.conn.close();
      }
      catch (SQLException ex)
      {
        ex.printStackTrace();
      }
    }
  }
  
  public void insert(ArrayList insert)
  {
    this.conn = connect();
    try
    {
      for (int count = 0; count < insert.size(); count++)
      {
        Statement stm = this.conn.createStatement();
        
        stm.execute(insert.get(count).toString());
        stm.close();
      }
      return;
    }
    catch (SQLException localSQLException1) {}finally
    {
      try
      {
        this.conn.close();
      }
      catch (SQLException localSQLException3) {}
    }
  }
  
  public void createTables()
  {
    try
    {
      Connection con = connect();
      String delivery = "create table if not exists dlr_status(dlr_id INT NOT NULL AUTO_INCREMENT, msisdn varchar(50), correlator varchar(50), status varchar(200), dlr_time timestamp NULL DEFAULT CURRENT_TIMESTAMP, primary key(dlr_id))";
      Statement stm = con.createStatement();
      stm.execute(delivery);
      con.close();
    }
    catch (SQLException ex)
    {
      Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public static void main(String[] args) {}
}
