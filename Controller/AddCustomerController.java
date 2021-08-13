package Controller;

import DBAccess.DBCustomer;
import Database.DBConnection;
import Database.DBQuery;
import Model.Customer;

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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCustomerController implements Initializable {
    @FXML
    private TextField emailField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField zipcodeTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private ComboBox<Customer> addCustomerStateComboBox;
    @FXML
    private  ComboBox<String> countrySelectCombo;

    /**saveButtonOnAction method:
     *the first query in the try block finds the max Customer_ID in the customers table in the
     * sql database. Once the maximum Customer_ID is found the variable customerId takes the value and increases
     * it by one so the new customer being added can have the max Customer_ID.
     * The second query takes the information entered in the textFields and selected items in the combo Boxes
     * and inserts them into the customers Table including the new max Customer_ID button.
     * If all the information given is properly entered and not blank it will take you back to the customer
     * table screen in the application showing you your new customer. Else, an alert will pop up showing you
     * what need to be fixed.**/

    @FXML
    public void saveButtonOnAction(ActionEvent event) throws IOException {

        int customerCity = addCustomerStateComboBox.getSelectionModel().getSelectedIndex() + 1;
        String name=nameTextField.getText();
        String address=addressTextField.getText();
        String zipcode= zipcodeTextField.getText();
        String phone=phoneTextField.getText();
        String customerName=nameTextField.getText();
        boolean existingCustomer=duplicateName(customerName,phone);

        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String theCreatedDate= ZonedDateTime.now(ZoneId.of("UTC")).format(formatter);

         try{
             String sqlMax=("SELECT MAX(Customer_ID) as Customer_ID from customers");
             PreparedStatement preparedStatement=DBConnection.getConnection().prepareStatement(sqlMax);
             ResultSet resultSet= preparedStatement.executeQuery();
             while(resultSet.next()) {
                  int custID = resultSet.getInt("Customer_ID");
                 int customerId = custID + 1;

                 String query="Select * from first_level_divisions where first_level_divisions.Division='"+addCustomerStateComboBox.getSelectionModel().getSelectedItem()+"'";
                 PreparedStatement ps=DBConnection.getConnection().prepareStatement(query);
                 ResultSet rs=ps.executeQuery();
                 rs.next();
                 int divisionId=rs.getInt("Division_ID");
                 String sql = "INSERT into customers(Customer_ID,Customer_Name,Address,Postal_Code,Phone,Create_Date,Created_By,Last_Update,Last_Updated_By,Division_ID)" +
                         "VALUES(?,?,?,?,?,?,?,?,?,?)";

                 PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                 statement.setInt(1, customerId);
                 statement.setString(2, name);
                 statement.setString(3, address);
                 statement.setString(4, zipcode);
                 statement.setString(5, phone);
                 statement.setString(6, theCreatedDate);
                 statement.setString(7, "Application");
                 statement.setString(8, theCreatedDate);
                 statement.setString(9, "Application");
                 statement.setInt(10,divisionId);

                 if(validateCustomerName()&&validateAddress()&&validateZipcode()&&validatePhone()&&existingCustomer&&validateCountry()&&validateState()){
                     statement.executeUpdate();
                     Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                     Object scene = FXMLLoader.load(getClass().getResource("/sample/CustomerTable.fxml"));
                     stage.setScene(new Scene((Parent) scene));
                     stage.show();
                 }

         }


         } catch (Exception e) {

         }


    }

    public boolean validateCustomerName() {
        if (nameTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setContentText("Please enter a name for this customer!");
            alert.show();
            return false;
        } else {
            return true;
        }
    }
    /** validateCustomerName() checks to see if the textfield is empty
     *  if so it will return at alerts and prevent the prepared statement from executing **/


    public boolean validatePhone() {
        String regex="(^\\d{10}$)";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(phoneTextField.getText());

        if(phoneTextField.getText().isEmpty()&& !matcher.matches()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setContentText("Please enter a valid phone number for this customer!");
            alert.show();
            return false;
        }
        else {
            return true;
        }
    }
    /** validatePhone() checks to see if the textfield is empty
     *  if so it will return at alerts and prevent the prepared statement from executing **/


    @FXML
    public void cancelButtonOnAction(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/CustomerTable.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }
    }


    /** validAddress method checks to see if the addressTextField is empty. If it is it will return an alert
     * telling the user to enter a valid address for the customer. **/

    public boolean validateAddress () {

        if(addressTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setContentText("Please enter a valid address for this customer!");
            alert.show();
            return false;
        }
        else {
            return true;
        }

    }

    /**If the zipcodeTextField is blank an alert will pop up **/
    public boolean validateZipcode () {
        if(zipcodeTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setContentText("Please enter a valid zip code for this customer!");
            alert.show();
            return false;
        }
        else {
            return true;
        }

    }
    public boolean validateCountry () {
        if(countrySelectCombo.getSelectionModel().getSelectedItem()==null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setContentText("Please enter a valid country for this customer!");
            alert.show();
            return false;
        }
        else {
            return true;
        }

    }
        public boolean validateState () {
        if(addCustomerStateComboBox.getSelectionModel().getSelectedItem()==null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setContentText("Please enter a valid country for this customer!");
            alert.show();
            return false;
        }
        else {
            return true;
        }

    }
    private boolean duplicateName(String customerName, String phone) {
        try {


            String findStart = "SELECT * FROM customers";
            DBQuery.makeQuery(findStart);
            ResultSet queryResult = DBQuery.getResult();

            while(queryResult.next()) {

                if(queryResult.getString("Customer_Name").equals(customerName) &&queryResult.getString("Phone").equals(phone)) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("Customer name and phone number already in system");
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            String sql="Select * from countries";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                countrySelectCombo.getItems().addAll(rs.getString("Country"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

       addCustomerStateComboBox.setItems(DBCustomer.getAllDivision());







    }


    public void addCustomerStateAction(ActionEvent event) {


    }

    public void countryComboAct(ActionEvent event) {
        int country=countrySelectCombo.getSelectionModel().getSelectedIndex()+1;
        if (country==1){
            addCustomerStateComboBox.setItems(DBCustomer.getUSDivision());
        }
        if (country==2){
            addCustomerStateComboBox.setItems(DBCustomer.getUKDivision());
        }
        if (country==3){
            addCustomerStateComboBox.setItems(DBCustomer.getCanadaDivision());
        }
    }

}



