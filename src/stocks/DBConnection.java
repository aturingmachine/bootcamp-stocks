package stocks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection conn;

    private static Connection makeConn() throws SQLException {
        try {
            System.out.println("DB Connection Attempting...");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/stocks?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "Sempervic95");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Connection getConn() throws SQLException {
        if (conn == null) {
            return makeConn();
        } else {
            return conn;
        }
    }

}
