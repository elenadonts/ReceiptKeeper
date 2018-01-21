package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private DBConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Properties DBProperties = getDBProperties();
            connection = DriverManager.getConnection(DBProperties.getProperty("DBUrl"),
                    DBProperties.getProperty("DBUser"),
                    DBProperties.getProperty("DBUserPassword"));
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private Properties getDBProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(("config/database/database.properties"));
            properties.load(inputStream);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return properties;
    }

    public Connection getConnection() {
        return connection;
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }
}
