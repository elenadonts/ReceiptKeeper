package db;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.User;

import java.sql.Connection;
import java.sql.SQLException;

public class DBRecord {

    private static Connection connection;
    private static final Logger log = Logger.getLogger(DBRecord.class.getName());

    static {
        try {
            connection = DBConnection.getInstance().getConnection();
        }
        catch (SQLException e){
            log.error(e);
        }
    }
    public static boolean userExists(User user){
        return false;////////////////////////////////////////////////////////////////////
    }
}
