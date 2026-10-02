package Controllers;

import DB.Database;
import model.Book;
import model.BorrowRecord;
import model.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class IssueBookController {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnIssueBook;

    @FXML
    private ComboBox<Book> cmbBook;

    @FXML
    private ComboBox<Member> cmbMember;

    @FXML
    private DatePicker dpDueDate;

    @FXML
    private DatePicker dpIssueDate;

    @FXML
    public void initialize() {

        loadMembers();
        loadBooks();

        dpIssueDate.setValue(LocalDate.now());
        dpDueDate.setValue(LocalDate.now().plusDays(14));
    }

    private void loadMembers() {
        ObservableList<Member> memberList = FXCollections.observableArrayList(Database.memberList);
        cmbMember.setItems(memberList);
    }

    private void loadBooks() {
        ObservableList<Book> bookList = FXCollections.observableArrayList(Database.bookList);
        cmbBook.setItems(bookList);
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnIssueBookOnAction(ActionEvent event) {
        Member selectedMember = cmbMember.getValue();
        Book selectedBook = cmbBook.getValue();
        LocalDate issueDate = dpIssueDate.getValue();
        LocalDate dueDate = dpDueDate.getValue();


        if (selectedMember == null || selectedBook == null || issueDate == null || dueDate == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select all required details!");
            return;
        }

        if (dueDate.isBefore(issueDate)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Due date cannot be before issue date!");
            return;
        }

        if (selectedBook.getQuantity() <= 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Selected book is out of stock!");
            return;
        }


        BorrowRecord record = new BorrowRecord(
                selectedMember.getMemberId(),
                selectedBook.getTitle(),
                issueDate,
                dueDate,
                "Borrowed"
        );

        Database.borrowList.add(record);


        selectedBook.setQuantity(selectedBook.getQuantity() - 1);


        if (DashboardController.getInstance() != null) {
            DashboardController.getInstance().updateDashboardCounts();
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Book issued successfully!");
        clearFields();
    }

    private void clearFields() {
        cmbMember.setValue(null);
        cmbBook.setValue(null);
        dpIssueDate.setValue(LocalDate.now());
        dpDueDate.setValue(LocalDate.now().plusDays(14));
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}