package db;

import org.telegram.telegrambots.api.objects.User;

import java.sql.Connection;
import java.sql.SQLException;

public class DBRecord {
    private static Connection connection;
    static {
        try {
            connection = DBConnection.getInstance().getConnection();
        }
        catch (SQLException e){
            e.getMessage();
        }
    }
    public static boolean userExists(User user){
        return false;////////////////////////////////////////////////////////////////////
    }
}
