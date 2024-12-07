package simu.dao;

import config.MariaDbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code ConstantsDao} class provides functionality to retrieve constant values from a database.
 * This class interacts with a MariaDB database to fetch key-value pairs of constants stored in the "Constants" table.
 * The constants are returned as a map with their names as keys and corresponding double values.
 *
 */
public class ConstantsDao {

    public Map<String, Double> loadConstants() {
        String query = "SELECT name, value FROM Constants";
        Map<String, Double> constantsMap = new HashMap<>();

        try (Connection conn = MariaDbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                double value = rs.getDouble("value");
                constantsMap.put(name, value);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return constantsMap;
    }
}