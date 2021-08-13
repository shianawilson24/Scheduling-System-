package DBAccess;
import Database.DBConnection;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DBAppointment {
    /**getAppointments() selects data from the appointments, customers, contacts table where Appointment_ID and Customer_ID
     *  share the same value from both the customers, appointments and contacts table **/


    public static ObservableList<Appointment> getAppointments() {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from appointments,contacts where contacts.Contact_ID=appointments.Contact_ID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String email = rs.getString("Email");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                String location = rs.getString("Location");
                String description = rs.getString("Description");
                String Start = rs.getString("Start");
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = LocalDateTime.parse(Start, formatter1);
                LocalDateTime thetime=date.minusHours(4);
                String start= formatter1.format(thetime);

                String End = rs.getString("End");
                LocalDateTime date1 = LocalDateTime.parse(End, formatter1);
                LocalDateTime thetime1=date1.minusHours(4);
                String end= formatter1.format(thetime1);
                int appointmentID = rs.getInt("Appointment_ID");
                int customerID = rs.getInt("Customer_ID");
                int contactID=rs.getInt("Contact_ID");
                int userID=rs.getInt("User_ID");
                String name=rs.getString("Contact_Name");

                Appointment appointment = new Appointment(email, title, type, location, description, start, end,appointmentID, customerID,contactID,userID,name);
                appointmentsList.add(appointment);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointmentsList;

    }
    /** getContactNames method:
     * gets contact names from the contacts table**/
    public static ObservableList<Appointment> getContactNames() {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from contacts";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name=rs.getString("Contact_Name");

                Appointment appointment = new Appointment(name);
                appointmentsList.add(appointment);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointmentsList;

    }
    /** the currentWeek method:
     * used for the appointment table screen for viewing the week.
     * each appointment in the appointment table in sql is sorted out by its date using a substring.
     * If the appointments start substring from (0, 10) equals any date of the current week it returns it
     * to the appointment table screen in this application**/
    public static ObservableList<Appointment> currentWeek(){
        ObservableList<Appointment> theCurrentWeek= FXCollections.observableArrayList();
        try {
            String sql = "SELECT * from appointments,contacts where appointments.Contact_ID=contacts.Contact_ID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String email = rs.getString("Email");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                String location = rs.getString("Location");
                String description = rs.getString("Description");
                String Start = rs.getString("Start");
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = LocalDateTime.parse(Start, formatter1);
                LocalDateTime thetime=date.minusHours(4);
                String start= formatter1.format(thetime);

                String End = rs.getString("End");
                LocalDateTime date1 = LocalDateTime.parse(End, formatter1);
                LocalDateTime thetime1=date1.minusHours(4);
                String end= formatter1.format(thetime1);
                int appointmentID = rs.getInt("Appointment_ID");
                int customerID = rs.getInt("Customer_ID");
                int contactID=rs.getInt("Contact_ID");
                int userID=rs.getInt("User_ID");
                String name=rs.getString("Contact_Name");

                Calendar Monday = Calendar.getInstance();
                Monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                java.util.Date mondayTime = Monday.getTime();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String findMonday = format1.format(mondayTime);


                Calendar Tuesday = Calendar.getInstance();
                Tuesday.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                java.util.Date tuesdayTime = Tuesday.getTime();
                String findTuesday = format1.format(tuesdayTime);

                Calendar Wednesday = Calendar.getInstance();
                Wednesday.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                java.util.Date wednesdayTime = Wednesday.getTime();
                String findWednesday = format1.format(wednesdayTime);

                Calendar Thursday = Calendar.getInstance();
                Thursday.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                java.util.Date thursdayTime = Thursday.getTime();
                String findThursday = format1.format(thursdayTime);

                Calendar Friday = Calendar.getInstance();
                Friday.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                Date fridayTime = Friday.getTime();
                String findFriday = format1.format(fridayTime);

                if (start.substring(0, 10).equals(findMonday) || start.substring(0, 10).equals(findTuesday) || start.substring(0, 10).equals(findWednesday)|| start.substring(0, 10).equals(findThursday) || start.substring(0, 10).equals(findFriday)) {
                    Appointment week = new Appointment(email, title, type, location, description, start, end,appointmentID, customerID,contactID,userID,name);
                    theCurrentWeek.add(week);

                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return theCurrentWeek;

    }
    /** the currentMonth method:
     * used for the appointment table screen for viewing the Month.
     * each appointment in the appointment table in sql is sorted out by its date using a substring.
     * If the appointments start substring from (0,7) equals any date of the current Month it returns it
     * to the appointment table screen in this application**/
    public static ObservableList<Appointment> currentMonth(){
        ObservableList<Appointment> theCurrentMonth= FXCollections.observableArrayList();
        try {
            String sql = "SELECT * from appointments,contacts where appointments.Contact_ID=contacts.Contact_ID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String email = rs.getString("Email");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                String location = rs.getString("Location");
                String description = rs.getString("Description");
                String Start = rs.getString("Start");
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = LocalDateTime.parse(Start, formatter1);
                LocalDateTime thetime=date.minusHours(4);
                String start= formatter1.format(thetime);

                String End = rs.getString("End");
                LocalDateTime date1 = LocalDateTime.parse(End, formatter1);
                LocalDateTime thetime1=date1.minusHours(4);
                String end= formatter1.format(thetime1);

                int appointmentID = rs.getInt("Appointment_ID");
                int customerID = rs.getInt("Customer_ID");
                int contactID=rs.getInt("Contact_ID");
                int userID=rs.getInt("User_ID");
                String name=rs.getString("Contact_Name");

                Calendar Month = Calendar.getInstance();
                String currentMonth = new SimpleDateFormat("yyyy-MM").format(Month.getTime());

                if (start.substring(0, 7).equals(currentMonth)) {
                    Appointment month= new Appointment(email, title, type, location, description, start, end,appointmentID, customerID,contactID,userID,name);
                    theCurrentMonth.add(month);

                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return theCurrentMonth;

    }



}

