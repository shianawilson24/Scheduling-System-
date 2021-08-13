package DBAccess;

import Database.DBConnection;
import Model.*;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class DBCustomer {

    /**getAllCustomers method:
     * this method gets all the information needed from the customers, first_level_division and countries
     * inorder for the data to properly be retrieved and collected for the customer table in this application.
      */
    public static  int  insertCustomer(String name, String address, String postalCode, String phone, int divisionID) throws SQLException {

        String sqlStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Updated_By, Division_ID) VALUES(?,?,?,?,now(),?,?,?)";
        PreparedStatement ps= DBConnection.getConnection().prepareStatement(sqlStatement);
     /**   ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setString(5, DataProvider.userName);
        ps.setString(6, DataProvider.userName);
        ps.setInt(7, divisionID);**/
        ps.setString(1, "Shiana");
        ps.setString(2, "111 roads");
        ps.setString(3, "3333");
        ps.setString(4,"222-333-3333");
        ps.setString(5, "1");
        ps.setString(6, "2");
        ps.setInt(7, 24);
        ps.execute();
        return ps.getUpdateCount();
    }

    public static ObservableList<Customer> cuslist = FXCollections.observableArrayList();
    public static ObservableList<Customer> getAllCustomers(){

        try{
            String sql= "SELECT * from customers,first_level_divisions,countries where countries.Country_ID=first_level_divisions.Country_ID and customers.Division_ID=first_level_divisions.Division_ID";

            PreparedStatement ps= DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs= ps.executeQuery();

            while(rs.next()) {
                int customerID=rs.getInt("Customer_ID");
                String customerName= rs.getString("Customer_Name");
                String customerAddress=rs.getString("Address");
                String customerCity=rs.getString("Division");
                String customerCountry=rs.getString("Country");
                String postalCode=rs.getString("Postal_Code");
                String customerPhone=rs.getString("Phone");

                Customer customer= new Customer(customerID,customerName,customerAddress,customerCity,customerCountry,postalCode,customerPhone);
                cuslist.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cuslist;
    }


    public static void getSelectedCustomer(){
        ObservableList<Customer>selectCustomer=FXCollections.observableArrayList();
        try{
            String sql= "SELECT * from customers,first_level_divisions,countries,appointments where countries.Country_ID=first_level_divisions.Country_ID and customers.Division_ID=first_level_divisions.Division_ID and customers.Customer_ID!=appointments.Customer_ID";

            PreparedStatement ps= DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs= ps.executeQuery();

            while(rs.next()) {
                int customerID=rs.getInt("Customer_ID");
                String customerName= rs.getString("Customer_Name");
                String customerAddress=rs.getString("Address");
                String customerCity=rs.getString("Division");
                String customerCountry=rs.getString("Country");
                String postalCode=rs.getString("Postal_Code");
                String customerPhone=rs.getString("Phone");

                Customer appointment= new Customer(customerID,customerName,customerAddress,customerCity,customerCountry,postalCode,customerPhone);
                selectCustomer.add(appointment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public static ObservableList<Customer> getAllDivision(){
        ObservableList<Customer> divisionlist= FXCollections.observableArrayList();

        try{
            String sql= "SELECT * from first_level_divisions";

            PreparedStatement ps= DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs= ps.executeQuery();

            while(rs.next()) {
                int id=rs.getInt("Country_ID");
                String divisionName= rs.getString("Division");

                Customer division= new Customer(divisionName);
                divisionlist.add(division);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return divisionlist;
    }
    public static ObservableList<Customer> getUSDivision(){
        ObservableList<Customer> divisionlist= FXCollections.observableArrayList();

        try{
            String sql= "SELECT * from first_level_divisions where Country_ID=1";

            PreparedStatement ps= DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs= ps.executeQuery();

            while(rs.next()) {
                String divisionName=rs.getString("Division");


                Customer division= new Customer(divisionName);
                divisionlist.add(division);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return divisionlist;
    }
    public static ObservableList<Customer> getCanadaDivision(){
        ObservableList<Customer> divisionlist= FXCollections.observableArrayList();

        try{
            String sql= "SELECT * from first_level_divisions where Country_ID=3";

            PreparedStatement ps= DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs= ps.executeQuery();

            while(rs.next()) {
                String divisionName=rs.getString("Division");


                Customer division= new Customer(divisionName);
                divisionlist.add(division);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return divisionlist;
    }
    public static ObservableList<Customer> getUKDivision(){
        ObservableList<Customer> divisionlist= FXCollections.observableArrayList();

        try{
            String sql= "SELECT * from first_level_divisions where Country_ID=2";

            PreparedStatement ps= DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs= ps.executeQuery();

            while(rs.next()) {
                String divisionName=rs.getString("Division");


                Customer division= new Customer(divisionName);
                divisionlist.add(division);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return divisionlist;
    }
    public static ObservableList<Customer> getAllCountries(){
        ObservableList<Customer> countrylist= FXCollections.observableArrayList();

        try{
            String sql= "SELECT * from countries";

            PreparedStatement ps= DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs= ps.executeQuery();

            while(rs.next()) {
                String countryName= rs.getString("Country");
                int id=rs.getInt("Country_ID");


                Customer country= new Customer(countryName,id);
                countrylist.add(country);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return countrylist;
    }



}
