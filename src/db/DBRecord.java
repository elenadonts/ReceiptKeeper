package db;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.User;
import processed.Receipt;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
        String toCheckIfExists = "SELECT user_id FROM users WHERE user_id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(toCheckIfExists);
            preparedStatement.setString(1, user.getId().toString());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                if (rs.getString(1).equals(user.getId().toString())) return true;
            }
        }
        catch (SQLException e){
            log.error(e);
        }
        return false;
    }

    public static void addNewUser(User user){
        String toAddNewUser = "INSERT INTO users (user_id, date_joined) VALUES (?, ?)";
        LocalDate currentDate = LocalDate.now();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(toAddNewUser);
            preparedStatement.setString(1,user.getId().toString());
            preparedStatement.setDate(2, Date.valueOf(currentDate));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void addNewReceipt(User user, Receipt receipt){
        String toAddNewReceipt = "INSERT INTO receipts (r_total, r_date, user_id, created_at) VALUES (?, ?, ?, ?)";
        try {
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            PreparedStatement preparedStatement = connection.prepareStatement(toAddNewReceipt);
            preparedStatement.setDouble(1, receipt.getTotal());
            preparedStatement.setDate(2, Date.valueOf(receipt.getReceiptDate()));
            preparedStatement.setString(3, user.getId().toString());
            preparedStatement.setTimestamp(4, now);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editLastUserReceipt(User user, Receipt editedReceipt) {
        try {
            String toDeleteLastUserReceipt = "DELETE FROM receipts WHERE user_id = ? ORDER BY created_at DESC limit 1";
            PreparedStatement preparedStatement = connection.prepareStatement(toDeleteLastUserReceipt);
            preparedStatement.setString(1, user.getId().toString());
            preparedStatement.executeUpdate();

            addNewReceipt(user, editedReceipt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
