package sample;

import DBAccess.DBAppointment;
import DBAccess.DBCustomer;

import Database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    /**
     *
     * @author shiana
     */



    @Override
    public void start(Stage primaryStage) throws Exception{
        Locale currentLocale=Locale.getDefault();
        ResourceBundle bundle=ResourceBundle.getBundle("resources.shiana",currentLocale);
        Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"),bundle);
        primaryStage.setScene(new Scene(root, 641, 431));
        primaryStage.show();
    }



    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        try{
            FileWriter fw= new FileWriter("README.txt");
            PrintWriter pw= new PrintWriter(fw);
            pw.println("title: C195 Appointment Scheduler");
            pw.println("The purpose of this project is to add, delete and modify customers and appointments using using java and sql queries");
            pw.println("Author: Shiana Wilson, StudentID:001369775 ,email: swi2112@wgu.edu,version:the newest version, date:7/22/2021");
            pw.println("IDE version number:IntelliJ Community 2021.01, JDK:Java SE 16.01, compatible with JavaFX-SDK-11.0.2.");
            pw.println("Once Login Button is clicked it will take you to a main screen showing four options those options are");
            pw.println("Customer: which takes you to the customer table where you can add customer, modify and delete a customer and add customer to " +
                    "appointment.");
            pw.println("Appointment: Takes you to the appointment table where you can add, modify, delete appointments, and update contact information.");
            pw.println("Add to Appointments: this allows you to take a customer and create and contact name and appointment for them");
            pw.println("Also, you can view appointments by month or the current week using radio buttons.");
            pw.println("Generate Reports part A3f: The extra report I chose is finding the total appointment made for each location. ");
            pw.println("MYSQL Connector driver: mysql-connector-java-8.0.24");

            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DBConnection.startConnection() ;
        DBCustomer.getAllCustomers();
        DBAppointment.getAppointments();
        DBCustomer.getSelectedCustomer();












        launch(args);
        DBConnection.closeConnection();
    }
}
