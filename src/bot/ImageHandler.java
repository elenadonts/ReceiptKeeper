package bot;

import image.Image;
import org.apache.log4j.Logger;
import org.opencv.core.Core;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.User;

import java.awt.image.BufferedImage;



public class ImageHandler implements ReceivedDataHandler {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private BufferedImage image;
    private User user;
    private static final Logger log = Logger.getLogger(ImageHandler.class);

    public ImageHandler(BufferedImage image, User user) {
        this.image = image;
        this.user = user;
    }

    public SendMessage getReply(){
        log.info("Picture received");
        Image imageToProcess = new Image(image);

        String result = imageToProcess.processAndGetResult();

        return composeReply(result);
    }

    private SendMessage composeReply(String messageText){
        return new SendMessage()
                .setChatId(String.valueOf(user.getId()))
                .setText(messageText);
    }



}
//process, save result to db and send to user ->
//        checkImage();
//        processImage();
//        populateResults();
