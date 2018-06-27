package stocks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class DBSeeder {

    public static int getDataFromUrl (Connection conn, String urlString) {
        try {
            //try and open a connection to the URL provided
            URL url = new URL(urlString);
            URLConnection dataConnection =  url.openConnection();
            System.out.println("Reading File From Disk...");

            ObjectMapper mapper = new ObjectMapper(); //New up a Jackson Object Mapper
            List<StockData> data = mapper.readValue(dataConnection.getInputStream(), new TypeReference<List<StockData>>(){});

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

        for (StockData datum : data) {
            try {
                PreparedStatement stmt = conn.prepareStatement(insertQuery);
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

        return 0;
    }

    public static int migrateDB(Connection conn) {
        String dropString = "DROP TABLE IF EXISTS stocks";
        String createString = "create table stocks ( id int not null auto_increment, " +
                "symbol varchar(4) not null, price float not null, volume int not null, " +
                "date timestamp not null, primary key (id) );";

        Statement dropStmt = null;
        try {
            dropStmt = conn.createStatement();
            dropStmt.executeUpdate(dropString);
        } catch (SQLException e) {
            e.printStackTrace();
            return 5;
        }

        Statement createStmt = null;
        try {
            createStmt = conn.createStatement();
            dropStmt.executeUpdate(createString);
        } catch (SQLException e) {
            e.printStackTrace();
            return 6;
        }

        return 0;
    }
}
