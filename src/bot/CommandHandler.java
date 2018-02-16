package bot;

import db.DBRecord;
import globals.GLOBALS;
import image.ImageRecognizer;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.User;

public class CommandHandler implements ReceivedDataHandler {

    private static Logger log = Logger.getLogger(CommandHandler.class);
    private Command command;
    private User user;
    public static Command lastCommandExecuted = Command.START;

    public CommandHandler(Command command, User user) {
        this.command = command;
        this.user = user;
    }

    public SendMessage getReply(){
        log.info(command + " command executing");
        String replyText;
        switch (command){
            case START : replyText = getStartMessage();
                break;
            case HELP: replyText = getHelpMessage();
                break;
            case EDIT_LAST_BUTTON:
            case EDIT_LAST: replyText = getEditedReplyText();
                break;
            case GET_LAST_BUTTON:
            case GET_LAST: replyText = getLastReceipt();
                break;
            case GET_WEEK_BUTTON:
            case GET_WEEK: replyText = getWeekStats();
                break;
            case GET_MONTH_BUTTON:
            case GET_MONTH: replyText = getMonthStats();
                break;
            case SILPO_ATB_BUTTON:
            case SILPO_ATB: replyText = setSilpoATBFontAndGetReply();
                break;
            case KOSHIK_SAMMARKET_BUTTON:
            case KOSHIK_SAMMARKET: replyText = setKoshikSAMFontAndGetReply();
            break;
            default: replyText = "Unknown command";
                break;
        }
        lastCommandExecuted = command;
        return composeReply(replyText);
    }

    private String setKoshikSAMFontAndGetReply() {
        ImageRecognizer.setFontFilesToUse(GLOBALS.KOSHIK_SAM_FONT);
        return "Send me a receipt from Koshik or SAM-Market!";
    }

    private String setSilpoATBFontAndGetReply() {
        ImageRecognizer.setFontFilesToUse(GLOBALS.SILPO_ATB_FONT);
        return "Send me a receipt from Silpo or ATB!";
    }

    private String getEditedReplyText() {
        return "Write total and date in format:\n"+
                "0.0\n"+
                "DD.MM.YYYY";
    }

    private String getMonthStats() {
        throw new UnsupportedOperationException();
    }

    private String getWeekStats() {
        throw new UnsupportedOperationException();
    }

    private String getLastReceipt() {
        throw new UnsupportedOperationException();

    }

    private String getHelpMessage() {
        return "/help - See all commands\n" +
                "/edit - Edit Total value of last receipt\n" +
                "/choose - Choose АТБ/Сільпо - Кошик/САМ-Маркет. By default - last chosen\n" +
                "/getcode - Get your authorization code\n" +
                "/getlast - Get last receipt's details\n" +
                "/getweek - Get your week stats\n" +
                "/getmonth - Get your month stats";
    }

    private String getStartMessage() {
        if (!DBRecord.userExists(user)){
            DBRecord.addNewUser(user);
        }
        return "Hi, " + user.getFirstName() + "! Send me a receipt and I will save it for you.";
    }

    private SendMessage composeReply(String messageText){
        return new SendMessage()
                .setChatId(String.valueOf(user.getId()))
                .setText(messageText);
    }


}
