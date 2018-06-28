package stocks;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor {

    public static int runFunc(String symb, String date, Connection conn) {
        symb = symb.toUpperCase();
        System.out.println("High For " + symb + " for " + date);
        runQuery(conn, symb, date, "MAX(price)");
        System.out.println("Low For " + symb + " for " + date);
        runQuery(conn, symb, date, "MIN(price)");
        System.out.println("Volume Traded for " + symb + " for " + date);
        runQuery(conn, symb, date, "SUM(volume)");
        System.out.println("Closing Price for " + symb + " for " + date);
        closePrice(conn, symb, date);

        return 0;
    }

    private static int runQuery(Connection conn, String symb, String date, String type) {
        String dateString;
        if (date.length() == 2) {
            dateString = "'%-[DATE]-%'";
        } else {
            dateString = "'[DATE]%'";
        }
        String queryString = "select [TYPE] from stocks where date like "+ dateString +" and symbol = '[SYMBOL]';";

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

    //I made this method after the other 3 so I couldnt really figure out how to
    //extend that one. So we get this...
    private static int closePrice(Connection conn, String symb, String date)  {
        String dateString;
        if (date.length() == 2) {
            dateString = "'%-[DATE]-%'";
        } else {
            dateString = "'[DATE]%'";
        }
        String queryString = "select price from stocks where symbol = '[SYM]' " +
                "and date = (select max(date) from stocks where date like " + dateString + " and symbol = '[SYM]');";

        queryString = queryString.replace("[SYM]", symb);
        queryString = queryString.replace("[DATE]", date);

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
