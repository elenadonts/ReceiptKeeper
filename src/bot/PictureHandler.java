package bot;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureHandler {
    private BufferedImage image;
    private User user;
    private static final Logger log = Logger.getLogger(PictureHandler.class.getName());

    public PictureHandler(BufferedImage image, User user) {
        this.image = image;
        this.user = user;
    }

    public SendMessage getReply(){
        try {
            ImageIO.write(image,"jpg", new File("img.jpg"));
        }
        catch (IOException e){
            log.error(e);
        }

        return new SendMessage()
                .setChatId(String.valueOf(user.getId()))
                .setText("Picture received");
    }
    //process, save result to db and send to user ->
    //        checkImage();
    //        processImage();
    //        populateResults();

}
