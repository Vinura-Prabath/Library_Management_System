package Controllers;

import model.Member;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class UpdateMemberController {

    @FXML
    private AnchorPane btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    private Member selectedMember;


    public void setMemberData(Member member) {
        this.selectedMember = member;
        if (member != null) {
            txtName.setText(member.getFullName());
            txtEmail.setText(member.getEmail());
            txtContact.setText(member.getPhoneNumber());
            txtAddress.setText(member.getAddress());
        }
    }

    @FXML
    void btnCancelOnAction(MouseEvent event) {

        if (DashboardController.getInstance() != null) {
            DashboardController.getInstance().loadInnerPage("/View/ViewMembers.fxml");
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String contact = txtContact.getText().trim();
        String address = txtAddress.getText().trim();


        if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || address.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill all fields!");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid email address!");
            return;
        }

        if (selectedMember != null) {

            selectedMember.setFullName(name);
            selectedMember.setEmail(email);
            selectedMember.setPhoneNumber(contact);
            selectedMember.setAddress(address);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Member details updated successfully!");


            if (DashboardController.getInstance() != null) {
                DashboardController.getInstance().loadInnerPage("/View/ViewMembers.fxml");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}