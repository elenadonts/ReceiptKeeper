package image;

import org.apache.log4j.Logger;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Image {
    private Logger log = Logger.getLogger(Image.class);
    private BufferedImage image;

    public Image(BufferedImage image) {
        this.image = image;
    }



    public String processAndGetResult() {
        Mat original = bufferedImageToMat(image);
        PreparedImage preparedImage = new PreparedImage(original);
        Mat readyForRecognition = preparedImage.prepare();
        // RECOGNIZE
        log.info("Picture prepared");
        return "Picture received";
    }

    private Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }
}
