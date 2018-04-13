package image;

import org.apache.log4j.Logger;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import processed.Receipt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Image {
    private Logger log = Logger.getLogger(Image.class);
    private BufferedImage image;

    public Image(BufferedImage image) {
        this.image = image;
    }



    public Receipt processAndGetResult() {
        Mat original = bufferedImageToMat(image);
        ImagePreparer preparedImage = new ImagePreparer(original);
        Mat preparedMat = preparedImage.prepare();
        log.info("Picture is prepared");

        BufferedImage imageToRecognize = matToBufferedImage(preparedMat);
        ImageRecognizer recognizedImage = new ImageRecognizer(imageToRecognize);
        Receipt processedReceipt = recognizedImage.process();
        log.info("Picture is recognized");

        return processedReceipt;
    }

    private static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    private static BufferedImage matToBufferedImage(Mat mat){
        byte[] allPixelsData = new byte[mat.rows() * mat.cols() * (int) (mat.elemSize())];
        mat.get(0, 0, allPixelsData);
        BufferedImage bi = new BufferedImage(mat.cols(),
                mat.rows(), BufferedImage.TYPE_BYTE_BINARY);
        bi.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), allPixelsData);
        return bi;
    }
}
