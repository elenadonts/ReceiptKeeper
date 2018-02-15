package bot;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;

import java.text.SimpleDateFormat;

public class SimpleMessageHandler implements ReceivedDataHandler {

    private Logger log = Logger.getLogger(SimpleMessageHandler.class);
    private Message message;
    private User user;

    public SimpleMessageHandler(Message message, User user) {
        this.message = message;
        this.user = user;
    }
    public SendMessage getReply(){
        if (CommandHandler.lastCommandExecuted == Command.EDIT_LAST_BUTTON ||
                CommandHandler.lastCommandExecuted == Command.EDIT_LAST){
            editLastReceiptDetails();
            log.info("Last receipt edited");
            return composeReply("Changes saved");

        }
        log.info("Simple message received");
        return composeReply("Unknown command");
    }

    private void editLastReceiptDetails(){
        //TODO:edit
    }
    private SendMessage composeReply(String messageText){
        return new SendMessage()
                .setChatId(String.valueOf(user.getId()))
                .setText(messageText);
    }
}
