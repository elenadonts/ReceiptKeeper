package db;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.User;
import processed.ProcessedReceipt;

import java.sql.Connection;
import java.sql.SQLException;

public class DBRecord {

    private static Connection connection;
    private static final Logger log = Logger.getLogger(DBRecord.class);

    static {
        try {
            connection = DBConnection.getInstance().getConnection();
        }
        catch (SQLException e){
            log.error(e);
        }
    }
    public static boolean userExists(User user){
        throw new UnsupportedOperationException();
    }
    public static void addNewUser(User user){
        throw new UnsupportedOperationException();

    }
    public static void addNewReceipt(User user, ProcessedReceipt receipt){
        throw new UnsupportedOperationException();
    }

}
