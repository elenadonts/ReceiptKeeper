package processed;

import java.time.LocalDate;

public class ProcessedReceipt {

    private LocalDate receiptDate;
    private double total;

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public double getTotal() {
        return total;
    }

    public ProcessedReceipt(LocalDate receiptDate, double total) {
        this.receiptDate = receiptDate;
        this.total = total;
    }


}
