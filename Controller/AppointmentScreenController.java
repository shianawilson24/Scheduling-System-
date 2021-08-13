package Controller;

import DBAccess.DBAppointment;
import Database.DBConnection;
import Model.Appointment;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentScreenController implements Initializable {

    @FXML
    private Button updateContactButton;

    private Stage stage;
    private Parent scene;
    @FXML
    private Label userIdLabel;
    @FXML
    private Label yourZone;
    @FXML
    private  TableColumn <Appointment,Integer> contact_ID;
    @FXML
    private TableView<Appointment> AppointmentTable;
    @FXML
    private TableColumn<Appointment, String> ContactCol;
    @FXML
    private TableColumn<Appointment, String> TitleCol;
    @FXML
    private TableColumn<Appointment, String> TypeCol;
    @FXML
    private TableColumn<Appointment, String> LocationCol;
    @FXML
    private TableColumn<Appointment, String> DescriptionCol;
    @FXML
    private TableColumn<Appointment, String> StartCol;
    @FXML
    private TableColumn<Appointment, String> EndCol;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIDCol;
    @FXML
    private TableColumn<Appointment, Integer> customerIDCol;
    @FXML
    private TableColumn<Appointment, String> contactName;
    @FXML
    private TableColumn<Appointment, Integer> userIDCol;
    @FXML
    private ComboBox<String> ViewByBox;

    @FXML
    private Button ModifyButton;
    @FXML
    public Button DeleteButton;
    public Button BackButton;
    public Label WguLogo;
    public Label AppointmentLogo;
    public Button SearchButton;
    public RadioButton viewMonthButton;
    public RadioButton viewWeekButton;
    public Button addButton;


    /** addButtonMethod takes you to the AddAppointment screen **/
    @FXML
    private void addButtonAction(ActionEvent event) throws IOException {

        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/sample/AddAppointment.fxml"));
        stage.setScene(new Scene((Parent) scene));

    }

    /**the ModifyButtonAction method:
     *  When you select an item from the table to modify and click the modify button it will load the fxml
     *  ModifyAppointment.fxml. If you click the modify button without selecting an item from the table
     *  an alert will show saying please select an item**/
    @FXML
    private void ModifyButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/ModifyAppointment.fxml"));
            loader.load();

            // use getController() to get access to an instance of ModifyPartController
            ModifyAppointmentController MAController = loader.getController();
            MAController.selectAppointment(AppointmentTable.getSelectionModel().getSelectedItem());
            // Get event source from button
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an item.");
            alert.setTitle("Error Dialog");
            alert.showAndWait();
        }


    }
    /**DeleteButtonAction method:
     * Once an item is selected and the delete button is clicked an confirmation alert pops up asking if you really
     * want to delete the appointment. If the OK button in the alert is clicked is deletes it from the table then
     * takes the public static void deleteAppointment and deletes it from the sql database where the Appointment_ID
     * value matches the selectedID value for Appointment_ID**/
    @FXML
    private void DeleteButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the Appointment record, would you want to continue?");
        alert.setTitle("Confirmation of Deletion");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {



            ObservableList<Appointment>  allAppointments,singleAppointment;
            allAppointments=AppointmentTable.getItems();
            singleAppointment = AppointmentTable.getSelectionModel().getSelectedItems();
            singleAppointment.forEach(allAppointments::remove);
            Appointment selectedID=AppointmentTable.getSelectionModel().getSelectedItem();
            deleteAppointment(selectedID.getAppointmentID());
        }

    }
    public static void deleteAppointment (int selectedID) {
        ++selectedID;

        try {
            String delete="DELETE FROM appointments where Appointment_ID=?";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(delete, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,selectedID);
            int deletedAppointment=ps.executeUpdate();
            if (deletedAppointment==1){
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Deletion Conformation");
                alert.setContentText("Appointment:"+selectedID);
                alert.show();
            }

        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

   @FXML
    public void BackButtonAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }

    }





    @FXML
    private void viewMonthAction(ActionEvent event) {

        if (viewMonthButton.isSelected()) {
            viewWeekButton.setSelected(false);
            AppointmentTable.getItems().clear();
            AppointmentTable.setItems(DBAppointment.currentMonth());

        }
        else if(!viewMonthButton.isSelected()){
            viewWeekButton.setSelected(false);
            AppointmentTable.getItems().clear();
            AppointmentTable.setItems(DBAppointment.getAppointments());
        }


    }

    @FXML
    private void viewWeekAction(ActionEvent event) {
        if (viewWeekButton.isSelected()) {
            viewMonthButton.setSelected(false);
            AppointmentTable.getItems().clear();
            AppointmentTable.setItems(DBAppointment.currentWeek());

        }
        else if (!viewMonthButton.isSelected()) {
            viewMonthButton.setSelected(false);
            AppointmentTable.getItems().clear();
            AppointmentTable.setItems(DBAppointment.getAppointments());

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        DBAppointment.getAppointments().clear();
        AppointmentTable.setItems(DBAppointment.getAppointments());
        AppointmentTable.getSelectionModel().selectFirst();
        //Lambda expressions
        ContactCol.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getEmail()));
        TitleCol.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getTitle()));
        TypeCol.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getType()));
        LocationCol.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getLocation()));
        DescriptionCol.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getDescription()));
        StartCol.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getStart()));
        EndCol.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getEnd()));
        appointmentIDCol.setCellValueFactory(appointment -> new SimpleObjectProperty<>(appointment.getValue().getAppointmentID()));
        customerIDCol.setCellValueFactory(appointment -> new SimpleObjectProperty<>(appointment.getValue().getCustomerID()));
        contact_ID.setCellValueFactory(appointment-> new SimpleObjectProperty<>(appointment.getValue().getContact_ID()));
        userIDCol.setCellValueFactory(appointment-> new SimpleObjectProperty<>(appointment.getValue().getUserID()));
        contactName.setCellValueFactory(appointment-> new SimpleStringProperty(appointment.getValue().getName()));


    }


    public void UpdateContactAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/ContactUpdateScreen.fxml"));
            loader.load();

            // use getController() to get access to an instance of ModifyPartController
            contactUpdateController CUController = loader.getController();
            CUController.selectAppointment(AppointmentTable.getSelectionModel().getSelectedItem());
            // Get event source from button
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an item.");
            alert.setTitle("Error Dialog");
            alert.showAndWait();
        }


    }
}

