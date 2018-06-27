package stocks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/stocks?serverTimezone=UTC&useSSL=false", "root", "Sempervic95");
            return conn;
        } catch (SQLException e) {
            System.err.println(e);
        }

        return null;
    }

}
