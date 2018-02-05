package db;

import globals.GLOBALS;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static DBConnection instance;
    private Connection connection;
    private static final Logger log = Logger.getLogger(DBConnection.class);

    private DBConnection() throws SQLException {
        try {
            Class.forName(GLOBALS.JDBC_CLASS_NAME);
            Properties DBProperties = getDBProperties();
            connection = DriverManager.getConnection(DBProperties.getProperty("DBUrl"),
                    DBProperties.getProperty("DBUser"),
                    DBProperties.getProperty("DBUserPassword"));
        }
        catch (ClassNotFoundException e){
            log.error(e);
        }
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

    private Properties getDBProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream((GLOBALS.DATABASE_PROPERTIES_PATH));
            properties.load(inputStream);
        }
        catch (IOException e){
            log.error(e);
        }
        return properties;
    }
}
