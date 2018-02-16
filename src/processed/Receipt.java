package processed;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Receipt {

    private LocalDate receiptDate;
    private double total;

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public double getTotal() {
        return total;
    }

    public Receipt(LocalDate receiptDate, double total) {
        this.receiptDate = receiptDate;
        this.total = total;
    }


}
