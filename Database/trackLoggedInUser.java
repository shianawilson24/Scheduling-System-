package Database;

import com.mysql.cj.log.LogFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class trackLoggedInUser {

    public static final String filename = "userlog.txt";

    public trackLoggedInUser() {};

    public static void trackLog (String user, boolean loggedIn) {

        try {

            FileWriter fw = new FileWriter(filename, true);
            PrintWriter logFile = new PrintWriter(fw);
            LocalDateTime localTime = LocalDateTime.now();
            logFile.println(user + " access time: " + localTime);
            logFile.close();
            System.out.println(user + " - has been written to the log");


        }

        catch (IOException ex) {
            System.out.println("Logger error: " + ex.getMessage());
        }

    }
    /** correctAttempt method:
     * if the login attempt is correct it will write it into the log along with the time and user**/
    public static void correctAttempt(String user, boolean correct ){
        try{
            FileWriter fw=new FileWriter(filename,true);
            PrintWriter logFile=new PrintWriter(fw);
            LocalDateTime localTime = LocalDateTime.now();
            logFile.println("Login was Successful at: "+localTime+" for user: "+user);
            logFile.close();
            System.out.println("Login was Successful at: "+localTime+" for user: "+user);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**failedAttempt method:
     * if the username or password is incorrect it will be written into the log **/
    public static void failedAttempt(String user, boolean failed){
        try{
            FileWriter fw=new FileWriter(filename,true);
            PrintWriter logFile=new PrintWriter(fw);
            LocalDateTime localTime = LocalDateTime.now();
            logFile.println("Login was failed at: "+localTime+" for user: "+user);
            logFile.close();
            System.out.println("Login was failed at: "+localTime+" for user: "+user);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

