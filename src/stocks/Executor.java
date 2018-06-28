package stocks;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Executor {

    public static int runFunc(String symb, String date, Connection conn) {
        symb = symb.toUpperCase();
        System.out.println("High For " + symb + " on " + date);
        runQuery(conn, symb, date, "MAX(price)");
        System.out.println("Low For " + symb + " on " + date);
        runQuery(conn, symb, date, "MIN(price)");
        System.out.println("Volume Traded for " + symb + " on " + date);
        runQuery(conn, symb, date, "SUM(volume)");
        System.out.println("Closing Price for " + symb + " on " + date);
        closePrice(conn, symb, date);

        return 0;
    }

    private static int runQuery(Connection conn, String symb, String date, String type) {
        String queryString = "select [TYPE] from stocks where date like '[DATE]%' and symbol = '[SYMBOL]';";

        //Fill out the query with the user provided variables
        queryString = queryString.replace("[DATE]", date);
        queryString = queryString.replace("[SYMBOL]", symb);
        queryString = queryString.replace("[TYPE]", type);

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

    private static int closePrice(Connection conn, String symb, String date)  {
        String queryString = "select price from stocks where symbol = '[SYM]' " +
                "and date = (select max(date) from stocks where symbol = '[SYM]');";

        queryString = queryString.replace("[SYM]", symb);

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryString);

            //If we have a thing in the set (which we should)
            if (rs.next()) {
                    System.out.format("%.2f", rs.getDouble(1)); //This needs to be formatted
                    System.out.println();
            }
            rs.close();
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 9;
        }
    }
}
