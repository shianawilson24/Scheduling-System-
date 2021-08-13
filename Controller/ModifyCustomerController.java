package Controller;

import DBAccess.DBCustomer;
import Database.DBConnection;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ModifyCustomerController implements Initializable {
    @FXML
    private Label originalState;
    @FXML
    private ComboBox<Customer> addStateBox;
    @FXML
    private ComboBox<String> addCountryBox;
    @FXML
    private Label customerIDText;
    @FXML
    private TextField nameText;
    @FXML
    private TextField addressText;
    @FXML
    private TextField postalText;
    @FXML
    private TextField phoneText;



    @FXML
    private void addStateAction(ActionEvent event){


    }


    @FXML
    private void cancelButtonAction(ActionEvent event) throws IOException {

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            String sql="Select * from countries";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                addCountryBox.getItems().addAll(rs.getString("Country"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /** SaveButtonAction takes the information inputted and if the information isn't null
     * or doesn't make some values of another customer it submits it into the sql database  **/
    public void SaveButtonAction(ActionEvent event) throws IOException {

        SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date= new Date();
        String createDate=format.format(date);
        String lastUpdate=format.format(date);
        String selectedID=customerIDText.getText();
        String phone=phoneText.getText();
        String zipCode=postalText.getText();
        String address=addressText.getText();
        String customerName=nameText.getText();

        try{
            String query="Select * from first_level_divisions where first_level_divisions.Division='"+addStateBox.getSelectionModel().getSelectedItem()+"'";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");


                String sql = "Update customers SET Customer_Name=?,Address=?, Postal_Code=?, Phone=?, Create_Date=?, Last_Update=?,Division_ID=? where customers.Customer_ID=" + selectedID;
                PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, nameText.getText());
                statement.setString(2, addressText.getText());
                statement.setString(3, postalText.getText());
                statement.setString(4, phoneText.getText());
                statement.setString(5, createDate);
                statement.setString(6, lastUpdate);
                statement.setInt(7, divisionId);
                statement.executeUpdate();


            }

            if(validateCustomerName(customerName)&&validateAddress(address)&&validateZipcode(zipCode)&&validatePhone(phone)&&validState()){
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/sample/CustomerTable.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    /**if nameText is empty it returns an alert **/
    public boolean validateCustomerName(String customerName) {

        if(customerName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a name for this customer!");
            alert.show();
            return false;
        }
        else {
            return true;
        }
    }
    /**If addressText is empty it returns an alert **/
    public boolean validateAddress (String address) {

        if(address.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid address for this customer!");
            alert.show();
            return false;
        }
        else {
            return true;
        }

    }
    /** if postalText is empty it returns an alert  **/
    public boolean validateZipcode (String zipCode) {

        if(zipCode.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid zip code for this customer!");
            alert.show();
            return false;
        }
        else {
            return true;
        }

    }
    /** if phoneText is empty or doesn't match the regex it returns an alert **/
    public boolean validatePhone(String phone) {
        String regex="(^\\d{10}$)";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(phoneText.getText());

        if(phone.isEmpty()&& !matcher.matches()) {
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

    /**If the state is not selected it will return an alert */
    public boolean validState(){
        if(addStateBox.getSelectionModel().getSelectedItem()==null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setContentText("Please enter a State!");
            alert.show();
            return false;
        }
        else {
            return true;
        }

    }



    /** addCountryAction filters out the addCustomerStateBox depending on the index selected in the addCountryBox **/
    @FXML
    public void addCountryAction(ActionEvent event) {
        int country=addCountryBox.getSelectionModel().getSelectedIndex()+1;

        if (country==1){
            addStateBox.setPromptText("Select New Value");
            addStateBox.setItems(DBCustomer.getUSDivision());
        }
        if (country==2){
            addStateBox.setPromptText("Select New Value");
            addStateBox.setItems(DBCustomer.getUKDivision());
        }
        if (country==3){
            addStateBox.setPromptText("Select New Value");
            addStateBox.setItems(DBCustomer.getCanadaDivision());
        }
    }
    public void selectedItem(Customer selectedItem) {

        customerIDText.setText(String.valueOf(selectedItem.getCustomerID()));
        nameText.setText(String.valueOf(selectedItem.getCustomerName()));
        addressText.setText(String.valueOf(selectedItem.getCustomerAddress()));
        postalText.setText(String.valueOf(selectedItem.getPostalCode()));
        phoneText.setText(String.valueOf(selectedItem.getphoneNumber()));
        addCountryBox.setValue(String.valueOf(selectedItem.getCustomerCountry()));
        originalState.setText(selectedItem.getCustomerCity());
        int country=addCountryBox.getSelectionModel().getSelectedIndex()+1;

        if (country==1){
            addStateBox.setPromptText("Select a state");
            addStateBox.setItems(DBCustomer.getUSDivision());
        }
        if (country==2){
            addStateBox.setPromptText("Select a state");
            addStateBox.setItems(DBCustomer.getUKDivision());
        }
        if (country==3){
            addStateBox.setPromptText("Select a state");
            addStateBox.setItems(DBCustomer.getCanadaDivision());
        }



    }

}

