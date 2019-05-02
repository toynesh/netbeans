package subagairtime;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RefClass {

    static int seq = 0;

    static synchronized String newRef() {
        DataStore data = new DataStore();
        Connection conn = data.connect();
        try {
            String query = "select max(refe) from reference";
            PreparedStatement prep = conn.prepareStatement(query);
            ResultSet rst = prep.executeQuery();
            String ref;

            if (rst.next()) {
                ref = rst.getString(1);

                BigInteger a = new BigInteger(ref);
                BigInteger b = new BigInteger("11");
                BigInteger result = a.add(b);

                System.out.println("RESULT" + result);
                addRef(result.toString());

                conn.close();

                return result.toString();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    static synchronized String createSeq() {

        if (seq < 99999) {
            seq++;
        } else {
            seq = 1;
        }
        return Integer.toString(seq);
    }

    static synchronized String addRef(String ref) {
        DataStore data = new DataStore();
        Connection conn = data.connect();
        try {
            String values = "insert into reference(refe) values (?)";

            PreparedStatement prep = conn.prepareStatement(values);
            prep.setString(1, ref);

            prep.execute();
            prep.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "SAVE SECCESS";
    }

}
