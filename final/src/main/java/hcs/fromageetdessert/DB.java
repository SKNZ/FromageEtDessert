package hcs.fromageetdessert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by sknz on 4/19/15.
 */
public class DB {
    static java.sql.Connection CONN = null;

    static {
        try {
            CONN = DriverManager.getConnection("jdbc:postgresql://localhost/fromagetdessert", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
