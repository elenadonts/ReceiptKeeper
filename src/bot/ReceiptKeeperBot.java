package bot;

import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class ReceiptKeeperBot extends TelegramLongPollingBot {

    private static final String ROOT_FILE_URL = "https://api.telegram.org/file/";

    @Override
    public String getBotToken() {
        return getPropertyByKey("BotToken");
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage reply = null;

        if (update.hasMessage()) {
            Message message = update.getMessage();
            User user = message.getFrom();

            if (isPicture(message)){
                reply = processPhotoAndGetResult(update, user);
                //process, save result to db and send to user
                //reply = processed result
            }
            else if (isCommand(message)) {
                String msgText = message.getText();
                Command command = getCommand(msgText);
                reply = new CommandHandler(command, user).getReply();
            }
            else if (isSimpleMessage(message)) {
                //any other text that can be sent to bot?
                //reply = some reply
                reply = new SendMessage()
                        .setChatId(String.valueOf(user.getId()))
                        .setText("This bot is not smart yet");
            }
        }
        sendReplyToUser(reply);
    }

    private boolean isSimpleMessage(Message message){
        return message.hasText();
    }

    private boolean isPicture(Message message){
        return message.hasPhoto();
    }

    private boolean isCommand(Message message){
        return message.hasText() && message.getText().charAt(0) == '/';
    }

    private SendMessage processPhotoAndGetResult(Update update, User user){
        SendMessage result = null;
        String imageUrl = getImageUrl(update);
        try {
            InputStream is = new URL(imageUrl).openStream();
            BufferedImage image = ImageIO.read(is);
            ImageIO.write(image,"jpg", new File("img.jpg"));

            result = new SendMessage()
                    .setChatId(String.valueOf(user.getId()))
                    .setText("Picture received");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getImageUrl(Update update){
        String botToken = getPropertyByKey("BotToken");
        String filePath = "";
        List<PhotoSize> photoSizes = update.getMessage().getPhoto();
        String fileId = photoSizes.get(photoSizes.size()-1).getFileId();//getting the largest photo
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);

        try {
            org.telegram.telegrambots.api.objects.File file = execute(getFile);
            filePath = file.getFilePath();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return ROOT_FILE_URL + "bot" + botToken + '/' + filePath;
    }

    private void sendReplyToUser(SendMessage sendMessage){
        try {
            execute(sendMessage); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private Command getCommand(String commandName){
        Command command;
        switch (commandName){
            case "/start" : command = Command.START;
                break;
            case "/help" : command = Command.HELP;
                break;
            case "/getcode" : command = Command.GET_CODE;
                break;
            case "/getlast" : command = Command.GET_LAST;
                break;
            case "/getweek" : command = Command.GET_WEEK;
                break;
            case "/getmonth" : command = Command.GET_MONTH;
                break;
            default: command = Command.COMMAND_UNKNOWN;
                break;
        }
        return command;
    }
    @Override
    public String getBotUsername() {
        return getPropertyByKey("BotName");
    }

    private static String getPropertyByKey(String key){
        Properties properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(("config/bot/bot.properties"));
            properties.load(inputStream);
            return properties.getProperty(key);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
