package Controller;

import Database.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.EventObject;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML
    private Label theZone;
    @FXML
    private Button cancelButton;


    /**customerButtonOnAction method:
     * Takes you to the customer table**/

    @FXML
    private void customerButtonOnAction(ActionEvent event) throws IOException {


        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/sample/CustomerTable.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();



    }
    /** appointmentButtonOnAction method:
     * takes you to the appointment table **/
    @FXML
    private void appointmentButtonOnAction(ActionEvent event) throws IOException{

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/sample/AppointmentScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();


    }
    /**generateButtonOnAction takes you to the reports screen **/
    @FXML
    private void generateButtonOnAction(ActionEvent event) throws IOException{
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/sample/GenerateReportScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();


    }
    /**LogoutButtonOnAction takes you to the login screen */
    @FXML
    private void logoutButtonOnAction(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Locale currentLocale=Locale.getDefault();
            ResourceBundle bundle=ResourceBundle.getBundle("resources.shiana",currentLocale);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/LoginScreen.fxml"),bundle);
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }

    }
    /** cancelButtonOnAction closes the application**/
    @FXML
    private void cancelButtonOnAction(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to exit application?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    /** in the initialize method an alert shows if there is an appointment in the next 15 minutes if not
     *if not the alert says no appointments coming up **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zone = ZoneId.systemDefault();
        theZone.setText(String.valueOf(zone));





    }


}
