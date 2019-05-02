/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sellequitel;

import java.sql.Connection;
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
public class Sellequitel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DataStore data = new DataStore();
        try {
            Connection con = data.connect();
            DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            String datee = fmt.print(dt);
            String query = "SELECT equityid,transactionDate,bankreference,billNumber,phoneNumber,billAmount,debitCustName FROM equity where  equityid >14201";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Logger.getLogger(Sellequitel.class.getName()).log(Level.INFO, "SELLING:-"+rs.getString(1));
                String payres = data.sendPayment(rs.getString(2), "EQU_" + rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                Logger.getLogger(Sellequitel.class.getName()).log(Level.INFO, payres);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Sellequitel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
