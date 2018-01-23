package bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;

//any other text that can be sent to bot?
//reply = some reply

public class SimpleMessageHandler {
    private Message message;//Message message needed for reply
    private User user;

    public SimpleMessageHandler(Message message, User user) {
        this.message = message;
        this.user = user;
    }
    public SendMessage getReply(){
        //do something

        return new SendMessage()
                .setChatId(String.valueOf(user.getId()))
                .setText("This bot is not smart yet");
    }
}
