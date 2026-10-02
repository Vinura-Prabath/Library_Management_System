package Controllers;

import DB.Database;
import model.Book;
import model.BorrowRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class DashboardController {

    private static DashboardController instance;

    @FXML private Button btnAddBook;
    @FXML private Button btnAddMember;
    @FXML private Button btnBorrowHistory;
    @FXML private Button btnIssueBook;
    @FXML private Button btnLogout;
    @FXML private Button btnReturnBook;
    @FXML private Button btnViewMembers;

    @FXML private Label lblCurrentlyBorrowed;
    @FXML private Label lblOverdueBooks;
    @FXML private Label lblTotalBooks;
    @FXML private Label lblTotalMembers;

    @FXML private StackPane contentArea;
    @FXML private VBox dashboardHomeView;

    private Parent homeCardsView;

    public DashboardController() {
        instance = this;
    }

    public static DashboardController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        if (contentArea != null && !contentArea.getChildren().isEmpty()) {
            homeCardsView = (Parent) contentArea.getChildren().get(0);
        }
        updateDashboardCounts();
    }


    public void showDashboardHome() {
        if (contentArea != null && homeCardsView != null) {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(homeCardsView);
            updateDashboardCounts();
        }
    }

    public void updateDashboardCounts() {
        int totalBooks = 0;
        for (Book book : Database.bookList) {
            totalBooks += book.getQuantity();
        }

        int totalMembers = Database.memberList.size();
        int borrowedCount = 0;
        int overdueCount = 0;
        LocalDate today = LocalDate.now();

        for (BorrowRecord record : Database.borrowList) {
            if ("Borrowed".equalsIgnoreCase(record.getStatus())) {
                borrowedCount++;
                if (record.getDueDate() != null && record.getDueDate().isBefore(today)) {
                    overdueCount++;
                }
            }
        }

        if (lblTotalBooks != null) lblTotalBooks.setText(String.valueOf(totalBooks));
        if (lblTotalMembers != null) lblTotalMembers.setText(String.valueOf(totalMembers));
        if (lblCurrentlyBorrowed != null) lblCurrentlyBorrowed.setText(String.valueOf(borrowedCount));
        if (lblOverdueBooks != null) lblOverdueBooks.setText(String.valueOf(overdueCount));
    }


    public void loadInnerPage(String fxmlPath) {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(fxml);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Page view load failed!");
            alert.setContentText("Unable to load FXML path: " + fxmlPath);
            alert.show();
        }
    }


    public void loadInnerPageWithNode(Parent node) {
        if (contentArea != null) {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(node);
        }
    }

    @FXML
    void btnAddBookOnAction(ActionEvent event) {
        loadInnerPage("/View/AddBook.fxml");
    }

    @FXML
    void btnAddMemberOnAction(ActionEvent event) {
        loadInnerPage("/View/AddMember.fxml");
    }

    @FXML
    void btnViewMembersOnAction(ActionEvent event) {
        loadInnerPage("/View/ViewMembers.fxml");
    }

    @FXML
    void btnIssueBookOnAction(ActionEvent event) {
        loadInnerPage("/View/IssueBook.fxml");
    }

    @FXML
    void btnReturnBookOnAction(ActionEvent event) {
        loadInnerPage("/View/ReturnBook.fxml");
    }

    @FXML
    void btnBorrowHistoryOnAction(ActionEvent event) {
        loadInnerPage("/View/BorrowHistory.fxml");
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/View/login_page.fxml"));
                Stage stage = (Stage) btnLogout.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}