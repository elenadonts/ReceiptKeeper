package bot;

import db.DBRecord;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.User;

public class CommandHandler {

    private Command command;
    private User user;

    public CommandHandler(Command command, User user) {
        this.command = command;
        this.user = user;
    }

    public SendMessage getReply(){
        String replyText;
        switch (command){
            case START: replyText = getStartMessage();
                break;
            case HELP: replyText = getHelpMessage();
                break;
            case GET_CODE: replyText = getUserCode();
                break;
            case GET_LAST: replyText = getLastReceipt();
                break;
            case GET_WEEK: replyText = getWeekStats();
                break;
            case GET_MONTH: replyText = getMonthStats();
                break;
            default: replyText = "Unknown command";
                break;
        }
        return formReply(replyText);
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

    private String getUserCode() {
        return "Your personal code is " + user.getId();
    }

    private String getHelpMessage() {
        return "/help - See all commands\n" +
                "/getcode - Get your authorization code\n" +
                "/getlast - Get last receipt's details\n" +
                "/getweek - Get your week stats\n" +
                "/getmonth - Get your month stats";
    }

    private String getStartMessage() {
        if (!DBRecord.userExists(user)){
            //add user
        }
        return "Hi, " + user.getFirstName() + "! Send me a receipt and I will save it for you.";
    }

    private SendMessage formReply(String messageText){
        return new SendMessage()
                .setChatId(String.valueOf(user.getId()))
                .setText(messageText);
    }


}
