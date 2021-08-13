package Model;

public class Customer {
    private int CustomerID;
    private String CustomerName;
    private String CustomerAddress;
    private String CustomerCity;
    private String CustomerCountry;
    private String PostalCode;
    private String phoneNumber;
    private String divisionName;
    private String countryName;
    private int id;

    public Customer(String countryName, int id) {
        this.countryName = countryName;
        this.id = id;
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDivisionName() {
        return divisionName;
    }

    @Override
    public String toString(){
        return divisionName;

    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public Customer(String divisionName) {
        this.divisionName = divisionName;
    }

    public Customer(){}
    public Customer(String customerName, String customerAddress, String customerCity, String PostalCode, String phoneNumber){

    }

    public Customer(int customerID, String CustomerName, String CustomerAddress, String CustomerCity, String PostalCode) {
        this.CustomerID=customerID;
        this.CustomerName=CustomerName;
        this.CustomerAddress=CustomerAddress;
        this.CustomerCity=CustomerCity;
        this.PostalCode=PostalCode;


    }


    public void customer(int CustomerID, String CustomerName, String CustomerAddress,String CustomerCity, String CustomerCountry, String PostalCode, String phoneNumber){
        this.CustomerID=CustomerID;
        this.CustomerName=CustomerName;
        this.CustomerAddress=CustomerAddress;
        this.CustomerCity=CustomerCity;
        this.CustomerCountry=CustomerCountry;
        this.PostalCode=PostalCode;
        this.phoneNumber=phoneNumber;

    }

    public Customer(int CustomerID, String CustomerName, String CustomerAddress,
                    String CustomerCity, String CustomerCountry, String PostalCode, String phoneNumber){
        this.CustomerID=CustomerID;
        this.CustomerName=CustomerName;
        this.CustomerAddress=CustomerAddress;
        this.CustomerCity=CustomerCity;
        this.CustomerCountry=CustomerCountry;
        this.PostalCode=PostalCode;
        this.phoneNumber=phoneNumber;

    }
    public int getCustomerID(){return  CustomerID;}

    public void setCustomerID(int CustomerID){
        this.CustomerID=CustomerID;

    }
    public String getCustomerName(){
        return CustomerName;
    }
    public void setCustomerName(String CustomerName){
        this.CustomerName=CustomerName;
    }
    public String getCustomerAddress(){
        return CustomerAddress;
    }
    public void setCustomerAddress(String CustomerAddress){
        this.CustomerAddress=CustomerAddress;
    }
    public String getCustomerCity(){
        return CustomerCity;
    }
    public void setCustomerCity(String CustomerCity){
        this.CustomerCity=CustomerCity;
    }
    public String getCustomerCountry(){
        return CustomerCountry;
    }
    public void setCustomerCountry(String CustomerCountry){
        this.CustomerCountry=CustomerCountry;
    }
    public String getPostalCode(){
        return PostalCode;
    }
    public void setPostalCode(String PostalCode){
        this.PostalCode=PostalCode;
    }
    public String getphoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }
    public String getCountryName() {
        return countryName;
    }






    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }



}
