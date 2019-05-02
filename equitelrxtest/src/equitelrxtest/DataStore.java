package equitelrxtest;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julius
 */
public class DataStore {
	public Connection conn = null;
	public Connection conn2 = null;

	public DataStore() {
		createTables();

	}
	public Connection connect() {
		Connection conn = null;
		try {

			String myDriver = "org.gjt.mm.mysql.Driver";
			String myUrl = "jdbc:mysql://localhost/smsgateways";
			Class.forName(myDriver);
			conn = DriverManager.getConnection(myUrl, "root", "1root2");
			// System.out.println("Connecting to the database...");
			this.conn = conn;

		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());

		}

		return conn;

	}

	public void insert(String insert) {
		conn = connect();
		PreparedStatement pstm = null;
		try {

			System.out.println(insert);
			pstm = conn.prepareStatement(insert);
			pstm.execute();
			pstm.close();
		} catch (SQLException ex) {
		} finally {
			try {
				pstm.close();
				conn.close();

			} catch (SQLException ex) {
				//ex.printStackTrace();
			}
		}

	}

	public void createTables() {
		try {
			Connection con = connect();
			String send = "CREATE TABLE IF NOT EXISTS snd ("
					+ "snd_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Every message has a unique id',"
					+ "snd_sender varchar(255) DEFAULT NULL COMMENT 'The number that the message is sent from',"
					+ "snd_to varchar(255) DEFAULT NULL COMMENT 'The number that the message is sent to',"
					+ "snd_txt longtext COMMENT 'The body text of the message',"
					+ "snd_smsc varchar(255) DEFAULT NULL COMMENT 'The number that the message is route to',"
					+ "snd_sentat timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'The time that the message is inserted',"
					+ "snd_success datetime DEFAULT NULL COMMENT 'The time that the message was delivered to the hand set',"
					+ "snd_failure datetime DEFAULT NULL COMMENT 'The time that the message failed',"
					+ "snd_submitted datetime DEFAULT NULL COMMENT 'The time that the message is submitted to the smsc',"
					+ "snd_buffered datetime DEFAULT NULL COMMENT 'The time that the message is buffered at the smsc',"
					+ "snd_rejected datetime DEFAULT NULL COMMENT 'The time that the message was rejected',"
					+ "snd_intermediate datetime DEFAULT NULL COMMENT 'Time of intermediate status',"
					+ "snd_last int(11) NOT NULL DEFAULT '0'," + "status int(11) NOT NULL DEFAULT '0'," + "PRIMARY KEY (snd_id))";

			String inbox = "CREATE TABLE IF NOT EXISTS inbox (" 
					+ "id int(11) NOT NULL AUTO_INCREMENT,"
					+ "time_recieved timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "timesent timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," 
					+ "smsc varchar(100) NOT NULL,"
					+ "osmsc varchar(100) NOT NULL," 
					+ "sender varchar(200) NOT NULL,"
					+ "reciever varchar(200) NOT NULL," 
					+ "message text NOT NULL,"
					+ "status int(11) NOT NULL DEFAULT '0',"
					+ "PRIMARY KEY (id))";
			Statement stm = con.createStatement();
			stm.execute(send);
			stm.execute(inbox);
			con.close();

		} catch (SQLException ex) {
			Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
