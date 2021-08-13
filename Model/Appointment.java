package Model;


public class Appointment {
    private String email;
    private String title;
    private String type;
    private String location;
    private String description;
    private String start;
    private String end;
    private int appointmentID;
    private int customerID;
    private int contact_ID;
    private int userID;
    private String name;





    public Appointment() {
    }


    public Appointment(String email, String title, String type, String location, String description, String start, String end, int appointmentID, int customerID, int contactID,int userID, String name) {
     this.email=email;
     this.title=title;
     this.type=type;
     this.location=location;
     this.description=description;
     this.start=start;
     this.end=end;
     this.appointmentID=appointmentID;
     this.customerID=customerID;
     this.contact_ID=contactID;
     this.userID=userID;
     this.name=name;
    }

    public Appointment(String name) {
        this.name = name;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getContact_ID() {
        return contact_ID;
    }

    public void setContact_ID(int contact_ID) {
        this.contact_ID = contact_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString(){
        return name;
    }
}