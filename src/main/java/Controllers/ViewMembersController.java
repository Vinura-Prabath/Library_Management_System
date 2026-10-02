package Controllers;

import DB.Database;
import model.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class ViewMembersController {

    @FXML
    private Button btnAddMember;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdateMember;

    @FXML
    private TableColumn<Member, String> colAddress;

    @FXML
    private TableColumn<Member, String> colContact;

    @FXML
    private TableColumn<Member, String> colEmail;

    @FXML
    private TableColumn<Member, String> colID;

    @FXML
    private TableColumn<Member, String> colName;

    @FXML
    private TableView<Member> tblMembers;

    @FXML
    private TextField txtSearch;

    private ObservableList<Member> memberList;

    @FXML
    public void initialize() {

        colID.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));


        loadTableData();
    }

    private void loadTableData() {
        memberList = FXCollections.observableArrayList(Database.memberList);
        tblMembers.setItems(memberList);
    }

    @FXML
    void btnAddMemberOnAction(ActionEvent event) {
        if (DashboardController.getInstance() != null) {
            DashboardController.getInstance().loadInnerPage("/View/AddMember.fxml");
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) {
        if (DashboardController.getInstance() != null) {
            DashboardController.getInstance().showDashboardHome();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Member selectedMember = tblMembers.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Database.memberList.remove(selectedMember);
            loadTableData();

            if (DashboardController.getInstance() != null) {
                DashboardController.getInstance().updateDashboardCounts();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Member deleted successfully!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a member to delete!");
            alert.show();
        }
    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        txtSearch.clear();
        loadTableData();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchText = txtSearch.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            loadTableData();
            return;
        }

        ObservableList<Member> filteredList = FXCollections.observableArrayList();
        for (Member member : Database.memberList) {
            if (member.getMemberId().toLowerCase().contains(searchText) ||
                    member.getFullName().toLowerCase().contains(searchText)) {
                filteredList.add(member);
            }
        }
        tblMembers.setItems(filteredList);
    }

    @FXML
    void btnUpdateMemberOnAction(ActionEvent event) {
        Member selectedMember = tblMembers.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a member from the table to update!");
            alert.show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UpdateMember.fxml"));
            Parent updateMemberNode = loader.load();


            UpdateMemberController controller = loader.getController();
            controller.setMemberData(selectedMember);


            if (DashboardController.getInstance() != null) {
                DashboardController.getInstance().loadInnerPageWithNode(updateMemberNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to load Update Member view!");
            alert.show();
        }
    }
}