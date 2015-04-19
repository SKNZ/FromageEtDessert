package hcs.fededededed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by sknz on 4/19/15.
 */
public class DB {
    public static Connection conn;
    static {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://srv0.sknz.info/fed", "fed", "fedup");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
