package globals;

import java.time.format.DateTimeFormatter;

public final class GLOBALS {

    private GLOBALS(){}

    public static final char PATH_SEPARATOR = '/';
    public static final char COMMAND_FIST_SYMBOL = '/';
    public static final String ROOT_FILE_URL = "https://api.telegram.org/file/";
    public static final String BOT = "bot";
    public static final String BOT_PROPERTIES_PATH = "src/resources/bot.properties";
    public static final String DATABASE_PROPERTIES_PATH = "src/resources/database.properties";
    public static final String JDBC_CLASS_NAME = "com.mysql.jdbc.Driver";
    public static final String SILPO_ATB_FONT = "atbFont+digits";
    public static final String KOSHIK_SAM_FONT = "ukr";
    public static final DateTimeFormatter USER_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final String CURRENT_YEAR = "2018";
    public static final String TOTAL = "СУМА";
    public static final DateTimeFormatter RECEIPT_DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy");
}
