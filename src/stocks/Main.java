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
                System.out.println("DB Migration - Exit Code: " + migrateCode);

                //If we successfully migrated then we can seed the data from the JSON file
                if (migrateCode == 0) {
                    System.out.println("Seeding Database...");
                    int seedCode = DBSeeder.getDataFromFile(conn, f);
                    System.out.println("DB Seeder - Exit Code: " + seedCode);
                }

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
                    System.out.println("Enter A Date: ");
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
