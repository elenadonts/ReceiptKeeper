package bot;

import globals.GLOBALS;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ReceiptKeeperBot extends TelegramLongPollingBot {

    private static final Logger log = Logger.getLogger(ReceiptKeeperBot.class);
    private static ReplyKeyboardMarkup replyKeyboardMarkup;

    static {
        List<String> firstRowNames = new ArrayList<>(Arrays.asList("/Silpo&ATB", "/Koshik&SAM-Market"));
        List<String> secondRowNames = new ArrayList<>(Arrays.asList("/Edit Last", "/Get Last"));
        List<String> thirdRowNames = new ArrayList<>(Arrays.asList("/Get Week", "/Get Month"));
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();
        KeyboardRow thirdRow = new KeyboardRow();
        for (int i = 0; i < firstRowNames.size(); i++) {
            firstRow.add(firstRowNames.get(i));
            secondRow.add(secondRowNames.get(i));
            thirdRow.add(thirdRowNames.get(i));
        }
        List<KeyboardRow> keyboardButtons = new ArrayList<>(Arrays.asList(firstRow, secondRow, thirdRow));
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardButtons);
    }

    @Override
    public String getBotUsername() {
        return getBotPropertyByKey("BotName");
    }

    @Override
    public String getBotToken() {
        return getBotPropertyByKey("BotToken");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) return;

        Message message = update.getMessage();
        User user = message.getFrom();

        if (isPicture(message) || isDocument(message)){
            sendReplyToUser(handlePicture(user, update));
            return;
        }
        if (isCommand(message)) {
            sendReplyToUser(handleCommand(user, message));
            return;
        }
        if (isSimpleMessage(message)) {
            sendReplyToUser(handleSimpleMessage(user, message));
            return;
        }
    }

    private SendMessage handlePicture(User user, Update update){
        SendMessage result = null;
        String imageUrl = getImageUrl(update);
        try {
            InputStream is = new URL(imageUrl).openStream();
            BufferedImage image = ImageIO.read(is);
            result = new ImageHandler(image, user).getReply();
        } catch (IOException e) {
            log.error(e);
        }
        return result;
    }

    private SendMessage handleCommand(User user, Message message){
        String msgText = message.getText();

        Command command = parse(msgText);
        return new CommandHandler(command, user).getReply();

    }

    private SendMessage handleSimpleMessage(User user, Message message){
        return new SimpleMessageHandler(message, user).getReply();
    }

    private boolean isSimpleMessage(Message message){
        return message.hasText();
    }

    private boolean isPicture(Message message){
        return message.hasPhoto();
    }

    private boolean isDocument(Message message) {
        return message.hasDocument();
    }

    private boolean isCommand(Message message){
        return message.hasText() && message.getText().charAt(0) == GLOBALS.COMMAND_FIST_SYMBOL;
    }

    private String getImageUrl(Update update){
        Message message = update.getMessage();

        String botToken = getBotPropertyByKey("BotToken");
        String filePath = "", fileId = "";
        if (isDocument(message)){
            Document pictureInDocument = message.getDocument();
            fileId = pictureInDocument.getFileId();
        }
        if (isPicture(message)){
            List<PhotoSize> photoSizes = update.getMessage().getPhoto();
            fileId = photoSizes.get(photoSizes.size()-1).getFileId();//getting the largest photo
        }

        GetFile getFile = new GetFile().setFileId(fileId);

        try {
            org.telegram.telegrambots.api.objects.File file = execute(getFile);
            filePath = file.getFilePath();
        } catch (TelegramApiException e) {
            log.error(e);
        }
        return GLOBALS.ROOT_FILE_URL + GLOBALS.BOT + botToken + GLOBALS.PATH_SEPARATOR + filePath;
    }

    private void sendReplyToUser(SendMessage sendMessage){
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e);
        }
    }

    private static Command parse(String name) {
        for (Command value : Command.values()) {
            if (value.getName().equals(name.toLowerCase()))
                return value;
        }
        return Command.UNKNOWN_COMMAND;
    }

    private static String getBotPropertyByKey(String key){
        Properties properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream((GLOBALS.BOT_PROPERTIES_PATH));
            properties.load(inputStream);
            return properties.getProperty(key);
        }
        catch (IOException e){
            log.error(e);
        }
        throw new IllegalArgumentException();
    }
}
