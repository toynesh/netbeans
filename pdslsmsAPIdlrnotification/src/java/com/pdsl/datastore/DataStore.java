package com.pdsl.datastore;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cellular
 */
public class DataStore {

    public Connection conn = null;

    public DataStore(){
        createTables();
    }
    public Connection connect() {
        //Connection con = null;
        Connection conn = null;
        try {
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setServerName("127.0.0.1");
            dataSource.setDatabaseName("dspDelivery");
            dataSource.setUser("root");
            dataSource.setPassword("1root2");
            conn = dataSource.getConnection();
            this.conn = conn;

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());

        }
        return conn;

    }

    public Connection bbconnect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/pdslsmsAPI";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1root2");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void createTables() {
        Connection conn = bbconnect();
        try {
            //String smsdlr = "create table if not exists smsdlr(id INT NOT NULL AUTO_INCREMENT, msisdn varchar(20), mpesa_code varchar(50), dlry_status varchar(50), primary key(id), unique (mpesa_code))";
            String dlr_status= "CREATE TABLE IF NOT EXISTS `dlr_status` (`dlr_id` int(11) NOT NULL AUTO_INCREMENT, `msisdn` varchar(50) DEFAULT NULL, `correlator` varchar(50) DEFAULT NULL, `status` varchar(200) DEFAULT NULL, `dlr_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`dlr_id`))";
            Statement stm = conn.createStatement();
            stm.execute(dlr_status);
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void insert(String insert) {
        conn = connect();
        PreparedStatement pstm = null;
        try {

            System.out.println(insert);
            pstm = conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        }

    }
    public void bbinsert(String insert) {
        conn = bbconnect();
        PreparedStatement pstm = null;
        try {

            //System.out.println(insert);
            pstm = conn.prepareStatement(insert);
            pstm.execute();
            pstm.close();
            conn.close();
            Logger.getLogger(DataStore.class.getName()).log(Level.INFO, "...DRL SAVED!!...");
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, "Error Updating PDSLSMSAPI Delivery Status" + ex);
        } finally {
        }

    }

}
