package image;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

public class ImagePreparer {

    private Mat image;
    private static final int PIXEL_NEIGHBOURHOOD = 21;
    private static final int MEAN_VALUE_FOR_CONTOURS = 3;
    private static final int MEAN_VALUE_FOR_RECOGNITION = 15;


    public ImagePreparer(Mat image) {
        this.image = image;
    }

    public Mat prepare(){

        Mat initiallyPrepared = binarizeAndThickenElements(image);
        MatOfPoint2f biggestContour = findBiggestContour(initiallyPrepared);
        RotatedRect biggestRectangle = retrieveRectangleFromContour(biggestContour);
        RotatedRect potentialReceipt = adjustDimensions(biggestRectangle);
        rotateOriginalImageToRightAngle(potentialReceipt);
        cropOriginalImage(potentialReceipt);

        Imgcodecs.imwrite("cropped.jpg", image);//for debug
        image = binarize(image, MEAN_VALUE_FOR_RECOGNITION);

        Imgcodecs.imwrite("binarized-cropped.png", image);//for debug

        Imgproc.blur(image, image,new Size(2,2));
        return image;
    }

    private Mat binarize(Mat source, int meanValue){
        Mat gray = new Mat();
        Imgproc.cvtColor(source, gray,Imgproc.COLOR_RGB2GRAY);
        Mat normalized = new Mat();
        Core.normalize(gray, normalized, 0,255, Core.NORM_MINMAX, CvType.CV_8UC3);
        Mat binary = new Mat();
        Imgproc.adaptiveThreshold(gray, binary, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY,
                PIXEL_NEIGHBOURHOOD, meanValue);
        Imgproc.erode(binary, binary, new Mat());
        Imgproc.dilate(binary, binary, new Mat());

        return binary;
    }

    private Mat binarizeAndThickenElements(Mat source){
        Mat binary = binarize(source, MEAN_VALUE_FOR_CONTOURS);
        Mat eroded = new Mat();
        Imgproc.erode(binary, eroded, new Mat());
        return eroded;
    }

    private MatOfPoint2f findBiggestContour(Mat binarizedImage){
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(binarizedImage, contours, new Mat(),
                Imgproc.CHAIN_APPROX_NONE,Imgproc.CHAIN_APPROX_SIMPLE);

        double maxArea = 0;
        int index = 0;
        for (int i = 0; i < contours.size(); i++){
            MatOfPoint contour = contours.get(i);
            if (maxArea < Imgproc.contourArea(contour)){
                maxArea = Imgproc.contourArea(contour);
                index = i;
            }
        }
        //for debug begin
        Mat clone = image.clone();
        Imgproc.drawContours(clone, contours, index,new Scalar(255,0,0), 3);
        Imgcodecs.imwrite("contours.jpg", clone);
        //end

        MatOfPoint2f biggestContour = new MatOfPoint2f();
        contours.get(index).convertTo(biggestContour, CvType.CV_32FC2);
        return biggestContour;
    }

    private RotatedRect retrieveRectangleFromContour(MatOfPoint2f biggestContour){
        //for debug begin
        RotatedRect rotatedRect = Imgproc.minAreaRect(biggestContour);
        Point[] vertices = new Point[4];
        rotatedRect.points(vertices);
        List<MatOfPoint> boxContours = new ArrayList<>();
        boxContours.add(new MatOfPoint(vertices));
        Mat imgClone = image.clone();
        Imgproc.drawContours(imgClone, boxContours, 0, new Scalar(0, 255, 0), 4);
        Imgcodecs.imwrite("rectangle.jpg", imgClone);
        //end

        return Imgproc.minAreaRect(biggestContour);
    }

    private RotatedRect adjustDimensions(RotatedRect source){
        if (source.angle < -45) {
            source.angle = source.angle + 90.f;
            source.size = new Size(source.size.height, source.size.width);//swapping width and height
        }
        return source;
    }

    private void rotateOriginalImageToRightAngle(RotatedRect rotatedRectangle){
        Mat rotImage = Imgproc.getRotationMatrix2D(rotatedRectangle.center, rotatedRectangle.angle, 1.0);
        Size size = new Size(image.width(), image.height());
        Imgproc.warpAffine(image, image, rotImage, size, Imgproc.INTER_CUBIC);
    }

    private void cropOriginalImage(RotatedRect potentialReceipt){
        Imgproc.getRectSubPix(image, potentialReceipt.size, potentialReceipt.center, image);
    }
}
