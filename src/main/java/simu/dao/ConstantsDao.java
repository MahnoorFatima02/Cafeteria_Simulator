package simu.dao;

import config.MariaDbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConstantsDao {

    public Map<String, Double> loadConstants() {
        System.out.println("*******");
        System.out.println("IN LOAD CONSTANTS");
        System.out.println("*******");
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
            System.out.println("*******");
            System.out.println(constantsMap);
            System.out.println("*******");


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return constantsMap;
    }
}