package stocks;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Executor {

    public static int runFunc(String funcName, Connection conn) {
        switch (funcName) {
            case "high":
                return runQuery(conn, "MAX(price)");
            case "low":
                return runQuery(conn, "MIN(price)");
            case "volume":
                return runQuery(conn, "SUM(volume)");

            default:
                System.out.println("That function isn't supported!");
        }

        return 0;
    }

    private static int runQuery(Connection conn, String type) {
        String queryString = "select [TYPE] from stocks where date like '[DATE]%' and symbol = '[SYMBOL]';";
        Scanner sc = new Scanner(System.in);

        //Get Stock symbol as well as the date we want
        System.out.println("Enter A Stock Symbol (i.e. AMZN): ");
        String symbol = sc.nextLine().toUpperCase();
        System.out.println("Enter A Date (i.e 2018-06-26): ");
        String date = sc.nextLine();

        //Fill out the query with the user provided variables
        queryString = queryString.replace("[DATE]", date);
        queryString = queryString.replace("[SYMBOL]", symbol);
        queryString = queryString.replace("[TYPE]", type);

        sc.close();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryString);

            //If we have a thing in the set (which we should)
            if (rs.next()) {
                if (!type.equals("SUM(volume)")) {
                    System.out.format("%.2f", rs.getDouble(1)); //This needs to be formatted
                    System.out.println();
                } else {
                    System.out.println(rs.getInt(1));
                }
            }
            rs.close();
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 8;
        }
    }
}
