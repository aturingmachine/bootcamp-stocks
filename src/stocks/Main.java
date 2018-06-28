package stocks;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String f = System.getProperty("user.dir") + "/data/week1-stocks.json";

        try {
            //Get a new DB Connection
            Connection conn = DBConnection.getConn();
            if (conn != null) {
                System.out.println("Connected");

                //Will run a 'migration' just drops the table if it exists and creates it again
                System.out.println("Migrating Database...");
                int migrateCode = DBSeeder.migrateDB(conn);

                //If we successfully migrated then we can seed the data from the JSON file
                if (migrateCode == 0) {
                    System.out.println("Seeding Database...");
                    DBSeeder.getDataFromFile(conn, f);
                } else {
                    System.exit(migrateCode);
                }
                System.out.print("\033[H\033[2J");
                System.out.flush();

                Scanner sc = new Scanner(System.in);
                String symb = "";
                String date = "";
                int funcExit = 0;

                while (!symb.equals("exit") || funcExit == 0) {
                    System.out.println("Enter A Stock Symbol, or exit to quit:");
                    symb = sc.nextLine();
                    if (symb.equals("exit")) {
                        break;
                    }
                    System.out.println("Enter A Date (i.e. 2018-06-26) or Month (i.e. 06): ");
                    date = sc.nextLine();
                    funcExit = Executor.runFunc(symb, date, conn);
                }
            }
            //Close the DB Connection
            System.out.println("Closing DB Connection...");
            conn.close();
            System.out.println("DB Connection Closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
