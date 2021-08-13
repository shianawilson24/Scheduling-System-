package Controller;
import DBAccess.DBCustomer;
import Database.DBConnection;
import Model.Customer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.sql.Statement;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerTableController implements Initializable {

    @FXML
    private TableView<Customer> customerTab;
    @FXML
    private TableColumn<Customer,Integer> CustomerIdCol;
    @FXML
    private TableColumn<Customer,String> CustomerNameCol;
    @FXML
    private TableColumn<Customer,String> CustomerAddressCol;
    @FXML
    private TableColumn<Customer,String> CustomerCityCol;
    @FXML
    private TableColumn<Customer,String> CustomerCountryCol;
    @FXML
    private TableColumn<Customer,String> CustomerPostalCol;
    @FXML
    private TableColumn<Customer,String> CustomerPhoneCol;



    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBCustomer.getAllCustomers().clear();
        customerTab.setItems(DBCustomer.getAllCustomers());
        customerTab.getSelectionModel().selectFirst();
        CustomerIdCol.setCellValueFactory(customer-> new SimpleObjectProperty<>(customer.getValue().getCustomerID()));
        CustomerNameCol.setCellValueFactory(customer->new SimpleStringProperty(customer.getValue().getCustomerName()));
        CustomerAddressCol.setCellValueFactory(customer-> new SimpleStringProperty(customer.getValue().getCustomerAddress()));
        CustomerCityCol.setCellValueFactory(customer-> new SimpleStringProperty(customer.getValue().getCustomerCity()));
        CustomerCountryCol.setCellValueFactory(customer-> new SimpleStringProperty(customer.getValue().getCustomerCountry()));
        CustomerPostalCol.setCellValueFactory(customer->new SimpleStringProperty(customer.getValue().getPostalCode()));
        CustomerPhoneCol.setCellValueFactory(customer-> new SimpleStringProperty(customer.getValue().getphoneNumber()));
    }
    /**addButtonOnAction takes you to the AddCustomerScreen to insert a new customer into the sql database **/
    @FXML
    public void addButtonOnAction(ActionEvent event) throws IOException{
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/sample/AddCustomerScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }
    /** ModifyButtonOnAction method:
     * takes the selected item in table and loads it in the ModifyCustomerScreen to be modified. If
     * an item is not selected in table and the modify button is clicked it will send an alert saying
     * please select an item**/

    @FXML
    public void modifyButtonOnAction(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/ModifyCustomerScreen.fxml"));
            loader.load();

            ModifyCustomerController MCController = loader.getController();
            MCController.selectedItem(customerTab.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an item.");
            alert.setTitle("Error Dialog");
            alert.showAndWait();
        }

    }
    /**deleteButtonOnAction method:
     * Once customer is selected and the delete button is pressed it will show an confirmation alert
     * asking if you want to continue to continue once you press the OK button it will continue to delete
     * customer from customer table. Then it takes the public static void deleteCustomer and deletes it from
     * the appointments table first then the contacts table then lastly the customers table to avoid foreign key
     * restrictions**/

    @FXML
    public void deleteButtonOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the customer record, do you want to continue?");
        alert.setTitle("Confirmation of Deletion");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {


            ObservableList<Customer> allCustomers, singleCustomer;
            allCustomers = customerTab.getItems();
            singleCustomer = customerTab.getSelectionModel().getSelectedItems();
            singleCustomer.forEach(allCustomers::remove);

            Customer selectedCustomer=customerTab.getSelectionModel().getSelectedItem();


            deleteCustomer(selectedCustomer.getCustomerID());

        }



    }
    public static void deleteCustomer (int selectedCustomer) {
        ++selectedCustomer;
        try{



            String deleteApt="DELETE FROM appointments where Customer_ID='"+selectedCustomer+"'";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(deleteApt);
            ps.executeUpdate();
            String delete1="DELETE FROM customers where Customer_ID='"+selectedCustomer+"'";
            PreparedStatement ps1=DBConnection.getConnection().prepareStatement(delete1, Statement.RETURN_GENERATED_KEYS);
            int deletedCustomer=ps1.executeUpdate();
            if(deletedCustomer==1){
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Deletion Conformation");
                alert.setContentText("Customer is deleted");
                alert.show();
            }
            String delete2="DELETE FROM contacts where Contact_ID=?";
            PreparedStatement ps2=DBConnection.getConnection().prepareStatement(delete2,Statement.RETURN_GENERATED_KEYS);
            int contactId1=1;
            int contactId3=3;
            if(selectedCustomer==1){
                ps2.setInt(1,contactId3);

            }
            else if(selectedCustomer==3){
                ps2.setInt(1,contactId1);

            }
            else{
                ps2.setInt(1,selectedCustomer);

            }
            ps2.executeUpdate();





        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void backButtonOnAction(ActionEvent event) throws  IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Locale currentLocale=Locale.getDefault();
            ResourceBundle bundle=ResourceBundle.getBundle("resources.shiana",currentLocale);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }
    }

    /** appToButton method:
     * takes the selected Items in table and loads them into the CustomerToAppointment Screen in order.
     * If no item is selected in the table it will show an alert saying please select an item***/
    public void addToAppButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/CustomerToAppointment.fxml"));
            loader.load();

            CustomerToAppController CAController = loader.getController();
            CAController.selectedItem(customerTab.getSelectionModel().getSelectedItem());

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
