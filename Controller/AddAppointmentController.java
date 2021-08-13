package Controller;


import DBAccess.DBAppointment;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddAppointmentController implements Initializable {
    @FXML
    private ComboBox<String> endCombo;
    @FXML
    private ComboBox<Appointment> contactNameBox;

    @FXML
    private TextField titleCol;
    @FXML
    private TextField descriptionText;
    @FXML
    private DatePicker dateText;
    @FXML
    private ComboBox<String> locationCombo;
    @FXML
    private ComboBox<String> startCombo;
    @FXML
    private ComboBox<String> typeID;


    ObservableList<String> startTime = FXCollections.observableArrayList("08:00 AM", "09:00 AM","10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM","05:00 PM","06:00 PM",
            "07:00 PM", "08:00 PM","09:00 PM");
    ObservableList<String> endTime=FXCollections.observableArrayList("09:00 AM","10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM","05:00 PM","06:00 PM",
            "07:00 PM", "08:00 PM","09:00 PM","10:00 PM");
    ObservableList<String> locationList= FXCollections.observableArrayList("Phoenix,Arizona","White Plains,New York","Montreal,Canada","London,England");
    ObservableList<String> typeList=FXCollections.observableArrayList("De-Briefing","Planning-Session");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        locationCombo.setItems(locationList);
        startCombo.setItems(startTime);
        typeID.setItems(typeList);
        contactNameBox.setItems(DBAppointment.getContactNames());
        endCombo.setItems(endTime);

    }
    /**
     * *saveButtonAction method:
     *  theCreatedDate: this string takes the current UTC time of the appointments that's being created
     *  The String getDate gets the date value selected from the date picker and formats it to yyyy-MM-dd
     *  The startTime endTime and Starting combines the getDate and substrings startSubstring, enSubstring then
     *  is converted into UTC time by converting the local date time into instant.
     *
     *  When Inserting the appointment into the sql database a contact name need to be used. First, a contact name that matches
     *  a value used in the contactNameBox is found once that is found it gets the Contact_ID as well. Then, another query
     *  is used in the to find the max Appointment_ID number then the appointment being made takes the maximum value plus
     *  one.
     *  Then, using the data received from the appointments table and contacts the information for the appointment table
     *  can be properly inserted.
     *  Once the save button is clicked it will check to see if the title,type,date,start,description,location, contact name
     *  is empty or null and if there is a duplicate appointment time and location in sql, if not
     *  it will be submitted. If there is a duplicate start time at the same location it will show an alert preventing the
     *  information from inserting into the sql database.
     *
     *  */

    public void saveButtonAction(ActionEvent event) throws IOException, SQLException, ParseException {
        String startSubString=startCombo.getSelectionModel().getSelectedItem();
        String endSubString=endCombo.getSelectionModel().getSelectedItem();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String theCreatedDate= ZonedDateTime.now(ZoneId.of("UTC")).format(formatter);
        String getDate=dateText.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd "));
        String locate=locationCombo.getSelectionModel().getSelectedItem();

        // the block of code below converts the date and start time in EST to UTC time
        //block of code 1
        String StartTime=getDate+startSubString;
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        LocalDateTime date = LocalDateTime.parse(StartTime, formatter1);
        ZoneId z=ZoneId.of("America/New_York");
        ZonedDateTime zdt=date.atZone(z);
        Instant thestartTime=zdt.toInstant();
        String startTime=thestartTime.toString().substring(0,18);


        // the block of code below converts the date and start time in EST to UTC time.
        // the it takes the boolean duplicate time and checks to see if the newly converted start time is
        // already taken in the appointments table
        //Block of code 2:
        String Starting=getDate+startSubString;
        LocalDateTime thestarting=LocalDateTime.parse(Starting,formatter1);
        ZoneId zoneId=ZoneId.of("America/New_York");
        ZonedDateTime zonedDateTime=thestarting.atZone(zoneId);
        Instant theestarting=zonedDateTime.toInstant();
        String starting=theestarting.toString().substring(0,18);
        boolean duplicateTimes = duplicateStart(starting,locate);



        //Block of code 3:
        String EndTime=getDate+endSubString;
        LocalDateTime dt=LocalDateTime.parse(EndTime,formatter1);
        ZoneId zoneId1=ZoneId.of("America/New_York");
        ZonedDateTime zonedDateTime1=dt.atZone(zoneId1);
        Instant theeendTime=zonedDateTime1.toInstant();
        String endTime=theeendTime.toString().substring(0,18);


        String title=titleCol.getText();
        boolean emptyTitle=validateTitle(title);

        String type=typeID.getSelectionModel().getSelectedItem();
        boolean emptyType=validateType(type);

        String location=locationCombo.getSelectionModel().getSelectedItem();
        boolean emptylocation=validateLocate(location);

        String description= descriptionText.getText();
        boolean emptydescription=validateDescription(description);

        String validStart=startCombo.getSelectionModel().getSelectedItem();
        boolean emptyStart=validateStart(validStart);

        String validDate=dateText.getValue().toString();
        boolean emptyDate=validateDate(validDate);





        try{
            String sql="Select * from contacts where contacts.Contact_Name='"+contactNameBox.getSelectionModel().getSelectedItem()+"'";
           PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
           ResultSet rs=ps.executeQuery();
           while(rs.next()){
               int contactId=rs.getInt("Contact_ID");
               String max="Select MAX(Appointment_ID) as Appointment_ID from appointments";
               PreparedStatement ps1=DBConnection.getConnection().prepareStatement(max);
               ResultSet rs1=ps1.executeQuery();
               rs1.next();
               int appID=rs1.getInt("Appointment_ID");
               int AppId=appID+1;
               String query="Insert INTO appointments(Appointment_ID,Title,Description,Location,Type,Start,End,Create_Date,Created_By,Last_Update,Last_Updated_By,Customer_ID,User_ID,Contact_ID) "
                       +"Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
               PreparedStatement ps3=DBConnection.getConnection().prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
               ps3.setInt(1,AppId);
               ps3.setString(2, titleCol.getText());
               ps3.setString(3, descriptionText.getText());
               ps3.setString(4,locationCombo.getSelectionModel().getSelectedItem());
               ps3.setString(5, typeID.getSelectionModel().getSelectedItem());
               ps3.setString(6, startTime);
               ps3.setString(7,endTime);
               ps3.setString(8, theCreatedDate);
               ps3.setString(9,"Application");
               ps3.setString(10, theCreatedDate);
               ps3.setString(11,"Application");
               if(contactId==1){
                   ps3.setInt(12,3);
               }
               if(contactId==2){
                   ps3.setInt(12,2);
               }
               if(contactId==3){
                   ps3.setInt(12,1);
               }
               else{
                   ps3.setInt(12,contactId);
               }
               ps3.setInt(13, 1);
               ps3.setInt(14,contactId);
               ps3.executeUpdate();

               if(duplicateTimes && emptyStart && emptyDate && emptydescription && emptyType && emptyTitle &&emptylocation && appointmentTimeLong()){

                   Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                   Object scene = FXMLLoader.load(getClass().getResource("/sample/AppointmentScreen.fxml"));
                   stage.setScene(new Scene((Parent) scene));

               }

           }

        } catch (Exception e) {

        }



    }


    @FXML
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

    /**   The private boolean validTitle() checks to see if the titleCol.getText() is empty.
     * If so, it shows an alert and prevents the application from executing the prepared statement. Else, it returns true.  **/

   private boolean validateTitle(String title){
        if(title.isEmpty()){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter title");
            alert.show();
            return false;
        }
        else {
            return true;
        }
   }

    /**   The private boolean validType() checks to see if the typeID.getText() is empty.
     * If so, it shows an alert and prevents the application from executing the prepared statement. Else, it returns true.  **/

    private boolean validateType(String type){
        if(typeID.getSelectionModel().getSelectedItem()==null){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter Type");
            alert.show();
            return false;
        }
        else {
            return true;
        }
   }

    /** The private boolean validDescription() checks to see if the descriptionText.getText() is empty.
     * If so, it shows an alert and prevents the application from executing the prepared statement. Else, it returns true.  **/
    private boolean validateDescription(String description){
       if(descriptionText.getText().isEmpty()) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Warning Dialog");
           alert.setContentText("Please enter Description");
           alert.show();
           return false;
       }
       else {
           return true;
       }
   }

    /** validateDate() boolean sends an alert and prevents the prepared statement from executing if the date text value is empty, or if the date text volume is an old date. **/

    private boolean validateDate(String validDate){

        if(validDate.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select date");
            alert.show();
            return false;
        }
        if(dateText.getValue().isBefore(LocalDate.now())){
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
    /** validateStart() checks to see if the start dates value is empty. if so, it returns in alerts. **/
   private boolean validateStart(String validStart){

        if(validStart.isEmpty()){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select start time");
            alert.show();
            return false;
        }


        else{
            return true;
        }
   }

   /** the boolean method validLocate checks to see if an item has been selected in the
    * locationCombo box if not it will send an alert **/

   private boolean validateLocate(String location){

        if(location==null){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select location");
            alert.show();
            return false;

        }
        else{
            return true;
        }
   }

    public void titleAct(ActionEvent event){
    }
    public void typeAct(ActionEvent event) {

    }

    public void dateTextAction(ActionEvent event){

    }


    public void locationComboAction(ActionEvent event) {
    }

    @FXML
    private void startComboAction(ActionEvent event) {


    }

    @FXML
    private void contactNameBoxAct(ActionEvent event) {

    }
    @FXML

    public void endComboAction(ActionEvent event) {


    }

    /**duplicateStart() checks to see if the start value is equal to a file you already placed in the sequel workbench appointment table
     *  if so it will return in alerts telling you to select a different time or location.
     **/

    private boolean duplicateStart(String starting, String locate) {
        try {


            String findStart = "SELECT * FROM appointments";
            DBQuery.makeQuery(findStart);
            ResultSet queryResult = DBQuery.getResult();

            while(queryResult.next()) {

                if(queryResult.getString("Start").equals(starting) && queryResult.getString("Location").equals(locate)) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("Please new time and location");
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

    /** appointmentTimeLong boolean:
     * if the appointment time is over two hours long or the appointments end time goes outside of business hours
     * or the appointment time is equal to the start time it will
     * return an alert**/
    public boolean appointmentTimeLong(){
        int start=startCombo.getSelectionModel().getSelectedIndex()+1;
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
