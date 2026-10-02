package Controllers;

import DB.Database;
import model.Book;
import model.BorrowRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReturnBookController {

    @FXML
    private Button btnBackDashboard;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnReturnBook;

    @FXML
    private ComboBox<BorrowRecord> cmbBorrowedBooks;

    @FXML
    private DatePicker dpBorrowedDate;

    @FXML
    private DatePicker dpDueDate;

    @FXML
    private DatePicker dpReturnDate;

    @FXML
    private Label lblOverdueStatus;

    @FXML
    private TextField txtMemberInfo;

    @FXML
    public void initialize() {

        loadBorrowedRecords();


        dpReturnDate.setValue(LocalDate.now());


        cmbBorrowedBooks.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedRecord) -> {
            if (selectedRecord != null) {
                populateFields(selectedRecord);
            } else {
                clearFields();
            }
        });


        dpReturnDate.valueProperty().addListener((observable, oldValue, newReturnDate) -> {
            if (cmbBorrowedBooks.getValue() != null && newReturnDate != null) {
                checkOverdueStatus(cmbBorrowedBooks.getValue().getDueDate(), newReturnDate);
            }
        });
    }

    private void loadBorrowedRecords() {
        ObservableList<BorrowRecord> activeRecords = FXCollections.observableArrayList();
        for (BorrowRecord record : Database.borrowList) {
            if ("Borrowed".equalsIgnoreCase(record.getStatus())) {
                activeRecords.add(record);
            }
        }
        cmbBorrowedBooks.setItems(activeRecords);
    }

    private void populateFields(BorrowRecord record) {
        txtMemberInfo.setText(record.getMemberId());
        dpBorrowedDate.setValue(record.getIssueDate());
        dpDueDate.setValue(record.getDueDate());

        checkOverdueStatus(record.getDueDate(), dpReturnDate.getValue());
    }

    private void checkOverdueStatus(LocalDate dueDate, LocalDate returnDate) {
        if (dueDate != null && returnDate != null) {
            if (returnDate.isAfter(dueDate)) {
                long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
                lblOverdueStatus.setText("OVERDUE BY " + daysOverdue + " DAY(S)!");
                lblOverdueStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            } else {
                lblOverdueStatus.setText("ON TIME");
                lblOverdueStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            }
        }
    }

    @FXML
    void btnBackDashboardOnAction(ActionEvent event) {
        if (btnBackDashboard.getScene().lookup("#contentArea") instanceof AnchorPane) {
            AnchorPane contentArea = (AnchorPane) btnBackDashboard.getScene().lookup("#contentArea");
            contentArea.getChildren().clear();
        } else if (btnBackDashboard.getScene().lookup("#contentArea") instanceof StackPane) {
            StackPane contentArea = (StackPane) btnBackDashboard.getScene().lookup("#contentArea");
            contentArea.getChildren().clear();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnReturnBookOnAction(ActionEvent event) {
        BorrowRecord selectedRecord = cmbBorrowedBooks.getValue();

        if (selectedRecord == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a borrowed record!");
            return;
        }

        LocalDate returnDate = dpReturnDate.getValue();
        if (returnDate == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a valid return date!");
            return;
        }


        selectedRecord.setStatus("Returned");
        selectedRecord.setReturnDate(returnDate);


        for (Book book : Database.bookList) {
            if (book.getTitle().equals(selectedRecord.getBookTitle())) {
                book.setQuantity(book.getQuantity() + 1);
                break;
            }
        }


        if (DashboardController.getInstance() != null) {
            DashboardController.getInstance().updateDashboardCounts();
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Book returned successfully!");


        loadBorrowedRecords();
        clearFields();
    }

    private void clearFields() {
        cmbBorrowedBooks.setValue(null);
        txtMemberInfo.clear();
        dpBorrowedDate.setValue(null);
        dpDueDate.setValue(null);
        dpReturnDate.setValue(LocalDate.now());
        lblOverdueStatus.setText("");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}