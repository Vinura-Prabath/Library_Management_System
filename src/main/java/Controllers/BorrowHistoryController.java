package Controllers;

import DB.Database;
import model.BorrowRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.time.LocalDate;

public class BorrowHistoryController {

    @FXML
    private TableColumn<BorrowRecord, String> colBookTitle;

    @FXML
    private TableColumn<BorrowRecord, LocalDate> colDueDate;

    @FXML
    private TableColumn<BorrowRecord, LocalDate> colIssueDate;

    @FXML
    private TableColumn<BorrowRecord, String> colMemberId;

    @FXML
    private TableColumn<BorrowRecord, LocalDate> colReturnDate;

    @FXML
    private TableColumn<BorrowRecord, String> colStatus;

    @FXML
    private TableView<BorrowRecord> tblBorrowHistory;

    @FXML
    public void initialize() {

        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        colStatus.setCellFactory(column -> new TableCell<BorrowRecord, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    BorrowRecord record = getTableView().getItems().get(getIndex());
                    String statusText = item;


                    if ("Borrowed".equalsIgnoreCase(item) && record.getDueDate() != null && LocalDate.now().isAfter(record.getDueDate())) {
                        statusText = "Overdue";
                    }

                    setText(statusText);


                    switch (statusText.toUpperCase()) {
                        case "RETURNED":
                            setTextFill(Color.GREEN);
                            setStyle("-fx-font-weight: bold;");
                            break;
                        case "OVERDUE":
                            setTextFill(Color.RED);
                            setStyle("-fx-font-weight: bold;");
                            break;
                        default:
                            setTextFill(Color.DARKORANGE);
                            setStyle("-fx-font-weight: bold;");
                            break;
                    }
                }
            }
        });


        loadBorrowHistory();
    }

    private void loadBorrowHistory() {
        ObservableList<BorrowRecord> historyList = FXCollections.observableArrayList(Database.borrowList);
        tblBorrowHistory.setItems(historyList);
    }
}