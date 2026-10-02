package model;

import java.time.LocalDate;

public class BorrowRecord {
    private String memberId;
    private String bookTitle;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;

    public BorrowRecord(String memberId, String bookTitle, LocalDate issueDate, LocalDate dueDate, String status) {
        this.memberId = memberId;
        this.bookTitle = bookTitle;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
        this.returnDate = null;
    }

    public String getMemberId() { return memberId; }
    public String getBookTitle() { return bookTitle; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return memberId + " - " + bookTitle;
    }
}