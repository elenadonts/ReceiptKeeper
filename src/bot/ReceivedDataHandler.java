package bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;

public interface ReceivedDataHandler {
    SendMessage getReply();
}
