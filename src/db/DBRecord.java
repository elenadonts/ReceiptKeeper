package db;

import globals.GLOBALS;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.User;
import processed.Receipt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DBRecord {

    private static Connection connection;
    private static final Logger log = Logger.getLogger(DBRecord.class);
    private static final int DAYS_IN_A_MONTH = 31;
    private static final int DAYS_IN_A_WEEK = 7;

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
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(toAddNewUser);
            preparedStatement.setString(1,user.getId().toString());
            preparedStatement.setTimestamp(2, now);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e);
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
            log.error(e);
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
            log.error(e);
        }
    }

    public static class Statistics {

        public static String getLastReceipt(User user) {
            LocalDate date = LocalDate.now(); double total = 0.0;
            String toGetLastUserReceipt = "SELECT r_total, r_date FROM receipts WHERE " +
                    "user_id = ? ORDER BY created_at DESC limit 1";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(toGetLastUserReceipt);
                preparedStatement.setString(1, user.getId().toString());
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()){
                    total = rs.getDouble(1);
                    date = rs.getDate(2).toLocalDate();
                }
            } catch (SQLException e) {
                log.error(e);
            }
            String receiptDate = date.format(GLOBALS.USER_DATE_FORMAT);
            return composeReply("Last - " + receiptDate, total);
        }

        public static String getWeekStats(User user){
            LocalDate today = LocalDate.now();
            LocalDate weekBefore = today.minusDays(DAYS_IN_A_WEEK);
            double totalDuringPeriod = getTotalDuringPeriod(user, weekBefore, today);
            String dateStart = weekBefore.format(GLOBALS.USER_DATE_FORMAT);
            String dateEnd = today.format(GLOBALS.USER_DATE_FORMAT);
            return composeReply("Week " + dateStart + " - " + dateEnd, totalDuringPeriod);
        }

        public static String getMonthStats(User user) {
            LocalDate today = LocalDate.now();
            LocalDate monthBefore = today.minusDays(DAYS_IN_A_MONTH);
            double totalDuringPeriod = getTotalDuringPeriod(user, monthBefore, today);
            String dateStart = monthBefore.format(GLOBALS.USER_DATE_FORMAT);
            String dateEnd = today.format(GLOBALS.USER_DATE_FORMAT);
            return composeReply("Month " + dateStart + " - " + dateEnd, totalDuringPeriod);
        }

        private static double getTotalDuringPeriod(User user, LocalDate from, LocalDate to) {
            double total = 0.0;
            String toGetLastWeekReceipts = "SELECT SUM(r_total) FROM receipts " +
                    "WHERE user_id = ? AND r_date BETWEEN ? AND ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(toGetLastWeekReceipts);
                preparedStatement.setString(1, user.getId().toString());
                preparedStatement.setDate(2, Date.valueOf(from));
                preparedStatement.setDate(3, Date.valueOf(to));
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    total = rs.getDouble(1);
                    System.out.println(total);
                }
            } catch (SQLException e) {
                log.error(e);
            }
            return total;
        }

        private static String composeReply(String timePeriod, double moneySpent){
            Double moneySpentTruncated = BigDecimal.valueOf(moneySpent)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            return "Time period: " + timePeriod + "\n" +
                    "Money spent: " + moneySpentTruncated;
        }
    }
}
