package processed;

import java.time.Clock;
import java.time.LocalDateTime;

public class ProcessedReceipt {

    private LocalDateTime receiptDate;
    private double total;

    public LocalDateTime getReceiptDate() {
        return receiptDate;
    }

    public double getTotal() {
        return total;
    }



    public ProcessedReceipt(LocalDateTime receiptDate, double total) {
        this.receiptDate = receiptDate;
        this.total = total;
    }


}
