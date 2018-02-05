import bot.ReceiptKeeperBot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;


public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new ReceiptKeeperBot());
            log.info("starting bot..");
        } catch (TelegramApiException e) {
            log.error(e);
        }
    }
}
