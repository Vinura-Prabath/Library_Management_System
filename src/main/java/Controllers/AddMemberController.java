package Controllers;

import DB.Database;
import model.Member;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddMemberController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtMemberID;

    @FXML
    private TextField txtName;

    @FXML
    void btnBackOnAction(ActionEvent event) {

        if (DashboardController.getInstance() != null) {
            DashboardController.getInstance().showDashboardHome();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtMemberID.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String contact = txtContact.getText().trim();
        String address = txtAddress.getText().trim();


        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || contact.isEmpty() || address.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please include all details!");
            return;
        }


        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid email address!");
            return;
        }


        Member newMember = new Member(id, name, email, contact, address);
        Database.memberList.add(newMember);


        if (DashboardController.getInstance() != null) {
            DashboardController.getInstance().updateDashboardCounts();
        }

        showAlert(Alert.AlertType.INFORMATION, "Successful", "The member was successfully registered!");
        clearFields();
    }


    private void clearFields() {
        txtMemberID.clear();
        txtName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}