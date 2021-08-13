package Controller;

import Database.DBConnection;
import Database.DBQuery;
import Model.Appointment;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ModifyAppointmentController implements Initializable {
    @FXML
    private ComboBox<String> endCombo;
    @FXML
    private Label appointmentIDLabel;
    @FXML
    private Label contactIDLabel;
    @FXML
    private Label contactIDNum;
    @FXML
    private Label userID;
    @FXML
    private Label customerID;
    @FXML
    private TextField contactNameID;
    @FXML
    private TextField titleTextField;
    @FXML
    private ComboBox<String> typeCombo;
    @FXML
    private TextField descriptionText;
    @FXML
    private ComboBox<String> locationComboBox;
    @FXML
    private DatePicker dateTextField;
    @FXML
    private ComboBox<String> startComboBox;
    @FXML


    ObservableList<String> locationList= FXCollections.observableArrayList("Phoenix,Arizona","White Plains,New York","Montreal,Canada","London,England");
    ObservableList<String> startTime = FXCollections.observableArrayList("08:00 AM", "09:00 AM","10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM","05:00 PM","06:00 PM","07:00 PM","08:00 PM","09:00 PM");
    ObservableList<String> typeList=FXCollections.observableArrayList("De-Briefing","Planning-Session");




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        locationComboBox.setItems(locationList);

        startComboBox.setItems(startTime);
        contactIDLabel.setVisible(false);
        typeCombo.setItems(typeList);


    }


    @FXML
    public void titleGetAction() {
    }

    @FXML
    public void locationGetAction() {

    }
    @FXML
    public void dateGetAction() {
    }
    @FXML
    public void startGetAction(ActionEvent event) {

    }
    /**
     * *saveButtonAction method:
     *
     *  The startTime endTime and Starting combines the Date and substrings startSubstring, endSubstring then
     *  it's converted into UTC time by converting the local date time into instant.
     *
     *  When Updating the appointment the selectedID is used as a condition to show that the updated information should
     *  only be applied to only that Appointment_ID number.
     *  Once the save button is clicked it will check to see if the title,type,date,start,description,location, contact name
     *  is empty or null and if there is a duplicate appointment time and location in sql, if not
     *  it will be submitted. If there is a duplicate start time at the same location it will show an alert preventing the
     *  information from inserting into the sql database.
     *
     *  */

    public void saveButtonOnAction(ActionEvent event) throws IOException {
        String Date1 = dateTextField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd "));

        String Starting=Date1+startComboBox.getSelectionModel().getSelectedItem();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        LocalDateTime thestarting=LocalDateTime.parse(Starting,formatter2);
        ZoneId zoneId=ZoneId.of("America/New_York");
        ZonedDateTime zonedDateTime=thestarting.atZone(zoneId);
        Instant theStarting=zonedDateTime.toInstant();
        String starting=theStarting.toString().substring(0,18);

        String theLocation=locationComboBox.getSelectionModel().getSelectedItem();

        boolean duplicateTimes = duplicateStart(starting,theLocation);

        try {

            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String theUpdateDate= ZonedDateTime.now(ZoneId.of("UTC")).format(formatter);


            String Date = dateTextField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd "));
            String startSubString=startComboBox.getSelectionModel().getSelectedItem();
            String endSubString=endCombo.getSelectionModel().getSelectedItem();
            DateFormat formatterUTCConvert=new SimpleDateFormat("HH:mm:ss");
            formatterUTCConvert.setTimeZone(TimeZone.getTimeZone("UTC"));


            String StartTime=Date+startSubString;
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            LocalDateTime date = LocalDateTime.parse(StartTime, formatter1);
            ZoneId z=ZoneId.of("America/New_York");
            ZonedDateTime zdt=date.atZone(z);
            Instant thestartTime=zdt.toInstant();
            String startTime=thestartTime.toString().substring(0,18);

            String EndTime=Date+endSubString;
            LocalDateTime dt=LocalDateTime.parse(EndTime,formatter1);
            ZoneId zoneId1=ZoneId.of("America/New_York");
            ZonedDateTime zonedDateTime1=dt.atZone(zoneId1);
            Instant theeendTime=zonedDateTime1.toInstant();
            String endTime=theeendTime.toString().substring(0,18);

            String SelectedId = appointmentIDLabel.getText();
            String sql = "Update appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Last_Update=? where Appointment_ID=" + SelectedId;
            PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, titleTextField.getText());
            statement.setString(2, descriptionText.getText());
            statement.setString(3, locationComboBox.getSelectionModel().getSelectedItem());
            statement.setString(4, typeCombo.getSelectionModel().getSelectedItem());
            statement.setString(5, startTime);
            statement.setString(6, endTime);
            statement.setString(7, theUpdateDate);

            if((duplicateTimes && validStart()&&validDate()&&validTitle()&&validType()&&validDescription()&&validLocation()&&appointmentTimeLong() )){
                statement.executeUpdate();
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/sample/AppointmentScreen.fxml"));
                stage.setScene(new Scene((Parent) scene));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    private boolean duplicateStart(String starting, String theLocation) {

        try {

            String findStart = "SELECT * FROM appointments";
            DBQuery.makeQuery(findStart);
            ResultSet queryResult = DBQuery.getResult();

            while(queryResult.next()) {

                if(queryResult.getString("Start").equals(starting) && queryResult.getString("Location").equals(theLocation)) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("Please select another time or location");
                    alert.show();

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
    /**duplicateStart() checks to see if the start value is equal to a file you already placed in the sequel workbench appointment table
     *  if so it will return in alerts telling you to select a different time or location.
     **/

    public void cancelButtonOnAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/AppointmentScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }

    }

    public void selectAppointment(Appointment appointment) {
        contactIDNum.setText(String.valueOf(appointment.getContact_ID()));
        userID.setText(String.valueOf(appointment.getUserID()));
        customerID.setText(String.valueOf(appointment.getCustomerID()));
        contactIDLabel.setText(String.valueOf(appointment.getContact_ID()));
        titleTextField.setText(String.valueOf(appointment.getTitle()));
        typeCombo.setValue(String.valueOf(appointment.getType()));
        appointmentIDLabel.setText(String.valueOf(appointment.getAppointmentID()));
        contactNameID.setText(String.valueOf(appointment.getName()));
        descriptionText.setText(String.valueOf(appointment.getDescription()));
        dateTextField.setValue(LocalDate.parse(appointment.getStart().substring(0, 10)));
        startComboBox.setValue(appointment.getStart().substring(11, 19));
        endCombo.setValue(appointment.getEnd().substring(11, 19));
        locationComboBox.setValue(String.valueOf(appointment.getLocation()));
        String start=appointment.getStart().substring(11,13);
        String end=appointment.getEnd().substring(11,13);
        if (start.equals("08") || end.equals("09")){
            startComboBox.setValue("8:00 AM");
           endCombo.setValue("9:00 AM");
        }
        if (start.equals("09") || end.equals("10")){
            startComboBox.setValue("9:00 AM");
           endCombo.setValue("10:00 AM");
        }
        if (start.equals("10") || end.equals("11")){
            startComboBox.setValue("10:00 AM");
           endCombo.setValue("11:00 AM");
        }
        if (start.equals("11") || end.equals("12")){
            startComboBox.setValue("11:00 AM");
           endCombo.setValue("12:00 PM");
        }
        if (start.equals("12") || end.equals("13")){
            startComboBox.setValue("12:00 PM");
           endCombo.setValue("01:00 PM");
        }
        if (start.equals("13") || end.equals("14")){
            startComboBox.setValue("01:00 PM");
           endCombo.setValue("02:00 PM");
        }
        if (start.equals("14") || end.equals("15")){
            startComboBox.setValue("02:00 PM");
           endCombo.setValue("03:00 PM");
        }
        if (start.equals("15") || end.equals("16")){
            startComboBox.setValue("03:00 PM");
           endCombo.setValue("04:00 PM");
        }
        if (start.equals("16") || end.equals("17")){
            startComboBox.setValue("04:00 PM");
           endCombo.setValue("05:00 PM");
        }
        if (start.equals("17") || end.equals("18")){
            startComboBox.setValue("05:00 PM");
           endCombo.setValue("06:00 PM");
        }
        if (start.equals("18") || end.equals("19")){
            startComboBox.setValue("06:00 PM");
           endCombo.setValue("07:00 PM");
        }
        if (start.equals("19") || end.equals("20")){
            startComboBox.setValue("07:00 PM");
           endCombo.setValue("08:00 PM");
        }
        if (start.equals("20") || end.equals("21")){
            startComboBox.setValue("08:00 PM");
           endCombo.setValue("09:00 PM");
        }
        if (start.equals("21") || end.equals("22")){
            startComboBox.setValue("09:00 PM");
           endCombo.setValue("10:00 PM");
        }

    }

    public void contactNameAct(ActionEvent event) {
    }


    public boolean validTitle(){
        if(titleTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Error");
            alert.setContentText("Please enter title");
            alert.show();
            return false;
        }
        else {
            return true;
        }

    }
    public boolean validLocation(){
        if(locationComboBox.getSelectionModel().getSelectedItem()==null){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Error");
            alert.setContentText("Please select location");
            alert.show();
            return false;
        }
        else {
            return true;
        }
    }
    public boolean validType(){
        if(typeCombo.getSelectionModel().getSelectedItem()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Error");
            alert.setContentText("Please select value for type");
            alert.show();
            return false;
        }
        else{
            return true;
        }
    }
    public boolean validDescription(){
        if(descriptionText.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Error");
            alert.setContentText("Please select valid description");
            alert.show();

            return false;
        }
        else{
            return true;
        }
    }
    /**if the date selected in the date picker is an old date it send back an alert**/
    public boolean validDate(){
        if(dateTextField.getValue().isBefore(LocalDate.now())){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select valid day");
            alert.show();
            return false;
        }
        else{
            return true;
        }
    }


    public boolean validStart(){
        if(startComboBox.getSelectionModel().getSelectedItem()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Error");
            alert.setContentText("Please select ContactName");
            alert.show();

            return false;

        }
        else return true;
    }
    /** appointmentTimeLong boolean:
     * if the appointment time is over two hours long or the appointments end time goes outside of business hours
     * or the appointment time is equal to the start time it will
     * return an alert**/
    public boolean appointmentTimeLong(){
        int start=startComboBox.getSelectionModel().getSelectedIndex()+1;
        int end=endCombo.getSelectionModel().getSelectedIndex()+1;
        if((start==1)&& (end>3)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long select different end time.");
            alert.show();
            return false;
        }
        if((start==2)&& (end>4)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long select different end time.");
            alert.show();
            return false;
        }
        if((start==3)&&(end>5)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long select different end time.");
            alert.show();
            return false;
        }
        if((start==4)&& (end>6)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long select different end time.");
            alert.show();
            return false;
        }
        if((start==5)&& (end>7)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long select different end time.");
            alert.show();
            return false;
        }
        if((start==6)&& (end>8)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long select different end time.");
            alert.show();
            return false;
        }
        if((start==7)&&(end>9)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long select different end time.");
            alert.show();
            return false;
        }
        if((start==8)&&(end>10)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long select different end time.");
            alert.show();
            return false;

        }
        if((start==9)&&(end>10)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long and outside of business hours please select different end time.");
            alert.show();
            return false;
        }
        if((start==10)&&(end>12)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long and outside of business hours please select different end time.");
            alert.show();
            return false;
        }
        if((start==11)&&(end>13)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long and outside of business hours please select different end time.");
            alert.show();
            return false;
        }
        if((start==12)&&(end>14)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long and outside of business hours please select different end time.");
            alert.show();
            return false;
        }
        if((start==14)&&(end<15)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment Time is too long and outside of business hours please select different end time.");
            alert.show();
            return false;
        }
        if(start==15){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No appointments allowed for the end of the day");
            alert.show();
            return false;
        }
        if(start==end){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Appointment start cannot equal end time");
            alert.show();
            return false;
        }
        else{
            return true;
        }
    }




}
