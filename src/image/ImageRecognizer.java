package image;



import globals.GLOBALS;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import processed.ProcessedReceipt;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

public class ImageRecognizer {

    private BufferedImage image;
    private static String fontFilesToUse;
    private static final int NUMBER_OF_DIGITS_IN_A_DATE = 4;
    private static final int NUMBER_OF_DIGITS_IN_A_YEAR = 4;

    static {
        setFontFilesToUse(GLOBALS.KOSHIK_SAM_FONT);
    }

    public static void setFontFilesToUse(String fontFilesToUse) {
        ImageRecognizer.fontFilesToUse = fontFilesToUse;
    }

    public ImageRecognizer(BufferedImage image) {
        this.image = image;
    }

    public ProcessedReceipt process(){
        String[] allReceiptLines = doOcr();
        double total = getTotal(allReceiptLines);
        LocalDate date = getDate(allReceiptLines);

        return new ProcessedReceipt(date, total);

    }
    private double getTotal(String[] allReceiptLines){
        StringBuilder totalBuilder = new StringBuilder();
        for (String s : allReceiptLines){
            if (s.contains(GLOBALS.TOTAL)){
                char[] lineChars = s.toCharArray();
                for (int i = 0; i < lineChars.length; i++){
                    if (Character.isDigit(lineChars[i])&& i > s.indexOf(GLOBALS.TOTAL)) totalBuilder.append(lineChars[i]);
                }
                return Double.parseDouble(totalBuilder.toString())/100;
            }
        }
        return 0.0;
    }

    private LocalDate getDate(String[] allReceiptLines){
        LocalDate localDate = LocalDate.now();

        StringBuilder lineDigits = new StringBuilder();
        for (int i = allReceiptLines.length/2; i < allReceiptLines.length; i++){
            if (allReceiptLines[i].contains(GLOBALS.CURRENT_YEAR)){
                try {
                    char[] lineChars = allReceiptLines[i].toCharArray();
                    for (int j = 0; j < lineChars.length; j++){
                        if (Character.isDigit(lineChars[j]))lineDigits.append(lineChars[j]);
                    }
                    String lineDigitsString = lineDigits.toString();
                    int indexOfDateStart = lineDigitsString.indexOf(GLOBALS.CURRENT_YEAR) - NUMBER_OF_DIGITS_IN_A_DATE;

                    String expectedDate = lineDigitsString.substring(indexOfDateStart,
                            indexOfDateStart + NUMBER_OF_DIGITS_IN_A_DATE + NUMBER_OF_DIGITS_IN_A_YEAR);
                    localDate = LocalDate.parse(expectedDate, GLOBALS.RECEIPT_DATE_FORMAT);
                }
                catch (Exception e){//IndexOutOfBounds if line incorrect || parseException
                    return localDate;
                }
            }
        }
        return localDate;
    }

    private String[] doOcr(){
        String recognized = "";
        Tesseract instance = new Tesseract();
        try {
            instance.setLanguage(fontFilesToUse);
            recognized = instance.doOCR(image);

            System.out.println(recognized);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return recognized.split("\n");

    }
}
