package stocks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class DBSeeder {

    public static int getDataFromFile (Connection conn, String fileString) {
        try {
            System.out.println("Reading File From Disk...");

            ObjectMapper mapper = new ObjectMapper(); //New up a Jackson Object Mapper
            List<StockData> data = mapper.readValue(new File(fileString), new TypeReference<List<StockData>>(){});

            return seedDb(conn, data);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        } catch (SQLException e) {
            e.printStackTrace();
            return 3;
        } catch (ParseException e) {
            e.printStackTrace();
            return 7;
        }

    }

    public static int seedDb(Connection conn, List<StockData> data) throws SQLException, ParseException {
        String insertQuery = "INSERT INTO stocks (symbol, price, volume, date) values(?, ?, ?, ?)";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

        System.out.println("Beginning Data Load..");
        PreparedStatement stmt = conn.prepareStatement(insertQuery);
        for (StockData datum : data) {
            try {
                stmt.setString(1, datum.getSymbol());
                stmt.setDouble(2, datum.getPrice());
                stmt.setInt(3, datum.getVolume());
                stmt.setTimestamp(4, new Timestamp(formatter.parse(datum.getDate().replace("T", " ")).getTime()));
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return 4;
            }
        }
        stmt.close();

        System.out.println("Data Load Finished.");

        return 0;
    }

    public static int migrateDB(Connection conn) {
        String dropString = "DROP TABLE IF EXISTS stocks";
        String createString = "create table stocks ( id int not null auto_increment, " +
                "symbol varchar(4) not null, price float not null, volume int not null, " +
                "date timestamp not null, primary key (id) );";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(dropString);
        } catch (SQLException e) {
            e.printStackTrace();
            return 5;
        }
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return 6;
        }

        return 0;
    }
}
