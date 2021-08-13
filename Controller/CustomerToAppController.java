package Controller;

import Database.DBConnection;
import Database.DBQuery;
import Model.Customer;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerToAppController implements Initializable {
    @FXML
    private TextField emailText;
    @FXML
    private Label customerIdText;
    @FXML
    private TextField ContactNameText;
    @FXML
    private Label endLabel;
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
    @FXML
    private Label yourZone;
    ObservableList<String> type= FXCollections.observableArrayList("Planning-Session","De-briefing");
    ObservableList<String> startTime = FXCollections.observableArrayList("08:00 AM", "09:00 AM","10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM","05:00 PM","06:00 PM",
            "07:00 PM", "08:00 PM","09:00 PM");
    ObservableList<String> locationList= FXCollections.observableArrayList("Phoenix,Arizona","White Plains,New York","Montreal,Canada","London,England");


    /**
     * *saveButtonAction method:
     *  theCreatedDate: this string takes the current UTC time of the appointments that's being created
     *  The String getDate gets the date value selected from the date picker and formats it to yyyy-MM-dd
     *  The startTime endTime and Starting combines the getDate and substrings startSubstring, enSubstring then
     *  is converted into UTC time by converting the local date time into instant.
     *
     *  Before Inserting the appointment into the sql database a contact needs to be created. First, it finds the
     *  max Contact_ID found in the contacts table in sql. Then it takes that value and increases
     *  it by one for the new contact. Once, the new contact has a value the contact name
     *  and email is entered into the contacts table along with the new Contact_ID value.
     *  a contact name that matches
     *  Once the new contact is placed in the date base a query used to find the max Appointment_ID ib the appointments
     *  table.
     *  Once the max Appointment_ID is found a variable increases that value by one making it the new max Appointment_ID.
     *  Then, using the data received from the appointments table and contacts the information for the appointment table
     *  can be properly inserted.
     *  Once the save button is clicked it will check to see if the title,type,date,start,description,location, contact name
     *  is empty or null and if there is a duplicate appointment time and location in sql, if not
     *  it will be submitted. If there is a duplicate start time at the same location it will show an alert preventing the
     *  information from inserting into the sql database.
     *
     *  */
    public void saveButtonAction(ActionEvent event) throws IOException, SQLException {


        String title= titleCol.getText();
        String location=locationCombo.getSelectionModel().getSelectedItem();

        String typeBox=typeID.getSelectionModel().getSelectedItem();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String theCreatedDate= ZonedDateTime.now(ZoneId.of("UTC")).format(formatter);
        String startBox= startCombo.getSelectionModel().getSelectedItem();
        String startSubString=startCombo.getSelectionModel().getSelectedItem();
        String endSubString=endLabel.getText();
        String getDate=dateText.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd "));

        String StartTime=getDate+startSubString;
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        LocalDateTime date = LocalDateTime.parse(StartTime, formatter1);
        ZoneId z=ZoneId.of("America/New_York");
        ZonedDateTime zdt=date.atZone(z);
        Instant thestartTime=zdt.toInstant();
        String startTime=thestartTime.toString().substring(0,18);

        String EndTime=getDate+endSubString;
        LocalDateTime dt=LocalDateTime.parse(EndTime,formatter1);
        ZoneId zoneId1=ZoneId.of("America/New_York");
        ZonedDateTime zonedDateTime1=dt.atZone(zoneId1);
        Instant theeendTime=zonedDateTime1.toInstant();
        String endTime=theeendTime.toString().substring(0,18);


        String locate=locationCombo.getSelectionModel().getSelectedItem();
        String Starting=getDate+startSubString;
        LocalDateTime thestarting=LocalDateTime.parse(Starting,formatter1);
        ZoneId zoneId=ZoneId.of("America/New_York");
        ZonedDateTime zonedDateTime=thestarting.atZone(zoneId);
        Instant theestarting=zonedDateTime.toInstant();
        String starting=theestarting.toString().substring(0,18);



        boolean duplicateTimes = duplicateStart(starting,locate);
        String contactName=ContactNameText.getText();
        String email= emailText.getText();
        boolean duplicateContacts=duplicateContact(contactName,email);

        try{

                String sql="Select Max(Contact_ID) as Contact_ID from appointments";
                PreparedStatement ps= DBConnection.getConnection().prepareStatement(sql);
                ResultSet rs=ps.executeQuery();
                rs.next();
                int contactId=rs.getInt("Contact_ID");
                int contactID=contactId+1;
                int customerID=contactID;
                String insertSql="Insert INTO contacts(Contact_ID,Contact_Name,Email)"
                        +"Values(?,?,?)";
                PreparedStatement prep=DBConnection.getConnection().prepareStatement(insertSql,Statement.RETURN_GENERATED_KEYS);
                prep.setInt(1,contactID);
                prep.setString(2,ContactNameText.getText());
                prep.setString(3,emailText.getText());
                prep.executeUpdate();

                String maxContact="Select MAX(Contact_ID) as Contact_ID from contacts";
                PreparedStatement preparedStatement=DBConnection.getConnection().prepareStatement(maxContact);
                ResultSet resultSet=preparedStatement.executeQuery();
                resultSet.next();
                int contact_ID=rs.getInt("Contact");
                String max="Select MAX(Appointment_ID) as Appointment_ID from appointments";
                PreparedStatement ps1=DBConnection.getConnection().prepareStatement(max);
                ResultSet rs1=ps1.executeQuery();
                rs1.next();
                int appID=rs1.getInt("Appointment_ID");
                int AppId=appID+1;
                String query="Insert INTO appointments(Appointment_ID,Title,Description,Location,Type,Start,End,Create_Date,Created_By,Last_Update,Last_Updated_By,Customer_ID,User_ID,Contact_ID) "
                        +"Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps3=DBConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps3.setInt(1,AppId);
                ps3.setString(2, title);
                ps3.setString(3, descriptionText.getText());
                ps3.setString(4,location);
                ps3.setString(5, typeBox);
                ps3.setString(6,startTime);
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
                    ps3.setInt(12,customerID);
                }
                ps3.setInt(13, 1);
                ps3.setInt(14,contact_ID);


            if(duplicateTimes&&validateStart()&&validateDate()&&validateDescription()&&validateType()&&validateTitle()&&validateLocate()&&validateLocate()&&validateStart()&&validEmail()&&duplicateContacts){

                ps3.executeUpdate();
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/sample/AppointmentScreen.fxml"));
                stage.setScene(new Scene((Parent) scene));

            }


        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @FXML
    public void cancelButtonAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to go back?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Locale currentLocale=Locale.getDefault();
            ResourceBundle bundle=ResourceBundle.getBundle("resources.shiana",currentLocale);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/AppointmentScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }
    }


    public boolean validateTitle(){
        if(titleCol.getText().isEmpty()){
            Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter title");
            alert.showAndWait();
            return false;
        }
        else {
            return true;
        }
    }
    /**   The public Boolean validTitle() checks to see if the titleCol.getText() is empty.
     * If so, it shows an alert and prevents the application from executing the prepared statement. Else, it returns true.  **/

    public boolean validateType(){
        String type=typeID.getSelectionModel().getSelectedItem();
        if(type.isEmpty()){
            Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter Type");
            alert.showAndWait();
            return false;
        }
        else {
            return true;
        }
    }
    /**   The public Boolean validType() checks to see if the typeID.getText() is empty.
     * If so, it shows an alert and prevents the application from executing the prepared statement. Else, it returns true.  **/


    public boolean validateDescription(){
        if(descriptionText.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter Description");
            alert.showAndWait();
            return false;
        }
        else {
            return true;
        }
    }
    /**   The public Boolean validDescription() checks to see if the descriptionText.getText() is empty.
     * If so, it shows an alert and prevents the application from executing the prepared statement. Else, it returns true.  **/

    public boolean validateDate(){
        String date=dateText.getValue().toString();
        if(date.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select date");
            alert.showAndWait();
            return false;
        }
        if(dateText.getValue().isBefore(LocalDate.now())){
            Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select valid day");
            alert.showAndWait();
            return false;
        }
        else{
            return true;
        }
    }
    /** validateDate() boolean sends an alert and prevents the prepared statement from executing if the date text value is empty, or if the date text volume is an old date. **/

    public boolean validateStart(){
        String start=startCombo.getSelectionModel().getSelectedItem();

        if(start.isEmpty()){
            Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select start time");
            alert.showAndWait();
            return false;
        }


        else{
            return true;
        }
    }
    /** validateStart() checks to see if the start dates value is empty. if so, it returns in alerts. **/

    public boolean validateLocate(){

        if(locationCombo.getSelectionModel().getSelectedItem()==null){
            Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select location");
            alert.showAndWait();
            return false;

        }
        else{
            return true;
        }
    }

    public void titleAct(ActionEvent event){

    }
    @FXML
    public void typeAct(ActionEvent event) {


    }

    public void dateTextAction(ActionEvent event){

    }


    public void locationComboAction(ActionEvent event) {
    }
    /**
     *  startComboAction Method:
     *  Takes the index in the startCombo and sets the endLabel to an hour after the the value of the startCombo.
     *  For instance, if the selectedIndex is 2 the value is 10:00 AM, then the endLabel is set to 11:00 AM.

     **/
    @FXML
    private void startComboAction(ActionEvent event) {
        int time=startCombo.getSelectionModel().getSelectedIndex()+1;

        if(time==1){
            endLabel.setText("09:00 AM");

        }
        if(time==2){
            endLabel.setText("10:00 AM");
        }
        if(time==3){
            endLabel.setText("11:00 AM");
        }
        if(time==4){
            endLabel.setText("12:00 PM");
        }
        if(time==5){
            endLabel.setText("01:00 PM");
        }
        if(time==6){
            endLabel.setText("02:00 PM");
        }
        if(time==7){
            endLabel.setText("03:00 PM");
        }
        if(time==8){
            endLabel.setText("04:00 PM");
        }
        if(time==9){
            endLabel.setText("05:00 PM");
        }
        if(time==10){
            endLabel.setText("06:00 PM");
        }
        if(time==11){
            endLabel.setText("07:00 PM");
        }
        if(time==12){
            endLabel.setText("08:00 PM");
        }
        if(time==13){
            endLabel.setText(("09:00 PM"));
        }
        if(time==14){
            endLabel.setText("10:00 PM");
        }

    }

    /**If the contactName and email entered matches another contact in the contacts table it will show an alert **/
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
                    alert.setContentText("Please select location");
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


    public void selectedItem(Customer selectedItem) {

        customerIdText.setText(String.valueOf(selectedItem.getCustomerID()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeID.setItems(type);
        locationCombo.setItems(locationList);
        startCombo.setItems(startTime);
    }
    /** if email doesn't match the regex it will show an alert **/
    public boolean validEmail(){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher= pattern.matcher(emailText.getText());
        if(!matcher.matches() && emailText.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setContentText("Enter Valid Email");
            alert.show();
            return false;

        }

        else{
            return true;
        }

    }


    public void emailAct(ActionEvent event) {

    }
    public boolean validName(){
        if (ContactNameText.getText()==null){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Error");
            alert.setContentText("Please enter fill out Contact Name");
            alert.showAndWait();
            return true;
        }
        else {
            return false;
        }
    }


}
