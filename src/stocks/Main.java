package stocks;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            //Get a new DB Connection
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("Connected");

                //Will run a 'migration' just drops the table if it exists and creates it again
                System.out.println("Migrating Database...");
                int migrateCode = DBSeeder.migrateDB(conn);
                System.out.println("DB Migration - Exit Code: " + migrateCode);

                //If we successfully migrated then we can seed the data from the JSON file
                if (migrateCode == 0) {
                    System.out.println("Seeding Database...");
                    int seedCode = DBSeeder.getDataFromUrl(conn, "file:///Users/vincentblom/Desktop/week1-stocks.json");
                    System.out.println("DB Seeder - Exit Code: " + seedCode);
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
