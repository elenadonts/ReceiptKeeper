package bot;

import db.DBRecord;
import globals.GLOBALS;
import image.Image;
import org.apache.log4j.Logger;
import org.opencv.core.Core;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.User;
import processed.Receipt;

import java.awt.image.BufferedImage;
import java.time.LocalDate;


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

        Receipt result = imageToProcess.processAndGetResult();
        DBRecord.addNewReceipt(user, result);

        return composeReply(result);
    }

    private SendMessage composeReply(Receipt processedReceipt) {
        LocalDate receiptDate = processedReceipt.getReceiptDate();
        double total = processedReceipt.getTotal();
        return new SendMessage()
                .setChatId(String.valueOf(user.getId()))
                .setText("Total - " + total + '\n' + "Date - " + receiptDate.format(GLOBALS.USER_DATE_FORMAT));

    }
}
