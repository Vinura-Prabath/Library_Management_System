package Controllers;

import DB.Database;
import model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class AddBookController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private TextField txtAuthor;

    @FXML
    private TextField txtBookTitle;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtPublishedYear;

    @FXML
    private TextField txtQuantity;

    @FXML
    public void initialize() {

        ObservableList<String> categories = FXCollections.observableArrayList(
                "IT & Technology",
                "Science",
                "Novel",
                "History",
                "Mathematics",
                "Other"
        );
        cmbCategory.setItems(categories);
    }

    @FXML
    void btnBackOnAction(ActionEvent event) {
        if (btnBack.getScene().lookup("#contentArea") instanceof AnchorPane) {
            AnchorPane contentArea = (AnchorPane) btnBack.getScene().lookup("#contentArea");
            contentArea.getChildren().clear();
        } else if (btnBack.getScene().lookup("#contentArea") instanceof StackPane) {
            StackPane contentArea = (StackPane) btnBack.getScene().lookup("#contentArea");
            contentArea.getChildren().clear();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtID.getText().trim();
        String title = txtBookTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String category = cmbCategory.getValue();
        String quantityStr = txtQuantity.getText().trim();
        String yearStr = txtPublishedYear.getText().trim();


        if (id.isEmpty() || title.isEmpty() || author.isEmpty() || category == null || quantityStr.isEmpty() || yearStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all required fields!");
            return;
        }

        int quantity = 0;
        int publishedYear = 0;


        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Quantity must be greater than 0!");
                return;
            }

            publishedYear = Integer.parseInt(yearStr);
            if (publishedYear < 1000 || publishedYear > 2026) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a valid Published Year (e.g., 2020)!");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Quantity and Published Year must be valid numbers!");
            return;
        }


        Book newBook = new Book(id, title, author, category, quantity, publishedYear);
        Database.bookList.add(newBook);


        if (DashboardController.getInstance() != null) {
            DashboardController.getInstance().updateDashboardCounts();
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "The book was successfully added!");
        clearFields();
    }

    private void clearFields() {
        txtID.clear();
        txtBookTitle.clear();
        txtAuthor.clear();
        txtQuantity.clear();
        txtPublishedYear.clear();
        cmbCategory.setValue(null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}