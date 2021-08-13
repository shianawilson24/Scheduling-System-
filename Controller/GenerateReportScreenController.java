package Controller;

import DBAccess.DBReport;
import Database.DBConnection;
import Model.GenerateReport;
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

import java.time.LocalDateTime;
import java.util.*;

public class GenerateReportScreenController implements Initializable {


    public Button backButton;
    public Button BackButton;
    public Label nonlabel;

    @FXML
    private TextArea itemText;
    @FXML
    private ComboBox<String> viewBy;
    @FXML
    private TextArea itemText1;
    @FXML
    private ComboBox<String>viewBy1;
    @FXML
    private TextArea itemText2;
    @FXML
    private ComboBox<String>viewBy2;

    ObservableList<String> view= FXCollections.observableArrayList("Total appointments by type","Total Appointments at Each Location");
    ObservableList<String> months=FXCollections.observableArrayList("Jan","Feb","Mar","April","May","June","July","Aug","Sept","Oct","Nov","Dec");


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
    private void viewbyAct(ActionEvent event) throws SQLException {
        int itemsInCombo=viewBy.getSelectionModel().getSelectedIndex()+1;


        if(itemsInCombo==1) {
            try {

                String sql="Select Count(*) as Type from appointments where Type='Planning Session'";
                PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
                ResultSet rs= ps.executeQuery();
                while(rs.next()){
                    String type=rs.getString("Type");
                    String sql1="Select Count(*) as Type from appointments where Type='De-Briefing'";
                    PreparedStatement ps1=DBConnection.getConnection().prepareStatement(sql1);
                    ResultSet rs1= ps1.executeQuery();
                    while (rs1.next()){
                        String briefType=rs1.getString("Type");

                        itemText.setText("Total Planning Session Appointments: "+type+"Total De-Breifing Appointments: "+briefType);

                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        if(itemsInCombo==2) {
            try {

                String sql="Select Count(*) as Location from appointments where Location='White Plains, New York'";
                PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
                ResultSet rs= ps.executeQuery();
                while(rs.next()){
                    String newYork=rs.getString("Location");
                    String sql1="Select Count(*) as Location from appointments where Location='Phoenix,Arizona'";
                    PreparedStatement ps1=DBConnection.getConnection().prepareStatement(sql1);
                    ResultSet rs1= ps1.executeQuery();
                    while (rs1.next()){
                        String arizona= rs1.getString("Location");
                        String sql2="Select Count(*) as Location from appointments where Location='Montreal,Canada'";
                        PreparedStatement ps2=DBConnection.getConnection().prepareStatement(sql2);
                        ResultSet rs2= ps2.executeQuery();
                        while(rs2.next()){
                            String canada=rs2.getString("Location");
                            String sql3="Select Count(*) as Location from appointments where Location='London,England'";
                            PreparedStatement ps3=DBConnection.getConnection().prepareStatement(sql3);
                            ResultSet rs3=ps3.executeQuery();
                            while (rs3.next()){
                                String england=rs3.getString("Location");
                                itemText.setText("canada:"+canada+"\n"+"New York:"+newYork+"\n"+"arizona:"+arizona+"\n"+"England:"+england);
                            }




                        }

                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    /**viewby1Act is a scheduler for each contact in the contacts table in sql. Once a contact is selected from
     * the combo box the textArea show every appointment scheduled for the contacts selected **/
    public void viewby1Act(ActionEvent event) throws SQLException {
        try {
            itemText1.clear();
            String contactName=viewBy1.getSelectionModel().getSelectedItem();
            String sql="Select * from contacts,appointments where contacts.Contact_ID=appointments.Contact_ID and contacts.Contact_Name='"+contactName+"'";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            StringBuilder builder = new StringBuilder();
            while(rs.next()){

                builder.append("Name: "+rs.getString("Contact_Name")+" Title: "+rs.getString("Title")
                        +" Description: "+rs.getString("Description")+" Type: "+rs.getString("Type")
                        +" Start: "+rs.getString("Start")+" End: "+rs.getString("End")+" Appointment_ID: "+
                        rs.getInt("Appointment_ID")+" Customer_ID: "+rs.getInt("Customer_ID"));
                builder.append("\n");
                itemText1.setText(builder.toString());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    /**If an month in the combo box is selected it will show the how many appointments are scheduled for that month
     *  **/
    public void viewby2Act(ActionEvent event) {


        int itemsInCombo=viewBy2.getSelectionModel().getSelectedIndex();

        if(itemsInCombo==0){
            ObservableList<GenerateReport> jan=DBReport.getJanApps();
            itemText2.setText("Appointments in January: "+String.valueOf(jan.size()));

        }
        if(itemsInCombo==1){
            ObservableList<GenerateReport> feb=DBReport.getFebApps();
            itemText2.setText("Appointments in February: "+String.valueOf(feb.size()));

        }
        if(itemsInCombo==2){
            ObservableList<GenerateReport> mar=DBReport.getMarApps();
            itemText2.setText("Appointments in March: "+String.valueOf(mar.size()));

        }
        if(itemsInCombo==3){
            ObservableList<GenerateReport> apr=DBReport.getAprApps();
            itemText2.setText("Appointments in April: "+String.valueOf(apr.size()));
        }
        if(itemsInCombo==4){
            ObservableList<GenerateReport> may=DBReport.getMayApps();
            itemText2.setText("Appointments in May: "+String.valueOf(may.size()));

        }
        if(itemsInCombo==5){
            ObservableList<GenerateReport> june=DBReport.getJunApps();
            itemText2.setText("Appointments in June: "+String.valueOf(june.size()));

        }
        if(itemsInCombo==6){
            ObservableList<GenerateReport> july=DBReport.getJulyApps();
            itemText2.setText("Appointments in July: "+String.valueOf(july.size()));
        }
        if(itemsInCombo==7){
            ObservableList<GenerateReport> aug=DBReport.getAugApps();
            itemText2.setText("Appointments in August: "+String.valueOf(aug.size()));

        }
        if(itemsInCombo==8){
            ObservableList<GenerateReport> sep=DBReport.getSepApps();
            itemText2.setText("Appointments in September: "+String.valueOf(sep.size()));

            }

        if(itemsInCombo==9){
            ObservableList<GenerateReport> oct=DBReport.getOctApps();
            itemText2.setText("Appointments in October: "+String.valueOf(oct.size()));

        }
        if(itemsInCombo==10){
            ObservableList<GenerateReport> nov=DBReport.getNovApps();
            itemText2.setText("Appointments in November: "+String.valueOf(nov.size()));

        }
        if(itemsInCombo==11){
            ObservableList<GenerateReport> dec=DBReport.getDecApps();
            itemText2.setText("Appointments in December: "+String.valueOf(dec.size()));

        }


    }
    /**this automatically takes contact names from the contacts table and places them into the combobox **/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewBy.setItems(view);
        viewBy2.setItems(months);

        try {
            String sql = "Select * from contacts";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                viewBy1.getItems().addAll(rs.getString("Contact_Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

