package image;



import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import processed.ProcessedReceipt;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class ImageRecognizer {

    private BufferedImage image;

    public ImageRecognizer(BufferedImage image) {
        this.image = image;
    }
    public ProcessedReceipt process(){
        String[] allReceiptLines = doOcr();
        double total = getTotal(allReceiptLines);
        return new ProcessedReceipt(LocalDateTime.now(), total);

    }
    private double getTotal(String[] allReceiptLines){
        StringBuilder totalBuilder = new StringBuilder();
        for (String s : allReceiptLines){
            if (s.contains("СУМА")){
                System.out.println(s);
                char[] chars = s.toCharArray();
                for (int i = 0; i < chars.length; i++){
//                    if (Character.isDigit(chars[i])&& i > chars.length/2) totalBuilder.append(chars[i]);
                    if (Character.isDigit(chars[i])&& i > s.indexOf("СУМА")) totalBuilder.append(chars[i]);
                }
                return Double.parseDouble(totalBuilder.toString())/100;
            }
        }
        return 0.0;
    }
    private String[] doOcr(){
        String recognized = "";
        Tesseract instance = new Tesseract();
        try {
            instance.setLanguage("atbFont+digits");
//            instance.setLanguage("ukr");
            recognized = instance.doOCR(image);

            System.out.println(recognized);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return recognized.split("\n");

    }
}
