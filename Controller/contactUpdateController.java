package Controller;

import Database.DBConnection;
import Database.DBQuery;
import Model.Appointment;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class contactUpdateController implements Initializable {
    @FXML
    private Label originalNameText;
    @FXML
    private Label contactIdText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField ContactNameText;

    @FXML


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    /**takes the email, ContactName entered and updates them into the contacts table in sql. If the values
     * are properly entered in the textFields if will send back to the appointment table. If not, an alert will show
     * showing you what needs the be changed.**/
    public void saveButtonAction(ActionEvent event) throws IOException {
        String email = emailText.getText();
        String ContactName = ContactNameText.getText();
        boolean duplicateContactName=duplicateContact(ContactName,email);
        try {
            String sql = "Update contacts SET Contact_Name=?, Email=? where contacts.Contact_Name='"+originalNameText.getText()+"'";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ContactName);
            ps.setString(2, email);

            if(validEmail()&&duplicateContactName){
                ps.executeUpdate();
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/sample/AppointmentScreen.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    public void cancelButtonAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to go back?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/AppointmentScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }
    }

    public void emailAct() {
    }
    /** validEmail boolean method checks to see if the email matches the regex pattern for a valid email.
     * if not it will show an alert**/
    public boolean validEmail() {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailText.getText());
        if (!matcher.matches() && emailText.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setContentText("Enter Valid Email");
            alert.show();
            return false;

        } else {
            return true;
        }
    }
    /** duplicateContact boolean method check if the contactName and email entered is already entered in the
     * sql database. If so, it will send an alert saying this enter new contact information**/
    private boolean duplicateContact(String contactName, String email) {
        try {


            String findStart = "SELECT * FROM contacts";
            DBQuery.makeQuery(findStart);
            ResultSet queryResult = DBQuery.getResult();

            while(queryResult.next()) {

                if(queryResult.getString("Contact_Name").equals(contactName) &&queryResult.getString("Email").equals(email)) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("Contact Already Made! Enter New Contact");
                    alert.showAndWait();

                    return false;
                }

            }

            return true;

        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return true;
        }
    }

    public void selectAppointment(Appointment selectedItem) {
        originalNameText.setText(String.valueOf(selectedItem.getName()));
        emailText.setText(String.valueOf(selectedItem.getEmail()));
        contactIdText.setText(String.valueOf(selectedItem.getContact_ID()));
    }
}
