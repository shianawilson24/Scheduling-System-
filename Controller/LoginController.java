package Controller;

import Database.DBConnection;
import Database.DBQuery;
import Database.trackLoggedInUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.TimeStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LoginController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterpasswordField;
    @FXML
    private Label yourZone;
    ResourceBundle bundle;





    public void loginButtonOnAction(ActionEvent event) throws IOException {
        String username = usernameTextField.getText();
        String password = enterpasswordField.getText();
        //Converts the localDateTime to UTC then adds 15 minutes
        LocalDateTime fifthTeenMin = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15);
        String datetoString=fifthTeenMin.toString();
        String utcdate=datetoString.substring(0,10);
        String utctime=datetoString.substring(11,17);
        String convertedDate=utcdate+" "+utctime+"00";

        String newdate=convertedDate;


        boolean correctCredentials = attemptLogin(username, password);

        if ((correctCredentials)) {
            try{
                String sql="Select * from appointments";
                PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
                ResultSet rs=ps.executeQuery();
                while (rs.next()){
                    String start = rs.getString("Start");
                    int id=rs.getInt("Appointment_ID");
                    if(start.equals(newdate)){
                        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Warning Dialog");
                        alert.setContentText("Appointment time:"+start+"ID is:"+id);
                        alert.show();
                    }
                    if(!start.equals(newdate)){
                        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Warning Dialog");
                        alert.setContentText("No Upcoming appointment for Appointment_ID:"+id);
                        alert.show();
                    }


                }


            } catch (SQLException e) {
                e.printStackTrace();

            }


            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();


            trackLoggedInUser.trackLog(username, true);
            trackLoggedInUser.correctAttempt(username,true);

        } else {


            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("loginMessage"));
            alert.showAndWait();

            trackLoggedInUser.trackLog(username, false);
            trackLoggedInUser.failedAttempt(username,false);
        }

    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        stage.close();
    }

    /** attemptLogin boolean checks to see if the password and username entered in the test fields matches with the username and password
     * placed in the SQL workbench
     * **/

    private boolean attemptLogin(String username, String password) {
        try {

            String SQLLogin = "SELECT * FROM users";
            DBQuery.makeQuery(SQLLogin);
            ResultSet queryResult = DBQuery.getResult();

            while(queryResult.next()) {

                if(queryResult.getString("User_Name").equals(username) && queryResult.getString("Password").equals(password))

                    return true;

            }

            return false;

        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zone=ZoneId.systemDefault() ;
        yourZone.setText(String.valueOf(zone));
        bundle=ResourceBundle.getBundle("resources.shiana",Locale.getDefault());



    }

}

