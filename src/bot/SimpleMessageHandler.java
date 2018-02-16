package bot;

import db.DBRecord;
import globals.GLOBALS;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import processed.Receipt;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

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
            try {
                editLastReceiptDetails();
            }
            catch (IllegalArgumentException e){
                log.info(e);
                return composeReply(e.getMessage());
            }

            log.info("Last receipt edited");
            CommandHandler.lastCommandExecuted = Command.UNKNOWN_COMMAND;
            return composeReply("Changes saved");

        }
        log.info("Simple message received");
        return composeReply("Unknown command");
    }

    private void editLastReceiptDetails(){
        LocalDate date = null; double total = 0;
        try {
            String[] totalAndDate = message.getText().split("\n");
            String totalValue = totalAndDate[0];
            String dateValue = totalAndDate[1];
            if (totalValue.contains(",")) totalValue = totalValue.replace(",", ".");

            date = LocalDate.parse(dateValue, GLOBALS.USER_DATE_FORMAT);
            total = Double.parseDouble(totalValue);
            }
            catch (Exception e){//indexOfOfBounds || parse
                throw new IllegalArgumentException("Wrong date or total");
        }

        Receipt editedReceipt = new Receipt(date, total);
        DBRecord.editLastUserReceipt(user, editedReceipt);
    }

    private SendMessage composeReply(String messageText){
        return new SendMessage()
                .setChatId(String.valueOf(user.getId()))
                .setText(messageText);
    }
}
